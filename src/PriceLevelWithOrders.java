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

    void addOrder(long orderId, long quantity) {
        volume += quantity;
        ordersQuantities.put(orderId, quantity);
    }

    boolean removeOrder(long orderId) {
        if (!ordersQuantities.containsKey(orderId)) {
            return false;
        }

        Long quantity = ordersQuantities.remove(orderId);
        volume -= quantity;
        return true;
    }

    void decreaseVolume(long quantity) {
        volume -= quantity;
    }

    void decreaseOrderQuantity(long levelOrderId, long quantity) {
        ordersQuantities.put(levelOrderId, ordersQuantities.get(levelOrderId) - quantity);
        volume -= quantity;
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

    public int size() {
        return ordersQuantities.size();
    }
}
