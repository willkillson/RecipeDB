package util;

import entities.Ingredient;
import entities.Recipe;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Helpers {
    public static int displayMenu(Scanner scanner, HashMap<Integer, String> menuOptions) {
        int choice;
        do {
            System.out.println("***Menu Items***");
            for (Map.Entry<Integer, String> entry : menuOptions.entrySet()) {
                System.out.println(entry.getKey() + ") " + entry.getValue());
            }
            choice = scanner.nextInt();

            if (!menuOptions.containsKey(choice)) {
                System.out.println("ERROR: Please select a valid option!");
            }
        } while (!menuOptions.containsKey(choice));
        return choice;
    }

    public static void printIngredientList(Iterable<Ingredient> ingredients) {
        System.out.println("Ingredients");
        System.out.println("--------------");
        for (Ingredient ingredient : ingredients) {
            System.out.println(ingredient.getName());
        }
    }

    public static boolean displayContinue(Scanner scanner, String msg){
        System.out.println(msg+" (Y)es or (N)o");
        String choice = scanner.nextLine();
        choice = choice.toUpperCase();
        boolean cont = true;

        switch(choice){
            case "N":
            {
                cont = false;
                break;
            }
            case "Y":
            {
                cont = true;
                break;
            }
            default:
            {
                cont = false;
            }
        }
        return cont;

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
