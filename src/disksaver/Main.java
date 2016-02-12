package disksaver;

import disksaver.dbservice.DBService;
import disksaver.dbservice.H2DBService;
import disksaver.dbservice.MySqlDBService;
import disksaver.ui.console.ConsoleUserInterface;
import disksaver.ui.UserInterface;

import java.io.*;
import java.util.Calendar;

/**
 * Created by vampa on 06.02.2016.
 *
 * Main.
 */
public class Main {

    private static final Logger logger = Logger.getInstance();

    public static void main(String[] args) {
        File logFile = new File(String.format("log/%1$te%1$tm%1$tY-%1$tH%1$tM%1$tS.log", Calendar.getInstance().getTime()));
        try {
            boolean logFileCreated = logFile.createNewFile();
            logger.setOut(new PrintWriter(logFile));
            if (logFileCreated)
                logger.write("Log file created");
        } catch (IOException e) {
            System.out.println("Unable to create log file : " + e.getMessage());
        }
        logger.write("Started");

        DBService dbService;
        if (args != null && 0 < args.length && "--mysql".equals(args[0])) {
            logger.write("MySQL database selected");
            dbService = new MySqlDBService();
        } else {
            logger.write("H2 database selected");
            dbService = new H2DBService();
        }

        UserInterface userInterface = new ConsoleUserInterface(dbService);
        userInterface.initUI();

        logger.write("Closing...");
        dbService.close();
        logger.close();
    }

}
