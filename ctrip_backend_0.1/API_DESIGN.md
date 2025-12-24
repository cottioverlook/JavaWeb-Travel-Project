# API Design Outline

## 1. Train Management
### 1.1 Search Trains
- **Endpoint**: `GET /api/trains`
- **Parameters**:
    - `departure`: String (City name or Station name)
    - `arrival`: String (City name or Station name)
    - `date`: String (yyyy-MM-dd)
    - `sort`: String ("asc" | "desc") - Sort by departure time
    - `page`: Integer (default 1)
    - `size`: Integer (default 10)
- **Response**: `Result<List<TrainDTO>>`

### 1.2 Get Train Seats
- **Endpoint**: `GET /api/trains/{trainId}/seats`
- **Response**: `Result<List<TrainSeatDTO>>`

## 2. Flight Management
### 2.1 Search Flights
- **Endpoint**: `GET /api/flights`
- **Parameters**:
    - `departure`: String (City name or Airport name)
    - `arrival`: String (City name or Airport name)
    - `date`: String (yyyy-MM-dd)
    - `sort`: String ("asc" | "desc") - Sort by departure time
    - `page`: Integer (default 1)
    - `size`: Integer (default 10)
- **Response**: `Result<List<FlightDTO>>`

### 2.2 Get Flight Cabins
- **Endpoint**: `GET /api/flights/{flightId}/cabins`
- **Response**: `Result<List<FlightCabinDTO>>`

## 3. Search Management
### 3.1 Fuzzy Search Locations
- **Endpoint**: `GET /api/search/locations`
- **Parameters**:
    - `keyword`: String (Single character)
    - `page`: Integer (default 1)
    - `size`: Integer (default 10)
- **Response**: `Result<Map<String, List<Object>>>` (Returns matches for Cities, Stations, Airports)

## 4. Hotel Management
### 4.1 Search Hotels
- **Endpoint**: `GET /api/hotels`
- **Parameters**:
    - `city`: String (City name)
    - `rating`: Integer (optional)
    - `page`: Integer (default 1)
    - `size`: Integer (default 10)
- **Response**: `Result<List<HotelDTO>>`

### 4.2 Get Hotel Rooms
- **Endpoint**: `GET /api/hotels/{hotelId}/rooms`
- **Response**: `Result<List<HotelRoomDTO>>`
