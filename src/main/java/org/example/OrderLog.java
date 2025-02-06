package org.example;

import java.util.*;
import java.time.LocalDateTime;

public class OrderLog {
    List<Map<String, Object>> orderLog;


    public OrderLog() {
        orderLog = new ArrayList<>();
    }

    public List<Map<String, Object>> getLog() {
        return orderLog.reversed();
    }

    public void addLog(Order order) {

        Map<Product, Integer> orderCopy = new HashMap<>(order.getOrder());
        Map<String, Object> entry = new HashMap<>();

        entry.put("order", orderCopy);
        entry.put("is_urgent", order.isUrgent());
        entry.put("date", LocalDateTime.now().toString());

        orderLog.add(entry);

        System.out.println("order saved ");
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
