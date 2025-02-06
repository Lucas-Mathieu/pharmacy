package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Data implements Serializable {
    private static final Gson gson = new Gson();

    public static void savePharmacy(Pharmacy pharmacy) {
        try (Writer writer = new FileWriter("pharmacy.json")) {
            gson.toJson(pharmacy, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Pharmacy loadPharmacy() {
        try (Reader reader = new FileReader("pharmacy.json")) {
            return gson.fromJson(reader, Pharmacy.class);
        } catch (FileNotFoundException e) {
            System.out.println("No previous data found, starting fresh.");
            return new Pharmacy("Pharmacy", "Unknown");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveOrders(List<Order> orders) {
        try (Writer writer = new FileWriter("orders.json")) {
            List<Map<String, Object>> orderData = new ArrayList<>();

            for (Order order : orders) {
                Map<String, Object> orderMap = new HashMap<>();
                orderMap.put("type", order instanceof Standard ? "standard" : "emergency");
                orderMap.put("name", order.getName());

                Map<String, Integer> productQuantities = new HashMap<>();
                for (Map.Entry<Product, Integer> entry : order.getOrder().entrySet()) {
                    productQuantities.put(entry.getKey().getName(), entry.getValue());
                }

                orderMap.put("orderMap", productQuantities);
                orderData.add(orderMap);
            }

            gson.toJson(orderData, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Order> loadOrders(Pharmacy pharmacy) {
        try (Reader reader = new FileReader("orders.json")) {
            Type type = new TypeToken<List<Map<String, Object>>>() {}.getType();
            List<Map<String, Object>> rawOrders = gson.fromJson(reader, type);
            List<Order> orders = new ArrayList<>();

            for (Map<String, Object> rawOrder : rawOrders) {
                String orderType = (String) rawOrder.get("type");
                String orderName = (String) rawOrder.get("name");
                Map<String, Double> productQuantities = (Map<String, Double>) rawOrder.get("orderMap");

                Order order;
                if ("standard".equalsIgnoreCase(orderType)) {
                    order = new Standard(pharmacy, orderName);
                } else if ("emergency".equalsIgnoreCase(orderType)) {
                    order = new Emergency(pharmacy, orderName);
                } else {
                    System.out.println("Unknown order type: " + orderType);
                    continue;
                }

                for (Map.Entry<String, Double> entry : productQuantities.entrySet()) {
                    String productName = entry.getKey().trim();
                    int quantity = entry.getValue().intValue();

                    Product product = pharmacy.getProduct(productName);
                    if (product != null) {
                        pharmacy.addOrder(orderType, orderName);
                        pharmacy.setProductToOrder(orderName, product.getName(), quantity);
                    } else {
                        System.out.println("Product not found in pharmacy: " + productName);
                    }
                }
                orders.add(order);
            }
            return orders;
        } catch (FileNotFoundException e) {
            System.out.println("No previous orders found, starting fresh.");
            return new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}