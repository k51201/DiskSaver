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
public class CDProfileEditor {
    private static Logger logger = Logger.getInstance();

    // Returns list of available CD drives
    public static List<String> getAvailableDrives() {
        ArrayList<String> opticalDeviceList = new ArrayList<>();
        File[] roots = File.listRoots();
        // If we're in *nix reading /etc/mtab
        if (roots.length == 1 && roots[0].getPath().equals("/"))
            try (BufferedReader mtabIn = new BufferedReader(new FileReader("/etc/mtab"))) {
                logger.write("Unix-like system detected: reading /etc/mtab");
                String line = "";
                while (line != null) {
                    line = mtabIn.readLine();
                    if (line.contains("iso9660")) {
                        String opticalDevice = line.split("\\s")[1];
                        opticalDeviceList.add(opticalDevice);
                        logger.write("Found device: " + opticalDevice);
                    }
                }
            } catch (FileNotFoundException e) {
                logger.write("/etc/mtab not found");
            } catch (IOException e) {
                logger.write("Error reading /etc/mtab : " + e.getMessage());
            }
            // If we are running in Windows let's check all roots for suitable description
        else {
            logger.write("Windows system detected: searching for CD drive");
            for (File root : roots)
                if ("CD Drive".equals(FileSystemView.getFileSystemView().getSystemTypeDescription(root)) && root.exists()) {
                    logger.write("Found device: " + root.getPath());
                    opticalDeviceList.add(root.getPath());
                }
        }
        return opticalDeviceList;
    }
}
