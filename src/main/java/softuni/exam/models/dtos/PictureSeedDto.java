package softuni.exam.models.dtos;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.Size;

public class PictureSeedDto {
    @Expose
    private String name;
    @Expose
    private String dateAndTime;
    @Expose
    private Integer car;

    public PictureSeedDto() {
    }
    @Size(min = 2,max = 19)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public Integer getCar() {
        return car;
    }

    public void setCar(Integer car) {
        this.car = car;
    }
}
