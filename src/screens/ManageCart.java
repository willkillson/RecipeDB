package screens;

import static db.queries.CartQueries.getCart;


import db.ServerDB;
import db.queries.CartQueries;
import db.queries.ContainsQueries;
import db.queries.ListsQueries;
import db.queries.RecipeQueries;
import entities.*;

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
                    case (2):
                    {
                        rateRecipe( server, user);
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

    public static void showCart(ServerDB server, User user) {

        //builds our cart
        buildShoppingCart(server,user);


        Result<Cart> maybeCart = getCart(server, user);
        if (maybeCart.isSuccess()) {
            Cart cart = maybeCart.value();
            if (cart.size() == 0) {
                System.out.println("The cart is empty.");
            } else {
                Helpers.printIngredientList(cart.getIngredients());
            }
        } else {
            System.out.println(maybeCart.error());
        }
    }

    public static void showStoredRecipes(ServerDB server, User user){
        /*

            Lists all the stored recipes that the user has added.

         */


        ArrayList<Adds> adds = RecipeQueries.getAddsRecipe(server,user);
        Helpers.printAdds(adds);


    }

    public static void rateRecipe(ServerDB server, User user) {
        Scanner scanner = new Scanner(System.in);

        final int increment = 5;
        int start = 0;
        SelectAction<Recipe> action;
        do {
            //Get records
            Result<ArrayList<Recipe>> maybeRecipes = RecipeQueries.getRecipes(server, start, increment);

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
                    RecipeQueries.updateRecipe(server,user, action.getSelected(), Float.parseFloat(rating));

                } else { /* isback() handled as exit condition */ }

            } else { // system failure
                System.out.println(maybeRecipes.error());
                return;
            }
        } while (!action.isBack()); // back button exits the screen
    }

    public static void addRecipeCart(Scanner scanner, ServerDB server, User user){
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
            Result<ArrayList<Recipe>> recipesR = RecipeQueries.getRecipes(server, start, increment);

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

                    RecipeQueries.addRecipeCart(server,user,action.getSelected());
                    System.out.println("Adding recipe: "+action.getSelected().getName());

                } else { /* isback() handled as exit condition */ }

            } else { // system failure
                System.out.println(recipesR.error());
                return;
            }
        } while (!action.isBack()); // back button exits the screen


    }

    public static void buildShoppingCart(ServerDB server, User user){
        //This function should populate the CONTAINS relationship, see Deliverable 2

        //1. remove everything that is in our current contains relationship
        CartQueries.clearContainsRelation(server,user);

        //2. update our contains relationship   ADDS -> LISTS -> INGREDIENTS   map this to CONTAINS
        ArrayList<Adds> adds = RecipeQueries.getAddsRecipe(server, user);

        ArrayList<String> recipeIDs = new ArrayList<>();
        for(int i = 0;i< adds.size();i++){

            recipeIDs.add(adds.get(i).recipeID);
        }

        //now we have our lists
        ArrayList<ArrayList<Lists>> lists = ListsQueries.getLists(server,recipeIDs);

        ContainsQueries.updateContains(server,user,lists);


    }

}
