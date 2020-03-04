package screens;

import static db.queries.CartQueries.getCart;


import db.ServerDB;
import db.queries.ContainsQueries;
import db.queries.ListsQueries;
import db.queries.RecipeQueries;
import entities.Adds;
import entities.Cart;
import entities.Ingredient;
import entities.Lists;
import entities.Recipe;
import entities.User;
import java.util.ArrayList;
import java.util.Scanner;
import util.Helpers;
import util.Result;
import util.ui.BackSelect;
import util.ui.PaginatedSelect;
import util.ui.SelectAction;

/**
 * Displays to the user and completes the actions associated with the cart.
 */
public class ManageCart {

    public static ArrayList<String> menuOptions;

    static {
        menuOptions = new ArrayList<>();
        menuOptions.add("Show Cart");
        menuOptions.add("Show Current Recipes");
        menuOptions.add("Rate Recipe");
        menuOptions.add("Add Recipe To Cart");
    }


    /**
     * Method to view the menu for ManageCart
     * @param scanner
     * @param server
     * @param user
     */
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
                    case (0)://showCart
                    {
                        showCart(server, user);
                        break;
                    }
                    case (1)://Show current recipes
                    {
                        showStoredRecipes(server, user);
                        break;
                    }
                    case (2): {
                        rateRecipe(server, user);
                        break;
                    }
                    case (3)://addRecipeCart
                    {
                        addRecipeCart(scanner, server, user);
                        break;
                    }
                    default:
                        System.out.println("ERROR: That selection has not been implemented.");
                }
            }
        } while (!selected.isBack());
    }


    /**
     * method to show the current users cart
     * @param server
     * @param user
     */
    public static void showCart(ServerDB server, User user) {

        //builds our cart
        buildShoppingCart(server, user);


        Result<Cart> maybeCart = getCart(server, user);
        if (maybeCart.isSuccess()) {
            Cart cart = maybeCart.value();
            if (cart.size() == 0) {
                System.out.println("The cart is empty.");
            } else {

                Helpers.printCollection((ArrayList<Ingredient>) cart.getIngredients());
            }
        } else {
            System.out.println(maybeCart.error());
        }
    }


    /**
     * method to show the stored recipe for a user
     * @param server
     * @param user
     */
    public static void showStoredRecipes(ServerDB server, User user) {
        /*

            Lists all the stored recipes that the user has added.

         */


        Result<ArrayList<Adds>> adds = RecipeQueries.getAddsRecipe(server, user);
        if (adds.isSuccess()) {
            Helpers.printCollection(adds.value());
        } else System.out.println(adds.error());
    }


    /**
     * Method to rate a recipe. Calls a query to execute SQL update.
     * @param server
     * @param user
     */
    public static void rateRecipe(ServerDB server, User user) {
        Scanner scanner = new Scanner(System.in);

        final int increment = 5;
        int start = 0;
        SelectAction<Recipe> action;
        do {
            //Get records
            Result<ArrayList<Recipe>> maybeRecipes = RecipeQueries.getRecipesRange(server, start, increment);

            if (maybeRecipes.isSuccess()) { // got records
                ArrayList<Recipe> recipes = maybeRecipes.value();

                boolean hasPrevious = start > 0;
                boolean hasNext = recipes.size() == increment;

                action = PaginatedSelect.show(scanner, recipes, hasPrevious, hasNext);

                if (action.isNext()) {
                    start += increment;
                } else if (action.isPrevious()) {
                    start = Math.max(0, start - increment);
                } else if (action.isSelected()) {

                    System.out.println("Which recipe would you like to update the rating for?: ");
                    String rating = scanner.nextLine();
                    System.out.println("What would you like to update the rating to?: ");
                    Result updated = RecipeQueries.updateRecipe(server, user, action.getSelected(), Float.parseFloat(rating));
                    if (updated.isFailure()) {
                        System.out.println(updated.error());
                    }

                } else { /* isback() handled as exit condition */ }

            } else { // system failure
                System.out.println(maybeRecipes.error());
                return;
            }
        } while (!action.isBack()); // back button exits the screen
    }

    /**
     * Method to add a recipe to the cart
     * @param scanner
     * @param server
     * @param user
     */
    public static void addRecipeCart(Scanner scanner, ServerDB server, User user) {
        /*

            Adds a recipe by ID from a list of recipes.

            We should list all the recipes, before asking which recipe they would like to add.

            -show recipes
            -add recipe

         */

        final int increment = 5;
        int start = 0;
        SelectAction<Recipe> action;
        do {
            //Get records
            Result<ArrayList<Recipe>> recipesR = RecipeQueries.getRecipesRange(server, start, increment);

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

                    Result add = RecipeQueries.addRecipeCart(server, user, action.getSelected());
                    if (add.isSuccess()) {
                        System.out.println("Recipe: " + action.getSelected().getRecipeId() +
                            " added to " + user.getUserId() + "'s recipe cart");
                    } else {
                        System.out.println(add.error());
                    }

                } else { /* isback() handled as exit condition */ }

            } else { // system failure
                System.out.println(recipesR.error());
                return;
            }
        } while (!action.isBack()); // back button exits the screen


    }

    /**
     * Builds a new shopping cart for a user
     *
     * @param server databse
     * @param user   to rebuild
     */
    public static void buildShoppingCart(ServerDB server, User user) {
        //This function should populate the CONTAINS relationship, see Deliverable 2

        //1. remove everything that is in our current contains relationship
        Result r = ContainsQueries.clearContainsRelations(server, user);
        if (r.isFailure()) System.out.println(r);

        //2. update our contains relationship   ADDS -> LISTS -> INGREDIENTS   map this to CONTAINS
        Result<ArrayList<Adds>> addsR = RecipeQueries.getAddsRecipe(server, user);
        if (addsR.isSuccess()) {
            // build a recipe list
            ArrayList<String> recipeIDs = new ArrayList<>();
            for (Adds add : addsR.value()) {
                recipeIDs.add(add.recipeID);
            }

            //fetch the lists relations with selected recipeID
            Result<ArrayList<Lists>> lists = ListsQueries.getLists(server, recipeIDs);

            // add contains relationships or print error
            if (lists.isSuccess()) {
                ContainsQueries.insertContainsRelations(server, user, lists.value());
            } else {
                System.out.println(lists.error());
            }
        } else System.out.println(addsR.isFailure());
    }
}
