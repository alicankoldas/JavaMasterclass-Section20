import java.io.*;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.MatchResult;

public class Main {

    public static void main(String[] args) {
/*
        try(Scanner scanner = new Scanner(new File("file.txt"))) {
//            while(scanner.hasNextLine()){
//                System.out.println(scanner.nextLine());
//            }
//            System.out.println(scanner.delimiter());
//            scanner.useDelimiter("$");
//            scanner.tokens().forEach(System.out::println);
            scanner.findAll("[A-Za-z]{10,}")
                    .map(MatchResult::group)
                    .distinct()
                    .sorted()
                    .forEach(System.out::println);
        } catch(FileNotFoundException ex){
            throw new RuntimeException(ex);
        }
*/
        //try(Scanner scanner = new Scanner(new File("fixedWidth.txt"))) {
        //try(Scanner scanner = new Scanner(Path.of("fixedWidth.txt"))) {
        //try(Scanner scanner = new Scanner(new FileReader("fixedWidth.txt"))) {
        try(Scanner scanner = new Scanner(new BufferedReader(new FileReader("fixedWidth.txt")))) {
            var results = scanner.findAll("(.{15})(.{3})(.{12})(.{8})(.{2}).*")
                    .skip(1)
                    .map(m -> m.group(3).trim())
                    .distinct()
                    .sorted()
                    .toArray(String[]::new);
            System.out.println(Arrays.toString(results));
        } catch(IOException ex){
            throw new RuntimeException(ex);
        }
    }
}