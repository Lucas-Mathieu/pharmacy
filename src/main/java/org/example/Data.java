package org.example;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Data implements Serializable {
    private static final Gson gson = new Gson();
    private static List<User> users = new ArrayList<>();

    static {
        loadUsers();
    }

    public static void savePharmacy(Pharmacy pharmacy) {
        try (Writer writer = new FileWriter("src/main/java/org/example/pharmacy.json")) {
            gson.toJson(pharmacy, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Pharmacy loadPharmacy() {
        try (Reader reader = new FileReader("src/main/java/org/example/pharmacy.json")) {
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
        try (Writer writer = new FileWriter("src/main/java/org/example/orders.json")) {
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
        try (Reader reader = new FileReader("src/main/java/org/example/orders.json")) {
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

    private static void loadUsers() {
        try (FileReader reader = new FileReader("src/main/java/org/example/users.json")) {
            JsonArray userArray = gson.fromJson(reader, JsonArray.class);  // Récupère un tableau JSON

            // Parse chaque utilisateur et ajoute-les à la liste
            for (JsonElement element : userArray) {
                JsonObject userObj = element.getAsJsonObject();

                // Utilisation de get() pour éviter le NullPointerException
                String name = userObj.has("name") ? userObj.get("name").getAsString() : null;
                String password = userObj.has("password") ? userObj.get("password").getAsString() : null;
                String role = userObj.has("role") ? userObj.get("role").getAsString() : "client"; // Valeur par défaut

                // Vérification des valeurs récupérées
                if (name == null || password == null) {
                    System.out.println("Erreur : un ou plusieurs champs sont manquants pour un utilisateur.");
                    continue;  // Ignorer cet utilisateur et passer au suivant
                }

                // Création d'un utilisateur en fonction de son rôle
                User user = createUserFromJson(name, password, role);
                users.add(user);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static User createUserFromJson(String name, String password, String role) {
        if ("admin".equals(role)) {
            return new Admin(name, password);
        } else if ("client".equals(role)) {
            return new Client(name, password);
        } else if ("employee".equals(role)) {
            return new Employee(name, password);
        } else {
            throw new JsonParseException("Rôle inconnu : " + role);
        }
    }

    private static void saveUsers() {
        try (FileWriter writer = new FileWriter("src/main/java/org/example/users.json")) {
            List<JsonObject> userObjects = new ArrayList<>();
            for (User user : users) {
                JsonObject userObj = new JsonObject();
                userObj.addProperty("name", user.getName());
                userObj.addProperty("password", user.getPassword());
                userObj.addProperty("role", user.getRoleName());

                userObjects.add(userObj);
            }

            gson.toJson(userObjects, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveUser(User user) {
        for (User existingUser : users) {
            if (existingUser.getName().equals(user.getName())) {
                System.out.println("Error : username " + user.getName() + " already taken.");
                return;
            }
        }

        users.add(user);
        saveUsers();
        System.out.println("User " + user.getName() + " saved as " + user.getRoleName());
    }

    public static void removeUser(String user) {
        users.removeIf(existingUser -> existingUser.getName().equals(user));
        saveUsers();
    }
}
