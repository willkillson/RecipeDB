package screens;

import static db.queries.CartQueries.getCart;


import db.ServerDB;
import db.queries.RecipeQueries;
import entities.Cart;
import entities.Recipe;
import entities.User;
import java.util.ArrayList;
import java.util.Scanner;
import util.Helpers;
import util.Result;
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
        menuOptions.add("Show Stored Recipes");
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
                        //TODO
                        showStoredRecipes(server, user);
                        break;
                    }
                    case (3)://rateRecipe
                    {
                        //TODO
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
        System.out.println("TODO Show Stored Recipes");
        //TODO
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

    public static void addRecipeCart(Scanner scanner, ServerDB server, User user){
        /*

            Adds a recipe by ID from a list of recipes.

            We should list all the recipes, before asking which recipe they would like to add.

         */
        System.out.println("TODO Add Recipe To Cart");
        //TODO
    }


}
