package org.example;

public class Main {
    public static void main(String[] args) {
        Pharmacy p = new Pharmacy();

        p.addProduit("Smecta", 4.99, 2, "Medicine");
        p.addProduit("Aspirine", 9.99, 3, "Medicine");

        p.removeProduct("Smecta");

        p.displayProducts();
    }
}