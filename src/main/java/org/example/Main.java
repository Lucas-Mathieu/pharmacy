package org.example;

import com.google.gson.Gson;

public class Main {
    public static void main(String[] args) {
        /*
        Pharmacy p = new Pharmacy("Pharma-caca", "2 avenue de westphalie");

        p.addProduct("Smecta", 4.99, 2, "Medicine");
        p.addProduct("Aspirine", 9.99, 10, "Medicine");
        p.addProduct("Supo ;)", 1.99, 3, "Medicine");
        p.addProduct("Dentifrice", 4.99, 3, "Hygiene");
        p.addProduct("Shampoing", 9.99, 2, "Hygiene");

        //p.removeProduct("Smecta");

        //p.displayProducts();

        //p.displayLowStock();

        //p.searchProduct("Smecta");
        //p.searchProduct("Aspirine");
        //p.searchProduct("test");

        p.addOrder("standard", "test");
        p.setProductToOrder("test", "Smecta", 1);

        p.addOrder("emergency", "test2");
        p.setProductToOrder("test2", "Aspirine", 5);

        p.displayOrders();
        */

        Pharmacy p = Data.loadPharmacy();
        Data.loadOrders(p);

        //p.displayProducts();
        p.displayOrders();
    }
}