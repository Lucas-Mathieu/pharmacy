package org.example;

public class Standard extends Order{

    public Standard(Pharmacy p) {
        super(p);
    }

    @Override
    public boolean isUrgent() {
        return false;
    }
}
