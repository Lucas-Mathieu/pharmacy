package org.example;

public class UserConnexion extends User implements Role {
    public UserConnexion(String name, String password) {
        super(name, password);

    }

    @Override
    public String getRoleName() {
        return "";
    }
}
