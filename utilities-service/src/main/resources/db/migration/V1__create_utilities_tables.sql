-- Flyway migration script for utilities-service
-- This script creates the necessary tables for utilities functionality

-- Note: This assumes the base tables are already created by the main DDL script.

-- Country table (if not exists) - for nationalities
CREATE TABLE IF NOT EXISTS country (
    country_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    country_name VARCHAR(100) NOT NULL,
    country_code_2 VARCHAR(2) NOT NULL,
    country_code_3 VARCHAR(3) NOT NULL,
    numeric_code VARCHAR(3),
    region VARCHAR(100),
    sub_region VARCHAR(100),
    calling_code VARCHAR(10),
    currency VARCHAR(3),
    flag_icon_path VARCHAR(255),
    display_order INTEGER NOT NULL DEFAULT 999,
    created_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    
    CONSTRAINT uq_country_name UNIQUE (country_name),
    CONSTRAINT uq_country_code_2 UNIQUE (country_code_2),
    CONSTRAINT uq_country_code_3 UNIQUE (country_code_3)
);

CREATE INDEX IF NOT EXISTS idx_country_is_active ON country(is_active);
CREATE INDEX IF NOT EXISTS idx_country_region ON country(region);

-- VAC table (if not exists) - for locations
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

-- Applicant Photo table (if not exists) - for photo validation and upload
CREATE TABLE IF NOT EXISTS applicant_photo (
    photo_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    application_id UUID NOT NULL,
    photo_type VARCHAR(20) NOT NULL DEFAULT 'UPLOADED' 
        CHECK (photo_type IN ('UPLOADED', 'CAPTURED')),
    storage_path VARCHAR(500) NOT NULL,
    original_file_name VARCHAR(255),
    file_size BIGINT NOT NULL,
    width INTEGER,
    height INTEGER,
    validation_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    validation_errors JSONB,
    validation_date TIMESTAMP WITH TIME ZONE,
    captured_by_staff_id UUID,
    captured_device_id UUID,
    uploaded_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE INDEX IF NOT EXISTS idx_applicant_photo_application_id ON applicant_photo(application_id);

