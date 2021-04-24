package assignment;

public class Main {

  public static void main(String[] args) {
    if (args.length < 1) {
      System.out.println("Please specify as first argument the folder path");
      System.exit(-1);
    }

    String path = args[0];
    int n = 2;

    if (args.length == 2) {
      n = Integer.parseInt(args[1]);
    }
    new App(path, n).findLanguage();
  }
}
