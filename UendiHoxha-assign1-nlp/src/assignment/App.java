package assignment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class App {

  private final String dataPath;
  private final int n;
  public boolean debug = true;

  public App(String dataPath, int n) {
    this.dataPath = dataPath;
    this.n = n;
  }

  public void findLanguage() {
    ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> histograms = this.buildHistograms();

    Map<String, Double> norms = new HashMap<>();
    histograms.forEach((language, stringIntegerMap) -> {
      double n = calculateNorm(stringIntegerMap);
      norms.put(language, calculateNorm(stringIntegerMap));
    });

    ConcurrentHashMap<String, Integer> mysteryModel = new ConcurrentHashMap<>();
    this.processFile(Paths.get(String.format("%s/mystery.txt", dataPath)), mysteryModel);

//    for (Map.Entry<String, Map<String, Integer>> entry : histograms.entrySet()) {
//      System.out.println(entry.getKey());
//      System.out.println(entry.getValue());
//      TestUtils.storeSerialised(entry.getKey(), entry.getValue());
//    }
//    System.out.println(mysteryModel);

    double mysteryNorm = calculateNorm(mysteryModel);
    if (this.debug) {
      System.out.println(norms);
      System.out.println(mysteryNorm);
    }

    Map<String, Similarity> similarity = new HashMap<>();
    for (Map.Entry<String, ConcurrentHashMap<String, Integer>> entry : histograms.entrySet()) {
      double dotProduct = 1.0;
      for (Map.Entry<String, Integer> mysteryEntry : mysteryModel.entrySet()) {
        if (entry.getValue().containsKey(mysteryEntry.getKey())) {
          dotProduct += entry.getValue().get(mysteryEntry.getKey()) * mysteryEntry.getValue();
        }
      }
      double s = dotProduct / (norms.get(entry.getKey()) * mysteryNorm);
      similarity.put(entry.getKey(), new Similarity(s, Math.acos(s)));
    }

    if (this.debug) {
      for (Map.Entry<String, Similarity> s : similarity.entrySet()) {
        System.out.printf("%s %f %f\n", s.getKey(), s.getValue().getS(), s.getValue().getAngle());
      }
    }

    String closestLanguage = similarity.keySet().stream().max((o1, o2) -> {
      Similarity a = similarity.get(o1);
      Similarity b = similarity.get(o2);
      if (a.getS() > b.getS()) {
        return 1;
      } else if (a.getS() < b.getS()) {
        return -1;
      }
      return 0;
    }).get();

    System.out.printf("The closest language to mystery text is %s", closestLanguage);
  }

  private double calculateNorm(Map<String, Integer> histogram) {
    return Math.sqrt(histogram.values()
        .stream()
        .map(integer -> Math.pow(integer.doubleValue(), 2))
        .reduce(1.0, Double::sum));
  }

  public ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> buildHistograms() {
    ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> histograms = new ConcurrentHashMap<>();
    try (Stream<Path> paths = Files.walk(Paths.get(dataPath))) {
      paths
          .filter(Files::isDirectory)
          .forEach(dirPath -> {
            String language = dirPath.getFileName().toString();
            if (dirPath.toString().equals(dataPath)) {
              return;
            }
            try {
              Files.walk(dirPath)
                  .filter(Files::isRegularFile)
                  .collect(Collectors.toList())
                  .parallelStream()
                  .forEach(path -> {
                    if (this.debug) {
                      System.out.printf("Processing file %s for language %s\n", path, language);
                    }
                    histograms.computeIfAbsent(language, s -> new ConcurrentHashMap<>());
                    processFile(path, histograms.get(language));
                  });
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          });
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return histograms;
  }

  private void processFile(Path path, ConcurrentHashMap<String, Integer> histogram) {
    try {
      String actual = Files.readString(path);
      Arrays.stream(actual.split("\\s+")).map(s -> prepare(s).toString())
          .forEach(s -> this.group(s, histogram));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void group(String input, ConcurrentHashMap<String, Integer> histogram) {
    IntStream.range(0, input.length() - n + 1)
        .forEach(i -> {
          final String key = input.substring(i, i + this.n);
          histogram.merge(key, 1, Integer::sum);
        });
  }

  private StringBuilder prepare(String input) {
    return input.chars().filter(c -> {
      char cc = (char) c;
      return !Utils.isPunctuation(cc) && Character.isLetter(c);
    })
        .map(Character::toLowerCase)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append);
  }
}
