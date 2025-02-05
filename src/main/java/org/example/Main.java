package org.example;

public class Main {
    public static void main(String[] args) {
        Pharmacy p = new Pharmacy();

        p.addProduit("Smecta", 4.99, 2, "Medicine");
        p.addProduit("Aspirine", 9.99, 10, "Medicine");
        p.addProduit("Supo ;)", 1.99, 4, "Medicine");
        p.addProduit("Dentifrice", 4.99, 3, "Hygiene");
        p.addProduit("Shampoing", 9.99, 1, "Hygiene");

        p.removeProduct("Smecta");

        p.displayProducts();
    }
}