package com.lyschev;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "points")
public class PointEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "point_seq")
    @SequenceGenerator(name = "point_seq", sequenceName = "point_seq", allocationSize = 1, initialValue = 1)
    private Integer id;

    @Getter
    @Setter
    Double x;
    @Getter
    @Setter
    Double y;
    @Getter
    @Setter
    Double r;
    @Getter
    String isIn;

    public void setIsIn() {
        this.isIn = isIn();
    }

    private String isIn(){
        if ((x >= y-r/2)&&(x<=0)&&(y>=0)){
            return "true";
        }
        if ((x*x+y*y<=r*r)&&(x<=0)&&(y<=0)){
            return "true";
        }
        if ((x<=r)&&(y>=-r)&&(x>=0)&&(y<=0)){
            return "true";
        }
        return "false";
    }
}
