package screens;

import db.Queries;
import db.ServerDB;
import entities.Cupboard;
import entities.User;
import java.util.ArrayList;
import java.util.Scanner;
import util.Helpers;
import util.Result;
import util.ui.SelectAction;
import util.ui.SimpleSelect;

/**
 * Displays to the user and completes the actions associated with the cupboard.
 */
public class ManageCupboard {

    public static ArrayList<String> menuOptions;

    static {
        menuOptions = new ArrayList<>();
        menuOptions.add("Go back");
        menuOptions.add("Show Cupboard");
    }

    public static void view(Scanner scanner, ServerDB server, User user) {
        SelectAction<String> selected = null;
        do {
            // display menu
            selected = SimpleSelect.show(scanner, menuOptions, 0);
            if (selected.isSelected()) { // valid selection
                // get selected index
                String selectionText = selected.getSelected();
                int index = menuOptions.indexOf(selectionText);
                // process options
                switch (index) {
                    case (1):
                        showCupboard(server, user);
                        break;
                    default:
                        System.out.println("ERROR: That selection has not been implemented.");
                }
            }
        } while (!selected.isBack());
    }

    public static void showCupboard(ServerDB server, User user) {
        Result<Cupboard> maybeCupboard = Queries.getCupboard(server, user);
        if (maybeCupboard.isSuccess()) {
            Cupboard cupboard = maybeCupboard.value();
            if (cupboard.size() == 0) {
                System.out.println("The cupboard is empty.");
            } else {
                Helpers.printIngredientList(cupboard.getIngredients());
            }
        } else {
            System.out.println(maybeCupboard.error());
        }
    }
}
