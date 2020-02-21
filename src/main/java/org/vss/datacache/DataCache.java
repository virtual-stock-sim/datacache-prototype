package org.vss.datacache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * Created by Earl Kennedy
 * https://github.com/Mnenmenth
 */
public class DataCache
{
    private Connection db;

    private static final Logger logger = LoggerFactory.getLogger(DataCache.class);

    private static DataCache singleton = null;
    public static DataCache Instance()
    {
        return singleton == null ? (singleton = new DataCache()) : singleton;
    }

    private DataCache()
    {
        try
        {
            db = DriverManager.getConnection("jdbc:derby:vss_datacache;create=true");
            createTables();
        }
        catch (Exception e)
        {
            System.err.println("Error: " + e.getMessage());
            System.exit(-1);
        }
    }

    // Create tables if they don't exist
    private void createTables() throws SQLException
    {
        logger.info("Checking if tables exist");

        DatabaseMetaData metaData = db.getMetaData();
        ResultSet rs;
        int tablesCreated = 0;

        // Check to see if stocks_data table exists
        rs = metaData.getTables(null, "APP", "STOCKS_DATA", null);
        if(!rs.next() || !rs.getString(3).equals("STOCKS_DATA"))
        {
            // If it doesn't exist, create it
            logger.info("Creating table `stocks_data`");
            PreparedStatement stmt = db.prepareStatement(
                    "CREATE TABLE stocks_data(" +
                            "id INT NOT NULL " +
                                "PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
                            "data long varchar NOT NULL" +
                            ")"
            );
            stmt.execute();
            logger.info("Table `stocks_data` successfully created");
            ++tablesCreated;
        }

        // Check to see if stocks table exists
        rs = metaData.getTables(null, "APP", "STOCKS", null);
        if(!rs.next() || !rs.getString(3).equals("STOCKS"))
        {
            // If it doesn't exist, create it
            logger.info("Creating tables `stocks`");
            PreparedStatement stmt = db.prepareStatement(
                    "CREATE TABLE stocks(" +
                            "id INT NOT NULL " +
                                "PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
                            "symbol VARCHAR(10) NOT NULL UNIQUE," +
                            "curr_price DECIMAL(12, 2)," +
                            "data_id INT NOT NULL REFERENCES stocks_data(id)" +
                            ")"
            );
            stmt.execute();
            logger.info("Table `stocks` successfully created");
            ++tablesCreated;
        }

        if(tablesCreated > 0)
        {
            logger.info(String.format("(%d) tables created", tablesCreated));
        }
        else
        {
            logger.info("All tables exist");
        }
    }

    private static Object[] emptyObjArr = {};

    public void executeSql(String sql) throws SQLException
    {
        executeSql(sql, emptyObjArr);
    }
    public void executeSql(String sql, Object[] values) throws SQLException
    {
        logger.info(String.format("SQL: %s", sql));
        PreparedStatement stmt = db.prepareStatement(sql);
        for(int i = 0; i < values.length; ++i)
        {
            stmt.setObject(i, values[i]);
        }
        stmt.execute();
    }

    public ResultSet executeSqlQuery(String sql) throws SQLException
    {
        return executeSqlQuery(sql, emptyObjArr);
    }
    public ResultSet executeSqlQuery(String sql, Object[] values) throws SQLException
    {
        logger.info(String.format("SQL: %s", sql));
        PreparedStatement stmt = db.prepareStatement(sql);
        for(int i = 0; i < values.length; ++i)
        {
            stmt.setObject(i+1, values[i]);
        }
        return stmt.executeQuery();
    }

}
