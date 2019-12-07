package os.statistics.commons.model;

import java.util.Objects;

public class FileSystem {

    private final String path;

    private final long totalSpace;

    private final long usableSpace;

    private final long freeSpace;

    protected FileSystem() {
        this(null, 0L, 0L, 0L);
    }

    public FileSystem(String path, long totalSpace, long usableSpace, long freeSpace) {
        this.path = path;
        this.totalSpace = totalSpace;
        this.usableSpace = usableSpace;
        this.freeSpace = freeSpace;
    }

    public String getPath() {
        return path;
    }

    public long getTotalSpace() {
        return totalSpace;
    }

    public long getUsableSpace() {
        return usableSpace;
    }

    public long getFreeSpace() {
        return freeSpace;
    }

    @Override
    public String toString() {
        return "FileSystemInfo{" +
                "path='" + path + '\'' +
                ", totalSpace=" + totalSpace +
                ", usableSpace=" + usableSpace +
                ", freeSpace=" + freeSpace +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileSystem that = (FileSystem) o;
        return totalSpace == that.totalSpace &&
                usableSpace == that.usableSpace &&
                freeSpace == that.freeSpace &&
                Objects.equals(path, path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, totalSpace, usableSpace, freeSpace);
    }
}
