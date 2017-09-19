package ru.vampa.disksaver.ui.console;

import ru.vampa.disksaver.Logger;
import ru.vampa.disksaver.creator.DiskProfileCreator;
import ru.vampa.disksaver.dbservice.DBException;
import ru.vampa.disksaver.dbservice.DBService;
import ru.vampa.disksaver.ui.UserInterface;

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
public class ConsoleUserInterface implements UserInterface {
    private final MenuUtil menuUtil;
    private final DBService dbService;
    private final Logger logger;
    // Default categories for profile and elements ('no category')
    private final long defaultElementCategory;
    private final long defaultProfileCategory;

    public ConsoleUserInterface(DBService dbService) {
        long defaultElementCategory = 0;
        long defaultProfileCategory = 0;

        this.logger = Logger.getInstance();
        this.dbService = dbService;

        logger.write("Initializing Console UI");
        logger.write("Checking default categories");
        try {
            defaultElementCategory = dbService.getElementCategoryId("no category");
            if (defaultElementCategory == 0) {
                logger.write("No default element category. Creating a new one");
                defaultElementCategory = dbService.addElementCategory("no category", "not specified");
                logger.write("Created element category ID #" + defaultElementCategory);
            }
        } catch (DBException e) {
            logger.write("Database exception while working with element categories : " + e.getMessage());
        }
        try {
            defaultProfileCategory = dbService.getProfileCategoryId("no category");
            if (defaultProfileCategory == 0) {
                logger.write("No default profile category. Creating a new one");
                defaultProfileCategory = dbService.addProfileCategory("no category", "not specified");
                logger.write("Created profile category ID #" + defaultProfileCategory);
            }
        } catch (DBException e) {
            logger.write("Database exception while working with profile categories : " + e.getMessage());
        }

        this.defaultElementCategory = defaultElementCategory;
        this.defaultProfileCategory = defaultProfileCategory;

        this.menuUtil = MenuUtil.getInstance();
    }

    // Entry point. Starting UI
    @Override
    public void initUI() {
        try (BufferedReader cin = new BufferedReader(new InputStreamReader(System.in))) {
            menuUtil.setIn(cin);
            showMainMenu();
        } catch (IOException e) {
            System.out.println("I/O exception occurred: " + e.getMessage());
        }
    }

    // Main menu. Switching between browser and creator
    private void showMainMenu() throws IOException {
        List<String> mainMenu = new ArrayList<>();
        mainMenu.add("Browse & edit saved collection");
        mainMenu.add("Add new disk profile to collection");
        mainMenu.add("Print database info");

        int choice;

        logger.write("Main menu");

        do {
            choice = menuUtil.printPagedMenu("Main menu", mainMenu);
            switch (choice) {
                case 0:
                    logger.write("Browser selected");
                    showBrowserMenu();
                    break;
                case 1:
                    logger.write("Creator selected");
                    showCreatorMenu();
                    break;
                case 2:
                    logger.write("DB info selected");
                    dbService.printConnectInfo();
                    System.out.println();
                    break;
                default:
                    System.out.println("Goodbye!");
            }
        } while (0 <= choice && choice < mainMenu.size());
    }

    // NIY: Browser menu used to browse and edit saved collection
    private void showBrowserMenu() throws IOException {
        logger.write("Browser menu, NIY");
        int choice = menuUtil.printPagedMenu("Collection browser", new ArrayList<>());
    }

    // Creator menu used to manage profile creation
    private void showCreatorMenu() throws IOException {
        logger.write("Creator menu");

        int choice;
        DiskProfileCreator creator;
        List<String> drives = DiskProfileCreator.getAvailableDrives();

        if (drives == null || drives.isEmpty()) {
            System.out.println("CD drives not found.\n");
            logger.write("No CD drives");
            return;
        }

        choice = menuUtil.printPagedMenu("Add new disk profile. Select CD drive", drives);
        if (choice < 0) {
            logger.write("No drive selected");
            return;
        }

        if (!menuUtil.confirmationRequest("Start scan?", true)) {
            logger.write("Scan canceled");
            return;
        }

        creator = new DiskProfileCreator(drives.get(choice));
        creator.startScan();

        long profileCategory = showCategoryMenu(true);
        String profileDescription = menuUtil.askString("Description for this disk profile");

        if (!creator.isScanComplete()) {
            System.out.println("Performing scan. Please wait.");
            logger.write("Waiting for scan thread");
            creator.waitForRawCreator();
        }
        creator.setProfileUserFields(profileDescription, profileCategory);
        System.out.println("Scan complete");

        if (menuUtil.confirmationRequest("Do you want to edit elements before saving?", true))
            showEditElementMenu(creator);

        if (menuUtil.confirmationRequest("Save to database?", true)) {
            System.out.println("Saving. Please wait.");
            creator.saveProfileToDB(dbService, defaultElementCategory);
            System.out.println("Disk saved.\n");
        }
    }

    // Category menu for profiles and elements (switching with parameter: true=profile, false=element)
    private long showCategoryMenu(boolean profile) throws IOException {
        int choice;
        long categoryId = -1;
        List<String> categoriesMenu = null;

        logger.write((profile ? "Profile" : "Element") + " category menu");

        try {
            categoriesMenu = profile ? dbService.getProfileCategories() : dbService.getElementCategories();
        } catch (DBException e) {
            logger.write("DB exception while getting " + (profile ? "Profile" : "Element") +
                    " categories : " + e.getMessage());
        }

        if (categoriesMenu == null)
            categoriesMenu = new ArrayList<>(1);
        categoriesMenu.add("Add new category");
        choice = menuUtil.printPagedMenu("Select category", categoriesMenu);

        if (choice == categoriesMenu.size() - 1)
            try {
                String categoryName = menuUtil.askString("Name of new category");
                String categoryDesc = menuUtil.askString("Description of " + categoryName);

                categoryId = profile ?
                        dbService.addProfileCategory(categoryName, categoryDesc) :
                        dbService.addElementCategory(categoryName, categoryDesc);
            } catch (DBException e) {
                logger.write("DB exception while getting " + (profile ? "Profile" : "Element") +
                        " categories : " + e.getMessage());
            }
        else if (choice < 0)
            categoryId = profile ? defaultProfileCategory : defaultElementCategory;
        else
            try {
                categoryId = profile ?
                        dbService.getProfileCategoryId(categoriesMenu.get(choice)) :
                        dbService.getElementCategoryId(categoriesMenu.get(choice));
            } catch (DBException e) {
                logger.write("DB exception while getting " + (profile ? "Profile" : "Element") +
                        " categories : " + e.getMessage());
            }

        return categoryId;
    }

    // Menu for editing element list before it is sent to database
    private void showEditElementMenu(DiskProfileCreator creator) throws IOException {
        int choice;
        List<String> editElementMenu = new ArrayList<>();

        logger.write("Edit elements menu");

        editElementMenu.add("Add/edit description");
        editElementMenu.add("Select category");
        editElementMenu.add("Toggle saving element");

        List<String> elementPathList = creator.getElementsPathList();
        do {
            choice = menuUtil.printPagedMenu("Edit elements. Select \'back\' to save", elementPathList);

            if (0 <= choice) {
                if (creator.isElementADirectory(choice))
                    editElementMenu.add("Do not save all included elements");

                int editChoice = menuUtil.printPagedMenu("Edit element " + elementPathList.get(choice), editElementMenu);
                switch (editChoice) {
                    case 0:
                        creator.setElementDescription(choice, menuUtil.askString("Description"));
                        break;
                    case 1:
                        creator.setElementCategory(choice, showCategoryMenu(false));
                        break;
                    case 2:
                        creator.toggleSavingElement(
                                choice, menuUtil.confirmationRequest("Do you want to save this element?", true));
                        break;
                    case 3:
                        creator.toggleSavingIncludedElements(
                                choice, menuUtil.confirmationRequest("Do you want to save included elements?", true));
                }

                if (creator.isElementADirectory(choice))
                    editElementMenu.remove(editElementMenu.size() - 1);
            }
        } while (choice != -1);
    }
}
