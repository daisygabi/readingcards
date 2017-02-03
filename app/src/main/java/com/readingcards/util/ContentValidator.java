package com.readingcards.util;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by gabrielaradu on 12/12/2016.
 */

public class ContentValidator implements TextWatcher {

    private static boolean isValid = true;
    public static final int MIN_LENGTH = 10;
    public static final int MAX_LENGTH = 400;
    private static String onlyDigitRegex = "\\d+";

    public static boolean isContentValid(final CharSequence content) {
        System.out.print("0 Valid: " + isValid);
        if(content == null ||
                content.length() <= MIN_LENGTH ||
                content.length() > MAX_LENGTH ||
                content.toString().matches(onlyDigitRegex)) {
            isValid = false;
            System.out.print("set valid to false");
        }
        System.out.print("1 Valid: " + isValid);
        return isValid;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        isValid = isContentValid(editable);
    }
}
