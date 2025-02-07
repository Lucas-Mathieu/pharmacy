package org.example;
import java.util.*;

public abstract class Order {
    private String name;
    protected Map<Product, Integer> orderMap =  new HashMap<>();
    private List<Product> pharmacyList;

    public Order(Pharmacy p, String name) {
        this.name = name;
        if (p == null) {
            throw new IllegalArgumentException("Error: the pharmacy can't be null !");
        }
        pharmacyList = p.getProductList();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private boolean checkQuantity(int a, int b) {
        return a > b;
    }

    private boolean alreadyInMap(String name) {
        for (Product p : orderMap.keySet()) {
            if (p.getName().contains(name)) {
                return true;
            }
        }
        return false;
    }

    private Product getProduct(String name) {
        for (Product p : pharmacyList) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    public void clearOrder(){
        orderMap.clear();
        System.out.println("order cleared");

    }


    public boolean setOrder(String name, int quantity) {

        final Product product = getProduct(name);

        if (product == null) {
            System.out.println("error, the product is not in the pharmacy");
            return false;
        }

        if (alreadyInMap(name)) {
            orderMap.put(new Product(product.getName(), product.getPrice(), product.getQuantity(), product.getCategory()),quantity);
            System.out.println("the quantity of the product : " + name + " have been changed for quantity : " + quantity);
            return true;
        }

        if(!checkQuantity(product.getQuantity(),quantity)){
            System.out.println("the required quantity is too much");
            return false;
        }

        orderMap.put(new Product(product.getName(), product.getPrice(), product.getQuantity(), product.getCategory()),quantity);
        return true;
    }

    public Map<Product, Integer> getOrder() {
        return orderMap;
    }
    public List<String> getStringOrder(){
        List<String> StringList =  new ArrayList<>();

        for(Product p : orderMap.keySet()){
            StringList.add("product : "+p.getName() +" quantity : "+ orderMap.get(p).toString());

        }
        return StringList;

    }
    public void displayOrder() {
        for (Product p : orderMap.keySet()) {
            System.out.println("Name : " + p.getName() + "  Quantity : " + orderMap.get(p));
        }
    }

    public void validation(){

        for (Product p : orderMap.keySet()){
            p.setQuantity(p.getQuantity() - orderMap.get(p));
            if(p.getQuantity() < 5 ) {
                System.out.println("Warning ! The product quantity of name : " + p.getName() + " have decreased under 5 items ! New quantity : " + p.getQuantity() );
            }
        }
        System.out.println("validation of the order  is complete");
    }

    public boolean removeProductOrder(String name) {
        if (!alreadyInMap(name)) {
            System.out.println("Error: the product is not in the order.");
            return false;
        }

        orderMap.keySet().removeIf(ps -> ps.getName().equals(name));
        System.out.println("The product " + name + " has been removed from the order.");
        // Save orders after the product is removed
        return true;
    }

    public abstract boolean isUrgent();
}
