package dev.lukaszmichalak.fillers.helpers;

public class MonthTranslator {

  public static String getMonthNameInPolish(int month) {
    String[] months = {
      "Styczeń", "Luty", "Marzec", "Kwiecień", "Maj", "Czerwiec",
      "Lipiec", "Sierpień", "Wrzesień", "Październik", "Listopad", "Grudzień"
    };
    return months[month];
  }
}
