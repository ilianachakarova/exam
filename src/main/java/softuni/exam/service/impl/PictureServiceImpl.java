package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.constants.GlobalConstants;
import softuni.exam.models.dtos.PictureSeedDto;
import softuni.exam.models.entities.Car;
import softuni.exam.models.entities.Picture;
import softuni.exam.repository.CarRepository;
import softuni.exam.repository.PictureRepository;
import softuni.exam.service.PictureService;
import softuni.exam.util.FileUtil;
import softuni.exam.util.ValidationUtil;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PictureServiceImpl implements PictureService {
    private final PictureRepository pictureRepository;
    private final CarRepository carRepository;
    private final FileUtil fileUtil;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;
@Autowired
    public PictureServiceImpl(PictureRepository pictureRepository, CarRepository carRepository, FileUtil fileUtil, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.pictureRepository = pictureRepository;
    this.carRepository = carRepository;
    this.fileUtil = fileUtil;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    this.gson = gson;
}

    @Override
    public boolean areImported() {
        return this.pictureRepository.count()>0;
    }

    @Override
    public String readPicturesFromFile() throws IOException {
        return this.fileUtil.readFile(GlobalConstants.PICTURES_FILE);
    }

    @Override
    public String importPictures() throws IOException {
        StringBuilder result = new StringBuilder();

        PictureSeedDto[] pictureSeedDtos =
                this.gson.fromJson(new FileReader(GlobalConstants.PICTURES_FILE), PictureSeedDto[].class);
        for (PictureSeedDto pictureSeedDto : pictureSeedDtos) {
            if(this.validationUtil.isValid(pictureSeedDto)){
                Picture picture = this.pictureRepository.findByName(pictureSeedDto.getName()).orElse(null);
                Car car = this.carRepository.getOne(Long.valueOf(pictureSeedDto.getCar()));
                if(picture == null && car!=null){
                   picture = this.modelMapper.map(pictureSeedDto, Picture.class);
                   picture.setDateAndTime(LocalDateTime.parse(pictureSeedDto.getDateAndTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                   picture.setCar(car);
                    System.out.println();
                   this.pictureRepository.saveAndFlush(picture);


                    result.append(String.format("Successfully imported picture - %s",pictureSeedDto.getName()));
                }else {
                    result.append("Invalid picture");
                }


            }else {
                result.append("Invalid picture");
            }
            result.append(System.lineSeparator());
        }
        return result.toString().trim();
    }
}
