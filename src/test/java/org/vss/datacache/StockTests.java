package org.vss.datacache;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Created by Earl Kennedy
 * https://github.com/Mnenmenth
 */
public class StockTests
{

    @Test
    public void testIdSearch()
    {
        Optional<Stock> stockOpt = Stock.GetStock(1);
        assertTrue(stockOpt.isPresent());
        Stock stock = stockOpt.get();

        assertEquals("GOOGL", stock.symbol);
        assertEquals(new BigDecimal("200.03"), stock.curr_price);

        assertEquals(stock.stock_data.get().data, "hello");
    }

    @Test
    public void testSymSearch()
    {
        Optional<Stock> stockOpt = Stock.GetStock("GOOGL");
        assertTrue(stockOpt.isPresent());
        Stock stock = stockOpt.get();

        assertEquals("GOOGL", stock.symbol);
        assertEquals(new BigDecimal("200.03"), stock.curr_price);

        assertEquals(stock.stock_data.get().data, "hello");
    }
}
