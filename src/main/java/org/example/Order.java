package org.example;
import java.util.*;

public abstract class Order {

    protected  Map<Product, Integer> orderMap =  new HashMap<>();
    private final List<Product> pharmacyList;

    public  Order(Pharmacy p) {

        if (p == null) {
            throw new IllegalArgumentException("❌ the pharmacy can't be null !");
        }
        pharmacyList = p.getProductList();
    }

    private boolean checkQuantity(int a, int b ) {
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

    private Product getProduct(String name){

        for (Product p : pharmacyList) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    public void setOrder(String name, int quantity){

        final Product product = getProduct(name);

        if (product == null) {
            System.out.println("error, the product is not in the pharmacy");
            return;
        }

        if (alreadyInMap(name)) {
            orderMap.put(new Product(product.getName(), product.getPrice(), product.getQuantity(), product.getCategory()),quantity);
            System.out.println("the quantity of the product : " + name + " have been changed for quantity : " + quantity);
            return;
        }

        if(!checkQuantity(product.getQuantity(),quantity)){
            System.out.println("the required quantity is too much");
            return;
        }

        orderMap.put(new Product(product.getName(), product.getPrice(), product.getQuantity(), product.getCategory()),quantity);
    }

    public Map<Product, Integer>  getOrder(){
        return orderMap;
    }

    public void displayOrder(){
        for (Product p : orderMap.keySet()) {
            System.out.println("name : " + p.getName() + "  quantity : " + orderMap.get(p));
        }
    }

    public  void removeOrder(String name){

        if (!alreadyInMap(name)) {
            System.out.println("error, the product is not in the order");
            return;
        }
        orderMap.keySet().removeIf(ps ->ps.getName().equals(name));
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

    public abstract boolean isUrgent();
}
