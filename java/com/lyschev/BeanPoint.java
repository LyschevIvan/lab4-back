package com.lyschev;

import javax.ejb.Stateless;
import java.util.List;


@Stateless
public class BeanPoint {

    public void addPoint(Double x, Double y, Double r) {
        PointEntity p = new PointEntity();
        p.setX(x);
        p.setY(y);
        p.setR(r);
        p.setIsIn();
        DataBase.pointEM.getTransaction().begin();
        DataBase.pointEM.persist(p);
        DataBase.pointEM.flush();
        DataBase.pointEM.getTransaction().commit();
    }

    public List<PointEntity> getPoints() {
        return DataBase.pointEM.createQuery("select e from PointEntity e").getResultList();
    }

    public void deletePoint(int key){
        DataBase.pointEM.getTransaction().begin();
        PointEntity pointEntity =  DataBase.pointEM.find(PointEntity.class, key);
        DataBase.pointEM.remove(pointEntity);
        DataBase.pointEM.flush();
        DataBase.pointEM.getTransaction().commit();
    }

    public void clearPoints(){
        DataBase.pointEM.getTransaction().begin();
        DataBase.pointEM.createQuery("delete from PointEntity e").executeUpdate();
        DataBase.pointEM.flush();
        DataBase.pointEM.getTransaction().commit();

    }
}
