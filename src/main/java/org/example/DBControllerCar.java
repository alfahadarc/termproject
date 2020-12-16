package org.example;

import CommonClass.SharedCar;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DBControllerCar {

    private static DBControllerCar dbControllerCar = null;

    EntityManager em = dbConfig.getInstance();

    public static DBControllerCar getInstance(){
        if(dbControllerCar == null){
            dbControllerCar = new DBControllerCar();
        }
        return dbControllerCar;
    }

    public synchronized String createCar(allCars newCar){
        if(!em.getTransaction().isActive()){
            em.getTransaction().begin();
        }

        try{
            em.persist(newCar);
            em.getTransaction().commit();
            return "success";
        }catch (Exception e){
            return "failed";
        }
    }

    public synchronized String editCar(allCars car){
        allCars car_new = em.find(allCars.class,car.getId());  //find all Rahi

        try{
            em.getTransaction().begin();
            car_new.setId(car.getId());
            car_new.setCar_reg(car.getCar_reg());
            car_new.setYear_made(car.getYear_made());
            car_new.setColour1(car.getColour1());
            car_new.setColour2(car.getColour2());
            car_new.setColour3(car.getColour3());
            car_new.setCar_make(car.getCar_make());
            car_new.setCar_model(car.getCar_model());
            car_new.setPrice(car.getPrice());
            car_new.setImage(car.getImage());
            car_new.setQuantity(car.getQuantity());
            em.getTransaction().commit();

            return "success";
        }catch (Exception e){
            e.printStackTrace();
            return "failed";
        }

    }
    public synchronized String editCarQuantity(allCars car){
        allCars car_new = em.find(allCars.class,car.getId());  //find all Rahi

        try{
            em.getTransaction().begin();
            car_new.setId(car.getId());
            car_new.setCar_reg(car.getCar_reg());
            car_new.setYear_made(car.getYear_made());
            car_new.setColour1(car.getColour1());
            car_new.setColour2(car.getColour2());
            car_new.setColour3(car.getColour3());
            car_new.setCar_make(car.getCar_make());
            car_new.setCar_model(car.getCar_model());
            car_new.setPrice(car.getPrice());
            car_new.setImage(car.getImage());
            car_new.setQuantity(car.getQuantity()-1);
            em.getTransaction().commit();

            return "success";
        }catch (Exception e){
            e.printStackTrace();
            return "failed";
        }
    }


    public synchronized allCars searchByReg(String reg_num) {
        allCars car = null;
        try{
            Query query = em.createQuery("SELECT x FROM allCars x WHERE x.car_reg = :carReg");

            query.setParameter("carReg", reg_num);
            car = (allCars) query.getSingleResult();

            return car;
        }catch (Exception e){
            return car;
        }


    }

    public synchronized List<SharedCar> findAllCars() {
        em.getTransaction().begin();
        List<allCars> cars= em.createQuery("SELECT c from allCars c").getResultList();
        em.getTransaction().commit();
        List<SharedCar> carList = new ArrayList<>();

        for(allCars s : cars)
        {
            SharedCar shareCar = manipulate(s);
            carList.add(shareCar);
        }

        return carList;
    }

    public synchronized List<SharedCar> searchByMakeModel(List<String> s){ //0= make 1=Model
        List<allCars> carsList= null;
        List<SharedCar> carList = new ArrayList<>();

        if(s.get(1).equalsIgnoreCase("ANY")){
            Query query = em.createQuery("SELECT x FROM allCars x WHERE x.car_make = :carMake");
            query.setParameter("carMake", s.get(0));

            carsList = query.getResultList();

            for(allCars c : carsList)
            {
                SharedCar shareCar = manipulate(c);
                carList.add(shareCar);
            }
            if(carList.isEmpty()){
                return null;
            }
        }else{
            Query query = em.createQuery("SELECT x FROM allCars x WHERE x.car_make = :carMake AND x.car_model =:carModel");
            query.setParameter("carMake", s.get(0) );
            query.setParameter("carModel",s.get(1));

            carsList = query.getResultList();

            for(allCars c : carsList)
            {
                SharedCar shareCar = manipulate(c);
                carList.add(shareCar);
            }
            if(carList.isEmpty()){
                return null;
            }

        }
        return carList;
    }

    private SharedCar manipulate(allCars s) {
        SharedCar shareCar = new SharedCar();

        shareCar.setSuccessful(true);
        shareCar.setId(s.getId());
        shareCar.setCar_reg(s.getCar_reg());
        shareCar.setYear_made(s.getYear_made());
        shareCar.setColour1(s.getColour1());
        shareCar.setColour2(s.getColour2());
        shareCar.setColour3(s.getColour3());
        shareCar.setCar_make(s.getCar_make());
        shareCar.setCar_model(s.getCar_model());
        shareCar.setPrice(s.getPrice());
        shareCar.setQuantity(s.getQuantity());

        try{
            File file = new File(s.getImage());
            FileInputStream fileInputStream = new FileInputStream(file.getPath());
            shareCar.setByteArraySize((int) file.length());
            fileInputStream.read(shareCar.getImage(), 0, shareCar.getImage().length);
            fileInputStream.close();
        } catch (IOException | NullPointerException e) {
            shareCar.setSuccessful(false);
            e.printStackTrace();
        }

        return shareCar;
    }

    public String removeCar(String regNum) {

        allCars car = searchByReg(regNum);

        if(car != null){
            em.getTransaction().begin();
            em.remove(car);
            em.getTransaction().commit();
            return "success";
        }else {
            return "failed";
        }
    }


}
