-- Flyway migration script for payments-service
-- This script creates the necessary tables for payments functionality

-- Note: This assumes the base tables are already created by the main DDL script.

-- Create ENUM types if they don't exist
DO $$ BEGIN
    CREATE TYPE payment_status AS ENUM (
        'PENDING', 'PROCESSING', 'COMPLETED', 
        'FAILED', 'CANCELLED', 'REFUNDED', 'PARTIALLY_REFUNDED'
    );
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE payment_mode AS ENUM (
        'CASH', 'CREDIT_CARD', 'DEBIT_CARD', 
        'ONLINE', 'BANK_TRANSFER', 'WALLET'
    );
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE refund_status AS ENUM (
        'PENDING', 'APPROVED', 'REJECTED', 'PROCESSING', 'COMPLETED', 'FAILED'
    );
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

-- Payment table (if not exists)
CREATE TABLE IF NOT EXISTS payment (
    payment_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    payment_reference VARCHAR(50) NOT NULL,
    application_id UUID NOT NULL,
    vac_id UUID,
    sub_total DECIMAL(10, 2) NOT NULL,
    tax_amount DECIMAL(10, 2) NOT NULL DEFAULT 0,
    total_amount DECIMAL(10, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'USD',
    payment_mode payment_mode NOT NULL,
    payment_gateway VARCHAR(50),
    gateway_transaction_id VARCHAR(100),
    gateway_response JSONB,
    card_last_four VARCHAR(4),
    card_brand VARCHAR(20),
    status payment_status NOT NULL DEFAULT 'PENDING',
    receipt_number VARCHAR(50),
    receipt_path VARCHAR(500),
    collected_by_staff_id UUID,
    payment_date TIMESTAMP WITH TIME ZONE,
    reconciliation_status VARCHAR(20) NOT NULL DEFAULT 'PENDING' 
        CHECK (reconciliation_status IN ('PENDING', 'RECONCILED', 'DISCREPANCY')),
    reconciliation_date TIMESTAMP WITH TIME ZONE,
    notes TEXT,
    created_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT uq_payment_reference UNIQUE (payment_reference),
    CONSTRAINT uq_payment_receipt_number UNIQUE (receipt_number)
);

CREATE INDEX IF NOT EXISTS idx_payment_application_id ON payment(application_id);
CREATE INDEX IF NOT EXISTS idx_payment_status ON payment(status);
CREATE INDEX IF NOT EXISTS idx_payment_date ON payment(payment_date);
CREATE INDEX IF NOT EXISTS idx_payment_reconciliation_status ON payment(reconciliation_status);

-- Payment Line Item table
CREATE TABLE IF NOT EXISTS payment_line_item (
    line_item_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    payment_id UUID NOT NULL REFERENCES payment(payment_id) ON DELETE CASCADE,
    item_type VARCHAR(30) NOT NULL CHECK (item_type IN ('VISA_FEE', 'SERVICE_FEE', 'VAS')),
    item_description VARCHAR(255) NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 1,
    unit_price DECIMAL(10, 2) NOT NULL,
    tax_amount DECIMAL(10, 2) NOT NULL DEFAULT 0,
    total_price DECIMAL(10, 2) NOT NULL,
    reference_id UUID,
    reference_type VARCHAR(30)
);

CREATE INDEX IF NOT EXISTS idx_payment_line_item_payment_id ON payment_line_item(payment_id);

-- Refund table
CREATE TABLE IF NOT EXISTS refund (
    refund_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    refund_reference VARCHAR(50) NOT NULL,
    payment_id UUID NOT NULL REFERENCES payment(payment_id),
    application_id UUID NOT NULL,
    refund_amount DECIMAL(10, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    reason TEXT NOT NULL,
    refund_type VARCHAR(20) NOT NULL CHECK (refund_type IN ('FULL', 'PARTIAL')),
    refund_method VARCHAR(30) NOT NULL,
    status refund_status NOT NULL DEFAULT 'PENDING',
    requested_by_user_type VARCHAR(20) NOT NULL,
    requested_by_user_id UUID NOT NULL,
    requested_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    approved_by_staff_id UUID,
    approved_date TIMESTAMP WITH TIME ZONE,
    processed_by_staff_id UUID,
    processed_date TIMESTAMP WITH TIME ZONE,
    gateway_refund_id VARCHAR(100),
    notes TEXT,
    
    CONSTRAINT uq_refund_reference UNIQUE (refund_reference)
);

CREATE INDEX IF NOT EXISTS idx_refund_payment_id ON refund(payment_id);
CREATE INDEX IF NOT EXISTS idx_refund_application_id ON refund(application_id);
CREATE INDEX IF NOT EXISTS idx_refund_status ON refund(status);

