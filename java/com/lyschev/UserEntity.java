package com.lyschev;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class UserEntity {

    public UserEntity() {
        this.login= "";
        this.password = "";
    }

    private Long id;
    @Getter
    @Setter
    private String login;
    @Getter
    @Setter
    private String password;

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq")
    @SequenceGenerator(name = "id_seq", sequenceName = "id_seq", allocationSize = 1, initialValue = 1)
    public Long getId() {
        return id;
    }






}
