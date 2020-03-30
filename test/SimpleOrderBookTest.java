import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimpleOrderBookTest {

    @Test
    public void testSimpleBookCreationWithoutOrdersMatching() {
        OrderBook book = createTestOrderBook();
        assertTestOrderBook(book);
    }

    @Test
    public void testAddOrders() {
        OrderBook book = createTestOrderBook();

        // order quantity less than best offer
        book.addOrder(new Order(17.2, 58, Side.BUY));
        assertEquals(17.01, book.getBestOffer());
        assertEquals(118, book.getBestOfferVolume());
        assertEquals(2, book.getOfferLevels().size());
        assertEquals(3, book.getBidLevels().size());

        assertEquals(1, book.getTrades().size());

        // order quantity more than best offer
        book.addOrder(new Order(17.62, 350, Side.BUY));
        assertEquals(17.23, book.getBestOffer());
        assertEquals(1282, book.getBestOfferVolume());
        assertEquals(1, book.getOfferLevels().size());
        assertEquals(3, book.getBidLevels().size());
        assertEquals(4, book.getTrades().size());

        // order quantity more than 2 best levels
        book.addOrder(new Order(13.72, 600, Side.SELL));
        assertEquals(12.44, book.getBestBid());
        assertEquals(100, book.getBestBidVolume());
        assertEquals(1, book.getBidLevels().size());
        assertEquals(2, book.getOfferLevels().size());
        assertEquals(13.72, book.getBestOffer());
        assertEquals(41, book.getBestOfferVolume());
        assertEquals(7, book.getTrades().size());

        // order quantity more than total volume on the other side
        book.addOrder(new Order(20.55, 1400, Side.BUY));
        assertEquals(20.55, book.getBestBid());
        assertEquals(77, book.getBestBidVolume());
        assertEquals(Double.NaN, book.getBestOffer());
        assertEquals(0, book.getBestOfferVolume());
        assertEquals(10, book.getTrades().size());
    }

    @Test
    public void testRemoveOrders() {
        OrderBook book = createTestOrderBook();

        // remove order from top of the book
        Order order = new Order(16.5, 58, Side.SELL);
        book.addOrder(order);
        assertEquals(16.5, book.getBestOffer());
        assertEquals(58, book.getBestOfferVolume());

        assertTrue(book.removeOrder(order));
        assertEquals(17.01, book.getBestOffer());
        assertEquals(176, book.getBestOfferVolume());
        assertTestOrderBook(book);

        // remove order from the middle of the book
        order = new Order(15.32, 58, Side.BUY);
        book.addOrder(order);
        assertEquals(15.32, book.getBidLevels().get(1).getPrice());
        assertEquals(391, book.getBidLevels().get(1).getVolume());

        assertTrue(book.removeOrder(order));
        assertEquals(15.32, book.getBidLevels().get(1).getPrice());
        assertEquals(333, book.getBidLevels().get(1).getVolume());
        assertTestOrderBook(book);

        //remove already matched order
        order = new Order(15.32, 58, Side.SELL);
        assertFalse(book.removeOrder(order));
        assertTestOrderBook(book);
    }

    @Test
    public void testUpdateOrders() {
        OrderBook book = createTestOrderBook();

        Order oldOrder = new Order(16.5, 58, Side.SELL);
        book.addOrder(oldOrder);
        assertEquals(16.5, book.getBestOffer());
        assertEquals(58, book.getBestOfferVolume());

        Order newOrder = new Order(16.6, 258, Side.BUY);
        assertTrue(book.updateOrder(oldOrder, newOrder));
        assertEquals(17.01, book.getBestOffer());
        assertEquals(176, book.getBestOfferVolume());
        assertEquals(16.6, book.getBestBid());
        assertEquals(258, book.getBestBidVolume());

        //update already matched order
        oldOrder = new Order(15.5, 58, Side.SELL);
        book.addOrder(oldOrder);
        newOrder = new Order(16.6, 258, Side.BUY);
        assertFalse(book.updateOrder(oldOrder, newOrder));
    }

    /**
     * Returns order book:
     * Bids:
     * 16.2 -> 226
     * 15.32 -> 333
     * 12.44 -> 100
     * Offers:
     * 17.01 -> 176
     * 17.23 -> 1514
     */
    private OrderBook createTestOrderBook() {
        OrderBook book = new SimpleOrderBook();
        book.addOrder(new Order(12.44, 100, Side.BUY));
        book.addOrder(new Order(16.2, 108, Side.BUY));
        book.addOrder(new Order(17.23, 113, Side.SELL));
        book.addOrder(new Order(17.01, 176, Side.SELL));
        book.addOrder(new Order(16.2, 118, Side.BUY));
        book.addOrder(new Order(17.23, 198, Side.SELL));
        book.addOrder(new Order(15.32, 333, Side.BUY));
        book.addOrder(new Order(17.23, 1203, Side.SELL));

        return book;
    }

    private void assertTestOrderBook(OrderBook book) {
        assertEquals(16.2, book.getBestBid());
        assertEquals(226, book.getBestBidVolume());

        assertEquals(17.01, book.getBestOffer());
        assertEquals(176, book.getBestOfferVolume());

        List<Level> bidLevels = book.getBidLevels();
        assertEquals(3, bidLevels.size());
        assertEquals(16.2, bidLevels.get(0).getPrice());
        assertEquals(226, bidLevels.get(0).getVolume());
        assertEquals(15.32, bidLevels.get(1).getPrice());
        assertEquals(333, bidLevels.get(1).getVolume());
        assertEquals(12.44, bidLevels.get(2).getPrice());
        assertEquals(100, bidLevels.get(2).getVolume());

        List<Level> offerLevels = book.getOfferLevels();
        assertEquals(2, offerLevels.size());
        assertEquals(17.01, offerLevels.get(0).getPrice());
        assertEquals(176, offerLevels.get(0).getVolume());
        assertEquals(17.23, offerLevels.get(1).getPrice());
        assertEquals(1514, offerLevels.get(1).getVolume());

        assertEquals(0, book.getTrades().size());
    }
}