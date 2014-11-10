package com.jtbdevelopment.TwistedHangman.dictionary;

import java.util.List;

/**
 * Date: 10/28/14
 * Time: 10:04 PM
 */
public interface Validator {
    public List<String> validateWordPhrase(final String wordPhrase);
}
