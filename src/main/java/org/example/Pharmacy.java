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
                new Product(productName, price, quantity, category)
        );
        System.out.println(productName + " has been successfully added.");
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
        System.out.println("Products : ");

        productList.sort(Comparator.comparing(Product::getName));
        for (Product product : productList) {
            System.out.println(product.getName());
        }
    }

    public void displayLowStock() {
        List<Product> lowStockProducts = new ArrayList<>();

        for (Product p : productList) {
            if (p.getQuantity() < 5) {
                lowStockProducts.add(p);
            }
        }

        //sorting algorithm by insertion
        for (int i = 1; i < lowStockProducts.size(); i++) {
            Product currentProduct = lowStockProducts.get(i);
            int j = i - 1;

            while (j >= 0 && lowStockProducts.get(j).getQuantity() > currentProduct.getQuantity()) {
                lowStockProducts.set(j + 1, lowStockProducts.get(j));
                j--;
            }

            lowStockProducts.set(j + 1, currentProduct);
        }

        System.out.println("Products with low stock : ");
        for (Product p : lowStockProducts) {
            System.out.println(p.getQuantity() + " : " + p.getName());
        }

    }

    public void searchProduct(String productName) {
        productList.sort(Comparator.comparing(Product::getName));

        int left = 0, right = productList.size() - 1;

        // Loop to implement Binary Search
        while (left <= right) {

            // Calculatiing mid
            int mid = left + (right - left) / 2;

            int res = productName.compareTo(productList.get(mid).getName());

            // Check if productName is present at mid
            if (res == 0)
                if (productList.get(mid).getQuantity() > 0) {
                    System.out.println("the product " + productList.get(mid).getName() + " is in stock with " + productList.get(mid).getQuantity() + " units.");
                    return;
                }
                else {
                    System.out.println("The product " + productList.get(mid).getName() + " is not in stock.");
                    return;
                }

            // If productName greater, ignore left half
            if (res > 0)
                left = mid + 1;

            // If productName is smaller, ignore right half
            else
                left = mid - 1;
        }

        System.out.println("The product " + productName + " could not be found.");
    }
}




