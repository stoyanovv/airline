package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.Plane;
import softuni.exam.models.dtos.xmls.PlaneImportDto;
import softuni.exam.models.dtos.xmls.PlaneImportRootDto;
import softuni.exam.repository.PlaneRepository;
import softuni.exam.service.PlaneService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class PlaneServiceImpl implements PlaneService {

    private static final String PLANES_PATH = "src/main/resources/files/xml/planes.xml";

    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final PlaneRepository planeRepository;
    private final ValidationUtil validationUtil;

    @Autowired
    public PlaneServiceImpl(XmlParser xmlParser, ModelMapper modelMapper, PlaneRepository planeRepository, ValidationUtil validationUtil) {
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.planeRepository = planeRepository;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return this.planeRepository.count() > 0;
    }

    @Override
    public String readPlanesFileContent() throws IOException {
        return Files.readString(Path.of(PLANES_PATH));
    }

    @Override
    public String importPlanes() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        PlaneImportRootDto planeImportRootDto = this.xmlParser.parseXml(PlaneImportRootDto.class, PLANES_PATH);
        for (PlaneImportDto planeDto : planeImportRootDto.getPlanes()) {
            if (this.validationUtil.isValid(planeDto)) {
                if (this.planeRepository.findByRegisterNumber(planeDto.getRegisterNumber()) == null) {
                    Plane plane = this.modelMapper.map(planeDto, Plane.class);
                    this.planeRepository.saveAndFlush(plane);
                    sb.append(String.format("Successfully imported Plane %s", plane.getRegisterNumber()));
                }   else {
                    sb.append("Plane already in DB");
                }
            }   else {
                sb.append("Invalid Plane");
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }
}
