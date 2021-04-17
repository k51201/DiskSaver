package ru.vampa.disksaver;

import ru.vampa.disksaver.dbservice.DBService;
import ru.vampa.disksaver.dbservice.H2DBService;
import ru.vampa.disksaver.dbservice.MySqlDBService;
import ru.vampa.disksaver.ui.UserInterface;
import ru.vampa.disksaver.ui.console.ConsoleUserInterface;

/**
 * Created by vampa on 06.02.2016.
 *
 * Main.
 */
public class Main {
    private static final Logger logger = Logger.getInstance();

    public static void main(String[] args) {
        logger.write("Started");

        final DBService dbService;
        if (args != null && 0 < args.length && "--mysql".equals(args[0])) {
            logger.write("MySQL database selected");
            dbService = new MySqlDBService();
        } else {
            logger.write("H2 database selected");
            dbService = new H2DBService();
        }

        final UserInterface userInterface = new ConsoleUserInterface(dbService);
        userInterface.initUI();

        logger.write("Closing...");
        dbService.close();
        logger.close();
    }

}
