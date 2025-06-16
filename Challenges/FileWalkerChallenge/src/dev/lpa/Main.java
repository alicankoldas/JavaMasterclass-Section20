package dev.lpa;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {

        Path startingPath = Path.of(".");
        FileVisitor<Path> statsVisitor = new StatsVisitor(1);
        try {
            Files.walkFileTree(startingPath, statsVisitor);
        } catch(IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static class DirectoryInfo {
        private int size;
        private int folderNbr;
        private int fileNbr;
    }
    private static class StatsVisitor extends SimpleFileVisitor<Path> {

        private Path initialPath = null;
        private final Map<Path, DirectoryInfo> folderSizes = new LinkedHashMap<>();
        private int initialCount;
        private int printLevel;

        public StatsVisitor(int printLevel) {
            this.printLevel = printLevel;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Objects.requireNonNull(file);
            Objects.requireNonNull(attrs);

            DirectoryInfo dirInfo = folderSizes.get(file.getParent());
            if(dirInfo == null)
                dirInfo = new DirectoryInfo();
            dirInfo.fileNbr++;

            dirInfo.size += attrs.size();
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            Objects.requireNonNull(dir);
            Objects.requireNonNull(attrs);

            if(initialPath == null){ // it is the first visit to this method
                initialPath = dir;
                initialCount = dir.getNameCount();
            } else {
                int relativeLevel = dir.getNameCount() - initialCount;
                if(relativeLevel == 1) {
                    folderSizes.clear();
                }
                folderSizes.put(dir, new DirectoryInfo());
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            Objects.requireNonNull(dir);
//            if (exc != null)
//                throw exc;
            if(dir.equals(initialPath)) {
                return FileVisitResult.TERMINATE;
            }

            int relativeLevel = dir.getNameCount() - initialCount;
            if(relativeLevel == 1) {
                folderSizes.forEach((key, value) -> {
                    int level = key.getNameCount() - initialCount - 1;
                    if(level < printLevel){
                        System.out.printf("%s[%s] - %,d bytes - %,d fileNbr - %,d folderNbr %n",
                                "\t".repeat(level), key.getFileName(), value.size, value.fileNbr, value.folderNbr);
                    }
                });
            } else {
                DirectoryInfo dirInfo = folderSizes.get(dir);
                DirectoryInfo parentDirInfo = folderSizes.get(dir.getParent());
                parentDirInfo.size += dirInfo.size;
                parentDirInfo.fileNbr += dirInfo.fileNbr;
                parentDirInfo.folderNbr += dirInfo.folderNbr + 1;
                folderSizes.put(dir.getParent(), parentDirInfo);
            }
            return FileVisitResult.CONTINUE;
        }
    }
}