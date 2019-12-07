package os.statistics.producer.statistics;

import os.statistics.commons.model.FileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileSystemStatisticsCollector {

    private final static Logger LOGGER  = LoggerFactory.getLogger(FileSystemStatisticsCollector.class);

    private final static java.nio.file.FileSystem fileSystem = FileSystems.getDefault();

    private final List<Path> fsRootPaths = new ArrayList<>();

    public FileSystemStatisticsCollector() {
        buildFSRootPaths();
    }

    private void buildFSRootPaths() {
        LOGGER.info("Build root FS paths");
        try {
            for (var rootDir: fileSystem.getRootDirectories())
                fsRootPaths.addAll(Files.walk(rootDir, 1).collect(Collectors.toList()));
        } catch (IOException ioe) {
            throw new RuntimeException("Couldn't build FS paths", ioe);
        }
        LOGGER.info("Will monitor the state of {} paths", fsRootPaths);
    }

    public List<FileSystem> loadFileSystemInfo() {
        return fsRootPaths.stream().map(path -> {
            final var dirInfo = path.toFile();
            return new FileSystem(
                    path.toString(),
                    dirInfo.getTotalSpace(),
                    dirInfo.getUsableSpace(),
                    dirInfo.getFreeSpace()
            );
        }).collect(Collectors.toUnmodifiableList());
    }

}
