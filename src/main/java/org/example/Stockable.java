package org.example;

public interface Stockable {
    void addProduct(String productName, double price, int quantity, String category);
    void removeProduct(String productName);
}
