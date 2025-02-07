package org.example;
import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import com.google.gson.*;
public class OrderLog {
    List<Map<String, Object>> orderLog;
    private  final Gson gson = new Gson();

    public OrderLog() {
        orderLog = new ArrayList<>();
    }

    public List<Map<String, Object>> getLog() {
        return orderLog.reversed();
    }

    public void addLog(Order order) {
        String isUrgent;
        if(order.isUrgent()){
            isUrgent = "yes";
        }else{
            isUrgent = "no";
        }
        List<String> orderCopy = new ArrayList<> (order.getStringOrder());
        Map<String, Object> entry = new HashMap<>();

        entry.put("order", orderCopy);
        entry.put("is_urgent", isUrgent);
        entry.put("date", LocalDateTime.now().toString());

        orderLog.add(entry);

        System.out.println("order saved ");
    }
    public void addInLog(){
        try (Writer writer = new FileWriter("src/main/java/org/example/log.json")) {
            gson.toJson(orderLog, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayLog() {

        for (Map<String, Object> l : orderLog.reversed() ){
            System.out.println("----------------");

            for(String n: l.keySet()){

                if (n.equals("order")) {
                    Map<Product, Integer> order = (Map<Product, Integer>) l.get(n);

                    for (Map.Entry<Product, Integer> entry : order.entrySet()) {
                        Product product = entry.getKey();
                        Integer quantity = entry.getValue();

                        System.out.println("   Product: " + product.getName() + ", Quantity: " + quantity);
                    }
                }else{
                    System.out.println(n + " : " + l.get(n));
                }
            }
            System.out.println("----------------");
        }
    }
}
