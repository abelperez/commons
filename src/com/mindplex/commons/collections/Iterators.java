package com.mindplex.commons.collections;

import com.mindplex.commons.base.Computation;
import com.mindplex.commons.base.Function;

import java.util.*;

/**
 *
 */
public class Iterators
{
    public static <E> Iterator<E> empty() {
        return new Iterator<E>() {
            public boolean hasNext() {
                return false;
            }

            public E next() {
                throw new NoSuchElementException("Iterator is empty.");
            }

            public void remove() {
                throw new UnsupportedOperationException("Iterator is read only.");
            }
        };
    }

    public static <E> Iterator<E> single(final E element) {
        return new Iterator<E>() {

            private boolean more = true;

            public boolean hasNext() {
                return more;
            }

            public E next() {
                if (more) {
                    more = false;
                    return element;
                }
                throw new NoSuchElementException("Iterator is empty.");
            }

            public void remove() {
                throw new UnsupportedOperationException("Iterator is read only.");
            }
        };
    }

    public static <E> Iterator<E> iterator(Collection<E> elements) {
        return elements.iterator();    
    }

    public static <E> Iterator<E> fill(final int n, final Computation<E> computation) {
        return new Iterator<E>() {

            private int index = 0;
            
            public boolean hasNext() {
                return index < n;
            }

            public E next() {
                if (hasNext()) {
                    index += 1;
                    return computation.compute();
                }
                throw new NoSuchElementException("Iterator is empty.");
            }

            public void remove() {
                throw new UnsupportedOperationException("Iterator is read only.");
            }
        };
    }

    public static <E> Iterator<E> tabulate(final int n, final Function<Integer, E> func) {
        return new Iterator<E>() {

            private int index = 0;
            
            public boolean hasNext() {
                return index < n;
            }

            public E next() {
                if (hasNext()) {
                    E result =  func.apply(index);
                    index += 1;
                    return result;
                }
                throw new NoSuchElementException("Iterator is empty.");
            }

            public void remove() {
                throw new UnsupportedOperationException("Iterator is read only.");
            }
        };
    }

    public static Iterator<Integer> range(final int start, final int end) {
        return range(start, end, 1);
    }

    public static Iterator<Integer> range(final int start, final int end, final int step) {
        if (step == 0) {
            throw new IllegalArgumentException("step cannot be zero");
        }
        return new Iterator<Integer>() {

            private int index = start;

            public boolean hasNext() {
                return index < end && 0 < index;
            }

            public Integer next() {
                if (hasNext()) {
                    Integer result = index;
                    index += step;
                    return result;
                }
                throw new NoSuchElementException("Iterator is empty.");
            }

            public void remove() {
                throw new UnsupportedOperationException("Iterator is read only.");
            }
        };
    }

    public static <E> Iterator<E> iterate(final E start, final Function<E,E> func) {
        return new Iterator<E>() {

            private boolean first = true;

            private E accumulated = start;
            
            public boolean hasNext() {
                return true;
            }

            public E next() {
                if (first) {
                    first = false;
                    return start; 
                }

                accumulated = func.apply(accumulated);
                return accumulated;
            }

            public void remove() {
                throw new UnsupportedOperationException("Iterator is read only.");
            }
        };
    }

    public static Iterator<Integer> from(final int start) {
        return from(start, 1);
    }

    public static Iterator<Integer> from(final int start, final int step) {
        return new Iterator<Integer>() {

            private boolean first = true;

            private int accumulated = start;

            public boolean hasNext() {
                return true;
            }

            public Integer next() {
                if (first) {
                    first = false;
                    return accumulated;
                }
                accumulated += step;
                return accumulated;

            }

            public void remove() {
                throw new UnsupportedOperationException("Iterator is read only.");
            }
        };
    }    

    public static <E> Iterator<E> continually(final Computation<E> computation) {
        return new Iterator<E>() {

            private int index = 0;

            public boolean hasNext() {
                return true;
            }

            public E next() {
                return computation.compute();
            }

            public void remove() {
                throw new UnsupportedOperationException("Iterator is read only.");
            }
        };
    }

    public static <E> Iterator<E> padTo(final List<E> elements, final int length, final E value) {
        return new Iterator<E>() {

            private int index = elements.size();

            private Iterator<E> it = elements.iterator();
            
            public boolean hasNext() {
                return it.hasNext() || index < length;
            }

            public E next() {
                if (hasNext()) {
                    if (it.hasNext()) {
                        return it.next();
                    } else {
                        index += 1;
                        return value;
                    }
                }
                throw new NoSuchElementException("Iterator is empty.");
            }

            public void remove() {
                throw new UnsupportedOperationException("Iterator is read only.");
            }
        };
    }
    
    /*
    public static Iterator<E> () {
        return new Iterator<E>() {
            public boolean hasNext() {
                return false;
            }

            public E next() {

                throw new NoSuchElementException("Iterator is empty.");
            }

            public void remove() {
                throw new UnsupportedOperationException("Iterator is read only.");
            }
        };
    }
    */

    public static <E> Iterator<List<E>> sliding(List<E> elements, int n) {
        return new SlidingListIterator<E>(elements, n);
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
                    x = 0;
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
            return new SlidingListIterator<E>(elements, n);
        }
    }
}
