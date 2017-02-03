package com.readingcards;

import com.readingcards.util.TitleValidator;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test all variations of title of Card
 */
public class TitleValidatorUnitTest {

    @Test
    public void titleValidator_CorrectTitleSimple_ReturnsTrue() {
        assertTrue(TitleValidator.isTitleValid("This is a title"));
    }

    @Test
    public void titleValidator_WhiteSpaceTitle_ReturnsFalse() {
        assertFalse(TitleValidator.isTitleValid(" "));
    }

    @Test
    public void titleValidator_OnlyNumbersTitle_ReturnsFalse() {
        assertFalse(TitleValidator.isTitleValid("12324"));
    }

    @Test
    public void titleValidator_LengthLessThenMinCharsTitle_ReturnsFalse() {
        assertFalse(TitleValidator.isTitleValid("Abc"));
    }

    @Test
    public void titleValidator_LengthLessThenMaxCharsTitle_ReturnsTrue() {
        assertTrue(TitleValidator.isTitleValid("abcdesadfg"));
    }

    @Test
    public void titleValidator_EmptyString_ReturnsFalse() {
        assertFalse(TitleValidator.isTitleValid(""));
    }

    @Test
    public void titleValidator_NullTitle_ReturnsFalse() {
        assertFalse(TitleValidator.isTitleValid(null));
    }
}