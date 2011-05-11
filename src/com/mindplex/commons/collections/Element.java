/**
 * Copyright (C) 2011 Mindplex Media, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.mindplex.commons.collections;

import java.util.concurrent.atomic.AtomicInteger;

import static com.mindplex.commons.base.Check.*;

/**
 * A {@code Element} that contains a value with a weight and a counter.
 * 
 * @author Abel Perez
 */
public class Element<E>
{
    /**
     * The value this element contains.
     */
    private E value;

    /**
     * The weight of the value this element contains.
     */
    private int weight;

    /**
     * A generic counter that can serve multiple purposes for example,
     * in a {@code List} of weighted elements, this counter can be used
     * to track the distribution of the element.
     */
    private AtomicInteger count = new AtomicInteger(0);

    /**
     * Constructs this element with the supplied value and weight.
     */
    private Element(E value, int weight) {
        this.value = value;
        this.weight = weight;
    }

    /**
     * Returns an {@code Element} from the supplied value and weight.
     * 
     * @param value the underlying value for this element.
     * @param weight the weight of the underlying value this element contains.
     * 
     * @return an {@code Element} from the supplied value and weight.
     *
     * @throws IllegalArgumentException if the specified value is null.
     */
    public static <E> Element<E> of(E value, int weight) {
        return new Element<E>(notNull(value), weight);
    }

    /**
     * Gets the value this element contains.
     * 
     * @return the value this element contains.
     */
    public E getValue() {
        return value;
    }

    /**
     * Sets the value this element contains.
     * 
     * @param value the value this element contains.
     *
     * @throws IllegalArgumentException if the specified value is null.
     */
    public void setValue(E value) {
        this.value = notNull(value);
    }

    /**
     * Gets the weight of the value this element contains.
     *
     * @return the weight of the value this element contains.
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Sets the weight of the value this element contains.
     *
     * @param weight the weight of the value this element contains.
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * Gets this elements count.
     * 
     * @return this elements count.
     */
    public int getCount() {
        return count.get();
    }

    /**
     * Sets this elements counter to the specified count.
     *
     * @param count the count to update this elements count with.
     */
    public void setCount(int count) {
        this.count.getAndSet(count);
    }

    /**
     * Increments this elements counter by 1.
     * 
     * @return the new count.
     */
    public int incrementCount() {
        return count.incrementAndGet();
    }

    /**
     * Decrements this elements counter by 1.
     *
     * @return the new count.
     */
    public int decrementCount() {
        return count.decrementAndGet();
    }

    /**
     * Equality is ultimately determined by comparing the underlying value
     * contained in this element.
     *
     * @param other the other element to compare against this one.
     *
     * @return {@code true} if the specified element is equal to this one;
     * otherwise {@code false}.
     */
    @Override public boolean equals(Object other) {
        if (! (other instanceof Element)) {
            return false;
        }
        Element otherElement = (Element)other;
        return this.value.equals(otherElement.value);
    }

    /**
     * {@inheritDoc}
     */
    @Override public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.value != null ? this.value.hashCode() : 0);
        return hash;
    }
}