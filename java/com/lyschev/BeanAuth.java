package com.lyschev;

import javax.ejb.Singleton;
import javax.ejb.Stateless;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

@Singleton
public class BeanAuth {


    private final static Set<String> securityKeys = new HashSet<>();

    public List<UserEntity> getRegistered(){
        return DataBase.userEM.createQuery("select e from UserEntity e").getResultList();
    }

    public String register(String login, String password) throws NoSuchAlgorithmException {

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        String encodedPass = Base64.getEncoder().encodeToString(hash);

        UserEntity user = new UserEntity();
        user.setLogin(login);
        user.setPassword(encodedPass);

        DataBase.userEM.getTransaction().begin();
        DataBase.userEM.persist(user);
        DataBase.userEM.flush();
        DataBase.userEM.getTransaction().commit();

        String secKey = new BigInteger(130, new SecureRandom()).toString(32);
        securityKeys.add(secKey);

        return secKey;

    }

    public String login(String login, String password) throws Exception{
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        String encodedPass = Base64.getEncoder().encodeToString(hash);
        System.out.println(encodedPass);

        List<UserEntity> users = getRegistered();
        for (UserEntity user : users) {
            if (login.equals(user.getLogin()) &&
                    encodedPass.equals(user.getPassword())) {
                String secKey = new BigInteger(130, new SecureRandom()).toString(32);
                securityKeys.add(secKey);
                return secKey;
            }
        }
        throw new Exception("Неправильный логин или пароль");
    }

    public Boolean logout(String secretKey) {
        return securityKeys.remove(secretKey);
    }

    public boolean isRegistered(String login){
        for (UserEntity user : getRegistered()){
            if (login.equals(user.getLogin())){
                return true;
            }
        }
        return false;
    }

    public Boolean isValidUser(String secKey) {
        return securityKeys.contains(secKey);
    }
}
