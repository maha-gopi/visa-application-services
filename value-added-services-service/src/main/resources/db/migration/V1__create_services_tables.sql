-- Flyway migration script for services-service
CREATE TABLE IF NOT EXISTS service_catalog (
    service_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    service_code VARCHAR(20) NOT NULL,
    service_name VARCHAR(255) NOT NULL,
    description TEXT,
    service_type VARCHAR(50) NOT NULL CHECK (service_type IN ('STANDARD', 'PREMIUM', 'VAS')),
    category VARCHAR(50),
    base_price DECIMAL(10, 2) NOT NULL DEFAULT 0,
    currency VARCHAR(3) NOT NULL DEFAULT 'USD',
    tax_rate DECIMAL(5, 2) NOT NULL DEFAULT 0,
    duration INTEGER,
    is_location_specific BOOLEAN NOT NULL DEFAULT FALSE,
    display_order INTEGER NOT NULL DEFAULT 999,
    created_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    
    CONSTRAINT uq_service_code UNIQUE (service_code)
);

CREATE INDEX IF NOT EXISTS idx_service_catalog_type ON service_catalog(service_type);
CREATE INDEX IF NOT EXISTS idx_service_catalog_is_active ON service_catalog(is_active);

