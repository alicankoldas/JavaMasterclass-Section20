import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.stream.Collectors;

public class Main {

// Tokenize the text into words, remove any punctuation.
// Ignore words with 5 characters or less.
// Count the occurrences of each word.
// Display the top 10 most used words.


    public static void main(String[] args) {

        try(Scanner scanner = new Scanner(new BufferedReader(new FileReader("article.txt")))){
            scanner.findAll("[A-Za-z]{6,}")
                    .map(MatchResult::group)
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                    .entrySet()
                    .stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .sorted(Map.Entry.comparingByKey())
                    .filter(x -> x.getValue() > 1)
                    .forEach(System.out::println);
        } catch(IOException ex) {
            throw new RuntimeException(ex);
        }

        System.out.println("----------------------------------");

        Map<String, Long> wordCounter = new HashMap<>();
        try(Scanner scanner = new Scanner(new BufferedReader(new FileReader("article.txt")))){
            scanner.useDelimiter("\\s+|\\p{Punct}");
            while(scanner.hasNext()){
                String str = scanner.next().trim();
                if(str.length() > 5){
                    wordCounter.put(str, wordCounter.getOrDefault(str, 0L) + 1L);
                }
            }
        } catch(IOException ex) {
            throw new RuntimeException(ex);
        }

        for(Map.Entry<String, Long> word : wordCounter.entrySet()) {
            if(word.getValue() > 1)
                System.out.println(word.getKey() + "=" + word.getValue());
        }

    }
}