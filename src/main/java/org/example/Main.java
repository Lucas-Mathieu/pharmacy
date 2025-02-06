package org.example;

public class Main {
    public static void main(String[] args) {
        Pharmacy p = new Pharmacy();
        p.addProduit("Smecta", 4.99, 0, "Medicine");
        p.addProduit("Aspirine", 9.99, 10, "Medicine");
        p.addProduit("Supo ;)", 1.99, 3, "Medicine");
        p.addProduit("Dentifrice", 4.99, 3, "Hygiene");
        p.addProduit("Shampoing", 9.99, 2, "Hygiene");

        //p.removeProduct("Smecta");

        //p.displayProducts();

        //p.displayLowStock();

        Standard s = new Standard(p);
        Emergency e = new Emergency(p);
        System.out.println(s.isUrgent());
        System.out.println(e.isUrgent());
        s.setOrder("Aspirine",5);
        s.setOrder("Dentifrice",1);
        s.validation();

        p.searchProduct("Smecta");
        p.searchProduct("Aspirine");
        p.searchProduct("test");

    }
}