package org.example;

public class Admin extends User implements  Role  {

    public Admin(String name,  String password) {
        super(name,password);
    }

    @Override
    public String getRoleName() {
        return "admin";
    }

}
