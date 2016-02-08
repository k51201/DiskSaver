package disksaver;

import disksaver.dbservice.DBService;
import disksaver.dbservice.H2DBService;
import disksaver.ui.ConsoleUserInterface;
import disksaver.ui.UserInterface;

import java.io.*;
import java.util.Calendar;

/**
 * Created by vampa on 06.02.2016.
 *
 * Main.
 */
public class Main {

    private static Logger logger = Logger.getInstance();

    public static void main(String[] args) {
        DBService dbService = new H2DBService();
        UserInterface userInterface = new ConsoleUserInterface(dbService);

        File logFile = new File(String.format("%1$te%1$tm%1$tY-%1$tH%1$tM%1$tS.log", Calendar.getInstance().getTime()));
        try {
            if (!logFile.exists())
                logFile.createNewFile();
            logger.setOut(new PrintWriter(logFile));
        } catch (IOException e) {
            System.out.println("Unable to create log file : " + e.getMessage());
        }
        logger.write("Started");
        userInterface.initUI();
    }

}
