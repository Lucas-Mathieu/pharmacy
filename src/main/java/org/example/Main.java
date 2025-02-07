package org.example;

import javax.net.ssl.SSLSession;
import java.security.KeyStore;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Pharmacy p = new Pharmacy("Pharma", "2 avenue de westphalie");

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
        OrderLog ol = new OrderLog();
        Order o = new Emergency(p,"order1");
        o.setOrder("Aspirine",2);
        o.setOrder("Dentifrice",1);
        ol.addLog(o);
        ol.addInLog();

        Pharmacy pharmacy = Data.loadPharmacy();
        Data.loadOrders(pharmacy);

        //p.displayProducts();
        pharmacy.displayOrders();

        Admin a = new Admin("monsieurPatate","patate");

        //System.out.println(a.getRoleName());
        Employee em = new Employee("monsieurbanane","banane");
        Employee em2 = new Employee("monsieurbanane2","banane2");
        Admin admin = new Admin("admin","jsp");


        UserManager um = new UserManager();

        um.addUser(admin ,em);
        um.addUser(admin ,em2);

        //um.removeUser(admin, "monsieurbanane");
        Menu menu = new Menu();
        menu.login();
    }
}

class Menu {
    private final Scanner scanner = new Scanner(System.in);
    private Pharmacy pharmacy;

    public void login() {
        Scanner scanner = new Scanner(System.in);
        Auth auth = new Auth();
        do {
            System.out.println("\n---CONNEXION----");
            System.out.println("for leave the application write : quit");
            System.out.print("Enter your username: ");
            String username = scanner.nextLine().trim();
            if(username.equals("quit")){
                System.out.println("application closed");
                scanner.close();
                break;}
            System.out.println("for leave the application write : quit");
            System.out.print("Enter your password: ");
            String password = scanner.nextLine().trim();
            if(password.equals("quit")){
                System.out.println("application closed");
                scanner.close();
                break;}
            System.out.print("wait for the check ... ");
            if (auth.userVerify(username, password)) {
                if (startSession(auth.getSession())){
                    auth.closeSession();
                    scanner.close();
                    break;
                }
                auth.closeSession();
            }
        }
        while (true);

    }
    private boolean startSession(final User newSession) {
        pharmacy = new Pharmacy("BigPharma", "2 avenue de westphalie");

        int choice;
        do {

            printGlobalMenu(newSession.getRoleName());
            choice = scanner.nextInt();
            mainChoiceHandler(choice, newSession.getRoleName());
            if(choice == 0){
                return true;
            }
            if(choice == 1){
                return false;
            }
        } while (true);
    }
    private void mainChoiceHandler(int choice,String role ) {
        int max = 0;
        if(role.equals("admin")){
            max = 8;
        }
        if(role.equals("employee")){
            max = 6;
        }
        if(role.equals("client")){
            max = 3;
        }

        if(choice > max){
            System.out.println("You don't have the permission");
        }
        switch (choice) {
            case 2:
                //consult pharmacy list
                pharmacy.displayProducts();
                break;
            case 3:
                //menu for passing order
                do{
                    OrderMenu();
                    System.out.print("Your choice : ");
                    int entry = scanner.nextInt();
                    if(menuChoiceHandler(entry)){
                        break;
                    }
                }
                while(true);
                System.out.print("name of the product : ");

                System.out.print("Your choice : ");
                break;
            case 4:
                //add a product in pharmacy
                do{

                    System.out.print("Your choice : ");
                    int entry = scanner.nextInt();
                    if(menuChoiceHandler(entry)){
                        break;
                    }
                }
                while(true);

                break;
            case 5:
                //remove a product in pharmacy
                break;
            case 6:
                //display product in low quantity
                pharmacy.displayLowStock();
                break;
            case 7:
                //add user
                break;
            case 8:
                //remove user
                break;
            case 1:
                System.out.println("Session disconnection");
                break;
            case 0:
                System.out.println("application closed");
                break;

            default:
                System.out.println("Invalid choice.");
        }
    }
    private boolean menuChoiceHandler(int choice) {
        switch (choice) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 0:
                return true;

            default:
                System.out.println("Invalid choice.");
        }
        return false;
    }

    private void printGlobalMenu(String role) {

        if(role.equals("admin")){
            printAdminMenu();
            printEmployeeMenu();
        }
        if(role.equals("employee")){
            printEmployeeMenu();
        }
        System.out.println("\n--- Main Menu---");
        System.out.println("3. Menu for passing order");
        System.out.println("2. Consult Pharmacy list");
        System.out.println("1. Disconnect from session");
        System.out.println("0. Left application");
        System.out.print("Your choice : ");
    }
    private void OrderMenu() {
        System.out.println("\n--- Order menu --");
        System.out.println("4. validate and save your order");
        System.out.println("3. Remove a product from the order");
        System.out.println("2. Add a product from the order");
        System.out.println("1. Create a order");
        System.out.println("0. Back to main menu");
    }
    private void printAdminMenu() {
        System.out.println("\n--- Admin Menu ---");
        System.out.println("8. Remove User");
        System.out.println("7. Create user");
    }
    private void printEmployeeMenu(){
        System.out.println("\n--- Employee Menu---");
        System.out.println("6. Display products in low quantity");
        System.out.println("5. Remove a product in the pharmacy");
        System.out.println("4. Add a product in the pharmacy");
    }
}