package is.tech.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Value
@Builder
public class ClientAddress {
    String city;
    String streetName;
    int postalCode;
    int houseNumber;
    int apartmentNumber;
}
