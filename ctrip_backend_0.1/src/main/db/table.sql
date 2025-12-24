-- User Table
CREATE TABLE tb_users (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    email VARCHAR(255) UNIQUE,
    phone VARCHAR(20) UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    avatar_url TEXT,
    auth_level VARCHAR(20) DEFAULT 'member' CHECK (auth_level IN ('member', 'vip', 'admin')),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_users_email ON tb_users(email);
CREATE INDEX idx_users_phone ON tb_users(phone);

-- Login & Register Verification-code Table
CREATE TABLE tb_verification_codes (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    target VARCHAR(100) NOT NULL,
    code VARCHAR(10) NOT NULL,
    type VARCHAR(20) NOT NULL,
    status VARCHAR(10) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_codes_target ON tb_verification_codes(target);

-- City table
CREATE TABLE tb_cities (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    name VARCHAR(100) NOT NULL,
    country VARCHAR(20) NOT NULL,
    latitude DECIMAL(10, 8) NOT NULL,
    longitude DECIMAL(11, 8) NOT NULL
);
CREATE INDEX idx_cities_name ON tb_cities(name);

-- Airport table
CREATE TABLE tb_airports (
    code VARCHAR(4) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    city_id CHAR(36) NOT NULL,
    FOREIGN KEY (city_id) REFERENCES tb_cities(id)
);
CREATE INDEX idx_airports_city ON tb_airports(city_id);

-- Flight table
CREATE TABLE tb_flights (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    number VARCHAR(20) NOT NULL,
    airline VARCHAR(100) NOT NULL,
    aircraft_type VARCHAR(50) NOT NULL,
    departure_airport VARCHAR(100) NOT NULL,
    departure_airport_code VARCHAR(3) NOT NULL,
    departure_time TIMESTAMP NOT NULL,
    arrival_airport VARCHAR(100) NOT NULL,
    arrival_airport_code VARCHAR(36) NOT NULL,
    arrival_time TIMESTAMP NOT NULL,
    status VARCHAR(20) DEFAULT 'scheduled' CHECK (status IN ('scheduled', 'delayed', 'cancelled', 'completed')),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (departure_airport) REFERENCES tb_airports(code),
    FOREIGN KEY (arrival_airport) REFERENCES tb_airports(code)
);

CREATE INDEX idx_flights_departure ON tb_flights(departure_airport, departure_time);
CREATE INDEX idx_flights_arrival ON tb_flights(arrival_airport, arrival_time);

-- Cabin table
CREATE TABLE tb_flight_cabins (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    flight_id CHAR(36) NOT NULL,
    type VARCHAR(50) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    available_seats INTEGER NOT NULL DEFAULT 0,
    baggage_allowance JSON,
    change_policy JSON,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (flight_id) REFERENCES tb_flights(id) ON DELETE CASCADE
);

CREATE INDEX idx_cabins_flight ON tb_flight_cabins(flight_id);
CREATE INDEX idx_cabins_price ON tb_flight_cabins(price);

-- Flight order table
CREATE TABLE tb_flight_orders (
                                  id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
                                  user_id CHAR(36) NOT NULL,
                                  flight_id CHAR(36) NOT NULL,
                                  cabin_type VARCHAR(50) NOT NULL,
                                  cabin_id CHAR(36) NOT NULL,
                                  passengers JSON NOT NULL,
                                  amount DECIMAL(10, 2) NOT NULL,
                                  status VARCHAR(20) DEFAULT 'pending' CHECK (status IN ('pending', 'paid', 'cancelled', 'refunded')),
                                  created_at TIMESTAMP DEFAULT NOW(),
                                  updated_at TIMESTAMP DEFAULT NOW(),
                                  FOREIGN KEY (user_id) REFERENCES tb_users(id),
                                  FOREIGN KEY (flight_id) REFERENCES tb_flights(id),
                                  FOREIGN KEY (cabin_id) REFERENCES tb_flight_cabins(id)
);

CREATE INDEX idx_flight_orders_user ON tb_flight_orders(user_id);
CREATE INDEX idx_flight_orders_flight ON tb_flight_orders(flight_id, cabin_id);

-- Train station table
CREATE TABLE tb_train_stations (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    name VARCHAR(100) NOT NULL,
    city_id CHAR(36) NOT NULL,
    FOREIGN KEY (city_id) REFERENCES tb_cities(id)
);
CREATE INDEX idx_stations_city ON tb_train_stations(city_id);

-- Train table
CREATE TABLE tb_trains (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    train_number VARCHAR(20) NOT NULL,
    train_type VARCHAR(20) NOT NULL,
    departure_station VARCHAR(100) NOT NULL,
    departure_station_id CHAR(36) NOT NULL,
    departure_time TIMESTAMP NOT NULL,
    arrival_station VARCHAR(100) NOT NULL,
    arrival_station_id CHAR(36) NOT NULL,
    arrival_time TIMESTAMP NOT NULL,
    status VARCHAR(20) DEFAULT 'scheduled' CHECK (status IN ('scheduled', 'delayed', 'cancelled', 'completed')),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (departure_station_id) REFERENCES tb_train_stations(id),
    FOREIGN KEY (arrival_station_id) REFERENCES tb_train_stations(id)
);

CREATE INDEX idx_trains_departure ON tb_trains(departure_station_id, departure_time);
CREATE INDEX idx_trains_arrival ON tb_trains(arrival_station_id, arrival_time);

-- Seats table
CREATE TABLE tb_train_seats (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    train_id CHAR(36) NOT NULL,
    type VARCHAR(50) NOT NULL CHECK (type IN ('business', 'first', 'second', 'soft_sleeper', 'hard_sleeper')),
    price DECIMAL(10, 2) NOT NULL,
    available_seats INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (train_id) REFERENCES tb_trains(id) ON DELETE CASCADE
);

CREATE INDEX idx_seats_train ON tb_train_seats(train_id);
CREATE INDEX idx_seats_price ON tb_train_seats(price);

-- Passenger table
CREATE TABLE tb_user_passengers (
                                    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
                                    user_id CHAR(36) NOT NULL,
                                    name VARCHAR(100) NOT NULL,
                                    citizen_id VARCHAR(18) NOT NULL,
                                    phone VARCHAR(20),
                                    created_at TIMESTAMP DEFAULT NOW(),
                                    updated_at TIMESTAMP DEFAULT NOW(),
                                    FOREIGN KEY (user_id) REFERENCES tb_users(id) ON DELETE CASCADE,
                                    UNIQUE KEY idx_user_citizen_id (user_id, citizen_id)
);

CREATE INDEX idx_passengers_user ON tb_user_passengers(user_id);

-- Train order table
CREATE TABLE tb_train_orders (
                                 id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
                                 user_id CHAR(36) NOT NULL,
                                 train_id CHAR(36) NOT NULL,
                                 seat_type VARCHAR(50) NOT NULL,
                                 seat_id CHAR(36) NOT NULL,
                                 passengers JSON NOT NULL,
                                 amount DECIMAL(10, 2) NOT NULL,
                                 status VARCHAR(20) DEFAULT 'pending' CHECK (status IN ('pending', 'paid', 'cancelled', 'refunded')),
                                 created_at TIMESTAMP DEFAULT NOW(),
                                 updated_at TIMESTAMP DEFAULT NOW(),
                                 FOREIGN KEY (user_id) REFERENCES tb_users(id),
                                 FOREIGN KEY (train_id) REFERENCES tb_trains(id),
                                 FOREIGN KEY (seat_id) REFERENCES tb_train_seats(id)
);

CREATE INDEX idx_train_orders_user ON tb_train_orders(user_id);
CREATE INDEX idx_train_orders_train ON tb_train_orders(train_id, seat_id);

-- Hotel Table
CREATE TABLE tb_hotels (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    name VARCHAR(200) NOT NULL,
    address VARCHAR(200) NOT NULL,
    latitude DECIMAL(10, 8) NOT NULL,
    longitude DECIMAL(11, 8) NOT NULL,
    description TEXT,
    rating INTEGER CHECK (rating >= 0 AND rating <= 5),
    amenities JSON,
    city_id CHAR(36) NOT NULL,
    city VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (city_id) REFERENCES tb_cities(id) ON DELETE CASCADE
);

CREATE INDEX idx_hotels_location ON tb_hotels(latitude, longitude);
CREATE INDEX idx_hotels_address ON tb_hotels(address);
CREATE INDEX idx_hotels_city ON tb_hotels(city);
CREATE INDEX idx_hotels_city_id ON tb_hotels(city_id);

-- Rooms table for hotels
CREATE TABLE tb_hotel_rooms (
    id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    hotel_id CHAR(36) NOT NULL,
    type VARCHAR(100) NOT NULL,
    occupancity INTEGER NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    available_count INTEGER NOT NULL DEFAULT 0,
    amenities JSON,
    policy JSON,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (hotel_id) REFERENCES tb_hotels(id) ON DELETE CASCADE
);

CREATE INDEX idx_rooms_hotel_id ON tb_hotel_rooms(hotel_id);
CREATE INDEX idx_rooms_price ON tb_hotel_rooms(price);


-- Hotel orders table
CREATE TABLE tb_hotel_orders (
                                 id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
                                 user_id CHAR(36) NOT NULL,
                                 hotel_id CHAR(36) NOT NULL,
                                 room_type VARCHAR(100) NOT NULL,
                                 room_id CHAR(36) NOT NULL,
                                 check_in_date DATE NOT NULL,
                                 check_out_date DATE NOT NULL,
                                 guest_info JSON NOT NULL,
                                 total_amount DECIMAL(10, 2) NOT NULL,
                                 status VARCHAR(20) DEFAULT 'pending' CHECK (status IN ('pending', 'paid', 'cancelled', 'refunded')),
                                 created_at TIMESTAMP DEFAULT NOW(),
                                 updated_at TIMESTAMP DEFAULT NOW(),
                                 FOREIGN KEY (user_id) REFERENCES tb_users(id),
                                 FOREIGN KEY (hotel_id) REFERENCES tb_hotels(id),
                                 FOREIGN KEY (room_id) REFERENCES tb_hotel_rooms(id)
);

CREATE INDEX idx_hotel_orders_user ON tb_hotel_orders(user_id);
CREATE INDEX idx_hotel_orders_hotel ON tb_hotel_orders(hotel_id, room_id);