package com.lyschev;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


@Path("/auth")
public class RestAuth {
    @EJB
    BeanAuth beanAuth;

    @Path("/login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String login(String s ){
        System.out.println(s);
        Gson gson = new Gson();
        JsonElement root = new JsonParser().parse(s);
        String login = root.getAsJsonObject().get("username").getAsString();
        String pass = root.getAsJsonObject().get("password").getAsString();
        System.out.println(login+' '+pass);
        Response response = new Response();
        try {
            response.key = beanAuth.login(login, pass);
            response.status = Response.statusOk;
            return gson.toJson(response, Response.class);

        } catch (Exception e) {
            response.status = Response.statusFail;
            response.message = e.getMessage();
            return gson.toJson(response, Response.class);
        }
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String register(String s) {
        Gson gson = new Gson();
        Response response = new Response();
        try {
            JsonElement root = new JsonParser().parse(s);
            String username = root.getAsJsonObject().get("username").getAsString();
            String password = root.getAsJsonObject().get("password").getAsString();
            if (beanAuth.isRegistered(username)) {
                response.status = Response.statusFail;
                response.message = "Пользователь с таким иеменем уже есть";
            }
            else {
                response.status = Response.statusOk;
                response.key = beanAuth.register(username, password);
            }
            return gson.toJson(response, Response.class);
        } catch (Exception e) {
            response.status = Response.statusFail;
            response.message = e.getMessage();
            return gson.toJson(response, Response.class);
        }
    }

    @POST
    @Path("/logout")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String logout(String s) {
        Gson gson = new Gson();
        Response response = new Response();

        JsonElement root = new JsonParser().parse(s);
        String key = root.getAsJsonObject().get("token").getAsString();
        response.status = beanAuth.logout(key) ? Response.statusOk : Response.statusFail;
        return gson.toJson(response, Response.class);
    }
}
