package com.vfsglobal.bookings.service;

import com.vfsglobal.bookings.dto.*;
import com.vfsglobal.bookings.entity.Application;
import com.vfsglobal.bookings.entity.ApplicationService;
import com.vfsglobal.bookings.entity.Applicant;
import com.vfsglobal.bookings.entity.VisaType;
import com.vfsglobal.bookings.entity.Vac;
import com.vfsglobal.bookings.entity.ServiceCatalog;
import com.vfsglobal.bookings.entity.Appointment;
import com.vfsglobal.bookings.entity.Payment;
import com.vfsglobal.bookings.repository.ApplicationRepository;
import com.vfsglobal.bookings.repository.ApplicationServiceRepository;
import com.vfsglobal.bookings.repository.ApplicantRepository;
import com.vfsglobal.bookings.repository.VisaTypeRepository;
import com.vfsglobal.bookings.repository.VacRepository;
import com.vfsglobal.bookings.repository.ServiceCatalogRepository;
import com.vfsglobal.bookings.repository.AppointmentRepository;
import com.vfsglobal.bookings.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {
    
    private final ApplicationRepository applicationRepository;
    private final ApplicationServiceRepository applicationServiceRepository;
    private final ApplicantRepository applicantRepository;
    private final VisaTypeRepository visaTypeRepository;
    private final VacRepository vacRepository;
    private final ServiceCatalogRepository serviceCatalogRepository;
    private final AppointmentRepository appointmentRepository;
    private final PaymentRepository paymentRepository;
    
    @Transactional
    public BookingResponse createBooking(BookingCreateRequest request) {
        // Resolve applicant_id: use provided ID, or lookup by passport number
        UUID applicantId = resolveApplicantId(request);
        
        // Resolve visa_type_id from visa_type_code
        UUID visaTypeId = resolveVisaTypeId(request.getVisaTypeCode());
        
        Application application = new Application();
        application.setApplicationId(UUID.randomUUID());
        application.setReferenceNumber(generateReferenceNumber());
        application.setApplicantId(applicantId);
        application.setVisaTypeId(visaTypeId);
        
        // Get destination_country_id from visa type
        VisaType visaType = visaTypeRepository.findById(visaTypeId)
            .orElseThrow(() -> new RuntimeException("Visa type not found: " + visaTypeId));
        application.setDestinationCountryId(visaType.getCountryId());
        
        // Resolve vac_id from location_code (vac_code) if provided
        UUID vacId = request.getLocationCode() != null ? resolveVacId(request.getLocationCode()) : null;
        application.setVacId(vacId);
        application.setStatus(Application.ApplicationStatus.PAYMENT_PENDING);
        application.setIsDraft(false);
        application.setCreatedDate(OffsetDateTime.now());
        application.setUpdatedDate(OffsetDateTime.now());
        application.setCreatedByUserType("APPLICANT");
        // When created_by_user_type is APPLICANT, created_by_user_id should match applicant_id
        application.setCreatedByUserId(applicantId);
        
        application = applicationRepository.save(application);
        
        // Save selected services
        if (request.getSelectedServices() != null && !request.getSelectedServices().isEmpty()) {
            for (String serviceCode : request.getSelectedServices()) {
                // Resolve service_id from service_code
                ServiceCatalog service = resolveService(serviceCode);
                
                ApplicationService appService = new ApplicationService();
                appService.setApplicationServiceId(UUID.randomUUID());
                appService.setApplicationId(application.getApplicationId());
                appService.setServiceId(service.getServiceId());
                appService.setQuantity(1);
                
                // Use actual price from service catalog
                appService.setUnitPrice(service.getBasePrice());
                
                // Calculate tax and total price
                BigDecimal taxAmount = service.getBasePrice()
                    .multiply(service.getTaxRate())
                    .divide(BigDecimal.valueOf(100), 2, java.math.RoundingMode.HALF_UP);
                appService.setTaxAmount(taxAmount);
                appService.setTotalPrice(service.getBasePrice().add(taxAmount));
                
                appService.setAddedDate(OffsetDateTime.now());
                applicationServiceRepository.save(appService);
            }
        }
        
        return mapToBookingResponse(application);
    }
    
    public Page<BookingSummary> listBookings(String status, String visaTypeCode, String startDate, 
                                            String endDate, int page, int pageSize, 
                                            String sortBy, String sortOrder) {
        Pageable pageable = createPageable(page, pageSize, sortBy, sortOrder);
        
        Application.ApplicationStatus appStatus = status != null ? 
            Application.ApplicationStatus.valueOf(status.toUpperCase().replace("-", "_")) : null;
        // TODO: Translate logical ID (visa_type_code) to UUID for filtering
        UUID visaType = visaTypeCode != null ? UUID.randomUUID() : null; // TODO: Translate visaTypeCode to UUID
        LocalDate start = startDate != null ? LocalDate.parse(startDate) : null;
        LocalDate end = endDate != null ? LocalDate.parse(endDate) : null;
        
        Page<Application> applications = applicationRepository.findApplicationsWithFilters(
            appStatus, visaType, start, end, pageable);
        
        return applications.map(this::mapToBookingSummary);
    }
    
    public BookingResponse getBookingById(String referenceNumber) {
        // Accept logical ID (reference_number) instead of UUID
        Application application = applicationRepository.findByReferenceNumber(referenceNumber)
            .orElseThrow(() -> new RuntimeException("Booking not found: " + referenceNumber));
        return mapToBookingResponse(application);
    }
    
    public BookingResponse getBookingByReference(String referenceNumber) {
        Application application = applicationRepository.findByReferenceNumber(referenceNumber)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
        return mapToBookingResponse(application);
    }
    
    @Transactional
    public BookingResponse updateBooking(String referenceNumber, BookingUpdateRequest request) {
        // Accept logical ID (reference_number) instead of UUID
        Application application = applicationRepository.findByReferenceNumber(referenceNumber)
            .orElseThrow(() -> new RuntimeException("Booking not found: " + referenceNumber));
        
        // Update fields if provided
        application.setUpdatedDate(OffsetDateTime.now());
        
        application = applicationRepository.save(application);
        return mapToBookingResponse(application);
    }
    
    @Transactional
    public void cancelBooking(String referenceNumber, String reason) {
        // Accept logical ID (reference_number) instead of UUID
        Application application = applicationRepository.findByReferenceNumber(referenceNumber)
            .orElseThrow(() -> new RuntimeException("Booking not found: " + referenceNumber));
        
        application.setStatus(Application.ApplicationStatus.CANCELLED);
        application.setCancellationReason(reason);
        application.setCancelledDate(OffsetDateTime.now());
        application.setUpdatedDate(OffsetDateTime.now());
        
        applicationRepository.save(application);
    }
    
    private String generateReferenceNumber() {
        return "VFS-" + OffsetDateTime.now().getYear() + "-" + 
               String.format("%06d", (int)(Math.random() * 1000000));
    }
    
    private Pageable createPageable(int page, int pageSize, String sortBy, String sortOrder) {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortOrder) ? 
            Sort.Direction.DESC : Sort.Direction.ASC;
        String field = sortBy != null ? sortBy : "createdDate";
        
        // Map API field names to entity field names
        if ("createdAt".equalsIgnoreCase(field)) {
            field = "createdDate";
        } else if ("updatedAt".equalsIgnoreCase(field)) {
            field = "updatedDate";
        }
        
        return PageRequest.of(page - 1, pageSize, Sort.by(direction, field));
    }
    
    private BookingResponse mapToBookingResponse(Application application) {
        // Fetch related entities
        VisaType visaType = visaTypeRepository.findById(application.getVisaTypeId())
            .orElse(null);
        
        Applicant applicant = applicantRepository.findById(application.getApplicantId())
            .orElse(null);
        
        Vac vac = application.getVacId() != null ? 
            vacRepository.findById(application.getVacId()).orElse(null) : null;
        
        Appointment appointment = appointmentRepository.findByApplicationId(application.getApplicationId())
            .orElse(null);
        
        Payment payment = paymentRepository.findByApplicationId(application.getApplicationId())
            .orElse(null);
        
        // Fetch and map services
        List<ApplicationService> applicationServices = applicationServiceRepository.findByApplicationId(application.getApplicationId());
        BigDecimal servicesTotal = applicationServices.stream()
            .map(ApplicationService::getTotalPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        List<SelectedService> selectedServices = applicationServices.stream()
            .map(appService -> {
                ServiceCatalog service = serviceCatalogRepository.findById(appService.getServiceId())
                    .orElse(null);
                return SelectedService.builder()
                    .serviceId(service != null ? service.getServiceCode() : null)
                    .name(service != null ? service.getServiceName() : null)
                    .price(appService.getTotalPrice())
                    .build();
            })
            .collect(java.util.stream.Collectors.toList());
        
        // Map visa type
        VisaTypeInfo visaTypeInfo = visaType != null ? VisaTypeInfo.builder()
            .id(visaType.getVisaTypeCode())
            .name(visaType.getVisaTypeName())
            .fee(visaType.getBaseFee())
            .build() : null;
        
        // Map personal details
        PersonalDetails personalDetails = applicant != null ? PersonalDetails.builder()
            .firstName(applicant.getFirstName())
            .lastName(applicant.getLastName())
            .dateOfBirth(applicant.getDateOfBirth() != null ? applicant.getDateOfBirth().toString() : null)
            .passportNumber(applicant.getPassportNumber())
            .passportExpiry(applicant.getPassportExpiryDate() != null ? applicant.getPassportExpiryDate().toString() : null)
            .email(applicant.getEmail())
            .phone(applicant.getPhone())
            .address(applicant.getAddress())
            .build() : null;
        
        // Map appointment
        String appointmentDate = appointment != null && appointment.getAppointmentDate() != null ?
            appointment.getAppointmentDate().toString() : null;
        String appointmentTime = appointment != null && appointment.getAppointmentTime() != null ?
            appointment.getAppointmentTime().toString() : null;
        
        // Map payment
        String paymentMethod = payment != null ? payment.getPaymentMode().name().toLowerCase().replace("_", "-") : null;
        String paymentStatus = payment != null ? payment.getStatus().name().toLowerCase() : null;
        
        // Map location
        String locationId = vac != null ? vac.getVacCode() : null;
        String locationName = vac != null ? vac.getVacName() : null;
        
        String status = mapStatus(application.getStatus());
        
        return BookingResponse.builder()
            .id(application.getReferenceNumber())
            .referenceNumber(application.getReferenceNumber())
            .status(status)
            .visaType(visaTypeInfo)
            .personalDetails(personalDetails)
            .photo(null) // Photo would need to be fetched from applicant_photo table if needed
            .appointmentDate(appointmentDate)
            .appointmentTime(appointmentTime)
            .selectedServices(selectedServices.isEmpty() ? null : selectedServices)
            .servicesTotal(servicesTotal)
            .visaFee(application.getTotalFeeAmount())
            .totalAmount(application.getTotalFeeAmount().add(servicesTotal))
            .currency(application.getCurrency())
            .paymentMethod(paymentMethod)
            .paymentStatus(paymentStatus)
            .locationId(locationId)
            .locationName(locationName)
            .createdAt(application.getCreatedDate())
            .updatedAt(application.getUpdatedDate())
            .build();
    }
    
    private BookingSummary mapToBookingSummary(Application application) {
        String status = mapStatus(application.getStatus());
        
        return BookingSummary.builder()
            .id(application.getReferenceNumber()) // Expose logical ID instead of UUID
            .referenceNumber(application.getReferenceNumber())
            .status(status)
            .totalAmount(application.getTotalFeeAmount())
            .currency(application.getCurrency())
            .createdAt(application.getCreatedDate())
            .build();
    }
    
    private String mapStatus(Application.ApplicationStatus status) {
        return status.name().toLowerCase().replace("_", "-");
    }
    
    /**
     * Resolves applicant_id from the request.
     * Priority:
     * 1. If applicantId is provided in request, use it
     * 2. If passportNumber is provided in personalDetails, lookup by passport number
     * 3. Otherwise, throw an exception
     */
    private UUID resolveApplicantId(BookingCreateRequest request) {
        // Option 1: Use provided applicantId if available
        if (request.getApplicantId() != null && !request.getApplicantId().trim().isEmpty()) {
            try {
                UUID applicantId = UUID.fromString(request.getApplicantId());
                // Verify the applicant exists
                Applicant applicant = applicantRepository.findById(applicantId)
                    .orElseThrow(() -> new RuntimeException("Applicant not found with ID: " + applicantId));
                if (!applicant.getIsActive()) {
                    throw new RuntimeException("Applicant is not active: " + applicantId);
                }
                log.debug("Using provided applicant ID: {}", applicantId);
                return applicantId;
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid applicant ID format: " + request.getApplicantId(), e);
            }
        }
        
        // Option 2: Lookup by passport number from personalDetails
        if (request.getPersonalDetails() != null && 
            request.getPersonalDetails().getPassportNumber() != null && 
            !request.getPersonalDetails().getPassportNumber().trim().isEmpty()) {
            
            String passportNumber = request.getPersonalDetails().getPassportNumber().trim();
            Applicant applicant = applicantRepository.findByPassportNumber(passportNumber)
                .orElseThrow(() -> new RuntimeException(
                    "Applicant not found with passport number: " + passportNumber + 
                    ". Please provide applicantId or ensure the applicant exists."));
            
            if (!applicant.getIsActive()) {
                throw new RuntimeException("Applicant is not active for passport number: " + passportNumber);
            }
            
            log.debug("Found applicant by passport number: {} -> {}", passportNumber, applicant.getApplicantId());
            return applicant.getApplicantId();
        }
        
        // No way to resolve applicant_id
        throw new RuntimeException(
            "Cannot resolve applicant_id. Please provide either 'applicantId' in the request " +
            "or 'passportNumber' in 'personalDetails'.");
    }
    
    /**
     * Resolves visa_type_id from visa_type_code.
     * Looks up the VisaType by code and returns its UUID.
     */
    private UUID resolveVisaTypeId(String visaTypeCode) {
        if (visaTypeCode == null || visaTypeCode.trim().isEmpty()) {
            throw new RuntimeException("visaTypeCode is required");
        }
        
        VisaType visaType = visaTypeRepository.findByVisaTypeCode(visaTypeCode.trim())
            .orElseThrow(() -> new RuntimeException(
                "Visa type not found with code: " + visaTypeCode + 
                ". Please provide a valid visa type code."));
        
        log.debug("Found visa type by code: {} -> {}", visaTypeCode, visaType.getVisaTypeId());
        return visaType.getVisaTypeId();
    }
    
    /**
     * Resolves vac_id from location_code (vac_code).
     * Looks up the Vac by code and returns its UUID.
     */
    private UUID resolveVacId(String locationCode) {
        if (locationCode == null || locationCode.trim().isEmpty()) {
            throw new RuntimeException("locationCode cannot be empty when provided");
        }
        
        Vac vac = vacRepository.findByVacCode(locationCode.trim())
            .orElseThrow(() -> new RuntimeException(
                "VAC (location) not found with code: " + locationCode + 
                ". Please provide a valid location code."));
        
        if (!vac.getIsActive()) {
            throw new RuntimeException("VAC (location) is not active: " + locationCode);
        }
        
        log.debug("Found VAC by code: {} -> {}", locationCode, vac.getVacId());
        return vac.getVacId();
    }
    
    /**
     * Resolves ServiceCatalog from service_code.
     * Looks up the service by code and validates it's active.
     */
    private ServiceCatalog resolveService(String serviceCode) {
        if (serviceCode == null || serviceCode.trim().isEmpty()) {
            throw new RuntimeException("serviceCode cannot be empty");
        }
        
        ServiceCatalog service = serviceCatalogRepository.findByServiceCode(serviceCode.trim())
            .orElseThrow(() -> new RuntimeException(
                "Service not found with code: " + serviceCode + 
                ". Please provide a valid service code."));
        
        log.debug("Found service by code: {} -> {} (price: {})", 
            serviceCode, service.getServiceId(), service.getBasePrice());
        return service;
    }
}

