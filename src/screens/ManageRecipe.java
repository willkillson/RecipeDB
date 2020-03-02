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

/**
 * Displays to the user and completes the actions associated with recipes.
 */
public class ManageRecipe {
    public static ArrayList<String> menuOptions;

    static {
        menuOptions = new ArrayList<>();
        menuOptions.add("Go back");
        menuOptions.add("Show All Recipes");
        menuOptions.add("Show Stored Recipes");
        menuOptions.add("Rate Recipe");
        menuOptions.add("Add Recipe To Cart");
        menuOptions.add("Delete Recipe");
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
                    case (2):
                    {
                        //TODO
                        showStoredRecipes();
                        break;
                    }
                    case (3):
                        rateRecipe(scanner, server, user);
                    case (4):
                    {
                        //TODO
                        addRecipeCart();
                        break;
                    }
                    case (5):
                    {

                        //TODO
                        deleteRecipeCart();
                        break;
                    }
                    default:
                        System.out.println("ERROR: That selection has not been implemented.");
                }
            }
        } while (!selected.isBack()); // exit
    }

    public static void showAllRecipes(Scanner scanner, ServerDB server, User user) {
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

    public static void rateRecipe(Scanner scanner, ServerDB server, User user) {
        SelectAction<Recipe> action;
        do {
            //Get records
            Result<ArrayList<Recipe>> recipesR =
                RecipeQueries.getRecipes(server, 0, 100);

            if (recipesR.isSuccess()) { // got records
                ArrayList<Recipe> recipes = recipesR.value();
                int i =0;
                for (Recipe recipe : recipes) {
                    System.out.println(i);
                    i++;
                    showRecipe(recipe);
                }
                Scanner in = new Scanner(System.in);
                System.out.println("Which recipe would you like to update the rating for?: ");
                int recipe = in.nextInt();
                System.out.println("What would you like to update the rating to?: ");
                int rating = in.nextInt();
                Recipe update = recipes.get(recipe);
                RecipeQueries.updateRecipe(server, update.getRecipeId(), recipes, rating);
                return;
            } else { // system failure
                System.out.println(recipesR.error());
                return;
            }
        } while (!action.isBack());
    }

    public static void showStoredRecipes(){
        /*

            Lists all the stored recipes that the user has added.

         */
        System.out.println("TODO Show Stored Recipes");
        //TODO
    }

    public static void addRecipeCart(){
        /*

            Adds a recipe by ID from a list of recipes.

            We should list all the recipes, before asking which recipe they would like to add.

         */
        System.out.println("TODO Add Recipe To Cart");
        //TODO
    }

    public static void deleteRecipeCart(){
        /*

            Removes a recipe from a user.

         */
        System.out.println("TODO Delete Recipe");
        //TODO
    }

    /**public static void addRecipe(Scanner scanner, ServerDB server, User user) {
     SelectAction<Recipe> action;
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
     } while (!action.isBack());
     }**/
}
