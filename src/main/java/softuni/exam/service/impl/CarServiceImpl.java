package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softuni.exam.constants.GlobalConstants;
import softuni.exam.models.dtos.CarSeedDto;
import softuni.exam.models.entities.Car;
import softuni.exam.repository.CarRepository;
import softuni.exam.service.CarService;
import softuni.exam.util.FileUtil;
import softuni.exam.util.ValidationUtil;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final FileUtil fileUtil;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;


@Autowired
    public CarServiceImpl(CarRepository carRepository, FileUtil fileUtil, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.carRepository = carRepository;
        this.fileUtil = fileUtil;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    this.gson = gson;
}

    @Override
    public boolean areImported() {
        return this.carRepository.count()>0;
    }

    @Override
    public String readCarsFileContent() throws IOException {
        return this.fileUtil.readFile(GlobalConstants.CARS_FILE);
    }

    @Override
    public String importCars() throws IOException {
    StringBuilder result = new StringBuilder();
        CarSeedDto[] carSeedDtos = this.gson.fromJson(new FileReader(GlobalConstants.CARS_FILE), CarSeedDto[].class);

        for (CarSeedDto carSeedDto : carSeedDtos) {
            if(this.validationUtil.isValid(carSeedDto)){
                Car car = this.carRepository.
                        findByMakeAndModelAndKilometers(carSeedDto.getMake(), carSeedDto.getModel(),
                                carSeedDto.getKilometers()).orElse(null);
                if(car == null){
                    car = this.modelMapper.map(carSeedDto, Car.class);
                    car.setRegisteredOn(LocalDate.parse(carSeedDto.getRegisteredOn(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    System.out.println();
                    this.carRepository.saveAndFlush(car);

                    result.append(String.format("Successfully imported car - %s - %s", carSeedDto.getMake(), carSeedDto.getModel()));
                }else {
                    result.append("Invalid car");
                }
            }else {
               result.append("Invalid car");
            }
            result.append(System.lineSeparator());
        }
        return result.toString().trim();
    }
    @Transactional
    @Override
    public String getCarsOrderByPicturesCountThenByMake() {
        StringBuilder sb = new StringBuilder();
       this.carRepository.orderByPicturesAndMake().forEach(c->{
           sb.append("Car make - ").append(c.getMake()).append(", model").append(c.getModel()).append(System.lineSeparator());
           sb.append("   Kilometers: ").append(c.getKilometers()).append(System.lineSeparator());
           sb.append("   Registered on: ").append(c.getRegisteredOn().toString()).append(System.lineSeparator());
           sb.append("   Number of pictures: ").append(c.getPictures().size()).append(System.lineSeparator());
       });
        return  sb.toString().trim();
    }
}
