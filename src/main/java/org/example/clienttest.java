package org.example;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class clienttest {



    public static void main(String[] args) {

        Socket client;

        {
            try {
                client = new Socket("localhost", 1234);

                OutputStream outputStream = client.getOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);




                List<String> adminUser = new ArrayList<String>();
                adminUser.add("user1"); //first username
                adminUser.add("123"); //second password

                objectOutputStream.writeObject(adminUser);
                objectOutputStream.flush();

                try {
                    InputStream inputStream = client.getInputStream();
                    ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

                    List<String> userinfo = (List<String>) objectInputStream.readObject();  //same object name
                    for(String s: userinfo){
                        System.out.println(s);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }



            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
