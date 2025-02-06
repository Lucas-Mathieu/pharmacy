package org.example;
import java.util.*;

public abstract class Order {

    protected  Map<Product, Integer> orderMap =  new HashMap<>();
    private  List<Product> pharmacyList;

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

    public boolean setOrder(String name, int quantity){

        if (alreadyInMap(name)) {
            orderMap.get(getProduct(name));
            System.out.println("quantity of the product : " + name + " have been changed for quantity : " + quantity);
            return true;
        }

        if ( getProduct(name) == null ){
            System.out.println("error the product is not in the pharmacy");
            return false;
        }

        final Product product = getProduct(name);

        if(!checkQuantity(product.getQuantity(),quantity)){
            System.out.println("the required quantity is too much");
            return false;
        }

        orderMap.put(product,quantity);
        return true;
    }

    public  Map<Product, Integer> getOrder(){
        return orderMap;
    }

    public void displayOrder(){
        for (Product p : orderMap.keySet()) {
            System.out.println("name : " + p.getName() + "  quantity : " + orderMap.get(p));
        }
    }

    public  boolean removeOrder(String name){
        if (!alreadyInMap(name)) {
            System.out.println("error the product is not in the order");
            return  false;
        }
        orderMap.keySet().removeIf(ps ->ps.getName().equals(name));
        return true;
    }

    public abstract boolean isUrgent();
}
