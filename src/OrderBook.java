import java.util.List;

public interface OrderBook {

    void addOrder(Order order);

    boolean updateOrder(Order oldOrder, Order newOrder);

    boolean removeOrder(Order order);


    //Level 1
    double getBestBid();

    double getBestOffer();

    long getBestBidVolume();

    long getBestOfferVolume();


    // Level 2
    List<Level> getBidLevels();

    List<Level> getOfferLevels();


    //TODO: Level 3
    // - Return all PriceLevelWithOrders but either make it immutable for the user or make a copy so user cannot
    //   change state of order book

    List<Trade> getTrades();
}
