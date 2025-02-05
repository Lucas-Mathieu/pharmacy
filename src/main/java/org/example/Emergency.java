import java.util.List;

public class Emergency extends Order {


    @Override
    public List<Product> getOrder() {
        return orderList;
    }

    @Override
    public void removeProduct(Product product) {
        orderList.remove(product);
    }

    @Override
    public boolean isUrgent() {
        return true;
    }
}
