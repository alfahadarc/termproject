package org.example;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class dbConfig {
    private static EntityManager em;

    private dbConfig(){

    }

    private static EntityManagerFactory createEntityManagerFactory(){
        return Persistence.createEntityManagerFactory("pu");
    }

    public static EntityManager getInstance(){
        return createEntityManagerFactory().createEntityManager();
    }
}
