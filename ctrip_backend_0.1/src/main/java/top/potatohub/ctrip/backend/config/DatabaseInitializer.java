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
import java.util.Random;

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

        // tb_reviews
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS tb_reviews (" +
                "id VARCHAR(64) PRIMARY KEY, " +
                "user_id VARCHAR(64), " +
                "order_id VARCHAR(64), " +
                "product_id VARCHAR(64), " +
                "score DECIMAL(3, 1), " +
                "content TEXT, " +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)");

        // attractions
        attractionMapper.createTableIfNotExists();
    }

    private void seedData() {
        System.out.println("Seeding rich data...");

        seedUsers();
        seedCities();
        seedHotels();
        seedAirports();
        seedStations();
        
        // Dynamic data - always refresh to keep dates current
        refreshFlights();
        refreshTrains();
        
        // Attractions
        refreshAttractions();
        
        System.out.println("Data seeding completed.");
    }

    private void seedUsers() {
        if (count("tb_users") == 0) {
            String pwd = PasswordUtils.hashPassword("123456");
            jdbcTemplate.update("INSERT INTO tb_users (id, email, phone, name, password_hash, auth_level) VALUES (?, ?, ?, ?, ?, ?)",
                    UUID.randomUUID().toString(), "admin@ctrip.com", "13800000000", "Admin", pwd, "ADMIN");
            jdbcTemplate.update("INSERT INTO tb_users (id, email, phone, name, password_hash, auth_level) VALUES (?, ?, ?, ?, ?, ?)",
                    UUID.randomUUID().toString(), "user@ctrip.com", "13900000001", "Test User", pwd, "USER");
            System.out.println("Seeded users.");
        }
    }

    private void seedCities() {
        // Domestic
        upsertCity("SH", "上海", "China");
        upsertCity("BJ", "北京", "China");
        upsertCity("GZ", "广州", "China");
        upsertCity("SZ", "深圳", "China");
        upsertCity("CD", "成都", "China");
        upsertCity("HZ", "杭州", "China");
        upsertCity("XA", "西安", "China");
        upsertCity("CQ", "重庆", "China");
        upsertCity("NJ", "南京", "China");
        upsertCity("WH", "武汉", "China");
        upsertCity("SY", "三亚", "China");
        upsertCity("LJ", "丽江", "China");
        upsertCity("XM", "厦门", "China");
        upsertCity("CS", "长沙", "China");
        upsertCity("QD", "青岛", "China");
        
        // International
        upsertCity("TYO", "东京", "Japan");
        upsertCity("SEL", "首尔", "South Korea");
        upsertCity("SIN", "新加坡", "Singapore");
        upsertCity("BKK", "曼谷", "Thailand");
        upsertCity("LON", "伦敦", "UK");
        upsertCity("NYC", "纽约", "USA");
        upsertCity("PAR", "巴黎", "France");
        upsertCity("DXB", "迪拜", "UAE");
        
        System.out.println("Seeded/Updated cities.");
    }

    private void upsertCity(String id, String name, String country) {
        if (countCities(id) == 0) {
            jdbcTemplate.update("INSERT INTO tb_cities (id, name, country) VALUES (?, ?, ?)", id, name, country);
        } else {
            jdbcTemplate.update("UPDATE tb_cities SET name = ?, country = ? WHERE id = ?", name, country, id);
        }
    }

    private void seedHotels() {
        if (count("tb_hotels") > 0) return;

        String[] hotelTypes = {"豪华型", "高档型", "舒适型", "经济型"};
        Random rand = new Random();
        
        // Fetch all city IDs
        List<String> cityIds = jdbcTemplate.queryForList("SELECT id FROM tb_cities", String.class);
        
        for (String cityId : cityIds) {
            String cityName = jdbcTemplate.queryForObject("SELECT name FROM tb_cities WHERE id = ?", String.class, cityId);
            
            // Create 3-5 hotels per city
            int numHotels = 3 + rand.nextInt(3);
            for (int i = 0; i < numHotels; i++) {
                String hotelId = UUID.randomUUID().toString();
                String name = cityName + (i == 0 ? "国际大酒店" : (i == 1 ? "度假村" : "快捷酒店 " + (i+1)));
                int rating = 5 - (i > 1 ? i - 1 : 0);
                if (rating < 2) rating = 2;
                
                jdbcTemplate.update("INSERT INTO tb_hotels (id, name, address, city_id, city, rating, description) VALUES (?, ?, ?, ?, ?, ?, ?)",
                        hotelId, name, cityName + "市中心路" + (i+1) + "号", cityId, cityName, rating, "这是一个位于" + cityName + "的" + (rating > 4 ? "豪华" : "舒适") + "酒店");
                
                // Rooms
                createHotelRooms(hotelId);
            }
        }
        System.out.println("Seeded hotels.");
    }

    private void createHotelRooms(String hotelId) {
        String[][] roomTypes = {
            {"标准间", "300"}, {"大床房", "400"}, {"豪华套房", "800"}, {"总统套房", "2000"}
        };
        
        for (String[] type : roomTypes) {
            jdbcTemplate.update("INSERT INTO tb_hotel_rooms (id, hotel_id, type, occupancy, price, available_count) VALUES (?, ?, ?, ?, ?, ?)",
                    UUID.randomUUID().toString(), hotelId, type[0], 2, new BigDecimal(type[1]), 20);
        }
    }

    private void seedAirports() {
         if (count("tb_airports") > 0) return;

         // Map city ID to airports
         addAirport("SHA", "上海虹桥机场", "SH");
         addAirport("PVG", "上海浦东机场", "SH");
         addAirport("PEK", "北京首都机场", "BJ");
         addAirport("PKX", "北京大兴机场", "BJ");
         addAirport("CAN", "广州白云机场", "GZ");
         addAirport("SZX", "深圳宝安机场", "SZ");
         addAirport("CTU", "成都双流机场", "CD");
         addAirport("TFU", "成都天府机场", "CD");
         addAirport("HGH", "杭州萧山机场", "HZ");
         addAirport("XIY", "西安咸阳机场", "XA");
         addAirport("CKG", "重庆江北机场", "CQ");
         addAirport("NKG", "南京禄口机场", "NJ");
         addAirport("WUH", "武汉天河机场", "WH");
         addAirport("SYX", "三亚凤凰机场", "SY");
         addAirport("LJG", "丽江三义机场", "LJ");
         addAirport("XMN", "厦门高崎机场", "XM");
         addAirport("CSX", "长沙黄花机场", "CS");
         addAirport("TAO", "青岛胶东机场", "QD");
         
         // International
         addAirport("NRT", "东京成田机场", "TYO");
         addAirport("HND", "东京羽田机场", "TYO");
         addAirport("ICN", "首尔仁川机场", "SEL");
         addAirport("SIN", "新加坡樟宜机场", "SIN");
         addAirport("BKK", "曼谷素万那普机场", "BKK");
         addAirport("LHR", "伦敦希思罗机场", "LON");
         addAirport("JFK", "纽约肯尼迪机场", "NYC");
         addAirport("CDG", "巴黎戴高乐机场", "PAR");
         addAirport("DXB", "迪拜国际机场", "DXB");
         
         System.out.println("Seeded airports.");
    }

    private void addAirport(String code, String name, String cityId) {
        jdbcTemplate.update("INSERT INTO tb_airports (code, name, city_id) VALUES (?, ?, ?)", code, name, cityId);
    }

    private void seedStations() {
        if (count("tb_stations") > 0) return;
        
        addStation("SHHQ", "上海虹桥", "SH");
        addStation("SHS", "上海南", "SH");
        addStation("BJS", "北京南", "BJ");
        addStation("BJX", "北京西", "BJ");
        addStation("GZN", "广州南", "GZ");
        addStation("SZN", "深圳北", "SZ");
        addStation("CDE", "成都东", "CD");
        addStation("HZE", "杭州东", "HZ");
        addStation("XAN", "西安北", "XA");
        addStation("CQN", "重庆北", "CQ");
        addStation("NJN", "南京南", "NJ");
        addStation("WHN", "武汉", "WH");
        addStation("CSN", "长沙南", "CS");
        
        System.out.println("Seeded stations.");
    }

    private void addStation(String id, String name, String cityId) {
        jdbcTemplate.update("INSERT INTO tb_stations (id, name, city_id) VALUES (?, ?, ?)", id, name, cityId);
    }

    private void refreshFlights() {
        jdbcTemplate.update("DELETE FROM tb_flight_cabins");
        jdbcTemplate.update("DELETE FROM tb_flights");
        
        System.out.println("Seeding flights...");
        
        String[] hubs = {"SHA", "PVG", "PEK", "PKX", "CAN", "SZX", "CTU", "HGH"};
        String[] intl = {"NRT", "ICN", "SIN", "BKK", "LHR", "JFK", "CDG", "DXB"};
        
        for (int i = 0; i < 14; i++) {
            long timeBase = System.currentTimeMillis() + (i * 24L * 60 * 60 * 1000);
            
            for (String from : hubs) {
                for (String to : hubs) {
                    if (from.equals(to)) continue;
                    if (Math.random() > 0.8) createFlight(from, to, timeBase);
                }
                
                if (from.equals("PVG") || from.equals("PEK") || from.equals("CAN")) {
                    for (String to : intl) {
                         if (Math.random() > 0.7) createFlight(from, to, timeBase);
                         if (Math.random() > 0.7) createFlight(to, from, timeBase);
                    }
                }
            }
        }
    }

    private void createFlight(String fromCode, String toCode, long dateBase) {
        Random r = new Random();
        long departureTime = dateBase + (r.nextInt(14) + 6) * 3600 * 1000;
        Date depDate = new Date(departureTime);
        
        String[] airlines = {"MU", "CA", "CZ", "HU", "ZH", "MF"};
        String airlineCode = airlines[r.nextInt(airlines.length)];
        String flightNo = airlineCode + (1000 + r.nextInt(9000));
        String airlineName = getAirlineName(airlineCode);
        
        String fromName = getAirportName(fromCode);
        String toName = getAirportName(toCode);
        
        String flightId = UUID.randomUUID().toString();
        
        jdbcTemplate.update("INSERT INTO tb_flights (id, number, airline, departure_airport_code, departure_airport, arrival_airport_code, arrival_airport, departure_time, arrival_time, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, DATE_ADD(?, INTERVAL ? HOUR), 'Scheduled')",
                flightId, flightNo, airlineName, fromCode, fromName, toCode, toName, depDate, depDate, 2 + r.nextInt(10));

        jdbcTemplate.update("INSERT INTO tb_flight_cabins (id, flight_id, type, price, available_seats) VALUES (?, ?, ?, ?, ?)",
                UUID.randomUUID().toString(), flightId, "Economy", new BigDecimal(500 + r.nextInt(2000)), 100 + r.nextInt(50));
        jdbcTemplate.update("INSERT INTO tb_flight_cabins (id, flight_id, type, price, available_seats) VALUES (?, ?, ?, ?, ?)",
                UUID.randomUUID().toString(), flightId, "Business", new BigDecimal(2000 + r.nextInt(5000)), 20);
    }
    
    private String getAirlineName(String code) {
        switch (code) {
            case "MU": return "东方航空";
            case "CA": return "中国国航";
            case "CZ": return "南方航空";
            case "HU": return "海南航空";
            case "ZH": return "深圳航空";
            case "MF": return "厦门航空";
            default: return "未知航空";
        }
    }
    
    private String getAirportName(String code) {
        try {
            return jdbcTemplate.queryForObject("SELECT name FROM tb_airports WHERE code = ?", String.class, code);
        } catch (Exception e) { return code; }
    }

    private void refreshTrains() {
        jdbcTemplate.update("DELETE FROM tb_train_seats");
        jdbcTemplate.update("DELETE FROM tb_trains");
        
        System.out.println("Seeding trains...");
        String[] stations = {"SHHQ", "BJS", "GZN", "SZN", "HZE", "NJN", "WHN"};
        
        for (int i = 0; i < 14; i++) {
            long timeBase = System.currentTimeMillis() + (i * 24L * 60 * 60 * 1000);
             for (String from : stations) {
                for (String to : stations) {
                    if (from.equals(to)) continue;
                    if (Math.random() > 0.8) createTrain(from, to, timeBase);
                }
             }
        }
    }

    private void createTrain(String fromId, String toId, long dateBase) {
        Random r = new Random();
        long departureTime = dateBase + (r.nextInt(14) + 6) * 3600 * 1000;
        Date depDate = new Date(departureTime);
        
        String trainNo = "G" + (1 + r.nextInt(500));
        String fromName = getStationName(fromId);
        String toName = getStationName(toId);
        
        String trainId = UUID.randomUUID().toString();
        
        jdbcTemplate.update("INSERT INTO tb_trains (id, train_number, departure_station_id, arrival_station_id, departure_time, arrival_time, status) VALUES (?, ?, ?, ?, ?, DATE_ADD(?, INTERVAL ? HOUR), 'Scheduled')",
                trainId, trainNo, fromId, toId, depDate, depDate, 1 + r.nextInt(5));

        jdbcTemplate.update("INSERT INTO tb_train_seats (id, train_id, type, price, available_seats) VALUES (?, ?, ?, ?, ?)",
                UUID.randomUUID().toString(), trainId, "二等座", new BigDecimal(200 + r.nextInt(500)), 500);
        jdbcTemplate.update("INSERT INTO tb_train_seats (id, train_id, type, price, available_seats) VALUES (?, ?, ?, ?, ?)",
                UUID.randomUUID().toString(), trainId, "一等座", new BigDecimal(500 + r.nextInt(800)), 100);
    }
    
    private String getStationName(String id) {
        try {
            return jdbcTemplate.queryForObject("SELECT name FROM tb_stations WHERE id = ?", String.class, id);
        } catch (Exception e) { return id; }
    }

    private void refreshAttractions() {
        attractionMapper.deleteAll();
        
        String baseUrl = "http://localhost:8080/scenery/";
        
        List<Attraction> list = Arrays.asList(
            new Attraction("1", "上海迪士尼乐园", new BigDecimal("399"), "主题乐园", 4.8, "20w+", baseUrl + "1.jpg", "SH"),
            new Attraction("2", "北京环球影城", new BigDecimal("450"), "热门打卡", 4.7, "15w+", baseUrl + "2.jpg", "BJ"),
            new Attraction("3", "三亚亚特兰蒂斯水世界", new BigDecimal("288"), "亲子避暑", 4.9, "5w+", baseUrl + "3.jpg", "SY"),
            new Attraction("4", "成都大熊猫繁育基地", new BigDecimal("55"), "看熊猫", 4.9, "30w+", baseUrl + "4.jpg", "CD"),
            new Attraction("5", "西安秦始皇兵马俑", new BigDecimal("120"), "历史古迹", 4.9, "10w+", baseUrl + "5.jpg", "XA"),
            new Attraction("6", "杭州西湖风景区", new BigDecimal("0"), "自然风光", 4.8, "18w+", baseUrl + "6.jpg", "HZ"),
            new Attraction("7", "云南丽江古城", new BigDecimal("50"), "古镇漫游", 4.6, "8w+", baseUrl + "7.jpg", "LJ"),
            new Attraction("8", "厦门鼓浪屿", new BigDecimal("35"), "海岛风情", 4.7, "12w+", baseUrl + "8.jpg", "XM"),
            new Attraction("9", "广州塔", new BigDecimal("150"), "城市地标", 4.6, "10w+", baseUrl + "1.jpg", "GZ"),
            new Attraction("10", "深圳世界之窗", new BigDecimal("220"), "主题乐园", 4.5, "8w+", baseUrl + "2.jpg", "SZ"),
            new Attraction("11", "重庆洪崖洞", new BigDecimal("0"), "夜景打卡", 4.8, "25w+", baseUrl + "3.jpg", "CQ"),
            new Attraction("12", "南京夫子庙", new BigDecimal("0"), "历史文化", 4.6, "15w+", baseUrl + "4.jpg", "NJ"),
            new Attraction("13", "武汉黄鹤楼", new BigDecimal("70"), "名胜古迹", 4.5, "9w+", baseUrl + "5.jpg", "WH"),
            new Attraction("14", "青岛栈桥", new BigDecimal("0"), "海滨风光", 4.7, "11w+", baseUrl + "6.jpg", "QD"),
            new Attraction("15", "长沙橘子洲", new BigDecimal("0"), "红色旅游", 4.8, "13w+", baseUrl + "7.jpg", "CS"),
            new Attraction("16", "东京塔", new BigDecimal("180"), "城市地标", 4.7, "5w+", baseUrl + "8.jpg", "TYO"),
            new Attraction("17", "首尔塔", new BigDecimal("60"), "浪漫夜景", 4.6, "4w+", baseUrl + "1.jpg", "SEL"),
            new Attraction("18", "新加坡鱼尾狮公园", new BigDecimal("0"), "地标打卡", 4.8, "6w+", baseUrl + "2.jpg", "SIN"),
            new Attraction("19", "曼谷大皇宫", new BigDecimal("100"), "佛教文化", 4.7, "7w+", baseUrl + "3.jpg", "BKK"),
            new Attraction("20", "伦敦眼", new BigDecimal("250"), "城市全景", 4.6, "5w+", baseUrl + "4.jpg", "LON"),
            new Attraction("21", "纽约自由女神像", new BigDecimal("180"), "世界地标", 4.8, "8w+", baseUrl + "5.jpg", "NYC"),
            new Attraction("22", "巴黎埃菲尔铁塔", new BigDecimal("200"), "浪漫地标", 4.9, "10w+", baseUrl + "6.jpg", "PAR"),
            new Attraction("23", "迪拜哈利法塔", new BigDecimal("350"), "世界最高", 4.9, "9w+", baseUrl + "7.jpg", "DXB")
        );

        for (Attraction a : list) {
            attractionMapper.insertAttraction(a);
        }
        System.out.println("Seeding attractions.");
    }

    private int count(String tableName) {
        try {
            return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + tableName, Integer.class);
        } catch (Exception e) {
            return 0;
        }
    }

    private int countCities(String id) {
        try {
            return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM tb_cities WHERE id = ?", Integer.class, id);
        } catch (Exception e) {
            return 0;
        }
    }
}
