package com.lyschev;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DataBase {
    public static EntityManagerFactory userEntityManagerFactory = Persistence.createEntityManagerFactory("UserEntity");
    public static EntityManager userEM = userEntityManagerFactory.createEntityManager();

    public static EntityManagerFactory pointsEntityManagerFactory = Persistence.createEntityManagerFactory("PointEntity");
    public static EntityManager pointEM = pointsEntityManagerFactory.createEntityManager();
}
