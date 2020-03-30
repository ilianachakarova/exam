package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.constants.GlobalConstants;
import softuni.exam.models.dtos.SellerSeedDto;
import softuni.exam.models.dtos.SellerSeedRootDto;
import softuni.exam.models.entities.Rating;
import softuni.exam.models.entities.Seller;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.SellerService;
import softuni.exam.util.FileUtil;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;
    private final FileUtil fileUtil;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    @Autowired
    public SellerServiceImpl(SellerRepository sellerRepository, FileUtil fileUtil, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.sellerRepository = sellerRepository;
        this.fileUtil = fileUtil;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return this.sellerRepository.count()>0;
    }

    @Override
    public String readSellersFromFile() throws IOException {
        return this.fileUtil.readFile(GlobalConstants.SELLERS_FILE);
    }

    @Override
    public String importSellers() throws IOException, JAXBException {
        StringBuilder result = new StringBuilder();
        SellerSeedRootDto sellerSeedRootDto = this.xmlParser.parseXml(SellerSeedRootDto.class, GlobalConstants.SELLERS_FILE);
        List<SellerSeedDto> sellerSeedDtos = sellerSeedRootDto.getSellers();
        List<String> ratings = new ArrayList<>();
        ratings.add("BAD"); ratings.add("GOOD"); ratings.add("UNKNOWN");

        for (SellerSeedDto sellerSeedDto : sellerSeedDtos) {
            if(this.validationUtil.isValid(sellerSeedDto)){
                Seller seller = this.sellerRepository.findByEmail(sellerSeedDto.getEmail()).orElse(null);
                if(seller == null && ratings.contains(sellerSeedDto.getRating())){

                    seller = this.modelMapper.map(sellerSeedDto, Seller.class);
                    seller.setRating(Rating.valueOf(sellerSeedDto.getRating()));

                    this.sellerRepository.saveAndFlush(seller);
                    result.append(String.format("Successfully imported seller %s - %s",sellerSeedDto.getLastName(), sellerSeedDto.getEmail()));
                }else {
                    result.append("Invalid seller");
                }
            }else {
                result.append("Invalid seller");
            }
            result.append(System.lineSeparator());
        }
        return result.toString().trim();
    }
}
