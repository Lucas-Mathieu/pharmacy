import java.util.ArrayList;
import java.util.List;

public abstract class Order {

    protected List<Product> orderList = new ArrayList<>();

    public abstract void setOrder();

    public abstract List<Product> getOrder();

    public abstract void removeOrder();

    public abstract boolean isUrgent();
}
