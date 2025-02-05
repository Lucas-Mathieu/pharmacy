package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;


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
        productList.removeIf(p -> p.getName().equalsIgnoreCase(productName));
    }

    public void displayProducts() {
        productList.sort(Comparator.comparing(Product::getName));

        for (Product product : productList) {
            System.out.println(product.getName());
        }
    }
}



