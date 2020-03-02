package screens;

import static db.queries.CartQueries.getCart;


import db.ServerDB;
import db.queries.RecipeQueries;
import entities.Cart;
import entities.Recipe;
import entities.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;
import util.Helpers;
import util.Result;
import util.ui.PaginatedSelect;
import util.ui.SelectAction;
import util.ui.SimpleSelect;

/**
 * Displays to the user and completes the actions associated with the cart.
 */
public class ManageCart {

    public static ArrayList<String> menuOptions;

    static {
        menuOptions = new ArrayList<>();
        menuOptions.add("Go back");
        menuOptions.add("Show Cart");
        menuOptions.add("Show Current Recipes");
        menuOptions.add("Rate Recipe");
        menuOptions.add("Add Recipe To Cart");
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
                    case (1)://showCart
                    {
                        showCart(server, user);
                        break;
                    }
                    case (2)://showStoredRecipes
                    {
                        showStoredRecipes(server, user);
                        break;
                    }
                    case (3)://rateRecipe
                    {
                        rateRecipe(scanner, server, user);
                        break;
                    }
                    case (4)://addRecipeCart
                    {
                        //TODO
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
        Result<ArrayList<Recipe>> maybeRecipes = RecipeQueries.getAddsRecipes(server,user);
        if(maybeRecipes.isSuccess()){
            ArrayList<Recipe> recipes = maybeRecipes.value();
            Helpers.printRecipes(recipes);
        }
        else{
            System.out.println(maybeRecipes.error());
        }
    }

    public static void rateRecipe(Scanner scanner, ServerDB server, User user) {
        /*

            This function looks at all the users recipes in the ADDS relation (see documention deliverable 2)
            and displays those recipes for the user to rate on.

         */

        //get our recipes from ADDS relation
        Result<ArrayList<Recipe>> maybeRecipes = RecipeQueries.getAddsRecipes(server,user);

        if(maybeRecipes.isSuccess()){
            ArrayList<Recipe> recipes = maybeRecipes.value();
            if(recipes.size()>0){
                System.out.print("Update Rating  --  ");
                SelectAction<Recipe> selected = SimpleSelect.show(scanner,recipes,-1);
                Recipe rec = selected.getSelected();

                System.out.println("What would you like to update the rating to?: ");
                Integer rating = scanner.nextInt();
                RecipeQueries.updateRecipe(server, rec.getRecipeId(), recipes, rating);
            }
            else{
                System.out.println("No recipes are in your cart.");
            }
        }
        else{
            System.out.println(maybeRecipes.error());
        }

/*        SelectAction<Recipe> action;
        do {
            //Get records
            Result<ArrayList<Recipe>> recipesR =
                    RecipeQueries.getRecipes(server, 0, 100);

            if (recipesR.isSuccess()) { // got records
                ArrayList<Recipe> recipes = recipesR.value();

                showStoredRecipes(server,user);
                int i =0;
                for (Recipe recipe : recipes) {
                    System.out.println(i);
                    i++;
                    Helpers.showRecipe(recipe);
                }

                System.out.println("Which recipe would you like to update the rating for?: ");
                int recipe = scanner.nextInt();
                System.out.println("What would you like to update the rating to?: ");
                int rating = scanner.nextInt();
                Recipe update = recipes.get(recipe);
                RecipeQueries.updateRecipe(server, update.getRecipeId(), recipes, rating);
                return;
            } else { // system failure
                System.out.println(recipesR.error());
                return;
            }
        } while (!action.isBack());*/
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

                    //TODO
                    RecipeQueries.addRecipe(server,action.getSelected().getRecipeId(),recipes);

                    System.out.println("Adding recipe: "+action.getSelected().getName());

                } else { /* isback() handled as exit condition */ }

            } else { // system failure
                System.out.println(recipesR.error());
                return;
            }
        } while (!action.isBack()); // back button exits the screen


    }




}
