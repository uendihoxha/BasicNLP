package assignment;

public class Utils {

  public static boolean isPunctuation(char c) {
    return c == ','
        || c == '.'
        || c == '!'
        || c == '?'
        || c == ':'
        || c == ';'
        ;
  }
}
