package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;


public class Pharmacy {
    private List<Product> productList;

    public Pharmacy() {
        this.productList = new ArrayList<>();
    }

    public void addProduct(String productName, double price, int quantity, String category) {
        this.productList.add(
            new Product(productName, price, quantity, category
        ));
    }

    public void displayProducts() {
        Collections.sort(productList, Comparator.comparing(Product::getName));

        for (Product product : productList) {
            System.out.println(product.getName());
        }
    }
}



