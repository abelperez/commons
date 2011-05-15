package com.mindplex.commons.base;

/**
 *
 * @author Abel Perez
 */
public interface Saver<T, E extends Exception>
{
    /**
     * 
     * @param data
     * @return
     */
    public boolean save(T data) throws E;
}
