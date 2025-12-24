package top.potatohub.ctrip.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightCabinDTO {
    private String id;
    private String flightId;
    private String type;
    private BigDecimal price;
    private Integer availableSeats;
    private String baggageAllowance;
    private String changePolicy;

    public FlightCabinDTO(FlightCabin cabin) {
        this.id = cabin.getId();
        this.flightId = cabin.getFlightId();
        this.type = cabin.getType();
        this.price = cabin.getPrice();
        this.availableSeats = cabin.getAvailableSeats();
        this.baggageAllowance = cabin.getBaggageAllowance();
        this.changePolicy = cabin.getChangePolicy();
    }
}
