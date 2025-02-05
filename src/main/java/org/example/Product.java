package org.example;

public class Product {
    private String name;
    private double price;
    private int quantity;
    private Category category;

    public Product(String name, double price, int quantity, String categoryName) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = new Category(categoryName);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category.getType();
    }

    public void setCategory(String category) {
        this.category.setType(category);
    }

}
