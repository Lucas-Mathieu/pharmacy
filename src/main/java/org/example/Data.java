package org.example;

import com.google.gson.*;
import java.io.*;
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
            Map<String, Object> pharmacyData = new HashMap<>();
            pharmacyData.put("name", pharmacy.getName());
            pharmacyData.put("address", pharmacy.getAddress());

            // Save product list
            List<Map<String, Object>> productData = new ArrayList<>();
            for (Product product : pharmacy.getProductList()) {
                Map<String, Object> productMap = new HashMap<>();
                productMap.put("id", product.getId());
                productMap.put("name", product.getName());
                productMap.put("price", product.getPrice());
                productMap.put("quantity", product.getQuantity());
                productMap.put("category", product.getCategory());
                productData.add(productMap);
            }
            pharmacyData.put("productList", productData);

            // Save orderList
            List<Map<String, Object>> orderData = new ArrayList<>();
            for (Order order : pharmacy.getOrderList()) {
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
            pharmacyData.put("orderList", orderData);

            // Save orderSold
            List<Map<String, Object>> orderSoldData = new ArrayList<>();
            for (Order order : pharmacy.getOrderSold()) {
                Map<String, Object> orderMap = new HashMap<>();
                orderMap.put("type", order instanceof Standard ? "standard" : "emergency");
                orderMap.put("name", order.getName());

                Map<String, Integer> productQuantities = new HashMap<>();
                for (Map.Entry<Product, Integer> entry : order.getOrder().entrySet()) {
                    productQuantities.put(entry.getKey().getName(), entry.getValue());
                }
                orderMap.put("orderMap", productQuantities);
                orderSoldData.add(orderMap);
            }
            pharmacyData.put("orderSold", orderSoldData);

            // Write to the file
            gson.toJson(pharmacyData, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Pharmacy loadPharmacy() {
        try (Reader reader = new FileReader("src/main/java/org/example/pharmacy.json")) {
            JsonObject pharmacyJson = gson.fromJson(reader, JsonObject.class);

            String name = pharmacyJson.get("name").getAsString();
            String address = pharmacyJson.get("address").getAsString();
            Pharmacy pharmacy = new Pharmacy(name, address);

            // Load product list
            JsonArray productArray = pharmacyJson.getAsJsonArray("productList");
            for (JsonElement productElement : productArray) {
                JsonObject productJson = productElement.getAsJsonObject();
                int id = productJson.get("id").getAsInt();
                String productName = productJson.get("name").getAsString();
                double price = productJson.get("price").getAsDouble();
                int quantity = productJson.get("quantity").getAsInt();
                String category = productJson.get("category").getAsString();

                pharmacy.addProductWithoutSaving(productName, price, quantity, category);
            }

            // Load orderList
            JsonArray orderArray = pharmacyJson.getAsJsonArray("orderList");
            for (JsonElement orderElement : orderArray) {
                JsonObject orderJson = orderElement.getAsJsonObject();
                String orderType = orderJson.get("type").getAsString();
                String orderName = orderJson.get("name").getAsString();
                JsonObject orderMap = orderJson.getAsJsonObject("orderMap");

                Order order = "standard".equalsIgnoreCase(orderType)
                        ? new Standard(pharmacy, orderName)
                        : new Emergency(pharmacy, orderName);

                for (Map.Entry<String, JsonElement> entry : orderMap.entrySet()) {
                    String productName = entry.getKey();
                    int quantity = entry.getValue().getAsInt();
                    Product product = pharmacy.getProduct(productName);

                    if (product != null) {
                        order.setOrder(pharmacy, productName, quantity);
                    } else {
                        System.out.println("Warning: Product '" + productName + "' not found while loading orders.");
                    }
                }
                pharmacy.getOrderList().add(order);
            }

            // Load orderSold
            JsonArray orderSoldArray = pharmacyJson.has("orderSold") ? pharmacyJson.getAsJsonArray("orderSold") : null;
            if (orderSoldArray != null) {
                for (JsonElement orderSoldElement : orderSoldArray) {
                    JsonObject orderJson = orderSoldElement.getAsJsonObject();
                    String orderType = orderJson.get("type").getAsString();
                    String orderName = orderJson.get("name").getAsString();
                    JsonObject orderMap = orderJson.getAsJsonObject("orderMap");

                    Order order = "standard".equalsIgnoreCase(orderType)
                            ? new Standard(pharmacy, orderName)
                            : new Emergency(pharmacy, orderName);

                    for (Map.Entry<String, JsonElement> entry : orderMap.entrySet()) {
                        String productName = entry.getKey();
                        int quantity = entry.getValue().getAsInt();
                        Product product = pharmacy.getProduct(productName);

                        if (product != null) {
                            order.setOrder(pharmacy, productName, quantity);
                        } else {
                            System.out.println("Warning: Product '" + productName + "' not found while loading orders.");
                        }
                    }
                    pharmacy.getOrderSold().add(order);  // Add to orderSold
                }
            }

            return pharmacy;
        } catch (FileNotFoundException e) {
            System.out.println("No previous data found, starting fresh.");
            return new Pharmacy("Pharmacy", "Unknown");
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

    public static void exportSalesReport(Pharmacy p) {
        if (p.getOrderSold().isEmpty()) {
            System.out.println("No sales data available to export.");
            return;
        }

        Map<String, Integer> salesByProduct = new HashMap<>();
        Map<String, Double> revenueByProduct = new HashMap<>();
        double totalRevenue = 0;
        String mostSoldProduct = "";
        int highestQuantity = 0;

        for (Order order : p.getOrderSold()) {
            for (Map.Entry<Product, Integer> entry : order.getOrder().entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();
                double price = product.getPrice();

                salesByProduct.put(product.getName(), salesByProduct.getOrDefault(product.getName(), 0) + quantity);
                revenueByProduct.put(product.getName(), revenueByProduct.getOrDefault(product.getName(), 0.0) + (quantity * price));
                totalRevenue += quantity * price;
            }
        }

        for (Map.Entry<String, Integer> entry : salesByProduct.entrySet()) {
            if (entry.getValue() > highestQuantity) {
                highestQuantity = entry.getValue();
                mostSoldProduct = entry.getKey();
            }
        }
        
        try (FileWriter writer = new FileWriter("sales_report.csv")) {
            writer.write("Product Name,Quantity Sold,Revenue\n");
            for (Map.Entry<String, Integer> entry : salesByProduct.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue() + "," + revenueByProduct.get(entry.getKey()) + "\n");
            }
            writer.write("\nMost Sold Product: " + mostSoldProduct + " (" + highestQuantity + " units)\n");
            writer.write("Total Revenue: " + totalRevenue + "\n");

            System.out.println("Sales report exported successfully as 'sales_report.csv'.");
        } catch (IOException e) {
            System.out.println("Error exporting sales report: " + e.getMessage());
        }
    }
}