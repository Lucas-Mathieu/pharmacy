package org.example;

public class UserConnexion extends User implements Role {
    private final String role;
    public UserConnexion(String name, String password,String role) {
        super(name, password);
        this.role = role;
    }

    @Override
    public String getRoleName() {
        return role;
    }
}
