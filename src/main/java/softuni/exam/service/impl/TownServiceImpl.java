package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.jsons.TownImportDto;
import softuni.exam.models.Town;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class TownServiceImpl implements TownService {

    private static final String TOWNS_PATH = "src/main/resources/files/json/towns.json";

    private final Gson gson;
    private final ModelMapper modelMapper;
    private final TownRepository townRepository;
    private final ValidationUtil validationUtil;

    @Autowired
    public TownServiceImpl(Gson gson, ModelMapper modelMapper, TownRepository townRepository, ValidationUtil validationUtil) {
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.townRepository = townRepository;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return this.townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        return Files.readString(Path.of(TOWNS_PATH));
    }

    @Override
    public String importTowns() throws IOException {
        StringBuilder sb = new StringBuilder();
        TownImportDto[] townImportDtos = this.gson.fromJson(this.readTownsFileContent(), TownImportDto[].class);
        for (TownImportDto townImportDto : townImportDtos) {
            if (this.validationUtil.isValid(townImportDto)) {
                if (this.townRepository.findByName(townImportDto.getName()) == null) {
                    Town town = this.modelMapper.map(townImportDto, Town.class);
                    this.townRepository.saveAndFlush(town);
                    sb.append(String.format("Successfully imported Town %s - %d", townImportDto.getName(), townImportDto.getPopulation()));
                }   else {
                    sb.append("Town already in DB");
                }
            }   else {
                sb.append("Invalid Town");
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }
}
