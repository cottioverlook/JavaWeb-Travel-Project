-- Data for tb_users
INSERT INTO tb_users (email, phone, password_hash, name, avatar_url, auth_level) VALUES
('alice@example.com', '13800138001', 'hash123', 'Alice', 'https://example.com/avatar1.png', 'member'),
('bob@example.com', '13800138002', 'hash456', 'Bob', 'https://example.com/avatar2.png', 'vip'),
('admin@example.com', '13800138003', 'hash789', 'Admin', 'https://example.com/avatar3.png', 'admin');

-- Data for tb_verification_codes
INSERT INTO tb_verification_codes (target, code, type, status, expires_at) VALUES
('13800138001', '123456', 'register', 'active', DATE_ADD(NOW(), INTERVAL 10 MINUTE)),
('alice@example.com', '654321', 'login', 'used', DATE_ADD(NOW(), INTERVAL 10 MINUTE)),
('13800138002', '987654', 'reset_pwd', 'expired', DATE_SUB(NOW(), INTERVAL 10 MINUTE));

-- Data for tb_cities
INSERT INTO tb_cities (name, country, latitude, longitude) VALUES
('Beijing', 'China', 39.9042, 116.4074),
('Shanghai', 'China', 31.2304, 121.4737),
('Guangzhou', 'China', 23.1291, 113.2644);

-- Data for tb_airports
INSERT INTO tb_airports (code, name, city_id) VALUES
('PEK', 'Beijing Capital International Airport', (SELECT id FROM tb_cities WHERE name = 'Beijing')),
('SHA', 'Shanghai Hongqiao International Airport', (SELECT id FROM tb_cities WHERE name = 'Shanghai')),
('CAN', 'Guangzhou Baiyun International Airport', (SELECT id FROM tb_cities WHERE name = 'Guangzhou'));

-- Data for tb_flights
INSERT INTO tb_flights (number, airline, aircraft_type, departure_airport, departure_airport_code, departure_time, arrival_airport, arrival_airport_code, arrival_time, status) VALUES
('CA101', 'Air China', 'Boeing 737', 'PEK', 'PEK', '2023-12-01 08:00:00', 'SHA', 'SHA', '2023-12-01 10:15:00', 'scheduled'),
('MU5100', 'China Eastern', 'Airbus A320', 'SHA', 'SHA', '2023-12-01 12:00:00', 'CAN', 'CAN', '2023-12-01 14:30:00', 'delayed'),
('CZ3001', 'China Southern', 'Boeing 787', 'CAN', 'CAN', '2023-12-01 16:00:00', 'PEK', 'PEK', '2023-12-01 19:15:00', 'completed');

-- Data for tb_flight_cabins
INSERT INTO tb_flight_cabins (flight_id, type, price, available_seats, baggage_allowance, change_policy) VALUES
-- CA101
((SELECT id FROM tb_flights WHERE number = 'CA101'), 'Economy', 800.00, 120, '{"weight": 20}', '{"refund": "partial"}'),
((SELECT id FROM tb_flights WHERE number = 'CA101'), 'Business', 2000.00, 20, '{"weight": 30}', '{"refund": "full"}'),
((SELECT id FROM tb_flights WHERE number = 'CA101'), 'First', 3500.00, 8, '{"weight": 40}', '{"refund": "full"}'),
-- MU5100
((SELECT id FROM tb_flights WHERE number = 'MU5100'), 'Economy', 900.00, 110, '{"weight": 20}', '{"refund": "partial"}'),
((SELECT id FROM tb_flights WHERE number = 'MU5100'), 'Business', 2200.00, 18, '{"weight": 30}', '{"refund": "full"}'),
((SELECT id FROM tb_flights WHERE number = 'MU5100'), 'First', 3800.00, 6, '{"weight": 40}', '{"refund": "full"}'),
-- CZ3001
((SELECT id FROM tb_flights WHERE number = 'CZ3001'), 'Economy', 850.00, 150, '{"weight": 23}', '{"refund": "partial"}'),
((SELECT id FROM tb_flights WHERE number = 'CZ3001'), 'Business', 2100.00, 30, '{"weight": 32}', '{"refund": "full"}'),
((SELECT id FROM tb_flights WHERE number = 'CZ3001'), 'First', 3600.00, 10, '{"weight": 45}', '{"refund": "full"}');

-- Data for tb_train_stations
INSERT INTO tb_train_stations (name, city_id) VALUES
('Beijing South Railway Station', (SELECT id FROM tb_cities WHERE name = 'Beijing')),
('Shanghai Hongqiao Railway Station', (SELECT id FROM tb_cities WHERE name = 'Shanghai')),
('Guangzhou South Railway Station', (SELECT id FROM tb_cities WHERE name = 'Guangzhou'));

-- Data for tb_trains
INSERT INTO tb_trains (train_number, train_type, departure_station, departure_station_id, departure_time, arrival_station, arrival_station_id, arrival_time, status) VALUES
('G1', 'High-Speed', 'Beijing South Railway Station', (SELECT id FROM tb_train_stations WHERE name = 'Beijing South Railway Station'), '2023-12-02 09:00:00', 'Shanghai Hongqiao Railway Station', (SELECT id FROM tb_train_stations WHERE name = 'Shanghai Hongqiao Railway Station'), '2023-12-02 13:18:00', 'scheduled'),
('G2', 'High-Speed', 'Shanghai Hongqiao Railway Station', (SELECT id FROM tb_train_stations WHERE name = 'Shanghai Hongqiao Railway Station'), '2023-12-02 14:00:00', 'Beijing South Railway Station', (SELECT id FROM tb_train_stations WHERE name = 'Beijing South Railway Station'), '2023-12-02 18:18:00', 'scheduled'),
('G3', 'High-Speed', 'Beijing South Railway Station', (SELECT id FROM tb_train_stations WHERE name = 'Beijing South Railway Station'), '2023-12-02 10:00:00', 'Guangzhou South Railway Station', (SELECT id FROM tb_train_stations WHERE name = 'Guangzhou South Railway Station'), '2023-12-02 18:00:00', 'scheduled');

-- Data for tb_train_seats
INSERT INTO tb_train_seats (train_id, type, price, available_seats) VALUES
-- G1
((SELECT id FROM tb_trains WHERE train_number = 'G1'), 'second', 553.00, 500),
((SELECT id FROM tb_trains WHERE train_number = 'G1'), 'first', 933.00, 100),
((SELECT id FROM tb_trains WHERE train_number = 'G1'), 'business', 1748.00, 30),
-- G2
((SELECT id FROM tb_trains WHERE train_number = 'G2'), 'second', 553.00, 480),
((SELECT id FROM tb_trains WHERE train_number = 'G2'), 'first', 933.00, 90),
((SELECT id FROM tb_trains WHERE train_number = 'G2'), 'business', 1748.00, 25),
-- G3
((SELECT id FROM tb_trains WHERE train_number = 'G3'), 'second', 862.00, 600),
((SELECT id FROM tb_trains WHERE train_number = 'G3'), 'first', 1380.00, 120),
((SELECT id FROM tb_trains WHERE train_number = 'G3'), 'business', 2700.00, 40);

-- Data for tb_hotels
INSERT INTO tb_hotels (name, address, latitude, longitude, description, rating, amenities, city_id, city) VALUES
('Beijing Hotel', '33 East Chang An Avenue', 39.9080, 116.4090, 'Historic luxury hotel', 5, '["wifi", "pool", "gym"]', (SELECT id FROM tb_cities WHERE name = 'Beijing'), 'Beijing'),
('Peace Hotel', '20 Nanjing Road East', 31.2397, 121.4860, 'Famous art deco hotel', 5, '["wifi", "spa"]', (SELECT id FROM tb_cities WHERE name = 'Shanghai'), 'Shanghai'),
('White Swan Hotel', '1 Shamian South Street', 23.1065, 113.2425, 'Luxury hotel on Shamian Island', 5, '["wifi", "pool", "river_view"]', (SELECT id FROM tb_cities WHERE name = 'Guangzhou'), 'Guangzhou');

-- Data for tb_hotel_rooms
INSERT INTO tb_hotel_rooms (hotel_id, type, occupancity, price, available_count, amenities, policy) VALUES
-- Beijing Hotel
((SELECT id FROM tb_hotels WHERE name = 'Beijing Hotel'), 'Standard Room', 2, 1200.00, 10, '{"bed": "queen"}', '{"cancellation": "free"}'),
((SELECT id FROM tb_hotels WHERE name = 'Beijing Hotel'), 'Deluxe Room', 2, 1800.00, 8, '{"bed": "king"}', '{"cancellation": "24h"}'),
((SELECT id FROM tb_hotels WHERE name = 'Beijing Hotel'), 'Executive Suite', 3, 3000.00, 3, '{"bed": "king", "lounge_access": true}', '{"cancellation": "non-refundable"}'),
-- Peace Hotel
((SELECT id FROM tb_hotels WHERE name = 'Peace Hotel'), 'Standard Room', 2, 1500.00, 12, '{"bed": "queen"}', '{"cancellation": "free"}'),
((SELECT id FROM tb_hotels WHERE name = 'Peace Hotel'), 'River View Room', 2, 2500.00, 6, '{"bed": "king", "view": "bund"}', '{"cancellation": "48h"}'),
((SELECT id FROM tb_hotels WHERE name = 'Peace Hotel'), 'Sassoon Suite', 2, 8000.00, 1, '{"bed": "king", "history": "famous"}', '{"cancellation": "non-refundable"}'),
-- White Swan Hotel
((SELECT id FROM tb_hotels WHERE name = 'White Swan Hotel'), 'Standard Room', 2, 1000.00, 15, '{"bed": "twin"}', '{"cancellation": "free"}'),
((SELECT id FROM tb_hotels WHERE name = 'White Swan Hotel'), 'Club Room', 2, 1600.00, 10, '{"bed": "king", "club_access": true}', '{"cancellation": "24h"}'),
((SELECT id FROM tb_hotels WHERE name = 'White Swan Hotel'), 'Grand Suite', 4, 3800.00, 2, '{"bed": "2 kings", "living_room": true}', '{"cancellation": "non-refundable"}');
