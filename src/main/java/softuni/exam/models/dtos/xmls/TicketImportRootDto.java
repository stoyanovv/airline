package softuni.exam.models.dtos.xmls;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "tickets")
@XmlAccessorType(XmlAccessType.FIELD)
public class TicketImportRootDto {

    @XmlElement(name = "ticket")
    private List<TicketsImportDto> tickets;

    public TicketImportRootDto() {
    }

    public List<TicketsImportDto> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketsImportDto> tickets) {
        this.tickets = tickets;
    }
}
