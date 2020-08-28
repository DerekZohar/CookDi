package com.example.cookdi.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextHelper {
    static public Boolean isTextEmpty(String text) {
        return text == null || text.isEmpty();
    }
    static public Boolean isURL(String text){
        Pattern VALID_URL_REGEX = Pattern.compile("<\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]>");
        Matcher matcher = VALID_URL_REGEX.matcher(text);
        return matcher.find();
    }
    static public Boolean isEmail(String text){
        Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(text);
        return matcher.find();
    }

}
