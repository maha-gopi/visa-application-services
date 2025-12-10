-- Flyway migration script for appointments-service
-- This script creates the necessary tables for appointments functionality

-- Note: This assumes the base tables are already created by the main DDL script.
-- This migration is for service-specific setup if needed.

-- Create ENUM types if they don't exist
DO $$ BEGIN
    CREATE TYPE appointment_status AS ENUM (
        'SCHEDULED', 'CONFIRMED', 'CHECKED_IN', 'IN_PROGRESS',
        'COMPLETED', 'CANCELLED', 'NO_SHOW', 'RESCHEDULED'
    );
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

-- If tables don't exist, create them (for standalone service)
CREATE TABLE IF NOT EXISTS vac (
    vac_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    vac_code VARCHAR(20) NOT NULL,
    vac_name VARCHAR(255) NOT NULL,
    country_id UUID NOT NULL REFERENCES country(country_id),
    address TEXT NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100),
    postal_code VARCHAR(20),
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    contact_email VARCHAR(255),
    contact_phone VARCHAR(20),
    fax VARCHAR(20),
    website_url VARCHAR(255),
    timezone VARCHAR(50) NOT NULL DEFAULT 'UTC',
    operating_hours_json JSONB,
    max_daily_capacity INTEGER,
    default_slot_duration INTEGER NOT NULL DEFAULT 15,
    allow_walk_ins BOOLEAN NOT NULL DEFAULT TRUE,
    biometric_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    
    CONSTRAINT uq_vac_code UNIQUE (vac_code)
);

CREATE INDEX IF NOT EXISTS idx_vac_country_id ON vac(country_id);
CREATE INDEX IF NOT EXISTS idx_vac_city ON vac(city);
CREATE INDEX IF NOT EXISTS idx_vac_is_active ON vac(is_active);

-- Appointment Slot table
CREATE TABLE IF NOT EXISTS appointment_slot (
    slot_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    vac_id UUID NOT NULL REFERENCES vac(vac_id),
    slot_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    total_quota INTEGER NOT NULL,
    booked_count INTEGER NOT NULL DEFAULT 0,
    available_count INTEGER NOT NULL,
    slot_type VARCHAR(20) NOT NULL DEFAULT 'REGULAR' 
        CHECK (slot_type IN ('REGULAR', 'PREMIUM', 'AGENT')),
    created_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    
    CONSTRAINT uq_slot_vac_date_time UNIQUE (vac_id, slot_date, start_time)
);

CREATE INDEX IF NOT EXISTS idx_appointment_slot_vac_date ON appointment_slot(vac_id, slot_date);
CREATE INDEX IF NOT EXISTS idx_appointment_slot_available ON appointment_slot(available_count) WHERE available_count > 0;

-- Blackout Date table
CREATE TABLE IF NOT EXISTS blackout_date (
    blackout_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    vac_id UUID NOT NULL REFERENCES vac(vac_id),
    blackout_date DATE NOT NULL,
    reason VARCHAR(255) NOT NULL,
    blackout_type VARCHAR(20) NOT NULL DEFAULT 'FULL' 
        CHECK (blackout_type IN ('FULL', 'PARTIAL')),
    affected_slot_types JSONB,
    created_by_staff_id UUID NOT NULL,
    created_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    
    CONSTRAINT uq_blackout_vac_date UNIQUE (vac_id, blackout_date)
);

CREATE INDEX IF NOT EXISTS idx_blackout_date_vac_id ON blackout_date(vac_id);
CREATE INDEX IF NOT EXISTS idx_blackout_date_date ON blackout_date(blackout_date);

-- Appointment table
CREATE TABLE IF NOT EXISTS appointment (
    appointment_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    confirmation_number VARCHAR(50) NOT NULL,
    application_id UUID NOT NULL,
    slot_id UUID NOT NULL REFERENCES appointment_slot(slot_id),
    vac_id UUID NOT NULL REFERENCES vac(vac_id),
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    status appointment_status NOT NULL DEFAULT 'SCHEDULED',
    booked_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    booked_by_user_type VARCHAR(20) NOT NULL,
    booked_by_user_id UUID NOT NULL,
    check_in_time TIMESTAMP WITH TIME ZONE,
    check_in_by_staff_id UUID,
    completion_time TIMESTAMP WITH TIME ZONE,
    no_show_marked_time TIMESTAMP WITH TIME ZONE,
    rescheduled_from_id UUID REFERENCES appointment(appointment_id),
    reschedule_count INTEGER NOT NULL DEFAULT 0,
    cancellation_reason TEXT,
    cancelled_date TIMESTAMP WITH TIME ZONE,
    reminder_sent_date TIMESTAMP WITH TIME ZONE,
    special_requirements TEXT,
    
    CONSTRAINT uq_appointment_confirmation UNIQUE (confirmation_number)
);

CREATE INDEX IF NOT EXISTS idx_appointment_application_id ON appointment(application_id);
CREATE INDEX IF NOT EXISTS idx_appointment_slot_id ON appointment(slot_id);
CREATE INDEX IF NOT EXISTS idx_appointment_vac_id ON appointment(vac_id);
CREATE INDEX IF NOT EXISTS idx_appointment_date ON appointment(appointment_date);
CREATE INDEX IF NOT EXISTS idx_appointment_status ON appointment(status);

