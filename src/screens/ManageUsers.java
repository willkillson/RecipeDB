package screens;

import static db.queries.UserQueries.verifyUser;


import db.ServerDB;
import db.queries.UserQueries;
import entities.User;
import java.util.ArrayList;
import java.util.Scanner;
import util.Result;
import util.ui.BackSelect;
import util.ui.PaginatedSelect;
import util.ui.SelectAction;

public class ManageUsers {
    public static ArrayList<String> menuOptions;

    static {
        menuOptions = new ArrayList<>();
        menuOptions.add("Select User");
    }

    public static User view(Scanner scanner, ServerDB server) {
        SelectAction<String> selected = null;
        do {
            // display menu
            selected = BackSelect.show(scanner, menuOptions);
            if (selected.isSelected()) { // valid selection
                // get selected index
                String selectionText = selected.getSelected();
                int index = menuOptions.indexOf(selectionText);
                // process options
                switch (index) {
                    case (0)://todo
                    {
                        return selectUser(server);

                    }
                }
            }
        } while (!selected.isBack()); // exit
        return null;
    }

    public static User getDefaultUser(ServerDB server) {

        User user = null;
        do {
            // Get credentials

            /*   // add/remove slash to /* to toggle comment block
            System.out.print("UserName:");
            String name = scanner.nextLine();
            System.out.print("Password:");
            String password = scanner.nextLine();
            /*/
            String name = "38CA10BA";//todo remove this for auth
            String password = "03D64A6A";//automatic login
            //*/

            //TODO      We will be adding a select user feature here, that will allow us to
            //TODO      select a user, and so everything after this will modify the user we
            //TODO      choose.

            //TODO      There will not be a login feature.

            // Auth user
            Result<User> maybeUser = verifyUser(server, name, password);

            if (maybeUser.isFailure()) {
                System.out.println(maybeUser.error());
            } else {
                user = maybeUser.value();
            }
        } while (user == null); // exists on existing user
        return user;
    }

    public static void showAllUsers(ServerDB server) {
        System.out.println("TODO showAllUsers");
    }

    public static User selectUser(ServerDB server) {


        Scanner scanner = new Scanner(System.in);
        int increment = 5;
        int start = 0;
        SelectAction<User> action = SelectAction.Back();

        do {
            //Get records
            Result<ArrayList<User>> usersR = UserQueries.getUsers(server);
            if (usersR.isSuccess()) {
                ArrayList<User> users = usersR.value();
                boolean hasPrevious = start > 0;
                boolean hasNext = users.size() == increment;

                // get customer request
                action = PaginatedSelect.show(scanner, users, hasPrevious, hasNext);

                // handle the request
                if (action.isNext()) {
                    start += increment;
                } else if (action.isPrevious()) {
                    start = Math.max(0, start - increment);
                } else if (action.isSelected()) {
                    System.out.println("Selected user: " + action.getSelected().getUserId());
                    return action.getSelected();

                } else { /* isback() handled as exit condition */ }
            } else {
                System.out.println(usersR.error());
            }

        } while (!action.isBack()); // back button exits the screen
        return null;
    }


}
