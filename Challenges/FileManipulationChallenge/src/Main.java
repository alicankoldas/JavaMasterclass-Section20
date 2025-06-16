import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class Main {
    public static void main(String[] args) {
        Path path = Path.of("public/assets/icons");
        try {
            Files.createDirectories(path);
            createFile(path.subpath(0, 1), true);
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void createFile(Path target,boolean isMainDir) throws IOException {
        if(Files.isDirectory(target)){
            Path newPath = target.resolve("index.txt");
            Path copyNewPath = target.resolve("index_copy.txt");

            if(!Files.exists(newPath))
                Files.createFile(newPath);
            if(!Files.exists(copyNewPath))
                Files.copy(newPath, copyNewPath);
            if(isMainDir){
                BasicFileAttributes mainAttr = Files.readAttributes(target, BasicFileAttributes.class);
                System.out.println("Full Path: " + target.toAbsolutePath());
                System.out.println("Creation Time: " + mainAttr.creationTime());
                System.out.println("Modification Time: " + mainAttr.lastModifiedTime());
            }
            try (var dir = Files.newDirectoryStream(target)){
                dir.forEach(x -> {
                    try {
                        BasicFileAttributes attr = Files.readAttributes(x, BasicFileAttributes.class);
                        System.out.println("Full Path: " + x.toAbsolutePath());
                        System.out.println("Creation Time: " + attr.creationTime());
                        System.out.println("Modification Time: " + attr.lastModifiedTime());
                    } catch(IOException ex){
                        throw new RuntimeException(ex);
                    }

                });
            }
            try(var children = Files.list(target)) {
                children.toList().forEach(p -> {
                    try {
                        createFile(p, false);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            }
        }
    }
}