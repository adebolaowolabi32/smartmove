package com.interswitch.smartmoveserver.util;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Objects;

/**
 *
 */
public class RandomUtil {

    public static final String UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String LOWER_CASE = UPPER_CASE.toLowerCase(Locale.ROOT);

    public static final String NUMBER_DIGITS = "0123456789";

    public static final String ALPHA_NUMERIC = UPPER_CASE + LOWER_CASE + NUMBER_DIGITS;

    private final SecureRandom random;

    private final char[] symbols;

    private final char[] buf;

    public RandomUtil(int length, SecureRandom random, String symbols) {
        if (length < 1) throw new IllegalArgumentException();
        if (symbols.length() < 2) throw new IllegalArgumentException();
        this.random = Objects.requireNonNull(random);
        this.symbols = symbols.toCharArray();
        this.buf = new char[length];
    }

    /**
     * Create an alphanumeric string generator.
     */
    public RandomUtil(int length, SecureRandom random) {
        this(length, random, ALPHA_NUMERIC);
    }

    /**
     * Create an alphanumeric strings from a secure generator.
     */
    public RandomUtil(int length) {
        this(length, new SecureRandom());
    }

    /**
     * Create session identifiers.
     */
    public RandomUtil() {
        this(21);
    }

    public static String getRandomNumber(int n) {

        String randomCode = "";
        do {
            long randomNumber = generateRandomDigits(n);
            randomCode = String.format("%d", randomNumber);
        } while (randomCode.trim().length() < n);

        return randomCode;

    }

    /**
     * @param n the number of digits the code to be generated must comprise.
     * @return a random number,to be used as OTP code
     */
    private static long generateRandomDigits(int n) {
        int m = (int) Math.pow(10, n - 1);
        return m + new SecureRandom().nextInt(9 * m);
    }

    public String nextString() {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf);
    }

}
