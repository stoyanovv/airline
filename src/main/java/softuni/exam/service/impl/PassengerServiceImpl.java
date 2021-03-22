package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.Passenger;
import softuni.exam.models.Town;
import softuni.exam.models.dtos.jsons.PassengerImportDto;
import softuni.exam.repository.PassengerRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.PassengerService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

@Service
public class PassengerServiceImpl implements PassengerService {

    private static final String PASSENGERS_PATH = "src/main/resources/files/json/passengers.json";

    private final Gson gson;
    private final ModelMapper modelMapper;
    private final PassengerRepository passengerRepository;
    private final ValidationUtil validationUtil;
    private final TownRepository townRepository;

    @Autowired
    public PassengerServiceImpl(Gson gson, ModelMapper modelMapper, PassengerRepository passengerRepository, ValidationUtil validationUtil, TownRepository townRepository) {
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.passengerRepository = passengerRepository;
        this.validationUtil = validationUtil;
        this.townRepository = townRepository;
    }

    @Override
    public boolean areImported() {
        return this.passengerRepository.count() > 0;
    }

    @Override
    public String readPassengersFileContent() throws IOException {
        return Files.readString(Path.of(PASSENGERS_PATH));
    }

    @Override
    public String importPassengers() throws IOException {
        StringBuilder sb = new StringBuilder();
        PassengerImportDto[] passengerImportDtos = this.gson.fromJson(this.readPassengersFileContent(), PassengerImportDto[].class);
        for (PassengerImportDto passengerImportDto : passengerImportDtos) {
            if (this.validationUtil.isValid(passengerImportDto)) {
                if (this.townRepository.findByName(passengerImportDto.getTown()) != null) {
                    Passenger passenger = this.modelMapper.map(passengerImportDto, Passenger.class);
                    Town town = this.townRepository.findByName(passengerImportDto.getTown());
                    passenger.setTown(town);
                    this.passengerRepository.saveAndFlush(passenger);
                    sb.append(String.format("Successfully imported Passenger %s - %s", passenger.getLastName(), passenger.getEmail()));
                }   else {
                    sb.append("Town is not in DB");
                }
            }   else {
                sb.append("Invalid Passenger");
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    @Override
    public String getPassengersOrderByTicketsCountDescendingThenByEmail() {
        StringBuilder sb = new StringBuilder();
        Set<Passenger> passengersOrdered = this.passengerRepository.getPassengersOrdered();
        for (Passenger passenger : passengersOrdered) {
            sb.append(String.format("Passenger %s  %s\n" +
                    "\tEmail - %s\n" +
                    "\tPhone - %s\n" +
                    "\tNumber of tickets - %s", passenger.getFirstName(), passenger.getLastName(), passenger.getEmail(), passenger.getPhoneNumber(),
                    passenger.getTickets().size())).append(System.lineSeparator());
        }
        return sb.toString();
    }
}
