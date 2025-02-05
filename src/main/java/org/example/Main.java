
public class Main {
    public static void main(String[] args) {
        Pharmacy p = new Pharmacy();

        p.addProduct("Smecta", 4.99, 2, "Medicine");
        p.addProduct("Aspirine", 9.99, 3, "Medicine");

        p.displayProducts();
    }
}