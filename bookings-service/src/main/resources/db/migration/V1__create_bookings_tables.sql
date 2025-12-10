-- Flyway migration script for bookings-service
-- This script creates the necessary tables for bookings functionality

-- Note: This assumes the base tables are already created by the main DDL script.

-- Create ENUM types if they don't exist
DO $$ BEGIN
    CREATE TYPE application_status AS ENUM (
        'DRAFT', 'SUBMITTED', 'PAYMENT_PENDING', 'PAYMENT_COMPLETED',
        'APPOINTMENT_SCHEDULED', 'CHECKED_IN', 'IN_PROGRESS',
        'BIOMETRIC_CAPTURED', 'DATA_ENTERED', 'DOCUMENTS_SUBMITTED',
        'QUALITY_CHECK', 'DISPATCHED', 'IN_TRANSIT',
        'RECEIVED_BY_EMBASSY', 'UNDER_REVIEW',
        'APPROVED', 'REJECTED', 'READY_FOR_COLLECTION', 'COLLECTED',
        'CANCELLED', 'WITHDRAWN', 'EXPIRED'
    );
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

-- Application table (if not exists)
CREATE TABLE IF NOT EXISTS application (
    application_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    reference_number VARCHAR(50) NOT NULL,
    applicant_id UUID NOT NULL,
    agent_id UUID,
    visa_type_id UUID NOT NULL,
    vac_id UUID,
    destination_country_id UUID NOT NULL,
    application_type VARCHAR(20) NOT NULL DEFAULT 'NEW' 
        CHECK (application_type IN ('NEW', 'RENEWAL', 'EXTENSION')),
    status application_status NOT NULL DEFAULT 'DRAFT',
    sub_status VARCHAR(50),
    is_draft BOOLEAN NOT NULL DEFAULT TRUE,
    current_step INTEGER NOT NULL DEFAULT 1,
    total_steps INTEGER NOT NULL DEFAULT 5,
    form_data_json JSONB,
    travel_purpose VARCHAR(255),
    intended_arrival_date DATE,
    intended_departure_date DATE,
    accommodation_details TEXT,
    submission_date TIMESTAMP WITH TIME ZONE,
    expiry_date TIMESTAMP WITH TIME ZONE,
    completed_date TIMESTAMP WITH TIME ZONE,
    total_fee_amount DECIMAL(10, 2) NOT NULL DEFAULT 0,
    paid_amount DECIMAL(10, 2) NOT NULL DEFAULT 0,
    refunded_amount DECIMAL(10, 2) NOT NULL DEFAULT 0,
    currency VARCHAR(3) NOT NULL DEFAULT 'USD',
    sla_due_date TIMESTAMP WITH TIME ZONE,
    priority INTEGER NOT NULL DEFAULT 5,
    internal_notes TEXT,
    cancellation_reason TEXT,
    cancelled_by_id UUID,
    cancelled_date TIMESTAMP WITH TIME ZONE,
    created_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by_user_type VARCHAR(20) NOT NULL,
    created_by_user_id UUID NOT NULL,
    
    CONSTRAINT uq_application_reference_number UNIQUE (reference_number)
);

CREATE INDEX IF NOT EXISTS idx_application_applicant_id ON application(applicant_id);
CREATE INDEX IF NOT EXISTS idx_application_agent_id ON application(agent_id);
CREATE INDEX IF NOT EXISTS idx_application_status ON application(status);
CREATE INDEX IF NOT EXISTS idx_application_submission_date ON application(submission_date);
CREATE INDEX IF NOT EXISTS idx_application_vac_id ON application(vac_id);
CREATE INDEX IF NOT EXISTS idx_application_created_date ON application(created_date);
CREATE INDEX IF NOT EXISTS idx_application_reference_number ON application(reference_number);

-- Application Service table
CREATE TABLE IF NOT EXISTS application_service (
    application_service_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    application_id UUID NOT NULL REFERENCES application(application_id) ON DELETE CASCADE,
    service_id UUID NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 1,
    unit_price DECIMAL(10, 2) NOT NULL,
    tax_amount DECIMAL(10, 2) NOT NULL DEFAULT 0,
    total_price DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' 
        CHECK (status IN ('PENDING', 'COMPLETED', 'CANCELLED')),
    added_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_date TIMESTAMP WITH TIME ZONE
);

CREATE INDEX IF NOT EXISTS idx_application_service_application_id ON application_service(application_id);

