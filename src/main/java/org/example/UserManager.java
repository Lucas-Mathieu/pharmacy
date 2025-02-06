package org.example;

import java.util.HashSet;
import java.util.Set;

public class UserManager {

    private  Set<User> users = new HashSet<>();

    public void addUser(User user, User newUser) {

        if ( user instanceof Admin ) {
            users.add(newUser);
            System.out.println(newUser.getName() + " added as " + newUser);
        } else {
            System.out.println("Error, Only admins can add users!");
        }
    }

    public void removeUser(User user, String username) {

        if ( user instanceof Admin) {
            users.removeIf(u -> u.getName().equals(username));
            System.out.println(username + " has been removed.");
        } else {
            System.out.println("Error, Only admins can remove users!");
        }
    }

    public void displayUsers(User user) {

        if ( user instanceof Admin ) {
            users.forEach(System.out::println);
        } else {
            System.out.println("Error, Only admins can remove users!");
        }

    }
}

