package kr.kdev.dg1s.biowiki.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by nbradbury on 1/3/14.
 */
public class FormatUtils {

    /*
     * NumberFormat isn't synchronized, so a separate instance must be created for each thread
     * http://developer.android.com/reference/java/text/NumberFormat.html
     */
    private static final ThreadLocal<NumberFormat> IntegerInstance = new ThreadLocal<NumberFormat>() {
        @Override
        protected NumberFormat initialValue() {
            return NumberFormat.getIntegerInstance();
        }
    };

    private static final ThreadLocal<DecimalFormat> DecimalInstance = new ThreadLocal<DecimalFormat>() {
        @Override
        protected DecimalFormat initialValue() {
            return (DecimalFormat) DecimalFormat.getInstance();
        }
    };

    /*
     * returns the passed integer formatted with thousands-separators based on the current locale
     */
    public static final String formatInt(int value) {
        return IntegerInstance.get().format(value).toString();
    }

    public static final String formatDecimal(int value) {
        return DecimalInstance.get().format(value).toString();
    }
}
