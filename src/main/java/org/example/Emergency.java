package org.example;

import org.example.Product;

import java.util.List;

public class Emergency extends Order {


    public Emergency(Pharmacy p) {
        super(p);
    }

    @Override
    public boolean isUrgent() {
        return false;
    }
}
