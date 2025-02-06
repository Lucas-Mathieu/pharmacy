package org.example;

public abstract class User {
    private final String name;
    private final String password;
    public User(String name,String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword(){

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String stackedClass = stackTrace[2].getClassName();
        if (!stackedClass.equals("org.example.Auth")){
            System.out.println("you dont have acces to this method");
            return null;
        }
        return password;
    }

}


