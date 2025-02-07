package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Pharmacy pharmacy = Data.loadPharmacy();
        UserManager userManager = new UserManager();
        Auth auth = new Auth();

        Menu menu = new Menu(pharmacy, userManager);
        menu.login();

    }

    private static User login(Auth auth) {
        Scanner scanner = new Scanner(System.in);
        String username;
        String password;

        while (true) {
            System.out.print("Enter username: ");
            username = scanner.nextLine();
            System.out.print("Enter password: ");
            password = scanner.nextLine();

            // Verify the user login
            boolean loginSuccessful = auth.userVerify(username, password);

            if (loginSuccessful) {
                // If login is successful, return the session user (Admin, Employee, Client)
                return auth.getSession();
            } else {
                // If login fails, ask the user to try again
                System.out.println("Login failed. Please try again.");
            }
        }
    }
}

class Menu {
    private final Scanner scanner = new Scanner(System.in);
    private final Pharmacy pharmacy;
    private final UserManager userManager;
    private final Auth auth;

    public Menu(Pharmacy pharmacy, UserManager userManager) {
        this.pharmacy = pharmacy;
        this.userManager = userManager;
        this.auth = new Auth();  // Authentication instance
    }

    public void login() {
        while (true) {
            System.out.println("\n--- LOGIN ----");
            System.out.println("Type 'quit' to exit the application.");

            System.out.print("Username: ");
            String username = scanner.nextLine().trim();
            if (username.equalsIgnoreCase("quit")) {
                System.out.println("Application closed.");
                break;
            }

            System.out.print("Password: ");
            String password = scanner.nextLine().trim();
            if (password.equalsIgnoreCase("quit")) {
                System.out.println("Application closed.");
                break;
            }

            if (auth.userVerify(username, password)) {
                User sessionUser = auth.getSession();
                if (startSession(sessionUser)) {
                    auth.closeSession();
                    break;
                }
                auth.closeSession();
            } else {
                System.out.println("Login failed. Please try again.");
            }
        }
    }

    private boolean startSession(User user) {
        int choice;
        do {
            printGlobalMenu(user.getRoleName());
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // Clear invalid input
            }
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            if (choice == 1) return false;  // Logout
            if (choice == 0) return true;   // Exit application

            mainChoiceHandler(choice, user);
        } while (true);
    }

    private void mainChoiceHandler(int choice, User user) {
        switch (choice) {
            case 2:
                pharmacy.displayProducts();
                break;
            case 3:
                orderMenu();
                break;
            case 4:
                addProduct();
                break;
            case 5:
                removeProduct();
                break;
            case 6:
                pharmacy.displayLowStock();
                break;
            case 7:
                addUser(user);
                break;
            case 8:
                removeUser(user);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void addProduct() {
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();

        System.out.print("Enter price: ");
        while (!scanner.hasNextDouble()) {
            System.out.println("Invalid input. Enter a valid price:");
            scanner.next();
        }
        double price = scanner.nextDouble();

        System.out.print("Enter quantity: ");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Enter a valid quantity:");
            scanner.next();
        }
        int quantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter category: ");
        String category = scanner.nextLine();

        pharmacy.addProduct(name, price, quantity, category);
    }

    private void removeProduct() {
        System.out.print("Enter product name or ID to remove: ");
        String identifier = scanner.next();
        pharmacy.removeProduct(identifier);
    }

    private void addUser(User admin) {
        if (!(admin instanceof Admin)) {
            System.out.println("Error: Only admins can add users!");
            return;
        }

        System.out.print("Enter new username: ");
        String username = scanner.next();

        System.out.print("Enter password: ");
        String password = scanner.next();

        System.out.print("Enter role (admin/employee/client): ");
        String role = scanner.next().toLowerCase();

        User newUser;
        switch (role) {
            case "admin":
                newUser = new Admin(username, password);
                break;
            case "employee":
                newUser = new Employee(username, password);
                break;
            case "client":
                newUser = new Client(username, password);
                break;
            default:
                System.out.println("Invalid role!");
                return;
        }

        userManager.addUser(newUser);
        System.out.println("User added successfully.");
    }

    private void removeUser(User admin) {
        if (!(admin instanceof Admin)) {
            System.out.println("Error: Only admins can remove users!");
            return;
        }

        System.out.print("Enter username to remove: ");
        String username = scanner.next();
        userManager.removeUser(username);
        System.out.println("User removed successfully.");
    }

    private void orderMenu() {
        int choice;
        do {
            System.out.println("\n--- Order Menu ---");
            System.out.println("1. Create an order");
            System.out.println("2. Add product to order");
            System.out.println("3. Remove product from order");
            System.out.println("4. Validate and save order");
            System.out.println("5. Export sales data");
            System.out.println("0. Back to main menu");

            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Enter a number.");
                scanner.next();
            }
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 0) return;

            handleOrderChoice(choice);
        } while (true);
    }

    private void handleOrderChoice(int choice) {
        switch (choice) {
            case 1:
                System.out.print("Enter order name: ");
                String orderName = scanner.nextLine();
                System.out.print("Enter order type (standard/emergency): ");
                String orderType = scanner.nextLine();
                pharmacy.addOrder(orderType, orderName);
                break;
            case 2:
                System.out.print("Enter order name: ");
                String orderNameToAdd = scanner.nextLine();
                System.out.print("Enter product name: ");
                String productName = scanner.nextLine();
                System.out.print("Enter quantity: ");
                while (!scanner.hasNextInt()) {
                    System.out.println("Invalid input. Enter a valid quantity:");
                    scanner.next();
                }
                int quantityToAdd = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                pharmacy.setProductToOrder(pharmacy, orderNameToAdd, productName, quantityToAdd);
                break;
            case 3:
                System.out.print("Enter order name: ");
                String orderNameToRemove = scanner.nextLine();
                System.out.print("Enter product name: ");
                String productToRemove = scanner.nextLine();
                pharmacy.removeProductFromOrder(orderNameToRemove, productToRemove);
                break;
            case 4:
                System.out.print("Enter order name to validate: ");
                String orderToValidate = scanner.nextLine();
                pharmacy.validateOrder(orderToValidate);
                break;
            case 5:
                Data.exportSalesReport(pharmacy);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void printGlobalMenu(String role) {
        System.out.println("\n--- Main Menu ---");
        if (role.equals("admin")) {
            System.out.println("--------ADMIN MENU---------");
            System.out.println("7. Add user");
            System.out.println("8. Remove user");
            System.out.println("");
        }
        if (role.equals("employee") || role.equals("admin")) {
            System.out.println("--------EMPLOYEE MENU---------");
            System.out.println("4. Add product to pharmacy");
            System.out.println("5. Remove product from pharmacy");
            System.out.println("6. Display low-stock products");
            System.out.println("");
        }
        System.out.println("--------MAIN MENU---------");
        System.out.println("1. Logout");
        System.out.println("2. View products");
        System.out.println("3. Order Menu");
        System.out.println("0. Exit application");
        System.out.println("");
        System.out.print("Your choice: ");
    }
}