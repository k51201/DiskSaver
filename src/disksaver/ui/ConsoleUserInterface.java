package disksaver.ui;

import disksaver.profile.CDProfileCreator;
import disksaver.dbservice.DBException;
import disksaver.dbservice.DBService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vampa on 08.02.2016.
 *
 * User Interface for console.
 */
//TODO: Implement logging
public class ConsoleUserInterface implements UserInterface {
    // Reader for user answers
    private BufferedReader cin;
    private DBService dbService;

    public ConsoleUserInterface(DBService dbService) {
        this.dbService = dbService;
    }

    // Entry point. Starting UI
    @Override
    public void initUI() {
        try (BufferedReader cin = new BufferedReader(new InputStreamReader(System.in))) {
            this.cin = cin;
            showMainMenu();
        } catch (IOException e) {
            System.out.println("I/O exception occured: " + e.getMessage());
            System.out.println("Closing...");
        }
        dbService.close();
    }

    // Main menu. Switching between browser and creator
    private void showMainMenu() throws IOException {
        List<String> mainMenu = new ArrayList<>();
        mainMenu.add("Browse & edit saved collection");
        mainMenu.add("Add new disk profile to collection");
        mainMenu.add("Print database info");

        int choice;

        do {
            choice = printPagedMenu("Main menu", mainMenu, 0);
            switch (choice) {
                case 0:
                    showBrowserMenu();
                    break;
                case 1:
                    showCreatorMenu();
                    break;
                case 2:
                    dbService.printConnectInfo();
                    System.out.println();
            }
        } while (0 <= choice && choice < mainMenu.size());
    }

    //NIY: Browser menu used to browse and edit saved collection
    private void showBrowserMenu() throws IOException {
        int choice = printPagedMenu("Collection browser", new ArrayList<>(), 0);
    }

    // Creator menu used to manage profile creation
    private void showCreatorMenu() throws IOException {
        int choice;
        List<String> drives = CDProfileCreator.getAvailableDrives();
        if (drives == null || drives.isEmpty()) {
            System.out.println("CD drives not found");
            return;
        }

        do {
            choice = printPagedMenu("Add new disk profile. Select CD drive", drives, 0);
        } while (choice < 0 || drives.size() < choice);

        if (!confirmationRequest("Start scan?", false))
            return;

        CDProfileCreator creator = new CDProfileCreator(drives.get(choice));
        creator.startScan();

        String profileName = askString("Enter name of this disk");

        List<String> categories = null;
        try {
            categories = dbService.getProfileCategories();
        } catch (DBException e) {
            e.printStackTrace();
        }
        if (categories == null)
            categories = new ArrayList<>(1);
        categories.add("Add new category");
        choice = printPagedMenu("Select category", categories, 0);

        long profileCategoryId;
        if (choice == categories.size() - 1)
            try {
                String categoryName = askString("Enter a name of new category");
                String categoryDesc = askString("Enter a description of " + categoryName);
                profileCategoryId = dbService.addNewProfileCategory(categoryName, categoryDesc);
            } catch (DBException e) {
                e.printStackTrace();
            }
        else
            profileCategoryId = choice;

        String profileDesc = askString("Enter a description for this disk profile");

        if (creator.getRawProfile() == null) {
            System.out.println("Performing scan. Please wait.");
            creator.waitForRawCreator();
        }
        System.out.println("Scan complete");

    }

    // Simply asks a question (1st parameter) and returns answer
    private String askString(String requestText) throws IOException {
        System.out.println(requestText + " > ");
        String answer = new BufferedReader(cin).readLine();
        return answer;
    }

    // Prints question and accepts [y,Y,n,N] or, in case of pressing <Enter>, returns defaultAnswer
    private boolean confirmationRequest(String question, boolean defaultAnswer) throws IOException {
        do {
            System.out.print(question + " (" + (defaultAnswer ? "Y/n" : "y/N") + ") > ");
            String answer = cin.readLine();
            switch (answer) {
                case "y":
                case "Y":
                case "yes":
                case "YES":
                case "Yes":
                    System.out.println();
                    return true;
                case "n":
                case "N":
                case "no":
                case "NO":
                case "No":
                    System.out.println();
                    return false;
                case "":
                    System.out.println();
                    return defaultAnswer;
            }
        } while (true);
    }

    // Paged menu by 10 entries per page, takes digits [1-9, 0] and [+, -, q, Q] for user answer
    // Returns index of chosen entry in "entries" array or -1 for previous menu/quit
    private int printPagedMenu(String title, List<String> entries, int page) throws IOException {
        if (title == null || entries == null || entries.isEmpty())
            return -1;

        int choice = -2; // -2 - incorrect input
        boolean hasNextPage = false,
                hasPrevPage = false;

        int pages = entries.size()/10;
        if (0 < (entries.size() % 10))
            pages++;

        if (pages <= page)
            return -1;

        System.out.println(title + ":");
        for (int i = 0; i < 10 && (10*page + i) < entries.size(); i++) {
            String entryDigit;
            if (i < 9)
                entryDigit = String.valueOf(i + 1);
            else
                entryDigit = "0";
            System.out.println(entryDigit + ": " + entries.get(10*page + i));
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
            input = cin.readLine().charAt(0);
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
        } while (choice < -1 || entries.size() <= choice);

        System.out.println();

        return choice;
    }
}
