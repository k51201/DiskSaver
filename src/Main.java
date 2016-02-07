import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vampa on 06.02.2016.
 */
public class Main {

    Logger logger = Logger.getInstance();

    public static void main(String[] args) {
        Path path = Paths.get("/media/cdrom0/");
        System.out.println(path.getFileSystem());
    }

    private List<String> getOpticalDeviceList() {
        ArrayList<String> opticalDeviceList = new ArrayList<>();
        File[] roots = File.listRoots();
        // if we're in *nix reading /etc/mtab
        if (roots.length == 1 && roots[1].getPath().equals("/"))
            try (BufferedReader mtabIn = new BufferedReader(new FileReader("/etc/mtab"))) {
                logger.write("Unix-like system detected: reading /etc/mtab");
                String line = "";
                while (line != null) {
                    line = mtabIn.readLine();
                    if (line.contains("iso9660")) {
                        System.out.println(line.split("\\s")[1]);
                    }
                }
            } catch (FileNotFoundException e) {
                logger.write("/etc/mtab not found");
            } catch (IOException e) {
                logger.write("Error reading /etc/mtab : " + e.getMessage());
            }
        else // FIXME: if we are in Windows just sending roots to list
            for (int i = 0; i < roots.length; i++)
                opticalDeviceList.add(roots[i].getPath());

        return opticalDeviceList;
    }
}
