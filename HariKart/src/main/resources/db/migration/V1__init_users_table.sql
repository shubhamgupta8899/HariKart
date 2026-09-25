CREATE TYPE user_role AS ENUM ('ADMIN', 'VAN_STAFF', 'SHOPOKEEPER', 'RETAILER');
CREATE TYPE account_status AS ENUM ('PENDING', 'APPROVED', 'REJECT');

CREATE TABLE users (
    ID BIGSERIAL PRIMARY KEY,
    phone_number VARCHAR(15) UNIQUE NOT NULL,
    password_hash VARCHAR(255),
    role user_role NOT NULL,
    status account_status NOT NULL DEFAULT 'PENDING',
    shop_name VARCHAR(150),
    owner_name VARCHAR(100),
    address TEXT,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    photo_url VARCHAR(255),
    route_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT now()
)