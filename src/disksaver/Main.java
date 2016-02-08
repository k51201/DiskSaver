package disksaver;

import disksaver.ui.ConsoleUserInterface;

/**
 * Created by vampa on 06.02.2016.
 *
 * Main.
 */
public class Main {

    private static Logger logger = Logger.getInstance();

    public static void main(String[] args) {
        ConsoleUserInterface userInterface = new ConsoleUserInterface();
        logger.write("Started");
        userInterface.init();
    }

}
