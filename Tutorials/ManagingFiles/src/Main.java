import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class Main {
    public static void main(String[] args) {

//        File oldFile = new File("students.json");
//        File newFile = new File("student-activity.json");
//        if(oldFile.exists()) {
//            oldFile.renameTo(newFile);
//            System.out.println("File renamed successfully!");
//        } else {
//            System.out.println("File does not exist!");
//        }

//        Path oldPath = Path.of("students.json");
//        Path newPath = Path.of("files/student-activity.json");
//
//        try {
//            Files.createDirectories(newPath.subpath(0, newPath.getNameCount() - 1));
//            Files.move(oldPath, newPath);
//            System.out.println(oldPath + " moved (renamed to) --> " + newPath);
//        } catch(IOException ex){
//            ex.printStackTrace();
//        }

        Path fileDir = Path.of("files");
        Path resourceDir = Path.of("resources");
        try {
            recurseDelete(resourceDir);
            recurseCopy(fileDir, resourceDir);
            System.out.println("Directory copied to "+ resourceDir);
        } catch(IOException ex) {
            ex.printStackTrace();
        }

//        try(BufferedReader reader = new BufferedReader(new FileReader("files//student-activity.json"));
//            PrintWriter writer = new PrintWriter("students-backup.json")) {
//            reader.transferTo(writer);
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }

        String urlString = "https://api.census.gov/data/2019/pep/charagegroups?get=NAME,POP&for=state:*";
        URI uri = URI.create(urlString);
        try(var urlInputStream = uri.toURL().openStream();){
            urlInputStream.transferTo(System.out);
        } catch(IOException ex) {
            throw new RuntimeException(ex);
        }

        Path jsonPath = Path.of("USPopulationByState.txt");
        try(var reader = new InputStreamReader(uri.toURL().openStream());
            var writer = Files.newBufferedWriter(jsonPath)) {
            reader.transferTo(writer);
        } catch(IOException ex) {
            throw new RuntimeException(ex);
        }

        try(var reader = new InputStreamReader(uri.toURL().openStream());
            PrintWriter writer = new PrintWriter("USPopulationByState.csv")) {
                reader.transferTo(new Writer() {
                    @Override
                    public void write(char[] cbuf, int off, int len) throws IOException {
                        String jsonString = new String(cbuf, off, len).trim();
                        jsonString = jsonString.replace('[', ' ');
                        jsonString = jsonString.replaceAll("\\]", "");
                        writer.write(jsonString);
                    }

                    @Override
                    public void flush() throws IOException {
                        writer.flush();
                    }

                    @Override
                    public void close() throws IOException {
                        writer.close();
                    }
                });
            } catch(IOException ex) {
                throw  new RuntimeException(ex);
            }
    }

    public static void recurseCopy(Path source, Path target) throws IOException {
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        if(Files.isDirectory(source)) {
            try(var children = Files.list(source)) {
                children.toList().forEach(
                        p -> {
                            try {
                                Main.recurseCopy(
                                        p, target.resolve(p.getFileName()));
                            } catch(IOException ex){
                                throw new RuntimeException(ex);
                            }
                        }
                );
            }
        }
    }

    public static void recurseDelete(Path target) throws IOException {

        if(Files.isDirectory(target)) {
            try(var children = Files.list(target)) {
                children.toList().forEach(
                        p -> {
                            try {
                                Main.recurseDelete(p);
                            } catch(IOException ex){
                                throw new RuntimeException(ex);
                            }
                        }
                );
            }
        }
        Files.delete(target);
    }
}