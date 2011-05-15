
package com.mindplex.commons.base;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A {@code Clock} contains convenient static methods that are related to
 * date and time.
 * 
 * @author Abel Perez
 */
public class Clock
{
    /**
     *
     */
    private static final String TODAY_FORMAT = "yyyy-MM-dd";

    /**
     * 
     */
    private static final SimpleDateFormat formatter = new SimpleDateFormat(TODAY_FORMAT);

    /**
     * 
     * @return
     */
    public static long unixTime() {
        return System.currentTimeMillis();
    }

    /**
     * 
     * @return
     */
    public static String today() {
        return formatter.format(new Date());
    }
}
