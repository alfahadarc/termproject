package org.example;

import javax.persistence.*;

@Entity
public class allCars {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String car_reg;
    private int year_made;
    private String colour1;
    private String colour2;
    private String colour3;
    private String car_make;
    private String car_model;
    private int price;
    private int quantity;
    @Column(columnDefinition = "LONGTEXT")
    private String image;

    public allCars() {
    }

    public allCars(int id, String car_reg, int year_made, String colour1, String colour2, String colour3, String car_make, String car_model, int price, int quantity, String image) {
        this.id = id;
        this.car_reg = car_reg;
        this.year_made = year_made;
        this.colour1 = colour1;
        this.colour2 = colour2;
        this.colour3 = colour3;
        this.car_make = car_make;
        this.car_model = car_model;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCar_reg() {
        return car_reg;
    }

    public void setCar_reg(String car_reg) {
        this.car_reg = car_reg;
    }

    public int getYear_made() {
        return year_made;
    }

    public void setYear_made(int year_made) {
        this.year_made = year_made;
    }

    public String getColour1() {
        return colour1;
    }

    public void setColour1(String colour1) {
        this.colour1 = colour1;
    }

    public String getColour2() {
        return colour2;
    }

    public void setColour2(String colour2) {
        this.colour2 = colour2;
    }

    public String getColour3() {
        return colour3;
    }

    public void setColour3(String colour3) {
        this.colour3 = colour3;
    }

    public String getCar_make() {
        return car_make;
    }

    public void setCar_make(String car_make) {
        this.car_make = car_make;
    }

    public String getCar_model() {
        return car_model;
    }

    public void setCar_model(String car_model) {
        this.car_model = car_model;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
