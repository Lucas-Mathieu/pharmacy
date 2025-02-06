package org.example;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Auth {
    private final Gson gson;
    private List<UserConnexion> users;

    public Auth() {
     gson = new Gson();
     users = new ArrayList<>();
    }

    public Boolean passwordVerify(String name,String password){

        try (FileReader reader = new FileReader("src/main/java/org/example/users.json")) {
            Type listType = new TypeToken<List<UserConnexion>>(){}.getType();
            users = gson.fromJson(reader, listType);

        } catch (Exception e) {
            e.printStackTrace();
        }
        for (UserConnexion user : users) {
            if (user.getName().equals(name)){

                if(user.getPassword() == null){
                    System.out.println("incorrect acces to the method");
                    return false;
                }
                if (!user.getPassword().equals(password)){
                    System.out.println("incorrect password");
                    return false;
                }
                System.out.println("correct password");
                return true;
            }
        }
        System.out.println("user not found");
        return false;
    }
}
