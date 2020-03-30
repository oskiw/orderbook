import java.util.*;
import java.util.stream.Collectors;

public final class SimpleOrderBook implements OrderBook {

    private final Map<Double, PriceLevelWithOrders> bidLevels = new TreeMap<>(PriceUtils.descendingPriceComparator);
    private final Map<Double, PriceLevelWithOrders> offerLevels = new TreeMap<>(PriceUtils.ascendingPriceComparator);

    List<Trade> trades = new LinkedList<>();

    @Override
    public void addOrder(Order order) {
        long leftoverQuantity = matchOrder(order);

        if (leftoverQuantity > 0) {
            double price = order.getPrice();
            PriceLevelWithOrders level = order.getSide() == Side.BUY ? getOrCreatePriceLevel(bidLevels, price) : getOrCreatePriceLevel(offerLevels, price);
            level.addOrder(order.getId(), leftoverQuantity);
        }
    }

    private long matchOrder(Order order) {
        Map<Double, PriceLevelWithOrders> levels = order.getSide() == Side.BUY ? offerLevels : bidLevels;
        long leftoverQuantity = order.getQuantity();
        Iterator<PriceLevelWithOrders> plIt = levels.values().iterator();
        while (plIt.hasNext()) {
            PriceLevelWithOrders level = plIt.next();
            if (PriceUtils.morePassive(order, level.getPrice())) {
                return leftoverQuantity;
            }
            Iterator<Map.Entry<Long, Long>> it = level.iterator();
            Map.Entry<Long, Long> entry = null;
            boolean matchLastEntry = false;
            while (it.hasNext()) {
                entry = it.next();
                long levelOrderId = entry.getKey();
                long levelOrderQuantity = entry.getValue();
                if (leftoverQuantity <= levelOrderQuantity) {
                    matchLastEntry = true;
                    break;
                }
                trades.add(new Trade(levelOrderId, order.getId(), levelOrderQuantity, level.getPrice()));
                level.decreaseVolume(levelOrderQuantity);
                it.remove();
                leftoverQuantity -= levelOrderQuantity;
            }

            if (matchLastEntry) {
                long levelOrderId = entry.getKey();
                trades.add(new Trade(levelOrderId, order.getId(), leftoverQuantity, level.getPrice()));
                level.decreaseOrderQuantity(levelOrderId, leftoverQuantity);
                leftoverQuantity = 0;
            }

            if (level.size() == 0) {
                // all orders removed from this level so remove the level
                plIt.remove();
            }
        }

        return leftoverQuantity;
    }

    private PriceLevelWithOrders getOrCreatePriceLevel(Map<Double, PriceLevelWithOrders> levels, double price) {
        PriceLevelWithOrders level;
        if (levels.containsKey(price)) {
            level = levels.get(price);
        } else {
            level = new PriceLevelWithOrders(price);
            levels.put(price, level);
        }
        return level;
    }

    @Override
    public boolean updateOrder(Order oldOrder, Order newOrder) {
        if (!removeOrder(oldOrder)) return false;
        addOrder(newOrder);
        return true;
    }

    @Override
    public boolean removeOrder(Order order) {

        double price = order.getPrice();
        Map<Double, PriceLevelWithOrders> levels = order.getSide() == Side.BUY ? bidLevels : offerLevels;
        if (!levels.containsKey(price)) {
            return false;
        }

        PriceLevelWithOrders level = levels.get(price);
        if (!level.removeOrder(order.getId())) {
            return false;
        }

        if (level.size() == 0) {
            levels.remove(price);
        }

        return true;
    }

    @Override
    public double getBestBid() {
        return bidLevels.size() > 0 ? bidLevels.keySet().iterator().next() : Double.NaN;
    }

    @Override
    public double getBestOffer() {
        return offerLevels.size() > 0 ? offerLevels.keySet().iterator().next() : Double.NaN;
    }

    @Override
    public long getBestBidVolume() {
        return bidLevels.size() > 0 ? bidLevels.values().iterator().next().getVolume() : 0;
    }

    @Override
    public long getBestOfferVolume() {
        return offerLevels.size() > 0 ? offerLevels.values().iterator().next().getVolume() : 0;
    }

    @Override
    public List<Level> getBidLevels() {
        return bidLevels.values().stream()
                .map(b -> new PriceLevel(b.getPrice(), b.getVolume()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Level> getOfferLevels() {
        return offerLevels.values().stream()
                .map(b -> new PriceLevel(b.getPrice(), b.getVolume()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Trade> getTrades() {
        return trades;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Bids:\n");
        for (Level level : getBidLevels()) {
            sb.append(level.getPrice()).append(" -> ").append(level.getVolume()).append("\n");
        }
        sb.append("Offers:\n");
        for (Level level : getOfferLevels()) {
            sb.append(level.getPrice()).append(" -> ").append(level.getVolume()).append("\n");
        }
        return sb.toString();
    }
}
