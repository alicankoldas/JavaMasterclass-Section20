import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {

        String header = """
                Student Id,Country Code,Enrolled Year,Age,Gender,\
                Experienced,Course Code,Engagement Month,Engagement Year,\
                Engagement Type""";

        Course jmc = new Course("JMC","Java Masterclass");
        Course pymc = new Course("PYC","Python Masterclass");
        List<Student> students = Stream.generate(() -> Student.getRandomStudent(jmc, pymc))
                .limit(2)
                .toList();


        try(BufferedWriter writer = Files.newBufferedWriter(Path.of("challenge.txt") )) {
            for(int i = 0; i < students.size() ; i++) {
                    writer.write(students.get(i).toString());
                    if(i != students.size() - 1)
                        writer.write(",");
            }
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }
}