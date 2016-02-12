package disksaver.profile;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
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

    private RawDiskProfile rawProfile;

    RawProfileCreator(File drivePath) {
        this.drivePath = drivePath;
    }

    RawDiskProfile getRawProfile() {
        return rawProfile;
    }

    @Override
    public void run() {
        createElementsFrom(drivePath);
        String volumeName = DiskProfileCreator.getVolumeName(drivePath);
        long size = drivePath.getTotalSpace();
        Date burned = new Date(drivePath.lastModified());

        rawProfile = new RawDiskProfile(volumeName, size, burned);
    }

    // Recursively adds files and directories in rawElements list
    private void createElementsFrom(File path) {
        if (path.exists() && !path.equals(drivePath) && !Files.isSymbolicLink(path.toPath()))
            rawElements.add(new RawElement(path, drivePath.getPath().length()));
        if (path.isDirectory() && !Files.isSymbolicLink(path.toPath())) {
            File[] files = path.listFiles();
            if (files != null)
                for (File file : files)
                    createElementsFrom(file);
        }
    }

    List<RawElement> getRawElements() {
        return rawElements;
    }
}