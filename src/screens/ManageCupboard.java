package screens;

import static db.queries.CupboardQueries.getCupboard;
import static db.queries.CupboardQueries.addToCupboard;
import static db.queries.CupboardQueries.removeFromCupboard;


import db.ServerDB;
import db.queries.CupboardQueries;
import db.queries.IngredientQueries;
import entities.Cupboard;
import entities.Ingredient;
import entities.User;
import java.util.ArrayList;
import java.util.Scanner;
import util.Helpers;
import util.Result;
import util.ui.BackSelect;
import util.ui.SelectAction;
import util.ui.SimpleSelect;

/**
 * Displays to the user and completes the actions associated with the cupboard.
 */
public class ManageCupboard {

    public static ArrayList<String> menuOptions;

    static {
        menuOptions = new ArrayList<>();
        menuOptions.add("Show Cupboard");
        menuOptions.add("Add Ingredient");
        menuOptions.add("Delete Ingredient");
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
                    case (0):
                        showCupboard(server, user);
                        break;
                    case(1):
                    	addIngredient(scanner, server, user);
                    	break;
                    case(2):
                    	removeIngredient(scanner, server, user);
                    	break;
                    default:
                        System.out.println("ERROR: That selection has not been implemented.");
                }
            }
        } while (!selected.isBack());
    }

    public static void showCupboard(ServerDB server, User user) {
        Result<Cupboard> maybeCupboard = getCupboard(server, user);
        if (maybeCupboard.isSuccess()) {
            Cupboard cupboard = maybeCupboard.value();
            if (cupboard.size() == 0) {
                System.out.println("The cupboard is empty.");
            } else {
                Helpers.printIngredientList(cupboard.getIngredients());
            }
        } else {
            System.out.println(maybeCupboard.error());
        }
    }
    
    public static void addIngredient(Scanner scanner, ServerDB server, User user) {


        do {
            Result<ArrayList<Ingredient>> maybeIngredient = IngredientQueries.getIngredients(server);
            Result<Cupboard> maybeCupboard = CupboardQueries.getCupboard(server, user);

            if(maybeIngredient.isSuccess() && maybeCupboard.isSuccess()){
                ArrayList<Ingredient> ingredients = maybeIngredient.value();
                ArrayList<String> ingredientNames = new ArrayList<>();
                ArrayList<String> ingredientIds = new ArrayList<>();



                for(int i = 0;i< ingredients.size();i++){
                    ingredientNames.add(ingredients.get(i).getName());
                    ingredientIds.add(ingredients.get(i).getIngredientId());
                }

                SelectAction<String> selected = null;
                selected = SimpleSelect.show(scanner, ingredientNames, -1);
                String selectionText = selected.getSelected();        // get selected index
                int index = ingredientNames.indexOf(selectionText);


                Cupboard cupboard = maybeCupboard.value();
                Ingredient ing = ingredients.get(index);
                if(!cupboard.contains(ing)){
                    //check if the ingredient is not already in our cupboard
                    System.out.println("Adding: "+ingredients.get(index).getName());
                    Result result = CupboardQueries.addToCupboard(server,user,ingredientIds.get(index));

                    if (result.isSuccess()) {
                        System.out.println("\nSUCCESS: Ingredient was successfully added to the cupboard.");
                    } else {
                        System.out.println(result.error());
                    }
                }
                else{
                    //The ingredient we selected is already in our cupboard!
                    System.out.println("\nFAILED: Ingredient was already in our cupboard.");
                }

            }
            else{
                if(maybeIngredient.isFailure())
                    System.out.println(maybeIngredient.error());
                if(maybeCupboard.isFailure())
                    System.out.println(maybeCupboard.error());
            }


        }while(Helpers.displayContinue(scanner,"Would you like to add more?"));

        return;
    }

    public static void removeIngredient(Scanner scanner, ServerDB server, User user) {

    	// : ... see above addIngredient()
    	// BUT we only need ingredients in the cupboard so showCupboard could be called here
    	// getIngredients in IngredientQueries

        do {

            Result<ArrayList<Ingredient>> maybeIngredient = IngredientQueries.getIngredients(server);
            Result<Cupboard> maybeMyCupboard = CupboardQueries.getCupboard(server, user);

            if(maybeIngredient.isSuccess() && maybeMyCupboard.isSuccess()){
                Cupboard cupboard = maybeMyCupboard.value();
                if(cupboard.getIngredientNames().size()<1){
                    System.out.println("Cupboard does not contain anything.");
                    return;
                }

                SelectAction<String> selected = null;
                selected = SimpleSelect.show(scanner, cupboard.getIngredientNames(), -1);

                String selectionText = selected.getSelected();        // get selected index
                int index = cupboard.getIngredientNames().indexOf(selectionText);
                Ingredient ingredient = cupboard.getIngredient(index);


                System.out.println("Removing: "+ ingredient.getName());
                Result result = CupboardQueries.removeFromCupboard(server,user,ingredient.getIngredientId());

                if (result.isSuccess()) {
                    System.out.println("\nSUCCESS: Ingredient was successfully removed from the cupboard.");
                }
                else{

                    System.out.println("\nFAILED: Ingredient is not in our cupboard.");
                }

            }
            else{
                if(maybeIngredient.isFailure())
                    System.out.println(maybeIngredient.error());
                if(maybeMyCupboard.isFailure())
                    System.out.println(maybeMyCupboard.error());
            }

        }while(Helpers.displayContinue(scanner, "Would you like to remove more?"));

        return;
    }
}
