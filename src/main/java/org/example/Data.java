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
        /**
         * Saves the given Pharmacy object to a JSON file.
         *
         * This method takes a Pharmacy object, serializes its data by extracting relevant fields,
         * and replaces references to Product objects with their respective names in orders.
         * The result is saved in JSON format and written to a file located at:
         * "src/main/java/org/example/pharmacy.json".
         *
         * The JSON structure includes:
         * - "name": The name of the pharmacy.
         * - "address": The address of the pharmacy.
         * - "productList": A list of products available in the pharmacy.
         * - "orderList": A list of pending orders, including product names and their quantities.
         * - "orderSold": A list of completed orders, also including product details.
         *
         * @param pharmacy The Pharmacy object to be serialized and saved.
         * @throws IOException If an I/O error occurs while writing the file.
         */
        try (Writer writer = new FileWriter("src/main/java/org/example/pharmacy.json")) {
            Map<String, Object> pharmacyData = new HashMap<>();
            pharmacyData.put("name", pharmacy.getName());
            pharmacyData.put("address", pharmacy.getAddress());

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

            gson.toJson(pharmacyData, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Pharmacy loadPharmacy() {
        /**
         * Loads a Pharmacy object from a JSON file.
         *
         * This method reads the JSON file located at "src/main/java/org/example/pharmacy.json",
         * deserializes its contents, and reconstructs a Pharmacy object, including its name,
         * address, product list, pending orders, and sold orders. Orders are reconstructed by
         * retrieving product references using their names.
         *
         * The JSON structure includes:
         * - "name": The name of the pharmacy.
         * - "address": The address of the pharmacy.
         * - "productList": A list of products available in the pharmacy.
         * - "orderList": A list of pending orders, including product names and their quantities.
         * - "orderSold": A list of completed orders, also including product details.
         *
         * If the file is not found, a new Pharmacy object with default values is returned.
         *
         * @return The reconstructed Pharmacy object, or null if an I/O error occurs.
         */

        try (Reader reader = new FileReader("src/main/java/org/example/pharmacy.json")) {
            JsonObject pharmacyJson = gson.fromJson(reader, JsonObject.class);

            String name = pharmacyJson.get("name").getAsString();
            String address = pharmacyJson.get("address").getAsString();
            Pharmacy pharmacy = new Pharmacy(name, address);

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
        /**
         * Loads users from a JSON file.
         *
         * This method reads the JSON file located at "src/main/java/org/example/users.json",
         * deserializes its contents, and reconstructs a list of User objects with the goal of adding or removing to it.
         * Each user entry includes a name, password, and role (defaulting to "client" if missing).
         *
         * If a required field (name or password) is missing, the user is skipped,
         * and an error message is displayed.
         *
         * @throws IOException If an I/O error occurs while reading the file.
         */
        try (FileReader reader = new FileReader("src/main/java/org/example/users.json")) {
            JsonArray userArray = gson.fromJson(reader, JsonArray.class);

            for (JsonElement element : userArray) {
                JsonObject userObj = element.getAsJsonObject();

                String name = userObj.has("name") ? userObj.get("name").getAsString() : null;
                String password = userObj.has("password") ? userObj.get("password").getAsString() : null;
                String role = userObj.has("role") ? userObj.get("role").getAsString() : "client";

                if (name == null || password == null) {
                    System.out.println("Erreur : un ou plusieurs champs sont manquants pour un utilisateur.");
                    continue;
                }

                User user = createUserFromJson(name, password, role);
                users.add(user);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static User createUserFromJson(String name, String password, String role) {
        /**
         * Creates a User object from JSON data.
         *
         * This method takes user details (name, password, and role) and returns
         * a corresponding User object of type Admin, Client, or Employee based on the role.
         * If the role is unrecognized, a JsonParseException is thrown.
         *
         * @param name     The name of the user.
         * @param password The user's password.
         * @param role     The role of the user ("admin", "client", or "employee").
         * @return A User object of the appropriate type.
         * @throws JsonParseException If the role is unknown.
         */
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
        /**
         * Saves all users to a JSON file.
         *
         * This method serializes the list of users and writes it to the file
         * "src/main/java/org/example/users.json". Each user entry includes
         * the name, password, and role.
         *
         * @throws IOException If an I/O error occurs while writing the file.
         */
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
        /**
         * Saves a new user to the system.
         *
         * This method checks if the username is already taken. If not, the user
         * is added to the list and a confirmation message
         * is printed. Otherwise, an error message is displayed.
         *
         * @param user The User object to be saved.
         */
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
        /**
         * Removes a user from the system.
         *
         * This method searches for a user by their username and removes them
         * from the list if found.
         *
         * @param user The username of the user to remove.
         */
        users.removeIf(existingUser -> existingUser.getName().equals(user));
        saveUsers();
    }

    public static void exportSalesReport(Pharmacy p) {
        /**
         * Exports the sales report of a pharmacy to a CSV file.
         *
         * This method processes the sold orders from the given Pharmacy object to generate
         * a sales report. It calculates the total quantity sold and revenue for each product,
         * determines the most sold product, and computes the total revenue. The results are
         * written to a CSV file named "sales_report.csv".
         *
         * The CSV file includes:
         * - "Product Name": The name of the product.
         * - "Quantity Sold": The total quantity of the product sold.
         * - "Revenue": The total revenue generated by the product.
         *
         * Additional summary information includes:
         * - The most sold product and its quantity.
         * - The total revenue from all sales.
         *
         * If no sales data is available, a message is printed, and the method exits without writing a file.
         *
         * @param p The Pharmacy object whose sales data will be exported.
         */
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