package disksaver;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vampa on 08.02.2016.
 *
 * Class, that provides creating of CD profiles.
 */
public class CDProfileCreator {
    private static Logger logger = Logger.getInstance();

    private final File drivePath;
    private Thread creatorThread;

    public CDProfileCreator(String path) {
        this.drivePath = new File(path);
        this.creatorThread = new Thread(new CreatorThread());
    }

    // Used to start scan specified CD
    public void startScan() {
        creatorThread.start();
    }

    // Returns list of available CD drives
    public static String[] getAvailableDrives() {
        List<String> availableDevices;
        File[] roots = File.listRoots();

        if (roots.length == 1 && "/".equals(roots[0].getPath()))
            availableDevices = getDrivesForUnix();
        else
            availableDevices = getDrivesForWindows(roots);

        String[] result = new String[availableDevices.size()];
        availableDevices.toArray(result);
        return result;
    }

    // If we're in *nix, then reading /etc/mtab
    private static List<String> getDrivesForUnix() {
        List<String> availableDevices = new ArrayList<>();

        try (BufferedReader mtabReader = new BufferedReader(new FileReader("/etc/mtab"))) {
            logger.write("Unix-like system detected: reading /etc/mtab");
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
            logger.write("Error reading /etc/mtab : " + e.getMessage());
        }
        return availableDevices;
    }

    // If we are running in Windows let's check all roots for suitable description
    private static List<String> getDrivesForWindows(File[] roots) {
        List<String> availableDevices = new ArrayList<>();
        logger.write("Windows system detected: searching for CD drive");
        for (File root : roots)
            if (root.exists() && "CD Drive".equals(FileSystemView.getFileSystemView().getSystemTypeDescription(root))) {
                logger.write("Found device: " + root.getPath());
                availableDevices.add(root.getPath());
            }
        return availableDevices;
    }

    class CreatorThread implements Runnable {
        @Override
        public void run() {

        }
    }
}
