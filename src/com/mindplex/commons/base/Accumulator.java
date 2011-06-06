package com.mindplex.commons.base;

import java.util.List;
import java.util.concurrent.Future;

/**
 *
 * @author Abel Perez
 */
public interface Accumulator<T>
{
    public T foldLeft(T source, List<Future<T>> collection);
}
