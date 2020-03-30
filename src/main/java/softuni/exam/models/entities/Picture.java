package softuni.exam.models.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pictures")
public class Picture extends BaseEntity {
    private String name;
    private LocalDateTime dateAndTime;
    private Car car;
    private List<Offer>offers;

    public Picture() {
    }
    @Column(name = "name", unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "date_and_time")
    public LocalDateTime getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(LocalDateTime dateAndTime) {
        this.dateAndTime = dateAndTime;
    }
    @ManyToOne(cascade = CascadeType.ALL)
    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }
    @ManyToMany(mappedBy = "pictures",fetch = FetchType.EAGER, cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }
}
