package org.example;

import CommonClass.SharedCar;
import CommonClass.SharedUser;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{

    private static final String server_image_directory = "D:/server_image/";
    public static void main( String[] args )
    {
        System.out.println( "...start backend....." );


        final DBControllerUser dbControllerUser = DBControllerUser.getInstance();
        final DBControllerCar dbControllerCar = DBControllerCar.getInstance();
        //System.out.println(dbController);

        ServerSocket server = null;

        //server create
        try {
            server = new ServerSocket(4444);
            System.out.println("Waiting for client");
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true){
            try {
                final Socket socket = server.accept();
                System.out.println("Client joined");

                new Thread(){

                    @Override
                    public void run() {
                        try {
                            InputStream inputStream = socket.getInputStream();
                            OutputStream outputStream = socket.getOutputStream();
                            String role = null;
                            //System.out.println(role);
                            while(role == null)
                            {
                                role = roleChecker(dbControllerUser, inputStream, outputStream); //Checking for credentials

                                if(role != null)
                                {
                                    if(role.equalsIgnoreCase("Admin"))
                                    {
                                        adminControls(dbControllerUser, socket);
                                    }
                                    else if(role.equalsIgnoreCase("Viewer"))
                                    {
                                        viewControls(dbControllerCar,socket); //Viewer controls
                                    }
                                    else
                                    {
                                        manufacturerControls(dbControllerCar,socket); //manufacturer controls
                                    }
                                }

                                else
                                {
                                    role = roleChecker(dbControllerUser, inputStream, outputStream);
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private static void manufacturerControls(DBControllerCar dbControllerCar,Socket socket) {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            List<String> clientResponse = (List<String>) objectInputStream.readObject();  //Manufacturing client response

            if(clientResponse.get(0).equals("1")){//view all cars
                List<SharedCar> cars = dbControllerCar.findAllCars() ;
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectOutputStream.writeObject(cars);
                objectOutputStream.flush();
                manufacturerControls(dbControllerCar,socket);
            }
            else if(clientResponse.get(0).equals("2")){  //create new car
                ObjectInputStream objectInputStream_again = new ObjectInputStream(socket.getInputStream());
                SharedCar newCar = (SharedCar) objectInputStream_again.readObject();

                allCars isCar = dbControllerCar.searchByReg(newCar.getCar_reg());
                if(isCar == null){
                    String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                    String directory = server_image_directory + timeStamp + ".jpg";

                    FileOutputStream fileOutputStream = new FileOutputStream(directory);
                    fileOutputStream.write(newCar.getImage(), 0, newCar.getImage().length);
                    fileOutputStream.close();

                    allCars car = new allCars();
                    car.setCar_reg(newCar.getCar_reg()); //total 10-1 =9
                    car.setYear_made(newCar.getYear_made());
                    car.setColour1(newCar.getColour1());
                    car.setColour2(newCar.getColour2());
                    car.setColour3(newCar.getColour3());
                    car.setCar_make(newCar.getCar_make());
                    car.setCar_model(newCar.getCar_model());
                    car.setQuantity(newCar.getQuantity());
                    car.setPrice(newCar.getPrice());
                    car.setImage(directory);
                    if(dbControllerCar.createCar(car).equalsIgnoreCase("success")){
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        String response = "success";
                        objectOutputStream.writeObject(response);
                        objectOutputStream.flush();
                        manufacturerControls(dbControllerCar,socket);
                    }else{
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        String response = "failed";
                        objectOutputStream.writeObject(response);
                        objectOutputStream.flush();
                        manufacturerControls(dbControllerCar,socket);
                    }
                }else{
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    String response = "failed";
                    objectOutputStream.writeObject(response);
                    objectOutputStream.flush();
                    manufacturerControls(dbControllerCar,socket);
                }
            }else if(clientResponse.get(0).equals("3")){ //For edit
                ObjectInputStream objectInputStream_again = new ObjectInputStream(socket.getInputStream());
                String regNum = (String) objectInputStream_again.readObject();

                allCars carForEdit = dbControllerCar.searchByReg(regNum);
                if(carForEdit != null){
                    SharedCar carSend = new SharedCar();
                    carSend.setId(carForEdit.getId());
                    carSend.setCar_reg(carForEdit.getCar_reg());
                    carSend.setYear_made(carForEdit.getYear_made());
                    carSend.setColour1(carForEdit.getColour1());
                    carSend.setColour2(carForEdit.getColour2());
                    carSend.setColour3(carForEdit.getColour3());
                    carSend.setCar_make(carForEdit.getCar_make());
                    carSend.setCar_model(carForEdit.getCar_model());
                    carSend.setPrice(carForEdit.getPrice());
                    carSend.setQuantity(carForEdit.getQuantity());
                    try{
                        FileInputStream fileInputStream = new FileInputStream(carForEdit.getImage());
                        File file = new File(carForEdit.getImage());
                        carSend.setByteArraySize((int)file.length());
                        fileInputStream.read(carSend.getImage(), 0, carSend.getImage().length);
                        fileInputStream.close();
                        carSend.setImage(carSend.getImage());
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeObject(carSend);
                    objectOutputStream.flush();
                    //2nd response
                    try{
                        ObjectInputStream objectInputStream_again_again = new ObjectInputStream(socket.getInputStream());
                        SharedCar newCar = (SharedCar) objectInputStream_again_again.readObject();

                        allCars carForEditBack = new allCars();

                        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                        String directory = server_image_directory + timeStamp + ".jpg";

                        FileOutputStream fileOutputStream = new FileOutputStream(directory);
                        fileOutputStream.write(newCar.getImage(), 0, newCar.getImage().length);
                        fileOutputStream.close();

                        carForEditBack.setId(newCar.getId());
                        carForEditBack.setCar_reg(newCar.getCar_reg()); //total 10-1 =9
                        carForEditBack.setYear_made(newCar.getYear_made());
                        carForEditBack.setColour1(newCar.getColour1());
                        carForEditBack.setColour2(newCar.getColour2());
                        carForEditBack.setColour3(newCar.getColour3());
                        carForEditBack.setCar_make(newCar.getCar_make());
                        carForEditBack.setCar_model(newCar.getCar_model());
                        carForEditBack.setQuantity(newCar.getQuantity());
                        carForEditBack.setPrice(newCar.getPrice());
                        carForEditBack.setImage(directory);

                        String ans = dbControllerCar.editCar(carForEditBack);
                        ObjectOutputStream objectOutputStream_again_again = new ObjectOutputStream(socket.getOutputStream());
                        objectOutputStream_again_again.writeObject(ans);  // failed or success
                        objectOutputStream_again_again.flush();
                        manufacturerControls(dbControllerCar,socket);

                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }else{
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeObject("failed");
                    objectOutputStream.flush();
                    manufacturerControls(dbControllerCar,socket);

                }

            }
            else if(clientResponse.get(0).equals("4")){ //Remove a car
                ObjectInputStream objectInputStream_again = new ObjectInputStream(socket.getInputStream());
                String regNum = (String) objectInputStream_again.readObject();
                String result = dbControllerCar.removeCar(regNum);

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectOutputStream.writeObject(result);
                objectOutputStream.flush();
                manufacturerControls(dbControllerCar,socket);

            }
            } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void viewControls(DBControllerCar dbControllerCar, Socket socket) {
        try
        {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            List<String> clientResponse = (List<String>) objectInputStream.readObject(); //viewer client response

            if(clientResponse.get(0).equals("1")){ //All car
                List<SharedCar> cars = dbControllerCar.findAllCars() ;
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectOutputStream.writeObject(cars);
                objectOutputStream.flush();
                viewControls(dbControllerCar,socket);
            }
            else if(clientResponse.get(0).equals("2")){ //By Reg
                ObjectInputStream objectInputStream_again = new ObjectInputStream(socket.getInputStream());
                String reg_num = (String) objectInputStream_again.readObject();
                //System.out.println(reg_num);
                allCars car = dbControllerCar.searchByReg(reg_num);
                if(car != null){
                    SharedCar carSend = new SharedCar();
                    carSend.setId(car.getId());
                    carSend.setCar_reg(car.getCar_reg());
                    carSend.setYear_made(car.getYear_made());
                    carSend.setColour1(car.getColour1());
                    carSend.setColour2(car.getColour2());
                    carSend.setColour3(car.getColour3());
                    carSend.setCar_make(car.getCar_make());
                    carSend.setCar_model(car.getCar_model());
                    carSend.setPrice(car.getPrice());
                    carSend.setQuantity(car.getQuantity());
                try{
                    FileInputStream fileInputStream = new FileInputStream(car.getImage());
                    File file = new File(car.getImage());
                    carSend.setByteArraySize((int)file.length());
                    fileInputStream.read(carSend.getImage(), 0, carSend.getImage().length);
                    fileInputStream.close();
                    carSend.setImage(carSend.getImage());
                }catch (Exception e){
                    e.printStackTrace();
                }
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeObject(carSend);
                    objectOutputStream.flush();
                    viewControls(dbControllerCar,socket);
                }else{
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeObject(null);
                    objectOutputStream.flush();
                    viewControls(dbControllerCar,socket);
                }

            }else if(clientResponse.get(0).equals("3")){ //make , model
                ObjectInputStream objectInputStream_again = new ObjectInputStream(socket.getInputStream());
                List<String> make_model = (List<String>) objectInputStream_again.readObject();   //0= make 1=Model
                List<SharedCar> cars = dbControllerCar.searchByMakeModel(make_model);

                /*if(cars == null){
                    System.out.println("null" );
                }*/
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectOutputStream.writeObject(cars);
                objectOutputStream.flush();
                viewControls(dbControllerCar,socket);
            }else if(clientResponse.get(0).equals("4")){ //Buy a car
                ObjectInputStream objectInputStream1 = new ObjectInputStream(socket.getInputStream());
                String regNum = (String) objectInputStream1.readObject();
                allCars car = dbControllerCar.searchByReg(regNum);

                if(car.getQuantity() > 0){
                    String result = dbControllerCar.editCarQuantity(car);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeObject(result);
                    objectOutputStream.flush();
                    viewControls(dbControllerCar,socket);
                }else{
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeObject("failed");
                    objectOutputStream.flush();
                    viewControls(dbControllerCar,socket);
                }



            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private static void adminControls(DBControllerUser dbControllerUser, Socket socket) {
        try {
            ObjectInputStream  objectInputStream = new ObjectInputStream(socket.getInputStream());
            List<String> clientResponse = (List<String>) objectInputStream.readObject();

            if(clientResponse.get(0).equals("1")){  //create new user
                ObjectInputStream objectInputStream_again = new ObjectInputStream(socket.getInputStream());
                SharedUser newUser = (SharedUser) objectInputStream_again.readObject();

                allUsers user = new allUsers();
                user.setUsername(newUser.getUserName());
                user.setPassword(newUser.getPassword());
                user.setRole(newUser.getRole());

                if(dbControllerUser.createNewUser(user).equalsIgnoreCase("success")){
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    String response = "success";
                    objectOutputStream.writeObject(response);
                    objectOutputStream.flush();
                    adminControls(dbControllerUser,socket);
                }else{
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    String response = "failed";
                    objectOutputStream.writeObject(response);
                    objectOutputStream.flush();
                    adminControls(dbControllerUser,socket);
                }
            }
            else if(clientResponse.get(0).equals("2")){ //removing user
                //System.out.println("removed");
                ObjectInputStream objectInputStream_again = new ObjectInputStream(socket.getInputStream());
                List<String> clientResponse_again = (List<String>) objectInputStream.readObject();


                if(dbControllerUser.removeUserTest(clientResponse_again.get(0)).equalsIgnoreCase("success")){
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    String response = "success";
                    objectOutputStream.writeObject(response);
                    objectOutputStream.flush();
                    adminControls(dbControllerUser,socket);
                }else{
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    String response = "failed";
                    objectOutputStream.writeObject(response);
                    objectOutputStream.flush();
                    adminControls(dbControllerUser,socket);
                }

            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    private static String roleChecker(DBControllerUser dbControllerUser, InputStream inputStream, OutputStream outputStream) {

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            List<String> adminUser = (List<String>) objectInputStream.readObject();


            List<String> userinfo = dbControllerUser.findUser(adminUser.get(0), adminUser.get(1)); //o = user, 1= pass
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);


            //0=false means no error   true is error   1=username, 2=role
            if(userinfo.get(0).equals("false")){
               //admin, viewer, manufacturer

                if(userinfo.get(2).equalsIgnoreCase("admin")){
                       userinfo.add("Create new user");
                       userinfo.add("Remove user");
                }
                else if (userinfo.get(2).equalsIgnoreCase("viewer")){
                       userinfo.add("View all cars");
                       userinfo.add("Search car by registration number");
                       userinfo.add("Search car by make and model");
                       userinfo.add("Buy a car");
                }
                else if(userinfo.get(2).equalsIgnoreCase("manufacturer"))    {
                       userinfo.add("View all cars");
                       userinfo.add("Add a car");
                       userinfo.add("Update a car");
                       userinfo.add("Remove a car");
                }
            }

            //send response to client


            objectOutputStream.writeObject(userinfo);  //Sending to frontend
            objectOutputStream.flush();

            if(userinfo.get(0).equals("false")){
                return userinfo.get(2); //send role to programme
            }else{
                   return null;
            }


        } catch (Exception e) {
            e.printStackTrace();
            return "reset connection";
        }


    }
}
