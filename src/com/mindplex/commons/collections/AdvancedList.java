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

    /**
     * 
     * @param func
     * @param <R>
     * @return
     */
    public <R> List<R> map(Function<E, R> func) {

        List<R> result = new ArrayList<R>();

        for (E element : elements) {
            result.add(func.apply(element));
        }

        return result;
    }

    /**
     * 
     * @param func
     * @param <R>
     * @return
     */
    public <R> List<R> flatMap(Function<E, List<R>> func) {

        List<List<R>> temp = new ArrayList<List<R>>();
        List<R> result = new ArrayList<R>();

        for (E element : elements) {
            temp.add(func.apply(element));
        }

        for (List<R> list : temp) {
            for (R r : list) {
                result.add(r);
            }
        }
        
        return result;
    }

    /*
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     Element retreival
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    */

    /**
     *
     * @return
     */
    public E head() {
        if (elements.isEmpty()) {
            throw new NoSuchElementException("head of empty list");
        }
        return elements.get(0);
    }

    /**
     *
     * @return
     */
    public E last() {
        if (elements.isEmpty()) {
            throw new NoSuchElementException("head of empty list");
        }
        return elements.get(elements.size()-1);
    }

    /**
     * 
     * @param predicate
     * @return
     */
    public E find(Function<E, Boolean> predicate) {
        for (E element : elements) {
            if (predicate.apply(element)) {
                return element;
            }
        }
        return null;
    }
    
    /*
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     Subcollections
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    */

    /**
     *
     * @return
     */
    public List<E> tail() {
        if (elements.isEmpty()) {
            throw new UnsupportedOperationException("tail of empty list");
        }
        
        List<E> result = new ArrayList<E>();
        for (E element : elements) {
            result.add(element);
        }
        result.remove(0);
        return result;
    }

    /**
     *
     * @return
     */
    public List<E> init() {
        if (elements.isEmpty()) {
            throw new UnsupportedOperationException("tail of empty list");
        }

        List<E> result = new ArrayList<E>();
        for (E element : elements) {
            result.add(element);
        }
        result.remove(elements.size()-1);
        return result;
    }


    /**
     * 
     * @param from
     * @param to
     * @return
     */
    public List<E> slice(int from, int to) {
        if (from > elements.size()) {
            return new ArrayList<E>();
        }

        List<E> result = new ArrayList<E>();

        if (to > elements.size()) {
            to = elements.size();
        }

        for (int i = from; i < to; i++) {
            result.add(elements.get(i));
        }
        
        return result;
    }

    /**
     * 
     * @param n
     * @return
     */
    public List<E> take(int n) {
        if (n > elements.size()) {
            n = elements.size();
        }

        if (n < 1) {
            return new ArrayList<E>();
        }
        
        List<E> result = new ArrayList<E>();

        for (int i = 0; i < n; i++) {
            result.add(elements.get(i));
        }

        return result;
    }

/**
     *
     * @param n
     * @return
     */
    public List<E> drop(int n) {
        if (n > elements.size()) {
            return new ArrayList<E>();
        }

        if (n < 1) {
            return elements;
        }

        List<E> result = new ArrayList<E>();

        for (int i = n; i < elements.size(); i++) {
            result.add(elements.get(i));
        }

        return result;
    }


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
     Reversal Operations
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    */

    /**
     * 
     * @return
     */
    public List<E> reverse() {
        List<E> reverse = new ArrayList<E>();
        for (int i = elements.size(); i >= 0; i--) {
            reverse.add(elements.get(i));
        }
        return reverse;
    }

    public Iterator<E> reverseIterator() {
        return new Iterator<E>() {

            private int size = elements.size();
            private int index = size - 1;

            public boolean hasNext() {
                if (elements.size() != size) {
                    throw new ConcurrentModificationException();
                }
                return (index < size && index >= 0);
            }

            public E next() {
                if (index > size() || index < 0) {
                    throw new NoSuchElementException("next on empty iterator");
                }
                E result = elements.get(index);
                index--;
                return result;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public <R> List<R> reverseMap(Function<E, R> func) {

        List<R> result = new ArrayList<R>();

        for (E element : reverse()) {
            result.add(func.apply(element));
        }

        return result;
    }
    
    /*
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     Comparison Operations
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    */

    /**
     * 
     * @param elements
     * @return
     */
    public boolean startsWith(List<E> elements) {
        if (elements.isEmpty()) {
            return false;
        }
        
        for (int i = 0; i < elements.size(); i++) {
            if (! this.elements.get(i).equals(elements.get(i))) return false;
        }
        return true;
    }

    /**
     * 
     * @param elements
     * @return
     */
    public boolean endsWith(List<E> elements) {

        if (elements.isEmpty()) {
            return false;
        }
        
        int index = this.elements.size() - elements.size();
        if (index < 0) {
            return false;
        }

        int count = 0;
        for (int i = index; i < this.elements.size(); i++) {
            if (! this.elements.get(i).equals(elements.get(count))) {
                return false;
            }
            count++;
        }
        return true;
    }

    /*
    public boolean containsSlice(List<E> elements) {

        if (elements.isEmpty()) {
            return false;
        }
        
        E first = elements.get(0);
        
    }
    */
    
    /*
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     Strings
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    */

    /**
     *
     * @param sb
     * @return
     */
    public String addString(StringBuilder sb) {
        for (E element : elements) {
            sb.append(element);
        }
        return sb.toString();
    }

    /**
     *
     * @param sb
     * @param separator
     * @return
     */
    public String addString(StringBuilder sb, String separator) {
        int count = elements.size() - 1;
        for (E element : elements) {
            sb.append(element);
            if (count > 0) {
                sb.append(separator);
            }
            count--;
        }
        return sb.toString();
    }

    /**
     * 
     * @param sb
     * @param start
     * @param separator
     * @param end
     * @return
     */
    public String addString(StringBuilder sb, String start, String separator, String end) {

        sb.append(start);

        int count = elements.size() - 1;
        for (E element : elements) {
            sb.append(element);
            if (count > 0) {
                sb.append(separator);
            }
            count--;
        }

        sb.append(end);
        return sb.toString();
    }

    /**
     * 
     * @param separator
     * @return
     */
    public String mkString(String separator) {
        StringBuilder buffer = new StringBuilder();

        int count = elements.size() - 1;
        for (E element : elements) {
            buffer.append(element);
            if (count > 0) {
                buffer.append(separator);
            }
            count--;
        }
        return buffer.toString();
    }

    /**
     *
     * @param start
     * @param separator
     * @param end
     * @return
     */
    public String mkString(String start, String separator, String end) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(start);
        
        int count = elements.size() - 1;
        for (E element : elements) {
            buffer.append(element);
            if (count > 0) {
                buffer.append(separator);
            }
            count--;
        }

        buffer.append(end);
        return buffer.toString();
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
