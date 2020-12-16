package org.example;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class DBControllerUser {

    private static DBControllerUser dbControllerUser = null;

    EntityManager em = dbConfig.getInstance();

    public static DBControllerUser getInstance(){
        if(dbControllerUser == null){
            dbControllerUser = new DBControllerUser();
        }
        return dbControllerUser;
    }

    public synchronized String createNewUser(allUsers newUser){
        if(!em.getTransaction().isActive()){
            em.getTransaction().begin();
        }

        try{
            em.persist(newUser);
            em.getTransaction().commit();
            return "success";
        }catch (Exception e){
            return "failed";
        }

    }

    public void removeUser(allUsers user){

        em.getTransaction().begin();
        em.remove(user);
        em.getTransaction().commit();
    }

    public String removeUserTest(String userName){

        Query query = em.createQuery("SELECT x FROM allUsers x WHERE x.username = :username");

        query.setParameter("username", userName);
        List<allUsers> allUsersList = query.getResultList();
        try{
            for (allUsers a: allUsersList){
                em.getTransaction().begin();
                em.remove(a);
                em.getTransaction().commit();
            }
            return "success";
        }catch (Exception e){
            return "failed";
        }


    }


    public List<String> findUser(String username, String password) {

        Query query = em.createQuery("SELECT x FROM allUsers x WHERE x.username = :username");

        query.setParameter("username", username); //second username is for database
        List<allUsers> allUsersList = query.getResultList();

        for(allUsers a: allUsersList){
            if(a.getPassword().equals(password)){
                //System.out.println(a);

                List<String> userInfo = new ArrayList<>();

                userInfo.add("false");// error flag
                userInfo.add(a.getUsername());
                userInfo.add(a.getRole());

                return userInfo;

            }
        }
        List<String> userInfo = new ArrayList<>();
        userInfo.add("true"); //no user with password or username


        return userInfo;
    }

    //find user
}
