package com.mindplex.commons.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Abel Perez
 */
public interface LoadBalancedList<E>
{
    /**
     *
     * @param value
     * @param weight
     * @return
     */
    public LoadBalancedList add(E value, int weight);

    /**
     * 
     * @param value
     * @param weight
     * @return
     */
    public LoadBalancedList set(E value, int weight);

    /**
     * Removes the specified {@code element} from this list. If this list does
     * not contain the specified element, then no action is taken and this
     * method returns <tt>false</tt>; otherwise <tt>true</true>.
     *
     * @param element the element to remove from this list.
     *
     * @return {@code true} if this {@code LoadBalancedList} is modified,
     * {@code false} otherwise.
     */
    public boolean remove(E element);

    /**
     * Removes the specified collection of elements from this list. If this
     * list does not contain any of the specified elements, then no action is
     * taken and this method returns <tt>false</tt>; otherwise <tt>true</true>.
     *
     * @param collection the collection of elements to remove from this list.
     *
     * @return {@code true} if this {@code LoadBalancedList} is modified;
     * otherwise {@code false}.
     */
    public boolean removeAll(Collection<E> collection);

    /**
     * Removes all the elements in this list that are not contained in the
     * specified {@code collection}.  Once this method is complete, this
     * list will only contain the elements found in the specified
     * {@code collection}.
     *
     * @param collection the collection of elements to retain in this list.
     *
     * @return {@code true} if this {@code LoadBalancedList} is modified;
     * otherwise {@code false}.
     */
    public boolean retainAll(Collection<E> collection);

    /**
     * 
     * @return
     */
    public E get();

    /**
     * Returns {@code true} if the specified element is contained within this
     * list; otherwise {@code false}.
     *
     * @param element the element to search for in this list.
     *
     * @return {@code true} if the specified element is contained within this
     * list; otherwise {@code false}.
     */
    public boolean contains(E element);

    /**
     * Returns {@code true} if the specified collection of element is contained
     * within this list; otherwise {@code false}.
     *
     * @param collection the collection of elements to search for in this list.
     *
     * @return {@code true} if the specified collection of elements is contained
     * within this list; otherwise {@code false}.
     */
    public boolean containsAll(Collection<E> collection);

    /**
     * Returns <tt>true</tt> if this {@code LoadBalancedList} contains
     * elements; otherwise <tt>false</tt>.
     *
     * @return <tt>true</tt> if this {@code LoadBalancedList} contains
     * elements; otherwise <tt>false</tt>.
     */
    public boolean isEmpty();

    /**
     * Returns the count of elements contained in this list.
     *
     * @return the count of elements contained in this list.
     */
    public int size();

    /**
     * Removes all the elements contained in this {@code LoadBalancedList}.
     */
    public void clear();

    /**
     * Gets the list of elements contained in this {@code LoadBalancedList}.
     *
     * @return the list of elements contained in this {@code LoadBalancedList}.
     */
    public List<Element<E>> elements();

    /**
     * 
     * @return
     */
    public Iterator<E> iterator();
}