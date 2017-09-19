package ru.vampa.disksaver.ui.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class MenuUtil {
    private static final MenuUtil instance = new MenuUtil();

    // Reader for user answers
    private BufferedReader in;

    private MenuUtil() {
    }

    public static MenuUtil getInstance() {
        return instance;
    }

    // Simply asks a question (at parameter) and returns answer
    String askString(String requestText) throws IOException {
        System.out.print(requestText + " > ");
        String answer = in.readLine();
        return answer != null ? answer : "";
    }

    // Prints question and accepts [y,n] or, in case of pressing <Enter>, returns defaultAnswer
    boolean confirmationRequest(String question, boolean defaultAnswer) throws IOException {
        do {
            System.out.print(question + " (" + (defaultAnswer ? "Y/n" : "y/N") + ") > ");
            String answer = in.readLine();
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

    // See main method below. This method used to not specify start page (which always 0)
    int printPagedMenu(String title, List<String> entries) throws IOException {
        return printPagedMenu(title, entries, 0);
    }

    /*
     Paged menu by 10 entries per page, takes digits [1-9, 0] and [+, -, q, Q] for user answer
     Returns index of chosen entry in "entries" array or -1 for previous menu/quit
     */
    private int printPagedMenu(String title, List<String> entries, int page) throws IOException {
        if (title == null || entries == null || entries.isEmpty())
            return -1;

        int choice = -2; // -2 - incorrect input
        boolean hasNextPage = false,
                hasPrevPage = false;

        int pages = entries.size() / 10;
        if (0 < (entries.size() % 10))
            pages++;

        if (pages <= page)
            return -1;

        System.out.println(title + ":");
        for (int i = 0; i < 10 && (10 * page + i) < entries.size(); i++) {
            String entryDigit;
            if (i < 9)
                entryDigit = String.valueOf(i + 1);
            else
                entryDigit = "0";
            System.out.println(entryDigit + ": " + entries.get(10 * page + i));
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
            String input;
            input = in.readLine();
            switch (input) {
                case "1":
                case "2":
                case "3":
                case "4":
                case "5":
                case "6":
                case "7":
                case "8":
                case "9":
                    choice = 10 * page + input.charAt(0) - 49;
                    break;
                case "0":
                    choice = 10 * (page + 1);
                    break;
                case "+":
                    if (hasNextPage)
                        choice = printPagedMenu(title, entries, page + 1);
                    break;
                case "-":
                    if (hasPrevPage)
                        choice = printPagedMenu(title, entries, page - 1);
                    break;
                case "q":
                case "Q":
                    choice = -1;
            }
        } while (choice < -1 || entries.size() <= choice);

        System.out.println();

        return choice;
    }

    public void setIn(BufferedReader in) {
        this.in = in;
    }
}