import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public final class DefaultOrderBook implements OrderBook {

    private final static double THRESHOLD = 1E-10;

    private final Map<Double, PriceLevelWithOrders> bidLevels = new TreeMap<>((o1, o2) -> {
        if (Math.abs(o1 - o2) < THRESHOLD) return 0;
        if (o1 < o2) return 1;
        return -1;
    });

    private final Map<Double, PriceLevelWithOrders> offerLevels = new TreeMap<>((o1, o2) -> {
        if (Math.abs(o1 - o2) < THRESHOLD) return 0;
        if (o1 < o2) return -1;
        return 1;
    });

    List<Trade> trades = new LinkedList<>();

    @Override
    public boolean addOrder(Order order) {
        //TODO: Return false if invalid input

        double price = order.getPrice();
        PriceLevelWithOrders pl = order.getSide() == Side.BUY ? getOrCreatePriceLevel(bidLevels, price) : getOrCreatePriceLevel(offerLevels, price);
        pl.addOrder(order);

        return true;
    }

    private PriceLevelWithOrders getOrCreatePriceLevel(Map<Double, PriceLevelWithOrders> levels, double price) {
        PriceLevelWithOrders pl;
        if (levels.containsKey(price)) {
            pl = levels.get(price);
        } else {
            pl = new PriceLevelWithOrders(price);
            levels.put(price, pl);
        }
        return pl;
    }

    @Override
    public boolean updateOrder(Order oldOrder, Order newOrder) {
        //TODO: Return false if invalid input

        if (!removeOrder(oldOrder)) return false;
        return addOrder(newOrder);
    }

    @Override
    public boolean removeOrder(Order order) {
        //TODO: Return false if invalid input

        double price = order.getPrice();
        Map<Double, PriceLevelWithOrders> levels = order.getSide() == Side.BUY ? bidLevels : offerLevels;
         if (!levels.containsKey(price)) {
            return false;
        }

        PriceLevelWithOrders pl = levels.get(price);
        if (!pl.removeOrder(order)) {
            return false;
        }

        if (!pl.iterator().hasNext()) {
            levels.remove(price);
        }

        return true;
    }

    @Override
    public double getBestBid() {
        return bidLevels.keySet().iterator().next();
    }

    @Override
    public double getBestOffer() {
        return offerLevels.keySet().iterator().next();
    }

    @Override
    public long getBestBidVolume() {
        return bidLevels.values().iterator().next().getVolume();
    }

    @Override
    public long getBestOfferVolume() {
        return offerLevels.values().iterator().next().getVolume();
    }

    @Override
    public List<Level> getBidLevels() {
       return bidLevels.entrySet().stream()
               .map(b -> new PriceLevel(b.getKey(), b.getValue().getVolume()))
               .collect(Collectors.toList());
    }

    @Override
    public List<Level> getOfferLevels() {
        return offerLevels.entrySet().stream()
                .map(b -> new PriceLevel(b.getKey(), b.getValue().getVolume()))
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
        for (Level level: getBidLevels()) {
            sb.append(level.getPrice()).append(" -> ").append(level.getVolume()).append("\n");
        }
        sb.append("Offers:\n");
        for (Level level: getOfferLevels()) {
            sb.append(level.getPrice()).append(" -> ").append(level.getVolume()).append("\n");
        }
        return sb.toString();
    }
}
