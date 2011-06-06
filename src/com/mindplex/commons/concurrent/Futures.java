package com.mindplex.commons.concurrent;

import com.mindplex.commons.base.Accumulator;
import com.mindplex.commons.base.Function;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

/**
 *
 */
public class Futures
{
    /**
     * 
     * @param futures
     * @return
     */
    public static int accumulateSum(Collection<Future<Integer>> futures) {

        int sum = 0;

        for (Future<Integer> future : futures) {
            try {
                sum += future.get();
                
            } catch (Exception error) {
                throw new RuntimeException("failed to accumulate sum from futures", error);
            }
        }

        return sum;
    }

    /**
     * 
     * @param futures
     * @return
     */
    public static int accumulateProducts(Collection<Future<Integer>> futures) {

        int product = 0;

        for (Future<Integer> future : futures) {
            try {
                product *= future.get();

            } catch (Exception error) {
                throw new RuntimeException("failed to accumulate product from futures", error);
            }
        }

        return product;
    }

    /**
     *
     * @param futures
     * @return
     */
    public static double accumulateAverage(Collection<Future<Integer>> futures) {

        int sum = 0;

        for (Future<Integer> future : futures) {
            try {
                sum += future.get();

            } catch (Exception error) {
                throw new RuntimeException("failed to accumulate average from futures", error);
            }
        }

        return (double) sum / futures.size();
    }

    /**
     * 
     * @param futures
     * @return
     */
    public static int sum(List<Future<Integer>> futures) {
        return Sum().foldLeft(0, futures);    
    }

    /**
     * 
     * @param futures
     * @return
     */
    public static double avg(List<Future<Integer>> futures) {
        return (double)Sum().foldLeft(0, futures) / futures.size();
    }

    /**
     * 
     * @return
     */
    private static Accumulator<Integer> Sum() {
        return new Accumulator<Integer>() {
            public Integer foldLeft(Integer source, List<Future<Integer>> futures) {
                int sum = source;
                for (Future<Integer> future : futures) {
                    try {
                        sum += future.get();
                    } catch (Exception error) {
                        throw new RuntimeException("failed to accumulate sum from futures", error);
                    }
                }
                return sum;
            }
        };   
    }

    /**
     * 
     * @return
     */
    private static Accumulator<Double> Avg() {
        return new Accumulator<Double>() {
            public Double foldLeft(Double source, List<Future<Double>> futures) {
                double sum = source;
                for (Future<Double> future : futures) {
                    try {
                        sum += future.get();
                    } catch (Exception error) {
                        throw new RuntimeException("failed to accumulate sum from futures", error);
                    }
                }
                return (double)sum / futures.size();
            }
        };
    }

}
