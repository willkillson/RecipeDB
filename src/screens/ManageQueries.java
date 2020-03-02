package screens;

import db.ServerDB;
import db.queries.RecipeQueries;
import entities.Recipe;
import entities.User;
import util.Result;
import util.ui.PaginatedSelect;
import util.ui.SelectAction;
import util.ui.SimpleSelect;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Scanner;

public class ManageQueries {
        public static ArrayList<String> menuOptions;

        static {
            menuOptions = new ArrayList<>();
            menuOptions.add("Go back");
            menuOptions.add("Rating Query");
            menuOptions.add("Times Cooked Query");
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
                            searchRating(scanner, server, user);
                            break;
                        case (2):
                            searchTimesCooked(scanner, server, user);
                            break;
                        default:
                            System.out.println("ERROR: That selection has not been implemented.");
                    }
                }
            } while (!selected.isBack()); // exit
        }

        public static void searchRating(Scanner scanner, ServerDB server, User user) {
            final int increment = 10;
            int start = 0;
            SelectAction<Recipe> action;
            do {
                //Get records
                Scanner in = new Scanner(System.in);
                System.out.println("Enter a a minimum rating to search for recipes, 1-5: ");
                double rating = in.nextDouble();
                Result<ArrayList<Recipe>> recipesR =
                        RecipeQueries.getRecipesRating(server, start, increment, rating);

                if (recipesR.isSuccess()) { // got records
                    ArrayList<Recipe> recipes = recipesR.value();
                    // figure out if we're on either the upper or lower bound and
                    // enable options accordingly

                    boolean hasPrevious = start > 0;
                    boolean hasNext = recipes.size() == increment;

                    // get customer request
                    action = PaginatedSelect.show(scanner, recipes, hasPrevious, hasNext);

                    // handle the request
                    if (action.isNext()) {
                        start += increment;
                    } else if (action.isPrevious()) {
                        start = Math.max(0, start - 10);
                    } else if (action.isSelected()) {
                        ManageRecipe.showRecipe(action.getSelected());
                    } else { /* isback() handled as exit condition */ }

                } else { // system failure
                    System.out.println(recipesR.error());
                    return;
                }
            } while (!action.isBack()); // back button exits the screen
        }

    public static void searchTimesCooked(Scanner scanner, ServerDB server, User user) {
        final int increment = 10;
        int start = 0;
        SelectAction<Recipe> action;
        do {
            //Get records
            Scanner in = new Scanner(System.in);
            System.out.println("Enter the minimum number for the amount of times a recipe was cooked: ");
            double rating = in.nextDouble();
            Result<ArrayList<Recipe>> recipesR =
                    RecipeQueries.getRecipesCooked(server, start, increment, rating);

            if (recipesR.isSuccess()) { // got records
                ArrayList<Recipe> recipes = recipesR.value();
                // figure out if we're on either the upper or lower bound and
                // enable options accordingly

                boolean hasPrevious = start > 0;
                boolean hasNext = recipes.size() == increment;

                // get customer request
                action = PaginatedSelect.show(scanner, recipes, hasPrevious, hasNext);

                // handle the request
                if (action.isNext()) {
                    start += increment;
                } else if (action.isPrevious()) {
                    start = Math.max(0, start - 10);
                } else if (action.isSelected()) {
                    ManageRecipe.showRecipe(action.getSelected());
                } else { /* isback() handled as exit condition */ }

            } else { // system failure
                System.out.println(recipesR.error());
                return;
            }
        } while (!action.isBack()); // back button exits the screen
    }
}
