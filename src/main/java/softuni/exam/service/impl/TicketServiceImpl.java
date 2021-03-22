package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.Passenger;
import softuni.exam.models.Plane;
import softuni.exam.models.Ticket;
import softuni.exam.models.Town;
import softuni.exam.models.dtos.xmls.TicketImportRootDto;
import softuni.exam.models.dtos.xmls.TicketsImportDto;
import softuni.exam.repository.PassengerRepository;
import softuni.exam.repository.PlaneRepository;
import softuni.exam.repository.TicketRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TicketService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class TicketServiceImpl implements TicketService {

    private static final String TICKETS_PATH = "src/main/resources/files/xml/tickets.xml";

    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final TicketRepository ticketRepository;
    private final ValidationUtil validationUtil;
    private final PassengerRepository passengerRepository;
    private final PlaneRepository planeRepository;
    private final TownRepository townRepository;

    @Autowired
    public TicketServiceImpl(XmlParser xmlParser, ModelMapper modelMapper, TicketRepository ticketRepository, ValidationUtil validationUtil, PassengerRepository passengerRepository, PlaneRepository planeRepository, TownRepository townRepository) {
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.ticketRepository = ticketRepository;
        this.validationUtil = validationUtil;
        this.passengerRepository = passengerRepository;
        this.planeRepository = planeRepository;
        this.townRepository = townRepository;
    }

    @Override
    public boolean areImported() throws IOException {
        return this.ticketRepository.count() > 0;
    }

    @Override
    public String readTicketsFileContent() throws IOException {
        return Files.readString(Path.of(TICKETS_PATH));
    }

    @Override
    public String importTickets() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        TicketImportRootDto ticketImportRootDto = this.xmlParser.parseXml(TicketImportRootDto.class, TICKETS_PATH);
        System.out.println();
        for (TicketsImportDto ticketDto : ticketImportRootDto.getTickets()) {
            if (this.validationUtil.isValid(ticketDto)) {
                if (this.ticketRepository.findBySerialNumber(ticketDto.getSerialNumber()) == null) {
                    Ticket ticket = this.modelMapper.map(ticketDto, Ticket.class);
                    Plane plane = this.planeRepository.findByRegisterNumber(ticketDto.getPlaneRoot().getRegisterNumber());
                    Passenger passenger = this.passengerRepository.findByEmail(ticketDto.getPassengerRoot().getEmail());
                    Town fromTown = this.townRepository.findByName(ticketDto.getFromTownRoot().getName());
                    Town toTown = this.townRepository.findByName(ticketDto.getToTown().getName());
                    ticket.setPlane(plane);
                    ticket.setPassenger(passenger);
                    ticket.setFromTown(fromTown);
                    ticket.setToTown(toTown);
                    this.ticketRepository.saveAndFlush(ticket);
                    sb.append(String.format("Successfully imported Ticket %s - %s",  ticket.getFromTown().getName(), ticket.getToTown().getName()));
                }   else {
                    sb.append("Ticket is already in DB");
                }
            }   else {
                sb.append("Invalid Ticket");
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }
}
