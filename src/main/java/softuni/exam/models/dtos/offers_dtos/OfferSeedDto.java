package softuni.exam.models.dtos.offers_dtos;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlRootElement(name = "offer")
@XmlAccessorType(XmlAccessType.FIELD)
public class OfferSeedDto {
    @XmlElement(name = "description")
    private String description;
    @XmlElement
    private BigDecimal price;
    @XmlElement(name = "added-on")
    private String addedOn;
    @XmlElement(name = "has-gold-status")
    private boolean hasGoldenStatus;
    @XmlElement(name = "car")
    private CarOfferSeedDto car;
    @XmlElement(name = "seller")
    private SellerOfferSeedDto seller;

    public OfferSeedDto() {
    }
    @NotNull
    @Size(min = 5)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @Positive
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    @NotNull
    public String getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }

    public boolean isHasGoldenStatus() {
        return hasGoldenStatus;
    }

    public void setHasGoldenStatus(boolean hasGoldenStatus) {
        this.hasGoldenStatus = hasGoldenStatus;
    }

    public CarOfferSeedDto getCar() {
        return car;
    }

    public void setCar(CarOfferSeedDto car) {
        this.car = car;
    }

    public SellerOfferSeedDto getSeller() {
        return seller;
    }

    public void setSeller(SellerOfferSeedDto seller) {
        this.seller = seller;
    }
}
