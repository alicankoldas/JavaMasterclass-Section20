import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try(FileReader reader = new FileReader("file.txt")){
            char[] block = new char[1000];
            int data;
            while((data = reader.read(block)) != -1){
                String content = new String(block, 0, data);
                System.out.printf("---> [%d chars] %s%n", data, content);
            }
        } catch(IOException ex){
            ex.printStackTrace();
        }
            /*
            String line;
            while((line = bufferedReader.readLine()) != null){
                System.out.println(line);
            }
            */
        System.out.println("----------------------------------");
        try(BufferedReader bufferedReader = new BufferedReader(
                new FileReader("file.txt"))) {
            bufferedReader.lines().forEach(System.out::println);

        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }
}