package com.viamhealth.android.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by naren on 05/10/13.
 */
public class Validator {

    public static boolean isEmailValid(String email)
    {
        String regExpn = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }
}
