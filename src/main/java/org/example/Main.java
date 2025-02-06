package org.example;
import com.google.gson.Gson;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Gson gson = new Gson();

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

        OrderLog ol = new OrderLog();
        ol.addLog(s);
        e.setOrder("Aspirine",5);
        e.setOrder("Dentifrice",1);
        e.validation();
        for( int i = 0; i<50000; i++){

        }
        ol.addLog(e);

        ol.displayLog();

        Admin a = new Admin("monsieurPatate","patate");
        UserConnexion u = new UserConnexion("t","t");

        System.out.println(a.getRoleName());
        Employee em = new Employee("monsieurbanane","banane");
        System.out.println(em.getRoleName());

        UserManager um = new UserManager();

        //p.searchProduct("Smecta");
        //p.searchProduct("Aspirine");
        //p.searchProduct("test");
        Scanner scanner = new Scanner(System.in);
        Auth auth = new Auth();
        while (true) {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();  // Read username
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            System.out.print("wait for the check ... ");
            auth.passwordVerify(username,password);

        }
    }
}