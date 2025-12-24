package top.potatohub.ctrip.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Flight {
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
    private Date createdAt;
    private Date updatedAt;
}
