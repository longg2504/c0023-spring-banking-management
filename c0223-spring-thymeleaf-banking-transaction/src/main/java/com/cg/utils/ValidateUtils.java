package com.cg.utils;

import java.util.regex.Pattern;

public class ValidateUtils {
    public static final String PHONENUMBER_REGEX = "^0[1-9]\\d{8,9}$";
    public static final String FULLNAME_REGEX = "^[A-Za-z\\s]*";

    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]{2,}$";

    public static boolean isFullName (String fullName) {
        return Pattern.matches(FULLNAME_REGEX, fullName);
    }
    public static boolean isPhoneNumber(String number) {
        return Pattern.matches(PHONENUMBER_REGEX, number);
    }
    public static boolean isEmail(String email){
        return Pattern.matches(EMAIL_REGEX,email);
    }
}
