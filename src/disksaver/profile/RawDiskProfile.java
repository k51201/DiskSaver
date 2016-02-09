package disksaver.profile;

import java.util.Date;

/**
 * Created by vampa on 08.02.2016.
 */

class RawDiskProfile {
    private final String volumeName;
    private final long size;
    private final Date modified;
    private final Date burned;

    private String name;
    private String description;
    private long category;

    public RawDiskProfile(String volumeName, long size, Date burned) {
        this.volumeName = volumeName;
        this.size = size;
        this.burned = burned;
        this.modified = new Date();
    }

    public String getVolumeName() {
        return volumeName;
    }

    public long getSize() {
        return size;
    }

    public Date getModified() {
        return modified;
    }

    public Date getBurned() {
        return burned;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
