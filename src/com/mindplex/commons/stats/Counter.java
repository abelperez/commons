package com.mindplex.commons.stats;

import com.mindplex.commons.base.Check;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Counter provides operations for tracking the number of times an event or
 * action has occurred that is relevant to the subject the counter is associated
 * with.
 * 
 * @author Abel Perez
 */
public class Counter
{    
    /**
     * A name that can be used to associate this counter with a subject.
     */
    private String name;

    /**
     * Internal long used to keep track of this counters state.
     */
    private AtomicLong counter = new AtomicLong();

    /**
     * prevent direct instantiation.
     */
    private Counter(String name) {
        this.name = Check.notNull(name);
    }

    /**
     * Constructs a counter based on the specified name.
     *
     * @param name the name to associated the new counter with.
     * 
     * @return a counter based on the specified name.
     */
    public static Counter of(String name) {
        return new Counter(name);
    }
    
    /**
     * Increments this counter by 1.
     * 
     * @return the counters new state.
     */
    public Long increment() {
        return counter.incrementAndGet();    
    }

    /**
     * Increments this counter by the specified value.
     * 
     * @param n the number to increment by.
     *
     * @return the counters new state.
     */
    public Long increment(int n) {
        return counter.addAndGet(n);    
    }

    /**
     * Gets the current value of the counter.
     * 
     * @return the current value of the counter.
     */
    public Long get() {
        return counter.get();
    }

    /**
     * Updates the counters value with the specified value, disregarding the
     * previous state.
     * 
     * @param n the value to update the counter with.
     *
     * @return the current value of the counter.
     */
    public Long update(long n) {
        return counter.getAndSet(n);
    }

    /**
     * Resets the counter to the value 0.
     */
    public void reset() {
        counter.set(0L);
    }

    /**
     * Gets a human readable message that contains this counters current state.
     *
     * @return a human readable message that contains this counters current
     * state.
     */
    public String toString() {
        return "counter value: " + counter.get();
    }
}
