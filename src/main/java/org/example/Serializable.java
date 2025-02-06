package org.example;

import java.util.Map;

public interface Serializable {
    static void savePharmacy(Pharmacy pharmacy) {};
    static Pharmacy loadPharmacy() {return null;};
    static void saveOrders(Map<Product, Integer> orders) {};
    static Map<Product, Integer> loadOrders() {return null;};
}
