package top.potatohub.ctrip.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import top.potatohub.ctrip.backend.entities.Attraction;
import top.potatohub.ctrip.backend.mapper.AttractionMapper;
import top.potatohub.ctrip.backend.utils.PasswordUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private AttractionMapper attractionMapper;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Initializing Database Data...");
        
        initializeSchema();
        seedData();
    }

    private void initializeSchema() {
        System.out.println("Creating tables...");
        
        // tb_users
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS tb_users (" +
                "id VARCHAR(64) PRIMARY KEY, " +
                "email VARCHAR(255) UNIQUE, " +
                "phone VARCHAR(20) UNIQUE, " +
                "name VARCHAR(100), " +
                "password_hash VARCHAR(255), " +
                "avatar_url VARCHAR(500), " +
                "auth_level VARCHAR(20), " +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)");

        // tb_cities
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS tb_cities (" +
                "id VARCHAR(64) PRIMARY KEY, " +
                "name VARCHAR(100), " +
                "country VARCHAR(100), " +
                "latitude DECIMAL(10, 6), " +
                "longitude DECIMAL(10, 6))");

        // tb_hotels
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS tb_hotels (" +
                "id VARCHAR(64) PRIMARY KEY, " +
                "name VARCHAR(255), " +
                "address VARCHAR(500), " +
                "latitude DECIMAL(10, 6), " +
                "longitude DECIMAL(10, 6), " +
                "description TEXT, " +
                "rating INT, " +
                "amenities TEXT, " +
                "city_id VARCHAR(64), " +
                "city VARCHAR(100), " +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)");

        // tb_hotel_rooms
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS tb_hotel_rooms (" +
                "id VARCHAR(64) PRIMARY KEY, " +
                "hotel_id VARCHAR(64), " +
                "type VARCHAR(50), " +
                "occupancy INT, " +
                "price DECIMAL(10, 2), " +
                "available_count INT, " +
                "amenities TEXT, " +
                "policy TEXT, " +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)");

        // tb_airports
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS tb_airports (" +
                "code VARCHAR(10) PRIMARY KEY, " +
                "name VARCHAR(100), " +
                "city_id VARCHAR(64))");

        // tb_flights
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS tb_flights (" +
                "id VARCHAR(64) PRIMARY KEY, " +
                "number VARCHAR(20), " +
                "airline VARCHAR(100), " +
                "aircraft_type VARCHAR(50), " +
                "departure_airport VARCHAR(100), " +
                "departure_airport_code VARCHAR(10), " +
                "departure_time DATETIME, " +
                "arrival_airport VARCHAR(100), " +
                "arrival_airport_code VARCHAR(10), " +
                "arrival_time DATETIME, " +
                "status VARCHAR(20), " +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)");

        // tb_flight_cabins
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS tb_flight_cabins (" +
                "id VARCHAR(64) PRIMARY KEY, " +
                "flight_id VARCHAR(64), " +
                "type VARCHAR(50), " +
                "price DECIMAL(10, 2), " +
                "available_seats INT, " +
                "baggage_allowance VARCHAR(100), " +
                "change_policy VARCHAR(100), " +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)");

        // tb_stations
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS tb_stations (" +
                "id VARCHAR(64) PRIMARY KEY, " +
                "name VARCHAR(100), " +
                "city_id VARCHAR(64))");

        // tb_trains
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS tb_trains (" +
                "id VARCHAR(64) PRIMARY KEY, " +
                "train_number VARCHAR(20), " +
                "train_type VARCHAR(50), " +
                "departure_station VARCHAR(100), " +
                "departure_station_id VARCHAR(64), " +
                "departure_time DATETIME, " +
                "arrival_station VARCHAR(100), " +
                "arrival_station_id VARCHAR(64), " +
                "arrival_time DATETIME, " +
                "status VARCHAR(20), " +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)");

        // tb_train_seats
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS tb_train_seats (" +
                "id VARCHAR(64) PRIMARY KEY, " +
                "train_id VARCHAR(64), " +
                "type VARCHAR(50), " +
                "price DECIMAL(10, 2), " +
                "available_seats INT, " +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)");

        // tb_orders
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS tb_orders (" +
                "id VARCHAR(64) PRIMARY KEY, " +
                "user_id VARCHAR(64), " +
                "product_id VARCHAR(64), " +
                "product_name VARCHAR(255), " +
                "amount DECIMAL(10, 2), " +
                "status VARCHAR(20), " +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)");

        // attractions (handled by Mapper XML but let's ensure it here too for consistency, using the XML one mostly)
        attractionMapper.createTableIfNotExists();
    }

    private void seedData() {
        System.out.println("Seeding data...");

        // Users
        if (count("tb_users") == 0) {
            String pwd = PasswordUtils.hashPassword("123456");
            jdbcTemplate.update("INSERT INTO tb_users (id, email, phone, name, password_hash, auth_level) VALUES (?, ?, ?, ?, ?, ?)",
                    UUID.randomUUID().toString(), "admin@ctrip.com", "13800000000", "Admin", pwd, "ADMIN");
            jdbcTemplate.update("INSERT INTO tb_users (id, email, phone, name, password_hash, auth_level) VALUES (?, ?, ?, ?, ?, ?)",
                    UUID.randomUUID().toString(), "user@ctrip.com", "13900000001", "Test User", pwd, "USER");
            System.out.println("Seeded users.");
        }

        // Cities
        if (count("tb_cities") == 0) {
            jdbcTemplate.update("INSERT INTO tb_cities (id, name, country) VALUES (?, ?, ?)", "SH", "上海", "China");
            jdbcTemplate.update("INSERT INTO tb_cities (id, name, country) VALUES (?, ?, ?)", "BJ", "北京", "China");
            jdbcTemplate.update("INSERT INTO tb_cities (id, name, country) VALUES (?, ?, ?)", "GZ", "广州", "China");
        } else {
             // Ensure names are Chinese for consistency with frontend
            jdbcTemplate.update("UPDATE tb_cities SET name = '上海' WHERE id = 'SH'");
            jdbcTemplate.update("UPDATE tb_cities SET name = '北京' WHERE id = 'BJ'");
            jdbcTemplate.update("UPDATE tb_cities SET name = '广州' WHERE id = 'GZ'");
        }

        // Print cities for debug
        System.out.println("Current Cities in DB:");
        jdbcTemplate.query("SELECT * FROM tb_cities", (rs, rowNum) -> {
            System.out.println("  " + rs.getString("id") + ": " + rs.getString("name"));
            return null;
        });

        // Hotels
        if (count("tb_hotels") == 0) {
            String hotelId = UUID.randomUUID().toString();
            jdbcTemplate.update("INSERT INTO tb_hotels (id, name, address, city_id, city, rating, description) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    hotelId, "上海金茂君悦大酒店", "世纪大道88号金茂大厦", "SH", "上海", 5, "陆家嘴豪华酒店");
            
            jdbcTemplate.update("INSERT INTO tb_hotel_rooms (id, hotel_id, type, occupancy, price, available_count) VALUES (?, ?, ?, ?, ?, ?)",
                    UUID.randomUUID().toString(), hotelId, "豪华大床房", 2, new BigDecimal("1500"), 10);
            
            String hotelId2 = UUID.randomUUID().toString();
            jdbcTemplate.update("INSERT INTO tb_hotels (id, name, address, city_id, city, rating, description) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    hotelId2, "如家酒店(北京店)", "长安街1号", "BJ", "北京", 3, "经济舒适");
             jdbcTemplate.update("INSERT INTO tb_hotel_rooms (id, hotel_id, type, occupancy, price, available_count) VALUES (?, ?, ?, ?, ?, ?)",
                    UUID.randomUUID().toString(), hotelId2, "标准间", 2, new BigDecimal("300"), 20);
        }

        // Airports & Flights
        if (count("tb_airports") == 0) {
            jdbcTemplate.update("INSERT INTO tb_airports (code, name, city_id) VALUES (?, ?, ?)", "SHA", "上海虹桥机场", "SH");
            jdbcTemplate.update("INSERT INTO tb_airports (code, name, city_id) VALUES (?, ?, ?)", "PVG", "上海浦东机场", "SH");
            jdbcTemplate.update("INSERT INTO tb_airports (code, name, city_id) VALUES (?, ?, ?)", "PEK", "北京首都机场", "BJ");
            jdbcTemplate.update("INSERT INTO tb_airports (code, name, city_id) VALUES (?, ?, ?)", "PKX", "北京大兴机场", "BJ");
        }
        
        // Always re-seed flights to ensure valid dates for demo
        jdbcTemplate.update("DELETE FROM tb_flight_cabins");
        jdbcTemplate.update("DELETE FROM tb_flights");

        System.out.println("Seeding flights for next 7 days...");
        for (int i = 0; i < 7; i++) {
            long time = System.currentTimeMillis() + (i * 24L * 60 * 60 * 1000);
            Date depDate = new Date(time);
            
            // Beijing -> Shanghai (MU)
            String flightId1 = UUID.randomUUID().toString();
            jdbcTemplate.update("INSERT INTO tb_flights (id, number, airline, departure_airport_code, departure_airport, arrival_airport_code, arrival_airport, departure_time, arrival_time, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, DATE_ADD(?, INTERVAL 2 HOUR), 'Scheduled')",
                    flightId1, "MU510" + i, "东方航空", "PEK", "北京首都T3", "SHA", "上海虹桥T2", depDate, depDate);
             jdbcTemplate.update("INSERT INTO tb_flight_cabins (id, flight_id, type, price, available_seats) VALUES (?, ?, ?, ?, ?)",
                    UUID.randomUUID().toString(), flightId1, "Economy", new BigDecimal("800"), 100);

            // Beijing -> Shanghai (CA)
            String flightId2 = UUID.randomUUID().toString();
            jdbcTemplate.update("INSERT INTO tb_flights (id, number, airline, departure_airport_code, departure_airport, arrival_airport_code, arrival_airport, departure_time, arrival_time, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, DATE_ADD(?, INTERVAL 2 HOUR), 'Scheduled')",
                    flightId2, "CA150" + i, "中国国航", "PKX", "北京大兴", "SHA", "上海虹桥T2", depDate, depDate);
            jdbcTemplate.update("INSERT INTO tb_flight_cabins (id, flight_id, type, price, available_seats) VALUES (?, ?, ?, ?, ?)",
                    UUID.randomUUID().toString(), flightId2, "Economy", new BigDecimal("1200"), 50);

            // Shanghai -> Beijing (HU)
            String flightId3 = UUID.randomUUID().toString();
            jdbcTemplate.update("INSERT INTO tb_flights (id, number, airline, departure_airport_code, departure_airport, arrival_airport_code, arrival_airport, departure_time, arrival_time, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, DATE_ADD(?, INTERVAL 2 HOUR), 'Scheduled')",
                    flightId3, "HU760" + i, "海南航空", "SHA", "上海虹桥T2", "PEK", "北京首都T2", depDate, depDate);
             jdbcTemplate.update("INSERT INTO tb_flight_cabins (id, flight_id, type, price, available_seats) VALUES (?, ?, ?, ?, ?)",
                    UUID.randomUUID().toString(), flightId3, "Economy", new BigDecimal("850"), 80);
        }

        // Stations & Trains
        if (count("tb_stations") == 0) {
            jdbcTemplate.update("INSERT INTO tb_stations (id, name, city_id) VALUES (?, ?, ?)", "SHHQ", "Shanghai Hongqiao", "SH");
            jdbcTemplate.update("INSERT INTO tb_stations (id, name, city_id) VALUES (?, ?, ?)", "BJS", "Beijing South", "BJ");
        }

        if (count("tb_trains") == 0) {
            String trainId = UUID.randomUUID().toString();
            jdbcTemplate.update("INSERT INTO tb_trains (id, train_number, departure_station_id, arrival_station_id, departure_time, arrival_time, status) VALUES (?, ?, ?, ?, NOW(), DATE_ADD(NOW(), INTERVAL 5 HOUR), 'Scheduled')",
                    trainId, "G1", "SHHQ", "BJS");
            jdbcTemplate.update("INSERT INTO tb_train_seats (id, train_id, type, price, available_seats) VALUES (?, ?, ?, ?, ?)",
                    UUID.randomUUID().toString(), trainId, "Second Class", new BigDecimal("553"), 500);
        }

        // Attractions (Re-seed using the logic I just added previously)
        attractionMapper.deleteAll();
        seedAttractions();
    }

    private int count(String tableName) {
        try {
            return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + tableName, Integer.class);
        } catch (Exception e) {
            return 0;
        }
    }

    private void seedAttractions() {
        // Use local static images for reliability
        // These images are stored in src/main/resources/static/scenery/
        // Accessible via http://localhost:8080/scenery/x.jpg
        String baseUrl = "http://localhost:8080/scenery/";
        
        List<Attraction> list = Arrays.asList(
            new Attraction("1", "上海迪士尼乐园", new BigDecimal("399"), "主题乐园", 4.8, "20w+", baseUrl + "1.jpg"),
            new Attraction("2", "北京环球影城", new BigDecimal("450"), "热门打卡", 4.7, "15w+", baseUrl + "2.jpg"),
            new Attraction("3", "三亚亚特兰蒂斯水世界", new BigDecimal("288"), "亲子避暑", 4.9, "5w+", baseUrl + "3.jpg"),
            new Attraction("4", "成都大熊猫繁育基地", new BigDecimal("55"), "看熊猫", 4.9, "30w+", baseUrl + "4.jpg"),
            new Attraction("5", "西安秦始皇兵马俑", new BigDecimal("120"), "历史古迹", 4.9, "10w+", baseUrl + "5.jpg"),
            new Attraction("6", "杭州西湖风景区", new BigDecimal("0"), "自然风光", 4.8, "18w+", baseUrl + "6.jpg"),
            new Attraction("7", "云南丽江古城", new BigDecimal("50"), "古镇漫游", 4.6, "8w+", baseUrl + "7.jpg"),
            new Attraction("8", "厦门鼓浪屿", new BigDecimal("35"), "海岛风情", 4.7, "12w+", baseUrl + "8.jpg")
        );

        for (Attraction a : list) {
            attractionMapper.insertAttraction(a);
        }
        System.out.println("Seeding completed. Added " + list.size() + " attractions.");
    }
}
