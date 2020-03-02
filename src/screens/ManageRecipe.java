package screens;

import db.ServerDB;
import db.queries.RecipeQueries;
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
        menuOptions.add("Add Recipe To Global Recipes");
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
                    case (1)://Show All Recipes
                        showAllRecipes(scanner, server, user);
                        break;
                    case (2)://TODO Show Stored Recipes
                    {

                        showStoredRecipes();
                        break;
                    }
                    case (3)://Rate Recipe
                        rateRecipe(scanner, server, user);
                    case (4)://TODO Add Recipe To Cart
                    {

                        addRecipeCart(scanner,server,user);
                        break;
                    }
                    case (5):
                    {
                        addRecipeGlobal(scanner,server);
                        break;
                    }
                    case (6)://TODO Delete Recipe
                    {
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
            increment = increment*2;
        } while (!action.isBack()); // back button exits the screen
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
                    Helpers.showRecipe(recipe);
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

    public static void addRecipeCart(Scanner scanner, ServerDB server, User user){
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

    public static void addRecipeGlobal(Scanner scanner,ServerDB server){


            System.out.println("Enter a RecipeID for the recipe" );
            String recipeID = scanner.nextLine();
            System.out.println("Enter a name for the recipe" );
            String name = scanner.nextLine();
            System.out.println("Enter a URL for the recipe" );
            String URL = scanner.nextLine();
            Recipe recipe = new Recipe(recipeID,name,null,URL,0,null);

            RecipeQueries.addRecipe(server,recipe);
            return;
    }

}
