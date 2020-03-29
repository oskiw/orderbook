import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderBookTest {

    @Test
    public void testAddOrders() {
        OrderBook book = new DefaultOrderBook();
        book.addOrder(new DefaultOrder(12.2, 100, Side.BUY));
        book.addOrder(new DefaultOrder(16.2, 108, Side.BUY));
        book.addOrder(new DefaultOrder(16.2, 118, Side.BUY));
        book.addOrder(new DefaultOrder(10.2, 109, Side.SELL));

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


    //TODO: Implement below

    @Test
    public void testRemoveOrders() {

    }

    @Test
    public void testUpdateOrders() {

    }

    @Test
    public void testRemoveUnknownOrder() {

    }

    @Test
    public void testUpdateUnknownOrder() {

    }
}