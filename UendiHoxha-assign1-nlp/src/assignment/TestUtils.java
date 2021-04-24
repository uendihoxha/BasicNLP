package assignment;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

public class TestUtils {

  public static void storeSerialised(String language, Map<String, Integer> map) {
    try {
      FileOutputStream fos = new FileOutputStream(
          String.format("src/test/resources/%s.ser", language));
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(map);
      oos.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
