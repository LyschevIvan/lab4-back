package com.lyschev;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Arrays;

@Path("/point")
public class RestPoint {
    @EJB
    BeanAuth beanAuth;

    @EJB
    BeanPoint beanPoint;

    @POST
    @Path("/get")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getPoints(String s) {
        Gson gson = new Gson();
        Response response = new Response();

        JsonElement root = new JsonParser().parse(s);
        String key = root.getAsJsonObject().get("token").getAsString();

        if (beanAuth.isValidUser(key)) {
            try {
                response.points = beanPoint.getPoints();
                response.status = Response.statusOk;
            } catch (Exception e) {
                response.points = new ArrayList<PointEntity>();
                response.status = Response.statusOk;
            }
        } else {
            response.status = Response.statusFail;
            response.message = "invalid session!";
        }
        return gson.toJson(response, Response.class);
    }

    @Path("/check")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String checkPoint(String s){
        Gson gson = new Gson();
        JsonElement root = new JsonParser().parse(s);
        double x = root.getAsJsonObject().get("x").getAsDouble();
        double y = root.getAsJsonObject().get("y").getAsDouble();
        double r = root.getAsJsonObject().get("r").getAsDouble();
        String token = root.getAsJsonObject().get("token").getAsString();
        Response response = new Response();
        if (beanAuth.isValidUser(token)){

            if (Arrays.asList(-2.,-1.5, -1., -0.5, 0, 0.5, 1., 1.5, 2., 2).contains(r)) {
                beanPoint.addPoint(x,y,r);
                PointEntity pointEntity = new PointEntity();
                pointEntity.x = x;
                pointEntity.y = y;
                pointEntity.r = r;
                pointEntity.setIsIn();
                response.last_point = pointEntity; //beanPoint.getPoints().get(beanPoint.getPoints().size() - 1);
                response.status = Response.statusOk;
            }
            else {
                response.status = Response.statusFail;
                response.message = "Неправильное значение R";
            }
        }
        else{
            response.status = Response.statusFail;
            response.message = "Пользователь не идентифицирован!";
        }
        System.out.printf("%f %f %f \n",x,y,r);
        return gson.toJson(response, Response.class);
    }


    @Path("/remove")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String remove(String s){
        Gson gson = new Gson();
        JsonElement root = new JsonParser().parse(s);
        String token = root.getAsJsonObject().get("token").getAsString();
        int id = root.getAsJsonObject().get("id").getAsInt();
        Response response = new Response();
        if(beanAuth.isValidUser(token)){
            beanPoint.deletePoint(id);
            response.status = Response.statusOk;
        }
        else {
            response.status = Response.statusFail;
            response.message = "invalid session";
        }
        return gson.toJson(response, Response.class);
    }


    @Path("/clear")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String clear(String s){
        System.out.println(s);
        Gson gson = new Gson();
        JsonElement root = new JsonParser().parse(s);
        String token = root.getAsJsonObject().get("token").getAsString();
        Response response = new Response();

        if (beanAuth.isValidUser(token)){
            beanPoint.clearPoints();
            response.status = Response.statusOk;
        }
        else {
            response.status = Response.statusFail;
            response.message = "invalid session";
        }
        return gson.toJson(response, Response.class);

    }
}
