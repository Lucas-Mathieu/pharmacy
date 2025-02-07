package org.example;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Auth {
    private final Gson gson;
    private List<UserConnexion> users;
    private User session;

    public Auth() {
     gson = new Gson();
     users = new ArrayList<>();
    }

    public boolean passwordVerify(String name,String password){

        try (FileReader reader = new FileReader("src/main/java/org/example/users.json")) {
            Type listType = new TypeToken<List<UserConnexion>>(){}.getType();
            users = gson.fromJson(reader, listType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (UserConnexion user : users) {
            if (!user.getName().equals(name)) {
                System.out.println("user not found");
                return false;
            }
            if(user.getPassword() == null){
                System.out.println("incorrect acces to the method");
                return false;
            }
            if (!user.getPassword().equals(password)){
                System.out.println("incorrect password");
                return false;
            }
            System.out.println("correct password");
            System.out.println(user.getRoleName());
            session = createSession(user.getRoleName(),user.getName());
            return true;
        }
        return false;
    }
    private User createSession(String role,String name){
        return switch (role) {
            case "admin" -> new Admin(name, "");
            case "client" -> new Client(name, "");
            case "employee" -> new Employee(name, "");
            case null, default -> throw new JsonParseException("Rôle inconnu : " + role);
        };
    }
    protected User getSession(){
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String stackedClass = stackTrace[2].getClassName();
        if (!stackedClass.equals("org.example.Menu")){
            System.out.println("you dont have acces to this method");
            return null;
        }
        return session;
    }
    protected void closeSession(){
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String stackedClass = stackTrace[2].getClassName();
        if (!stackedClass.equals("org.example.Menu")){
            System.out.println("you dont have acces to this method");
            return;
        }
        session = null;
    }
}
