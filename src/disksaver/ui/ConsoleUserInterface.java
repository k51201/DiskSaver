package disksaver.ui;

import disksaver.CDProfileCreator;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Created by Vampa on 08.02.2016.
 *
 * User Interface for console.
 */
//TODO: Implement logging
public class ConsoleUserInterface {
    // Reader for user answers
    private Reader cin;

    // Entry point. Starting UI
    public void init() {
        try (Reader cin = new InputStreamReader(System.in)) {
            this.cin = cin;
            showMainMenu();
        } catch (IOException e) {
            System.out.println("I/O exception occured: " + e.getMessage());
            System.out.println("Closing...");
        }
    }

    // Main menu. Switching between browser and creator
    private void showMainMenu() throws IOException {
        String[] mainMenu = new String[]{"Browse & edit saved collection", "Add new disk profile to collection"};
        int choice;

        do {
            choice = printPagedMenu("Main menu", mainMenu, 0);
            switch (choice) {
                case 0:
                    showBrowserMenu();
                    break;
                case 1:
                    showCreatorMenu();
            }
        } while (0 <= choice && choice < mainMenu.length);
    }

    private void showBrowserMenu() throws IOException {
        int choice = printPagedMenu("Collection browser", new String[]{}, 0);
    }

    private void showCreatorMenu() throws IOException {
        int choice;
        String[] drives = CDProfileCreator.getAvailableDrives();
        if (drives == null || drives.length == 0) {
            System.out.println("CD drives not found");
            return;
        }

        do {
            choice = printPagedMenu("Add new disk profile. Select CD drive", drives, 0);
            if (choice < 0 || drives.length < choice)
                return;
        } while (confirmationRequest("Start scan?", false));
        CDProfileCreator creator = new CDProfileCreator(drives[choice]);
        creator.startScan();
        System.out.println("Creating");
    }

    // Prints question and accepts [y,Y,n,N] or, in case of pressing <Enter>, returns defaultAnswer
    private boolean confirmationRequest(String question, boolean defaultAnswer) throws IOException {
        do {
            System.out.print(question + " (" + (defaultAnswer ? "Y/n" : "y/N") + ") > ");
            char answer = (char) cin.read();
            switch (answer) {
                case 'y':
                case 'Y':
                    System.out.println();
                    return true;
                case 'n':
                case 'N':
                    System.out.println();
                    return false;
                case '\n':
                    System.out.println();
                    return defaultAnswer;
                default:
                    while (cin.ready())
                        cin.skip(1);
            }
        } while (true);
    }

    // Paged menu by 10 entries per page, takes digits [1-9, 0] and [+, -, q, Q] for user answer
    // Returns index of chosen entry in "entries" array or -1 for previous menu/quit
    private int printPagedMenu(String title, String[] entries, int page) throws IOException {
        if (title == null || entries == null || entries.length == 0)
            return -1;

        int choice = -2; // -2 - incorrect input
        boolean hasNextPage = false,
                hasPrevPage = false;

        int pages = entries.length/10;
        if (0 < (entries.length % 10))
            pages++;

        if (pages <= page)
            return -1;

        System.out.println(title + ":");
        for (int i = 0; i < 10 && (10*page + i) < entries.length; i++) {
            String entryDigit;
            if (i < 9)
                entryDigit = String.valueOf(i + 1);
            else
                entryDigit = "0";
            System.out.println(entryDigit + ": " + entries[10*page + i]);
        }

        if (page != 0) {
            hasPrevPage = true;
            System.out.println("-: previous page");
        }
        if (page != pages - 1) {
            hasNextPage = true;
            System.out.println("+: next page");
        }
        System.out.println("q: get back/quit\n");

        do {
            System.out.print("Select -> ");
            char input;
            do {
                input = (char) cin.read();
            } while (input == '\n');
            switch (input) {
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    choice = 10*page + input - 49;
                    break;
                case '0':
                    choice = 10*page + input - 38;
                    break;
                case '+':
                    if (hasNextPage)
                        choice = printPagedMenu(title, entries, page + 1);
                    break;
                case '-':
                    if (hasPrevPage)
                        choice = printPagedMenu(title, entries, page - 1);
                    break;
                case 'q':
                case 'Q':
                    choice = -1;
            }
        } while (choice < -1 || entries.length <= choice);

        while (cin.ready())
            cin.skip(1);
        System.out.println();

        return choice;
    }
}
