package org.example;

public class Employee extends User implements Role {

    public Employee(String name, String password) {
        super(name, password);
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getRoleName() {
        return "Employee";
    }


}
