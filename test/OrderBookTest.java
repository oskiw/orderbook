import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderBookTest {

    @Test
    public void testLevelsCreation() {
        Order order1 = new DefaultOrder(12.2, 100, Side.BUY);
        Order order2 = new DefaultOrder(16.2, 108, Side.BUY);
        Order order3 = new DefaultOrder(16.2, 118, Side.BUY);
        Order order4 = new DefaultOrder(10.2, 109, Side.SELL);

        OrderBook book = new DefaultOrderBook();
        book.addOrder(order1);
        book.addOrder(order2);
        book.addOrder(order3);
        book.addOrder(order4);

        System.out.println(book.toString());

        assertEquals(16.2, book.getBestBid());
        assertEquals(226, book.getBestBidVolume());

        assertEquals(10.2, book.getBestOffer());
        assertEquals(109, book.getBestOfferVolume());

        List<Level> bidLevels = book.getBidLevels();
        assertEquals(2, bidLevels.size());
        assertEquals(16.2, bidLevels.get(0).getPrice());
        assertEquals(226, bidLevels.get(0).getVolume());
        assertEquals(12.2, bidLevels.get(1).getPrice());
        assertEquals(100, bidLevels.get(1).getVolume());

        List<Level> offerLevels = book.getOfferLevels();
        assertEquals(1, offerLevels.size());
        assertEquals(10.2, offerLevels.get(0).getPrice());
        assertEquals(109, offerLevels.get(0).getVolume());
    }


    @Test
    public void testUnknownOrder() {

    }
}