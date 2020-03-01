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
import util.ui.SelectAction;
import util.ui.SimpleSelect;

/**
 * Displays to the user and completes the actions associated with the cupboard.
 */
public class ManageCupboard {

    public static ArrayList<String> menuOptions;



    static {
        menuOptions = new ArrayList<>();
        menuOptions.add("Go back");
        menuOptions.add("Show Cupboard");
        menuOptions.add("Add Ingredient");
        menuOptions.add("Delete Ingredient");


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
                        showCupboard(server, user);
                        break;
                    case(2):
                    	addIngredient(scanner, server, user);
                    	break;
                    case(3):
                    	removeIngredient(scanner, server, user);
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
    	
    	//TODO: show user list of ingredients and let them pick which to add
    	// show list once and then loop ask if they want to add another

        boolean cont = true;
        do {
            String ingredientId = null;
            Result<ArrayList<Ingredient>> maybeIngredient = IngredientQueries.getIngredients(server,user);
            Result<Cupboard> myCupboard = CupboardQueries.getCupboard(server, user);

            ArrayList<Ingredient> ingredients = maybeIngredient.value();
            ArrayList<String> ingredientNames = new ArrayList<>();
            ArrayList<String> ingredientIds = new ArrayList<>();

            if(maybeIngredient.isSuccess() && myCupboard.isSuccess()){
                for(int i = 0;i< ingredients.size();i++){
                    ingredientNames.add(ingredients.get(i).getName());
                    ingredientIds.add(ingredients.get(i).getIngredientId());
                }

                SelectAction<String> selected = null;
                selected = SimpleSelect.show(scanner, ingredientNames, 1);
                String selectionText = selected.getSelected();        // get selected index
                int index = ingredientNames.indexOf(selectionText);


                Cupboard cupboard = myCupboard.value();
                if(!cupboard.contains(ingredients.get(index))){
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
                if(myCupboard.isFailure())
                    System.out.println(myCupboard.error());
            }


            System.out.println("Would you like to add more? (Y)es or (N)o");
            String choice = scanner.nextLine();
            choice = choice.toUpperCase();

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

        }while(cont);
    	


        return;
    	

    }

    public static void removeIngredient(Scanner scanner, ServerDB server, User user) {
    	
    	//TODO : ... see above addIngredient()
    	// BUT we only need ingredients in the cupboard so showCupboard could be called here
    	// getIngredients in IngredientQueries
    	
    	String ingredientId = null;
    	
    	Result maybeRemove = removeFromCupboard(server, user, ingredientId);
    	
    	if (maybeRemove.isSuccess()) {
    		System.out.println("\nSUCCESS: Ingredient was successfully removed from the cupboard.");
    	} else {
    		System.out.println(maybeRemove.error());
    	}
    }
}
