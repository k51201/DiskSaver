package disksaver.profile;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Vampa on 09.02.2016.
 *
 * Used to create raw disk profile on it's own thread
 */
class RawProfileCreator implements Runnable {
    private final File drivePath;
    private List<RawElement> rawElements = new ArrayList<>();

    private RawCDProfile rawProfile;

    RawProfileCreator(File drivePath) {
        this.drivePath = drivePath;
    }

    RawCDProfile getRawProfile() {
        return rawProfile;
    }

    @Override
    public void run() {
        createElementsFrom(drivePath);
        String volumeName = CDProfileCreator.getVolumeName(drivePath);
        long size = drivePath.getTotalSpace();
        Date burned = new Date(drivePath.lastModified());

        rawProfile = new RawCDProfile(volumeName, size, burned, rawElements);
    }

    // Recursively adds files and directories in rawElements list
    private void createElementsFrom(File path) {
        if (path.exists() && !path.equals(drivePath))
            rawElements.add(new RawElement(path));
        if (path.isDirectory()) {
            File[] files = path.listFiles();
            if (files != null)
                for (File file : files)
                    createElementsFrom(file);
        }
    }
}