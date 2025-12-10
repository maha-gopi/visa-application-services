-- Flyway migration script for visa-types-service
-- This script creates the necessary tables for visa types functionality

-- Note: This assumes the base tables (country, visa_type) are already created
-- by the main DDL script. This migration is for service-specific setup if needed.

-- If tables don't exist, create them (for standalone service)
-- In a microservices architecture, these might be in a shared database
-- or each service might have its own database with only relevant tables

-- Country table (if not exists)
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

-- Visa Type table (if not exists)
CREATE TABLE IF NOT EXISTS visa_type (
    visa_type_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    visa_type_code VARCHAR(20) NOT NULL,
    visa_type_name VARCHAR(100) NOT NULL,
    country_id UUID NOT NULL REFERENCES country(country_id),
    category VARCHAR(50),
    description TEXT,
    processing_time_days INTEGER,
    validity_days INTEGER,
    max_stay_days INTEGER,
    entry_type VARCHAR(20) CHECK (entry_type IN ('SINGLE', 'MULTIPLE', 'TRANSIT')),
    base_fee DECIMAL(10, 2) NOT NULL DEFAULT 0,
    currency VARCHAR(3) NOT NULL DEFAULT 'USD',
    requires_biometric BOOLEAN NOT NULL DEFAULT TRUE,
    requires_interview BOOLEAN NOT NULL DEFAULT FALSE,
    display_order INTEGER NOT NULL DEFAULT 999,
    created_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    
    CONSTRAINT uq_visa_type_code UNIQUE (visa_type_code)
);

CREATE INDEX IF NOT EXISTS idx_visa_type_country_id ON visa_type(country_id);
CREATE INDEX IF NOT EXISTS idx_visa_type_category ON visa_type(category);
CREATE INDEX IF NOT EXISTS idx_visa_type_is_active ON visa_type(is_active);


