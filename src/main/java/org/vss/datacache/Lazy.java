package org.vss.datacache;

import java.util.function.Supplier;

/**
 * Created by Earl Kennedy
 * https://github.com/Mnenmenth
 */

// Container for an object to be lazily evaluated
public class Lazy<T>
{
    private Supplier<T> supplier;
    private T obj = null;

    private Lazy(Supplier<T> supplier)
    {
        this.supplier = supplier;
    }

    public boolean hasEvaluated()
    {
        return obj != null;
    }

    public synchronized T get()
    {
        if(obj == null)
        {
            obj = supplier.get();
        }
        return obj;
    }

    public static <T> Lazy<T> lazily(Supplier<T> supplier)
    {
        return new Lazy<>(supplier);
    }
}
