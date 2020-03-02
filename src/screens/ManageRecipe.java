package screens;

import db.ServerDB;
import db.queries.RecipeQueries;
import entities.Recipe;
import entities.User;
import java.util.ArrayList;
import java.util.Scanner;

import util.Helpers;
import util.Result;
import util.ui.PaginatedSelect;
import util.ui.SelectAction;
import util.ui.SimpleSelect;

/**
 * Displays to the user and completes the actions associated with recipes.
 */
public class ManageRecipe {
    public static ArrayList<String> menuOptions;

    static {
        menuOptions = new ArrayList<>();
        menuOptions.add("Go back");
        menuOptions.add("Show All Recipes");
        menuOptions.add("Delete Recipe");
        menuOptions.add("Add Recipe");
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
                        showAllRecipes(scanner, server);
                        break;
                    case (2):
                    {
                        //TODO
                        deleteRecipe(scanner, server);
                        break;
                    }
                    case (3):
                    {
                        //TODO
                        addRecipe(scanner,server);
                        break;
                    }
                    default:
                        System.out.println("ERROR: That selection has not been implemented.");
                }
            }
        } while (!selected.isBack()); // exit
    }

    public static void showAllRecipes(Scanner scanner, ServerDB server) {
        /*

            //lists the global recipes, not user dependent

        */

        final int increment = 5;
        int start = 0;
        SelectAction<Recipe> action;
        do {
            //Get records
            Result<ArrayList<Recipe>> recipesR =
                RecipeQueries.getRecipes(server, start, increment);

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
                    Helpers.showRecipe(action.getSelected());
                } else { /* isback() handled as exit condition */ }

            } else { // system failure
                System.out.println(recipesR.error());
                return;
            }
        } while (!action.isBack()); // back button exits the screen
    }

    public static void deleteRecipe(Scanner scanner, ServerDB server){
        /*

            //TODO modifies the global recipes, not user dependent

         */
        System.out.println("TODO Delete Recipe");
        //TODO
    }

    public static void addRecipe(Scanner scanner, ServerDB server) {
        /*

            //TODO modifies the global recipes, not user dependent

         */
        System.out.println("TODO addRecipe");

/*        SelectAction<Recipe> action;
        do {
            //Get records
            Result<ArrayList<Recipe>> recipesR =
                    RecipeQueries.getRecipes(server, 0, 100);

            if (recipesR.isSuccess()) { // got records
                ArrayList<Recipe> recipes = recipesR.value();
                Scanner in = new Scanner(System.in);
                System.out.println("Which a new recipeID for the recipe: ");
                String ID = in.nextLine();
                RecipeQueries.addRecipe(server, ID, recipes);
                return;
            } else { // system failure
                System.out.println(recipesR.error());
                return;
            }
        } while (!action.isBack());*/
    }
}
