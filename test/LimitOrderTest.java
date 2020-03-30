import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class LimitOrderTest {

    private static Stream<Arguments> invalidInputSource() {
        return Stream.of(
                Arguments.of(Double.NaN, 100, Side.SELL, LimitOrder.PRICE_IS_NOT_FINITE),
                Arguments.of(Double.POSITIVE_INFINITY, 100, Side.SELL, LimitOrder.PRICE_IS_NOT_FINITE),
                Arguments.of(Double.NEGATIVE_INFINITY, 100, Side.SELL, LimitOrder.PRICE_IS_NOT_FINITE),
                Arguments.of(123.43, -300, Side.BUY, LimitOrder.QUANTITY_NOT_POSITIVE),
                Arguments.of(123.43, 0, Side.BUY, LimitOrder.QUANTITY_NOT_POSITIVE),
                Arguments.of(123.43, 300, null, LimitOrder.SIDE_IS_NULL)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidInputSource")
    public void testInvalidInput(double price, long quantity, Side side, String expectedMessage) {
        IllegalArgumentException thrown = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new LimitOrder(price, quantity, side)
        );
        assertEquals(expectedMessage, thrown.getMessage());
    }

    @Test
    public void testOrdersHaveDifferentIds() {
        Set<Long> ids = new HashSet<>();
        Random rand = new Random();
        for (int i = 0; i < 100_000; i++) {
            Order order = new LimitOrder(rand.nextDouble() * 10_000, Math.abs(rand.nextLong()) + 1, rand.nextBoolean() ? Side.BUY : Side.SELL);
            if (ids.contains(order.getId())) {
                fail("Duplicate order ids were generated");
            }
            ids.add(order.getId());
        }
    }
}