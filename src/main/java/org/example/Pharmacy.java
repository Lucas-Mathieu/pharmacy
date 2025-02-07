package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.Scanner;

public class Pharmacy implements Stockable {
    private String name;
    private String address;
    public List<Product> productList;
    private List<Order> orderList;
    private List<Order> orderSold;

    public Pharmacy(String name, String address) {
        this.name = name;
        this.address = address;
        this.productList = new ArrayList<>();
        this.orderList = new ArrayList<>();
        this.orderSold = new ArrayList<>();
    }

    @Override
    public void addProduct(String productName, double price, int quantity, String category) {
        this.productList.add(
                new Product(getNewId(), productName, price, quantity, category)
        );
        Data.savePharmacy(this);
        System.out.println(productName + " has been successfully added.");
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Order> getOrderSold() {
        return orderSold;
    }

    public int getNewId() {
        if (productList.isEmpty()) {
            return 1;
        } else {
            return productList.get(productList.size() - 1).getId() + 1;
        }
    }

    public void addProductWithoutSaving(String productName, double price, int quantity, String category) {
        this.productList.add(
                new Product(getNewId(), productName, price, quantity, category)
        );
        System.out.println(productName + " has been successfully added.");
    }


    @Override
    public void removeProduct(String identifier) {
        Scanner scanner = new Scanner(System.in);
        Product productToRemove = null;

        for (Product p : productList) {
            if (p.getName().equalsIgnoreCase(identifier) || String.valueOf(p.getId()).equals(identifier)) {
                productToRemove = p;
                break;
            }
        }

        if (productToRemove != null) {
            System.out.print("Are you sure you want to remove '" + productToRemove.getName() + "' ? (yes/no) : ");
            String response = scanner.nextLine().trim().toLowerCase();

            if (response.equals("yes") || response.equals("y")) {
                productList.remove(productToRemove);
                Data.savePharmacy(this);
                System.out.println(productToRemove.getName() + " has been removed.");
            } else {
                System.out.println("Deletion aborted.");
            }
        } else {
            System.out.println("Product " + identifier + " not found");
        }
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void displayProducts() {
        System.out.println("Products : ");

        productList.sort(Comparator.comparing(Product::getName));
        for (Product product : productList) {
            System.out.println("Product : " + product.getName() + " Quantity : " + product.getQuantity() + " Category : " + product.getCategory() + " Price : " + product.getPrice());
        }
    }

    public void displayLowStock() {
        List<Product> lowStockProducts = new ArrayList<>();

        for (Product p : productList) {
            if (p.getQuantity() < 5) {
                lowStockProducts.add(p);
            }
        }

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
                } else {
                    System.out.println("The product " + productList.get(mid).getName() + " is not in stock.");
                    return;
                }

            // If productName greater, ignore left half
            if (res > 0)
                left = mid + 1;

                // If productName is smaller, ignore right half
            else
                right = mid - 1;
        }
    }

    public Product getProduct(String productName) {
        productList.sort(Comparator.comparing(Product::getName));

        int left = 0, right = productList.size() - 1;

        // Loop to implement Binary Search
        while (left <= right) {

            // Calculatiing mid
            int mid = left + (right - left) / 2;

            int res = productName.compareTo(productList.get(mid).getName());

            // Check if productName is present at mid
            if (res == 0)
                return productList.get(mid);

            // If productName greater, ignore left half
            if (res > 0)
                left = mid + 1;

                // If productName is smaller, ignore right half
            else
                right = mid - 1;
        }

        System.out.println("The product " + productName + " could not be found.");
        return null;
    }

    public void addOrder(String orderType, String orderName) {
        Order order = null;

        switch (orderType.toLowerCase()) {
            case "standard":
                order = new Standard(this, orderName);
                break;
            case "emergency":
                order = new Emergency(this, orderName);
                break;
            default:
                System.out.println("Error: Invalid order type " + orderType);
                return;
        }

        this.orderList.add(order);
        Data.savePharmacy(this);
        System.out.println("The " + orderType + " order has been added.");
    }

    public void removeOrder(String orderName) {
        for (Order order : orderList) {
            if (order.getName().equalsIgnoreCase(orderName)) {
                orderList.remove(order);
                Data.savePharmacy(this);
                System.out.println("Order '" + orderName + "' has been removed.");
                return;
            }
        }
        System.out.println("Order with name '" + orderName + "' not found.");
    }

    public void displayOrders() {
        if (orderList.isEmpty()) {
            System.out.println("No orders found.");
        } else {
            for (Order order : orderList) {
                System.out.println("Order Name: " + order.getName() + " | Urgent: " + order.isUrgent());
                order.displayOrder();
            }
        }
    }

    public void displayOrdersSold() {
        if (orderSold.isEmpty()) {
            System.out.println("No orders found.");
        } else {
            for (Order order : orderSold) {
                System.out.println("Order Name: " + order.getName() + " | Urgent: " + order.isUrgent());
                order.displayOrder();
            }
        }
    }

    public void setProductToOrder(Pharmacy p,String orderName, String productName, int quantity) {
        for (Order order : orderList) {
            if (order.getName().equalsIgnoreCase(orderName)) {
                order.setOrder(p, productName, quantity);
                Data.savePharmacy(this);
                return;
            }
        }
        System.out.println("Order with name '" + orderName + "' not found.");
    }

    public void removeProductFromOrder(String orderName, String productName) {
        for (Order order : orderList) {
            if (order.getName().equalsIgnoreCase(orderName)) {
                order.removeProductOrder(productName);
                Data.savePharmacy(this);
                return;
            }
        }
        System.out.println("Order with name '" + orderName + "' not found.");
    }

    public void validateOrder(String orderName) {
        for (Order order : orderList) {
            if (order.getName().equalsIgnoreCase(orderName)) {
                order.validation();
                this.orderList.remove(order);
                this.orderSold.add(order);
                Data.savePharmacy(this);
                System.out.println("Order '" + orderName + "' has been validated and moved to orderSold.");
                return;
            }
        }
        System.out.println("Order with name '" + orderName + "' not found.");
    }

    public void genStats() {
        Data.exportSalesReport(this);
    }
}