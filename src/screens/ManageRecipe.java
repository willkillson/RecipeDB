package screens;

import db.ServerDB;
import db.queries.IngredientQueries;
import db.queries.RecipeQueries;
import entities.Ingredient;
import entities.Lists;
import entities.Recipe;
import entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import util.Helpers;
import util.Result;
import util.ui.BackSelect;
import util.ui.PaginatedSelect;
import util.ui.SelectAction;
import util.ui.SimpleSelect;

/**
 * Displays to the user and completes the actions associated with recipes.
 * Note that these are not user-specific.
 */
public class ManageRecipe {
    public static ArrayList<String> menuOptions;

    static {
        menuOptions = new ArrayList<>();
        menuOptions.add("Show All Recipes");
        menuOptions.add("Add Recipe To Global Recipes");
        menuOptions.add("Delete Recipe");
    }

    public static void view(Scanner scanner, ServerDB server, User user) {
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
                    case (0)://Show All Recipes
                        showAllRecipes(server);
                        break;

                    case (1):
                    {
                        addRecipeGlobal(server);
                        break;
                    }
                    case (2)://TODO Delete Recipe
                    {
                        deleteRecipe(server);
                        break;
                    }
                    default:
                        System.out.println("ERROR: That selection has not been implemented.");
                }
            }
        } while (!selected.isBack()); // exit
    }

    public static void showAllRecipes(ServerDB server) {
        Scanner scanner = new Scanner(System.in);
        int increment = 5;
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
                    start = Math.max(0, start - increment);
                } else if (action.isSelected()) {
                    Helpers.showRecipe(action.getSelected());
                } else { /* isback() handled as exit condition */ }

            } else { // system failure
                System.out.println(recipesR.error());
                return;
            }
        } while (!action.isBack()); // back button exits the screen
    }

    public static void deleteRecipe(ServerDB server){
        Scanner scanner = new Scanner(System.in);
        int increment = 5;
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
                    start = Math.max(0, start - increment);
                } else if (action.isSelected()) {

                    RecipeQueries.deleteRecipe(server, action.getSelected());
                    System.out.println("Deleted "+action.getSelected().getName());

                } else { /* isback() handled as exit condition */ }

            } else { // system failure
                System.out.println(recipesR.error());
                return;
            }
        } while (!action.isBack()); // back button exits the screen

    }

    public static void addRecipeGlobal(ServerDB server){

        if(Helpers.displayContinue("Do you have all the ingredients in the system already?")){
            Scanner scanner = new Scanner(System.in);

            System.out.println("Enter a RecipeID for the recipe" );
            String recipeID = scanner.nextLine();
            System.out.println("Enter a name for the recipe" );
            String name = scanner.nextLine();
            System.out.println("Enter a URL for the recipe" );
            String URL = scanner.nextLine();
            Recipe recipe = new Recipe(recipeID,name,null,URL,0,null);

            ArrayList<Lists> lists = new ArrayList<>();
            System.out.println("Select ingredients that belong to the recipe");

            final int increment = 5;
            int start = 0;

            SelectAction<Ingredient> action;
            do {
                //Get records
                Result<ArrayList<Ingredient>> maybeIngredients = IngredientQueries.getIngredients(server);

                if (maybeIngredients.isSuccess()) { // got records
                    ArrayList<Ingredient> ingredients = maybeIngredients.value();

                    boolean hasPrevious = start > 0;
                    boolean hasNext = ingredients.size() == increment;

                    action = PaginatedSelect.show(scanner, ingredients, hasPrevious, hasNext);

                    // handle the request
                    if (action.isNext()) {
                        start += increment;
                    } else if (action.isPrevious()) {
                        start = Math.max(0, start - increment);
                    } else if (action.isSelected()) {

                        boolean isRequired = Helpers.displayContinue("Is this ingredient required?");

                        Lists newLists = new Lists(recipeID,action.getSelected().getIngredientId(),isRequired);

                        lists.add(newLists);
                    } else { /* isback() handled as exit condition */ }

                } else { // system failure
                    System.out.println(maybeIngredients.error());
                    return;
                }
            } while (!action.isBack()); // back button exits the screen

            RecipeQueries.addRecipe(server,recipe,lists);

        }

        return;
    }
}
