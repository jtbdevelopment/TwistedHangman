package com.jtbdevelopment.TwistedHangman.phrasegatherer.wofs

import groovyx.gpars.GParsPool
import org.cyberneko.html.parsers.SAXParser

/**
 * Date: 11/1/2014
 * Time: 1:37 PM
 *
 * Good for 10/2009 -> Present
 */
class WheelOfFortuneSolutionsParser {
    public static final String BASEURL = "http://www.wheeloffortunesolutions.com/";

    public static parseTimeframe(int startMonth, int startYear, int endMonth, int endYear, final Closure callback) {
        if (startYear < 2009 || (startYear == 2009 && startMonth < 10)) {
            throw new IllegalArgumentException("WOS Site format not usable prior to 7/2009")
        }
        int currentMonth = startMonth
        int currentYear = startYear
        List<String> urls = [];
        while (currentYear < endYear || (currentYear == endYear && currentMonth <= endMonth)) {
            String url = BASEURL + currentYear + ("" + currentMonth).padLeft(2, '0') + ".asp"
            urls += url;
            ++currentMonth
            if (currentMonth > 12) {
                currentMonth = 1;
                currentYear++
            }
        }
        GParsPool.withPool {
            urls.eachParallel {
                String url ->
                    parseURL(url, callback)
            }
        }
    }

    public static parseURL(final String url, final Closure callback) {

        try {
            def parser = new SAXParser()
            parser.setFeature('http://xml.org/sax/features/namespaces', false)
            def page = new XmlParser(parser).parse(url)

            //  Get main solutions
            def data = page.depthFirst().LI.grep { it.@class == "Solution" }
            data.each {
                def category = it.DIV[0].value()[0]
                def value = it.value()[2]
                if (value instanceof Node) {
                    value = value.value()[0]
                }
                String newValue = ((String) value).replace('\t', '').replace('\n', '').replace('\r', '');
                callback((String) category, newValue)
            }

            //  Get bonus solutions
            data = page.depthFirst().SPAN.grep { it.@class == "Solution" }
            data.each {
                def category = it.depthFirst().SPAN.find { it.@class = "Italic" }.value()[0]
                def value = it.value()[3]
                if (value instanceof Node) {
                    value = value.value()[0]
                }
                callback(category, value)
            }
        } catch (Exception e) {
            println "Exception processing " + url + "\n" + e
            e.printStackTrace()
        }
    }
}
