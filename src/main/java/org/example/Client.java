package org.example;

public class Client extends User implements Role {

    public Client(String name, String password) {
        super(name, password);
    }

    @Override
    public String getRoleName() {
        return "client";
    }
}
