package disksaver.creator;

import java.util.Date;

/**
 * Created by vampa on 08.02.2016.
 */

class RawDiskProfile {
    private final String name;
    private final long size;
    private final Date modified;
    private final Date burned;

    private String description;
    private long category;

    public RawDiskProfile(String name, long size, Date burned) {
        this.name = name;
        this.size = size;
        this.burned = burned;
        this.modified = new Date();
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
