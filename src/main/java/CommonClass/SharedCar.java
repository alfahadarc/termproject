package CommonClass;

import java.io.Serializable;

public class SharedCar implements Serializable {

    private static final long serialVersionUID = 6529685098267757690L;
    private boolean isSuccessful;
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
    private byte[] image;

    public SharedCar() {
    }



    public SharedCar(String[] str, byte[] image){
        //this.isSuccessful = (boolean) str[0];
        this.id = Integer.parseInt(str[1]);
        this.car_reg = str[2];
        this.year_made =Integer.parseInt(str[3]);
        this.colour1 = str[4];
        this.colour2 = str[5];
        this.colour3 = str[6];
        this.car_make = str[7];
        this.car_model = str[8];
        this.price = Integer.parseInt(str[9]);
        this.quantity = Integer.parseInt(str[10]);
        this.image = image;
    }

    public long getSerialUID() {
        return serialVersionUID;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
    public void setByteArraySize(int size)
    {
        image = new byte[size];
    }
    public int getByteArraySize()
    {
        return image.length;
    }
}
