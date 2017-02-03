package com.readingcards.util;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by gabrielaradu on 12/12/2016.
 */

public class TitleValidator implements TextWatcher {

    private static boolean isValid = false;
    private static final int MIN_LENGTH = 6;
    private static final int MAX_LENGTH = 20;
    private static String onlyDigitRegex = "\\d+";

    public static boolean isTitleValid(final CharSequence title) {

        if(title == null ||
                title.length() <= MIN_LENGTH || title.length() > MAX_LENGTH ||
                title.toString().matches(onlyDigitRegex)) {
            isValid = false;
        } else {
            isValid = true;
        }
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
        isValid = isTitleValid(editable);
    }
}