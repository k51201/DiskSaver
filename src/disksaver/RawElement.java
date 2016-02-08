package disksaver;

import java.io.File;

/**
 * Created by vampa on 08.02.2016.
 */
public class RawElement {
    private final File file;

    public RawElement(File file) {
        this.file = file;
    }

    public String getPath() {
        return file.getPath();
    }

    public long getSize() {
        return file.length();
    }
}
