package org.example;

import org.example.Product;

import java.util.List;

public class Emergency extends Order {


    public Emergency(Pharmacy p, String name) {
        super(p, name);
    }

    @Override
    public boolean isUrgent() {
        return true;
    }
}
