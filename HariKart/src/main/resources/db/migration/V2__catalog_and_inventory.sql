CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    sku VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    brand VARCHAR(100),
    category_id BIGINT NOT NULL REFERENCES categories(id),
    description TEXT,
    hsn_code VARCHAR(10),
    gst_percent NUMERIC(5,2) NOT NULL DEFAULT 18.00,
    mrp NUMERIC(12,2) NOT NULL,
    retail_price NUMERIC(12,2) NOT NULL,
    wholesale_price NUMERIC(12,2) NOT NULL,
    min_order_qty INTEGER NOT NULL DEFAULT 1,
    image_url VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP
);
CREATE INDEX idx_products_category ON products(category_id);
CREATE INDEX idx_products_name ON products(LOWER(name));

-- One row per product: how much is physically in the godown, and how much is promised to orders
CREATE TABLE inventory (
    product_id BIGINT PRIMARY KEY REFERENCES products(id),
    quantity_on_hand INTEGER NOT NULL DEFAULT 0 CHECK (quantity_on_hand >= 0),
    reserved_quantity INTEGER NOT NULL DEFAULT 0 CHECK (reserved_quantity >= 0),
    low_stock_threshold INTEGER NOT NULL DEFAULT 5,
    version BIGINT NOT NULL DEFAULT 0,
    updated_at TIMESTAMP
);

-- Diary of every stock change. Rows are only ever added, never edited
CREATE TABLE stock_movements (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES products(id),
    quantity_change INTEGER NOT NULL,
    quantity_after INTEGER NOT NULL,
    reason VARCHAR(30) NOT NULL,
    note VARCHAR(255),
    created_by BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);
CREATE INDEX idx_movements_product ON stock_movements(product_id, created_at DESC);

INSERT INTO categories(name) VALUES
    ('Chargers & Cables'), ('Earphones & Headphones'), ('Power Banks'),
    ('Mobile Covers & Glass'), ('Memory Cards & Pen Drives'), ('Speakers & Neckbands');