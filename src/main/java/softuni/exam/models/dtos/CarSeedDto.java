package softuni.exam.models.dtos;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class CarSeedDto {
    @Expose
    private String make;
    @Expose
    private String model;
    @Expose
    private Double kilometers;
    @Expose
    private String registeredOn;

    public CarSeedDto() {
    }
    @Size(min = 2, max = 19)
    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }
    @Size(min = 2, max = 19)
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
    @Positive
    public Double getKilometers() {
        return kilometers;
    }

    public void setKilometers(Double kilometers) {
        this.kilometers = kilometers;
    }

    public String getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(String registeredOn) {
        this.registeredOn = registeredOn;
    }
}
