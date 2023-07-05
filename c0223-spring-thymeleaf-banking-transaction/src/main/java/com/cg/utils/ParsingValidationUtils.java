package com.cg.utils;

public class ParsingValidationUtils {
    public static boolean isLongParsable(String number) {
        try {
            Long.parseLong(number);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }

    public static boolean isIntParsable(String number) {
        try {
            Integer.parseInt(number);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }

    public static boolean isDoubleParsable(String number) {
        try {
            Double.parseDouble(number);
            return true;
        } catch (final NumberFormatException e) {
            return false;
        }
    }
}
