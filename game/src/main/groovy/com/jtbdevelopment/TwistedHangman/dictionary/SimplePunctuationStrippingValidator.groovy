package com.jtbdevelopment.TwistedHangman.dictionary

import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils

/**
 * Date: 10/28/14
 * Time: 10:04 PM
 */
@Component
@CompileStatic
class SimplePunctuationStrippingValidator implements Validator {
    private static Logger log = LoggerFactory.getLogger(SimplePunctuationStrippingValidator.class)
    @Autowired
    com.jtbdevelopment.TwistedHangman.dictionary.Dictionary dictionary

    @Override
    String validateWordPhrase(final String wordPhrase) {
        if (wordPhrase == null) {
            log.info("Invalidating null word phrase " + wordPhrase);
            return "";
        }

        String working = fixUpInputString(wordPhrase)
        if (StringUtils.isEmpty(working)) {
            log.info("Invalidating word phrase as empty " + wordPhrase);
            return wordPhrase
        }

        String invalid = working.tokenize().find({
            String word ->
                if (!dictionary.isValidWord(word)) {
                    return true
                }
        })
        if (invalid) {
            log.info("Invalidating word phrase " + wordPhrase + " for " + invalid);
            return invalid
        }
        return null
    }

    private String fixUpInputString(final String wordPhrase) {
        String working = new String((char[]) wordPhrase.toCharArray().findAll {
            char c ->
                if (c.isLetter() || c == '\'' || c == ' ')
                    return true

                return false
        }.toArray()).trim()

        while (working.length() > 0 && working.charAt(working.length() - 1) == '\'') {
            working = working.substring(0, working.length() - 1)
        }

        return working
    }
}
