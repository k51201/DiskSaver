package disksaver.profile;

import java.io.File;

/**
 * Created by vampa on 08.02.2016.
 */

class RawElement {
    private final String name;
    private final String path;
    private final long size;
    private final boolean directory;

    private String description;
    private long category;

    private boolean save;

    public RawElement(File file, int drivePathLength) {
        String absolutePath = file.getPath();
        this.path = absolutePath.substring(drivePathLength);
        this.name = file.getName();
        this.size = file.length();
        this.directory = file.isDirectory();
        this.save = true;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public long getSize() {
        return size;
    }

    public boolean isDirectory() {
        return directory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCategory() {
        return category;
    }

    public void setCategory(long category) {
        this.category = category;
    }

    public boolean isSave() {
        return save;
    }

    public void setSave(boolean save) {
        this.save = save;
    }
}
