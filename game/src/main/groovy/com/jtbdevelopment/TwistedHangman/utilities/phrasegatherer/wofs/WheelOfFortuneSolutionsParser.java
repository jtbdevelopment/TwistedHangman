package com.jtbdevelopment.TwistedHangman.utilities.phrasegatherer.wofs;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Date: 11/1/2014 Time: 1:37 PM
 *
 * Good for 10/2009 -> Present
 */
class WheelOfFortuneSolutionsParser {

  private static final String BASEURL = "http://www.wheeloffortunesolutions.com/";

  static void parseTimeframe(int startMonth, int startYear, int endMonth, int endYear,
      final BiConsumer<String, String> callback) {
    if (startYear < 2009 || (startYear == 2009 && startMonth < 10)) {
      throw new IllegalArgumentException("WOS Site format not usable prior to 7/2009");
    }

    int currentMonth = startMonth;
    int currentYear = startYear;
    final List<String> urls = new LinkedList<>();
    while (currentYear < endYear || (currentYear == endYear && currentMonth <= endMonth)) {
      String url =
          BASEURL + currentYear + String.format("%2s", currentMonth).replace(' ', '0') + ".asp";
      urls.add(url);
      currentMonth = ++currentMonth;
      if (currentMonth > 12) {
        currentMonth = 1;
        currentYear++;
      }

    }

    urls.parallelStream().forEach(url -> parseURL(url, callback));
  }

  private static void parseURL(final String url, final BiConsumer<String, String> callback) {

    Document doc;
    try {
      doc = Jsoup.connect(url).get();
    } catch (IOException e) {
      return;
    }
    Elements li = doc.select(".Solution");

    li.forEach(element -> {
      try {
        if (element.tag().getName().equalsIgnoreCase("li")) {
          Elements divs = element.select("div");
          String category = divs.get(0).text().trim();
          String phrase = element.textNodes().get(element.textNodes().size() - 1).text().trim();
          callback.accept(category, phrase);
        }
        if (element.tag().getName().equalsIgnoreCase("span")) {
          Elements divs = element.select(".Italic");
          String category = divs.get(0).text().trim();
          String phrase = element.textNodes().get(element.textNodes().size() - 1).text().trim();
          callback.accept(category, phrase);
        }
      } catch (Exception e) {
        //  continue
      }
    });
  }
}
