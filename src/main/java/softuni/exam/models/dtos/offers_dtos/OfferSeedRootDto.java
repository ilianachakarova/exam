package softuni.exam.models.dtos.offers_dtos;

import softuni.exam.models.entities.Offer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "offers")
@XmlAccessorType(XmlAccessType.FIELD)
public class OfferSeedRootDto {
    @XmlElement(name = "offer")
    private List<OfferSeedDto>offers;

    public OfferSeedRootDto() {
    }

    public List<OfferSeedDto> getOffers() {
        return offers;
    }

    public void setOffers(List<OfferSeedDto> offers) {
        this.offers = offers;
    }
}
