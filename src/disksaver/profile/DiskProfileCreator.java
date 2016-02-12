package disksaver.profile;

import disksaver.Logger;
import disksaver.dbservice.DBException;
import disksaver.dbservice.DBService;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Vampa on 08.02.2016.
 *
 * Class, that provides creating of CD profiles.
 */
public class DiskProfileCreator {
    private static Logger logger = Logger.getInstance();
    private static boolean runningInUnix;

    private final Thread rawCreatorThread;
    private final RawProfileCreator rawCreator;

    public DiskProfileCreator(String path) {
        this.rawCreator = new RawProfileCreator(new File(path));
        this.rawCreatorThread = new Thread(rawCreator);
    }

    // Used to start scan specified CD
    public void startScan() {
        rawCreatorThread.start();
    }

    // Returns list of available CD drives
    public static List<String> getAvailableDrives() {
        List<String> availableDevices;
        File[] roots = File.listRoots();

        if (roots.length == 1 && "/".equals(roots[0].getPath())) {
            runningInUnix = true;
            availableDevices = getAvailableDrivesForUnix();
        } else {
            runningInUnix = false;
            availableDevices = getAvailableDrivesForWindows(roots);
        }

        return availableDevices;
    }

    // Returns volume label for specified drive path, algorithm depends on OS
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

    public boolean isScanComplete() {
        return rawCreator.getRawProfile() != null;
    }

    public void waitForRawCreator() {
        try {
            rawCreatorThread.join();
        } catch (InterruptedException ignore) {}
    }

    public List<String> getElementsPathList() {
        List<String> elementsPathList = new ArrayList<>();
        elementsPathList.addAll(rawCreator.getRawElements().stream().map(RawElement::getPath).collect(Collectors.toList()));
        return elementsPathList;
    }

    public boolean isElementADirectory(int index) {
        return rawCreator.getRawElements().get(index).isDirectory();
    }

    public void setProfileUserFields(String profileName, String profileDescription, long profileCategory) {
        rawCreator.getRawProfile().setName(profileName);
        rawCreator.getRawProfile().setDescription(profileDescription);
        rawCreator.getRawProfile().setCategory(profileCategory);
    }

    public void setElementDescription(int index, String description) {
        rawCreator.getRawElements().get(index).setDescription(description);
    }

    public void setElementCategory(int index, long category) {
        rawCreator.getRawElements().get(index).setCategory(category);
    }

    public void toggleSavingElement(int index, boolean save) {
        rawCreator.getRawElements().get(index).setSave(save);
    }

    public void toggleSavingIncludedElements(int index, boolean save) {
        List<RawElement> elements = rawCreator.getRawElements();
        for (int i = index + 1; i < elements.size(); i++)
            if (elements.get(i).getPath().startsWith(elements.get(index).getPath()))
                elements.get(i).setSave(save);
    }

    public void saveProfileToDB(DBService dbService, long defaultElementCategory) {
        RawDiskProfile profile = rawCreator.getRawProfile();
        List<RawElement> elements = rawCreator.getRawElements();

        long profileId = -1;
        try {
            profileId = dbService.addDiskProfile(profile.getName(), profile.getVolumeName(), profile.getSize(),
                    profile.getDescription(), profile.getModified(), profile.getBurned(), profile.getCategory());

            logger.write("Created profile: ID #" + profileId);
        } catch (DBException e) {
            e.printStackTrace();
        }

        if (profileId > 0) {
            int elementCounter = 0;
            for (RawElement element : elements)
                if (element.isSave())
                    try {
                        if (element.getDescription() == null)
                            element.setDescription("");
                        if (element.getCategory() == 0)
                            element.setCategory(defaultElementCategory);
                        long elementId = dbService.addElement(element.getName(), element.getPath(),
                                element.getDescription(), element.getSize(), element.isDirectory(),
                                element.getCategory(), profileId);

                        elementCounter++;

                        logger.write("Created element: ID #" + elementId);
                    } catch (DBException e) {
                        e.printStackTrace();
                    }
            logger.write("Created " + elementCounter + " elements.");
        }
    }
}
