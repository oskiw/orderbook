import java.util.List;

public interface OrderBook {

    boolean addOrder(Order order);

    boolean updateOrder(Order oldOrder, Order newOrder);

    boolean removeOrder(Order order);

    double getBestBid();

    double getBestOffer();

    long getBestBidVolume();

    long getBestOfferVolume();

    List<Level> getBidLevels();

    List<Level> getOfferLevels();

    List<Trade> getTrades();
}
