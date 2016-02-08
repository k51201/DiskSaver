package disksaver;

import java.util.Date;
import java.util.List;

/**
 * Created by vampa on 08.02.2016.
 */

public class RawCDProfile {
    private final String volumeName;
    private final long size;
    private final Date modified;
    private final List<RawElement> elements;

    public RawCDProfile(String volumeName, long size, List<RawElement> elements) {
        this.volumeName = volumeName;
        this.size = size;
        this.elements = elements;
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

    public List<RawElement> getElements() {
        return elements;
    }
}
