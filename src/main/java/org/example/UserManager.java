package org.example;

import java.util.HashSet;
import java.util.Set;

public class UserManager {

    private  Set<User> users = new HashSet<>();

    public void addUser(User newUser) {
        users.add(newUser);
        Data.saveUser(newUser);
    }

    public void removeUser(String username) {
        users.removeIf(u -> u.getName().equals(username));
        Data.removeUser(username);
    }

    public void displayUsers(User user) {

        if ( user instanceof Admin ) {
            users.forEach(System.out::println);
        } else {
            System.out.println("Error, Only admins can remove users!");
        }

    }
}