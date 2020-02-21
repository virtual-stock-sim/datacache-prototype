package org.vss.datacache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Created by Earl Kennedy
 * https://github.com/Mnenmenth
 */

// Holds data and performs operations for a row in the `stocks_data` table
public class StockData
{
    private static final Logger logger = LoggerFactory.getLogger(StockData.class);
    private static DataCache dataCache = DataCache.Instance();

    // Table columns
    public final int id;
    public String data;
    protected StockData(int id, String data)
    {
        this.id = id;
        this.data = data;
    }

    // Search database for entry based on id
    public static Optional<StockData> GetStockData(int id)
    {
        try(
                ResultSet rs = dataCache.executeSqlQuery("SELECT data FROM stocks_data WHERE id=?", new Object[]{id})
        )
        {
            if(!rs.next()) return Optional.empty();

            return Optional.of(new StockData(
                    id,
                    rs.getString("data")
            ));
        }
        catch(SQLException e)
        {
            logger.error(e.getMessage());
            logger.warn(String.format("StockData with ID of `%d` not found", id));
            return Optional.empty();
        }
    }
}