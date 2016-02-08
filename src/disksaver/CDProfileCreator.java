package disksaver;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Vampa on 08.02.2016.
 *
 * Class, that provides creating of CD profiles.
 */
public class CDProfileCreator {
    private static Logger logger = Logger.getInstance();
    private static boolean runningInUnix;

    private final File drivePath;
    private final Thread creatorThread;

    public CDProfileCreator(String path) {
        this.drivePath = new File(path);
        this.creatorThread = new Thread(new RawProfileCreator());
    }

    // Used to start scan specified CD
    public void startScan() {
        creatorThread.start();
    }

    // Returns list of available CD drives
    public static String[] getAvailableDrives() {
        List<String> availableDevices;
        File[] roots = File.listRoots();

        if (roots.length == 1 && "/".equals(roots[0].getPath())) {
            runningInUnix = true;
            availableDevices = getAvailableDrivesForUnix();
        } else {
            runningInUnix = false;
            availableDevices = getAvailableDrivesForWindows(roots);
        }

        String[] result = new String[availableDevices.size()];
        availableDevices.toArray(result);
        return result;
    }

    public static String getVolumeName(File drivePath) {
        if (runningInUnix)
            return getVolumeNameForUnix(drivePath);
        else
            return FileSystemView.getFileSystemView().getSystemDisplayName(drivePath);
    }

    // If we're in *nix, then reading /etc/mtab
    private static List<String> getAvailableDrivesForUnix() {
        List<String> availableDevices = new ArrayList<>();

        logger.write("Unix-like system detected: reading /etc/mtab");
        try (BufferedReader mtabReader = new BufferedReader(new FileReader("/etc/mtab"))) {
            String line = "";
            while (line != null) {
                line = mtabReader.readLine();
                if (line != null && line.contains("iso9660")) {
                    String opticalDevice = line.split("\\s")[1];
                    availableDevices.add(opticalDevice);
                    logger.write("Found device: " + opticalDevice);
                }
            }
        } catch (FileNotFoundException e) {
            logger.write("/etc/mtab not found");
        } catch (IOException e) {
            logger.write("I/O exception while reading /etc/mtab : " + e.getMessage());
        }
        return availableDevices;
    }

    // If we are running in Windows let's check all roots for suitable description
    private static List<String> getAvailableDrivesForWindows(File[] roots) {
        List<String> availableDevices = new ArrayList<>();
        logger.write("Windows system detected: searching for CD drive");
        for (File root : roots)
            if (root.exists() && "CD Drive".equals(FileSystemView.getFileSystemView().getSystemTypeDescription(root))) {
                logger.write("Found device: " + root.getPath());
                availableDevices.add(root.getPath());
            }
        return availableDevices;
    }

    // It is not so easy to get volume label in *nix
    // This method uses shell command 'volname'
    private static String getVolumeNameForUnix(File drivePath) {
        logger.write("Executing volname command");

        String volumeName = "";
        String devicePath = getDevicePathForUnix(drivePath);
        try {
            Process p = Runtime.getRuntime().exec("volname " + devicePath);
            p.waitFor();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                volumeName = br.readLine();
            } catch (IOException e) {
                logger.write("I/O exception while reading volname output : " + e.getMessage());
            }
        } catch (InterruptedException e) {
            logger.write("Executing volname interrupted : " + e.getMessage());
        } catch (IOException e) {
            logger.write("I/O exception while executing volname : " + e.getMessage());
        }

        return volumeName;
    }

    // Getting device path searching for line, containing drivePath, in /etc/mtab
    private static String getDevicePathForUnix(File drivePath) {
        logger.write("Reading /etc/mtab");

        String devicePath = "";
        try (BufferedReader mtabReader = new BufferedReader(new FileReader("/etc/mtab"))) {
            String line = "";
            while (line != null) {
                line = mtabReader.readLine();
                if (line != null && line.contains(drivePath.getPath())) {
                    devicePath = line.split("\\s")[0];
                    logger.write("Found device path: " + devicePath);
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            logger.write("/etc/mtab not found");
        } catch (IOException e) {
            logger.write("I/O exception while reading /etc/mtab : " + e.getMessage());
        }

        return devicePath;
    }

    private class RawProfileCreator implements Runnable {
        private List<RawElement> rawElements = new ArrayList<>();

        @Override
        public void run() {
            createElementsFrom(drivePath);
            String volumeName = getVolumeName(drivePath);
            long size = drivePath.getTotalSpace();
            Date burned = new Date(drivePath.lastModified());

            RawCDProfile rawProfile = new RawCDProfile(volumeName, size, burned, rawElements);
        }

        // Recursively adds files and directories in rawElements list
        private void createElementsFrom(File path) {
            if (path.exists() && !path.equals(drivePath))
                rawElements.add(new RawElement(path));
            if (path.isDirectory()) {
                File[] files = path.listFiles();
                for (File file : files)
                    createElementsFrom(file);
            }
        }
    }
}
