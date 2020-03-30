package softuni.exam.models.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "cars")
public class Car extends BaseEntity{
    private String make;
    private String model;
    private Double kilometers;
    private LocalDate registeredOn;
    private List<Picture>pictures;
    private List<Offer>offers;

    public Car() {
    }
    @Column(name = "make")
    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }
    @Column(name = "model")
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
    @Column(name = "kilometers")
    public Double getKilometers() {
        return kilometers;
    }

    public void setKilometers(Double kilometers) {
        this.kilometers = kilometers;
    }
    @Column(name = "registered_on")
    public LocalDate getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(LocalDate registeredOn) {
        this.registeredOn = registeredOn;
    }
    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    public List<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }
    @OneToMany(mappedBy = "car",cascade = CascadeType.ALL)
    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }
}
