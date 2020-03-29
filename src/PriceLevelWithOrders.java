import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

final class PriceLevelWithOrders implements Iterable<Map.Entry<Long, Long>>, Level {

    private final Map<Long, Long> ordersQuantities = new LinkedHashMap<>();
    private final double price;
    private long volume;

    PriceLevelWithOrders(double price) {
        this.price = price;
    }

    void addOrder(Order order) {
        volume += order.getQuantity();
        ordersQuantities.put(order.getId(), order.getQuantity());
    }

    boolean removeOrder(Order order) {
        if (!ordersQuantities.containsKey(order.getId())) {
            return false;
        }

        Long quantity = ordersQuantities.remove(order.getId());
        volume -= quantity;
        return true;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public long getVolume() {
        return volume;
    }

    @Override
    public Iterator<Map.Entry<Long, Long>> iterator() {
        return ordersQuantities.entrySet().iterator();
    }
}
