package com.interswitch.smartmoveserver.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class NumberFormatter {

    private static DecimalFormat decimalFormat;

    public static double getFormatted(double number,int decimalPlaces){

        String integerPart = "#";
        String decimalPart = ".";

        for (int i=0;i<decimalPlaces;i++) {
            decimalPart = decimalPart.concat("#");
        }

        String decimalNumberPattern = integerPart.concat(decimalPart);
        decimalFormat = new DecimalFormat(decimalNumberPattern);
        return Double.parseDouble(decimalFormat.format(number));
    }

    public static double getFormatted(double number,int decimalPlaces,RoundingMode roundingMode){

        String integerPart = "#";
        String decimalPart = ".";

        for (int i=1;i<=decimalPlaces;i++) {
            decimalPart.concat("#");
        }

        String decimalNumberPattern = integerPart.concat(decimalPart);
        decimalFormat = new DecimalFormat(decimalNumberPattern);
        decimalFormat.setRoundingMode(roundingMode);
        return Double.parseDouble(decimalFormat.format(number));
    }

}
