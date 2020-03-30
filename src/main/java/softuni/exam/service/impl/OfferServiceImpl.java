package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softuni.exam.constants.GlobalConstants;
import softuni.exam.models.dtos.offers_dtos.OfferSeedDto;
import softuni.exam.models.dtos.offers_dtos.OfferSeedRootDto;
import softuni.exam.models.entities.Car;
import softuni.exam.models.entities.Offer;
import softuni.exam.models.entities.Seller;
import softuni.exam.repository.CarRepository;
import softuni.exam.repository.OfferRepository;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.OfferService;
import softuni.exam.util.FileUtil;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class OfferServiceImpl implements OfferService {
    private final OfferRepository offerRepository;
    private final FileUtil fileUtil;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;
    private final CarRepository carRepository;
    private final SellerRepository sellerRepository;

    @Autowired
    public OfferServiceImpl(OfferRepository offerRepository, FileUtil fileUtil, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser, CarRepository carRepository, SellerRepository sellerRepository) {
        this.offerRepository = offerRepository;
        this.fileUtil = fileUtil;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
        this.carRepository = carRepository;
        this.sellerRepository = sellerRepository;
    }

    @Override
    public boolean areImported() {
        return this.offerRepository.count()>0;
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return this.fileUtil.readFile(GlobalConstants.OFFERS_FILE);
    }
    @Transactional
    @Override
    public String importOffers() throws IOException, JAXBException {
        StringBuilder importResult = new StringBuilder();
        OfferSeedRootDto offerSeedRootDto = this.xmlParser.parseXml(OfferSeedRootDto.class, GlobalConstants.OFFERS_FILE);
        List<OfferSeedDto> offerSeedDtos = offerSeedRootDto.getOffers();

        for (OfferSeedDto offerSeedDto : offerSeedDtos) {
            if(validationUtil.isValid(offerSeedDto)){
                Car car = this.carRepository.findById(offerSeedDto.getCar().getId()).orElse(null);
                Seller seller = this.sellerRepository.findById(offerSeedDto.getSeller().getId()).orElse(null);
                LocalDateTime dateTime =
                        LocalDateTime.parse(offerSeedDto.getAddedOn(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                Offer offer = this.offerRepository.findByDescriptionAndAddedOn(offerSeedDto.getDescription(), dateTime).orElse(null);

                if(offer == null && car!=null && seller != null){
                    offer = this.modelMapper.map(offerSeedDto,Offer.class);
                    offer.setAddedOn(dateTime);
                    offer.setCar(car);
                    offer.setSeller(seller);

                    this.offerRepository.saveAndFlush(offer);
                    importResult.append(String.format("Successfully imported offer %s - %s", offer.getAddedOn().toString(), offer.isHasGoldStatus()));
                }else {
                    importResult.append("Invalid offer");
                }
            }else {
                importResult.append("Invalid offer");
            }

            importResult.append(System.lineSeparator());
        }

        return importResult.toString().trim();
    }
}
