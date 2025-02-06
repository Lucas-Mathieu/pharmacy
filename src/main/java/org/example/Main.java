package org.example;

import java.util.Scanner;

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

        Pharmacy pharmacy = Data.loadPharmacy();

        pharmacy.addProduct("test", 9.0, 5, "test");

        //p.displayProducts();
        pharmacy.displayOrders();

        Admin a = new Admin("monsieurPatate","patate");
        UserConnexion u = new UserConnexion("t","t");

        //System.out.println(a.getRoleName());
        Employee em = new Employee("monsieurbanane","banane");
        Employee em2 = new Employee("monsieurbanane2","banane2");
        Admin admin = new Admin("admin","jsp");


        UserManager um = new UserManager();
        Scanner scanner = new Scanner(System.in);
        Auth auth = new Auth();

        //um.addUser(admin ,em);
        //um.addUser(admin ,em2);


        //um.removeUser(admin, "monsieurbanane");

        /*
        while (true) {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();  // Read username
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            System.out.print("wait for the check ... ");
            auth.passwordVerify(username,password);
        }*/
    }
}