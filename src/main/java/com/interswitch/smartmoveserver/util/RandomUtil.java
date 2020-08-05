package com.interswitch.smartmoveserver.util;

import java.util.Random;

public class RandomUtil {

    public static String getRandomNumber() {

        String randomCode = "";

        do {

            long randomNumber = generateRandomDigits(6);
            randomCode = String.format("%d", randomNumber);

        } while (randomCode.trim().length() < 6);


        return randomCode;

    }

    /**
     * @param n the number of digits the code to be generated must comprise.
     * @return a random number,to be used as OTP code
     */
    private static long generateRandomDigits(int n) {
        int m = (int) Math.pow(10, n - 1);
        return m + new Random().nextInt(9 * m);
    }
}
