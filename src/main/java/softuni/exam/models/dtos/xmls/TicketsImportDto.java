package softuni.exam.models.dtos.xmls;

import org.hibernate.validator.constraints.Length;
import softuni.exam.config.LocalDateAdapter;
import softuni.exam.config.LocalDateTimeAdapter;

import javax.validation.constraints.DecimalMin;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@XmlRootElement(name = "ticket")
@XmlAccessorType(XmlAccessType.FIELD)
public class TicketsImportDto {

    @XmlElement(name = "serial-number")
    private String serialNumber;
    @XmlElement
    private BigDecimal price;
    @XmlElement(name = "take-off")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime takeoff;
    @XmlElement(name = "from-town")
    private FromTownRoot fromTownRoot;
    @XmlElement(name = "to-town")
    private ToTown toTown;
    @XmlElement(name = "passenger")
    private PassengerRoot passengerRoot;
    @XmlElement(name = "plane")
    private PlaneRoot planeRoot;

    public TicketsImportDto() {
    }

    @Length(min = 2)
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @DecimalMin(value = "0")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getTakeoff() {
        return takeoff;
    }

    public void setTakeoff(LocalDateTime takeoff) {
        this.takeoff = takeoff;
    }

    public FromTownRoot getFromTownRoot() {
        return fromTownRoot;
    }

    public void setFromTownRoot(FromTownRoot fromTownRoot) {
        this.fromTownRoot = fromTownRoot;
    }

    public ToTown getToTown() {
        return toTown;
    }

    public void setToTown(ToTown toTown) {
        this.toTown = toTown;
    }

    public PassengerRoot getPassengerRoot() {
        return passengerRoot;
    }

    public void setPassengerRoot(PassengerRoot passengerRoot) {
        this.passengerRoot = passengerRoot;
    }

    public PlaneRoot getPlaneRoot() {
        return planeRoot;
    }

    public void setPlaneRoot(PlaneRoot planeRoot) {
        this.planeRoot = planeRoot;
    }
}
