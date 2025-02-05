package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.Scanner;

public class Pharmacy implements Stockable {
    private List<Product> productList;

    public Pharmacy() {
        this.productList = new ArrayList<>();
    }

    @Override
    public void addProduit(String productName, double price, int quantity, String category) {
        this.productList.add(
                new Product(productName, price, quantity, category
                ));
    }

    @Override
    public void removeProduct(String productName) {
        Scanner scanner = new Scanner(System.in);

        for (Product p : productList) {
            if (p.getName().equalsIgnoreCase(productName)) {
                System.out.print("Are you sure you want to remove '" + productName + "' ? (yes/no) : ");

                String response = scanner.nextLine().trim().toLowerCase();

                if (response.equals("yes") || response.equals("y")) {
                    productList.remove(p);
                    System.out.println(productName + " has been removed.");
                } else {
                    System.out.println("Deletion aborted.");
                }
                return;
            }
        }
        System.out.println("Product " + productName + " not found");
    }

    public void displayProducts() {
        productList.sort(Comparator.comparing(Product::getName));

        for (Product product : productList) {
            System.out.println(product.getName());
        }
    }
}



