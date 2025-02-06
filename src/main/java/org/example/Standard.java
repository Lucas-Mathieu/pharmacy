package org.example;

public class Standard extends Order{

    public Standard(Pharmacy p, String name) {
        super(p, name);
    }

    @Override
    public boolean isUrgent() {
        return false;
    }
}
