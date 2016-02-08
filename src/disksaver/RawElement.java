package disksaver;

import java.io.File;

/**
 * Created by vampa on 08.02.2016.
 */
class RawElement {
    private final String path;
    private final long size;
    private final boolean directory;

    public RawElement(File file) {
        this.path = file.getPath();
        this.size = file.length();
        this.directory = file.isDirectory();
    }

    public String getPath() {
        return path;
    }

    public long getSize() {
        return size;
    }
}
