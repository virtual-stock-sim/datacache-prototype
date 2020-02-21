import org.vss.datacache.DataCache;
import org.vss.datacache.Stock;

import java.util.Optional;

/**
 * Created by Earl Kennedy
 * https://github.com/Mnenmenth
 */
public class Main
{
    public static void main(String[] args)
    {
        Stock stock = Stock.GetStock(1).orElse(null);
    }
}
