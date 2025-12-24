package top.potatohub.ctrip.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightDTO {
    private String id;
    private String number;
    private String airline;
    private String aircraftType;
    private String departureAirport;
    private String departureAirportCode;
    private Date departureTime;
    private String arrivalAirport;
    private String arrivalAirportCode;
    private Date arrivalTime;
    private String status;

    public FlightDTO(Flight flight) {
        this.id = flight.getId();
        this.number = flight.getNumber();
        this.airline = flight.getAirline();
        this.aircraftType = flight.getAircraftType();
        this.departureAirport = flight.getDepartureAirport();
        this.departureAirportCode = flight.getDepartureAirportCode();
        this.departureTime = flight.getDepartureTime();
        this.arrivalAirport = flight.getArrivalAirport();
        this.arrivalAirportCode = flight.getArrivalAirportCode();
        this.arrivalTime = flight.getArrivalTime();
        this.status = flight.getStatus();
    }
}
