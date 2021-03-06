package org.vss.datacache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Created by Earl Kennedy
 * https://github.com/Mnenmenth
 */

// Holds data and performs operations for a row in the `stocks` table
public class Stock
{
    private static final Logger logger = LoggerFactory.getLogger(Stock.class);

    private static DataCache dataCache = DataCache.Instance();

    // Table columns
    public final int id;
    public String symbol;
    public BigDecimal curr_price;
    // Lazy eval so a large string isn't created/read from database every time the current price is needed
    public final Lazy<StockData> stock_data;

    protected Stock(int id, String symbol, BigDecimal curr_price, int stock_data)
    {
        this.id = id;
        this.symbol = symbol;
        this.curr_price = curr_price;
        this.stock_data = Lazy.lazily(StockData.GetStockData(stock_data).orElse(null));
    }

    // Search database for stock entry based on param
    public static Optional<Stock> GetStock(int id)
    {
        return get("SELECT id, symbol, curr_price, data_id FROM stocks WHERE id = ?", id);
    }
    public static Optional<Stock> GetStock(String symbol)
    {
        return get("SELECT id, symbol, curr_price, data_id FROM stocks WHERE symbol = ?", symbol);
    }

    // Generic search func
    private static Optional<Stock> get(String sql, Object param)
    {
        try(
                ResultSet rs = dataCache.executeSqlQuery(sql, new Object[]{param})
        )
        {
            // Return empty if there are no entries
            if(!rs.next()) return Optional.empty();

            return Optional.of(
                    new Stock(
                            rs.getInt("id"),
                            rs.getString("symbol"),
                            rs.getBigDecimal("curr_price"),
                            rs.getInt("data_id")
                    )
            );
        }
        catch(SQLException e)
        {
            logger.error(e.getMessage());
            logger.warn(String.format("Stock with search parameter %s not found", param));
            return Optional.empty();
        }
    }
}