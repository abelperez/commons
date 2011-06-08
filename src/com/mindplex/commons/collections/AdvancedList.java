package com.mindplex.commons.collections;

import com.mindplex.commons.base.Function;

import java.util.*;

/**
 * This AdvancedList is modeled after the Scala <code>trait <em>Iterable</em></code>.
 * A {@link "http://www.scala-lang.org/docu/files/collections-api/collections_4.html"}
 * 
 * @author Abel Perez
 */
public class AdvancedList<E> implements Iterable<E>
{
    /**
     *
     */
    private List<E> elements = new ArrayList<E>();

    /*
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     Maps
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    */

    public <R> List<R> map(Function<E, R> func) {

        List<R> result = new ArrayList<R>();

        for (E element : elements) {
            result.add(func.apply(element));
        }

        return result;
    }

    public String flatMap(Function<E, String> func) {

        String result = "";

        for (E element : elements) {
            result += func.apply(element);
        }

        return result;
    }
    
    /*
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     Subcollections
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    */

    /**
     * 
     * @param n
     * @return
     */
    public List<E> takeRight(int n) {
        if (n < 1) {
            return new ArrayList<E>();
        }

        if (n > elements.size()) {
            return elements;
        }
        
        int index = (elements.size() - n);

        List<E> result = new ArrayList<E>();

        for (int i = index; i < elements.size(); i++) {
            result.add(elements.get(i));
        }

        return result;
    }

    /**
     * 
     * @param n
     * @return
     */
    public List<E> dropRight(int n) {
        if (n < 1) {
            return elements;
        }

        if (n > elements.size()) {
            return new ArrayList<E>();
        }

        int index = (elements.size() - n);

        List<E> result = new ArrayList<E>();

        for (int i = 0; i < index; i++) {
            result.add(elements.get(i));
        }

        return result;
    }
    
    /*
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     Zippers
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~    
    */

    /**
     * 
     * @param elements
     * @param <Y>
     * @return
     */
    public <Y> List<Tuple<E, Y>> zip(List<Y> elements) {

        List<Tuple<E, Y>> zipped = new ArrayList<Tuple<E, Y>>();

        for (int i = 0; i < this.elements.size(); i++) {
            if (i < elements.size()) {
                zipped.add(new Tuple<E, Y>(this.elements.get(i), elements.get(i)));
            }
        }

        return zipped;
    }

    /**
     * 
     * @param elements
     * @param x
     * @param y
     * @param <Y>
     * @return
     */
    public <Y> List<Tuple<E, Y>> zipAll(List<Y> elements, E x, Y y) {

        List<Tuple<E, Y>> zipped = new ArrayList<Tuple<E, Y>>();

        for (int i = 0; i < this.elements.size(); i++) {
            if (i < elements.size()) {
                zipped.add(new Tuple<E, Y>(this.elements.get(i), elements.get(i)));
            } else {
                zipped.add(new Tuple<E, Y>(this.elements.get(i), y));
            }
        }

        if (elements.size() > this.elements.size()) {
            for (int i = this.elements.size(); i < elements.size(); i++) {
                zipped.add(new Tuple<E, Y>(x, elements.get(i)));    
            }
        }

        return zipped;
    }

    /**
     * 
     * @return
     */
    public List<Tuple<E, Integer>> zipWithIndex() {

        List<Tuple<E, Integer>> zipped = new ArrayList<Tuple<E, Integer>>();

        for (int i = 0; i < elements.size(); i++) {
            zipped.add(new Tuple<E, Integer>(elements.get(i), i));
        }

        return zipped;
    }

    /*
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     Sliding and Grouped Iterators
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    */

    /**
     *
     * @param size
     * @return
     */
    public Iterator<List<E>> sliding(int size) {
        return new SlidingListIterator<E>(elements, size);
    }

    /**
     *
     * @param size
     * @return
     */
    public Iterator<List<E>> grouped(int size) {
        return new GroupedListIterator<E>(elements, size);
    }

    /*
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     List Operations
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    */

    /**
     * 
     * @return
     */
    public Iterator<E> iterator() {
      return elements.iterator();
    }

    /**
     *
     * @param value
     * @return
     */
    public AdvancedList<E> add(E value) {
        elements.add(value);
        return this;
    }

    /**
     *
     * @param value
     * @return
     */
    public AdvancedList<E> set(E value) {
        return add(value);
    }

    /**
     *
     * @param element
     * @return
     */
    public boolean remove(E element) {
        return elements.remove(element);
    }

    /**
     *
     * @param es
     * @return
     */
    public boolean removeAll(Collection<E> es) {
        return elements.removeAll(es);
    }

    /**
     *
     * @param es
     * @return
     */
    public boolean retainAll(Collection<E> es) {
        return elements.retainAll(es);
    }

    /**
     * 
     * @param index
     * @return
     */
    public E get(int index) {
        return elements.get(index);
    }

    /**
     *
     * @param element
     * @return
     */
    public boolean contains(E element) {
        return elements.contains(element);
    }

    /**
     *
     * @param es
     * @return
     */
    public boolean containsAll(Collection<E> es) {
        return elements.containsAll(es);
    }

    /**
     *
     * @return
     */
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    /**
     *
     * @return
     */
    public int size() {
        return elements.size();
    }

    /**
     *
     */
    public void clear() {
        elements.clear();
    }

    public static class Tuple<X, Y>
    {
        private X x;

        private Y y;

        public Tuple(X x, Y y) {
            this.x = x;
            this.y = y;
        }

        public X getX() {
            return x;
        }

        public void setX(X x) {
            this.x = x;
        }

        public Y getY() {
            return y;
        }

        public void setY(Y y) {
            this.y = y;
        }

        public String toString() {
            return new StringBuilder().
                    append("x => ").append(x).append(", y => ").append(y).toString();
        }
    }

    private static final class GroupedListIterator<E> implements Iterable<List<E>>, Iterator<List<E>>
    {
        private final List<E> elements;

        /**
         *
         */
        private int index = 0;

        private final int n;

        public GroupedListIterator(List<E> elements, int n) {
            if (n < 1) {
                throw new IllegalArgumentException("group size cannot be less than one.");
            }
            this.elements = elements;
            this.n = n;
        }

        public Iterator<List<E>> iterator() {
            return this;
        }

        public boolean hasNext() {
            return (index < elements.size() - 1);
        }

        public List<E> next() {

            if (index > elements.size() - 1) {
                throw new NoSuchElementException("next on empty iterator");
            }

            List<E> window = new ArrayList<E>();

            int x = index;
            int cap = index + n;
            int size = elements.size();

            for (int i = index; i < cap; i++) {
                if (x < elements.size()) {
                    window.add(elements.get(x));
                    x++;
                } else {
                    break;
                }
            }

            if (x > size-1) {
                index = size;
            } else {
                index += (n);
            }
            return window;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private static final class SlidingListIterator<E> implements Iterable<List<E>>, Iterator<List<E>>
    {
        private final List<E> elements;

        private int index = 0;

        private final int n;

        public SlidingListIterator(List<E> elements, int n) {

            if (n < 1) {
                throw new IllegalArgumentException("window size cannot be less than one.");
            }
            this.elements = elements;
            this.n = n;
        }

        public boolean hasNext() {
            return (index < elements.size() - 1);
        }

        public List<E> next() {

            if (index > elements.size() - 1) {
                throw new NoSuchElementException("next on empty iterator");
            }

            List<E> window = new ArrayList<E>();

            int x = index;
            int cap = index + n;
            int size = elements.size();

            for (int i = index; i < cap; i++) {
                if (x < size) {
                    window.add(elements.get(x));
                    x++;
                } else {
                    break;
                }
            }

            if (x > size-1) {
                index = size;
            } else {
                index++;
            }
            return window;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Iterator<List<E>> iterator() {
            return this;
        }
    }    
}
