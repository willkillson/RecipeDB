package screens;

import db.ServerDB;
import db.queries.RecipeQueries;
import entities.Recipe;
import entities.User;
import java.util.ArrayList;
import java.util.Scanner;
import util.Result;
import util.ui.PaginatedSelect;
import util.ui.SelectAction;
import util.ui.SimpleSelect;

public class ManageRecipe {
    public static ArrayList<String> menuOptions;

    static {
        menuOptions = new ArrayList<>();
        menuOptions.add("Logout");
        menuOptions.add("Show All Recipes");
        menuOptions.add("Show Stored Recipes(WIP)");
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
                        showAllRecipes(scanner, server, user);
                        break;
                    default:
                        System.out.println("ERROR: That selection has not been implemented.");
                }
            }
        } while (!selected.isBack()); // exit
    }

    public static void showAllRecipes(Scanner scanner, ServerDB server, User user) {
        final int increment = 2;
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
                    showRecipe(action.getSelected());
                } else { /* isback() handled as exit condition */ }

            } else { // system failure
                System.out.println(recipesR.error());
                return;
            }
        } while (!action.isBack()); // back button exits the screen
    }

    /**
     * prints individual recipe info. This should be expanded to include an option for adding the recipe
     * to the user list
     *
     * @param recipe
     */
    public static void showRecipe(Recipe recipe) {
        System.out.println("Recipe: " + recipe.getName());
        System.out.println("\tUrl: " + recipe.getUrl());
        System.out.println("\tRating: " + recipe.getRating().get());
    }
}
