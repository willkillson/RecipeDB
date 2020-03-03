package util;

import entities.Adds;
import entities.Ingredient;
import entities.Recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Methods for displaying info/actions to the user.
 */
public class Helpers {

    /**
     * Asks the user if they would like to continue with an action.
     * 
     * @param msg the question to be answered
     * @return true if the actions should be continued, false if not
     */
    public static boolean displayContinue(String msg){
        Scanner scanner = new Scanner(System.in);

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

    public static <T> void printCollection(ArrayList<T> col){
        for(int i = 0;i< col.size();i++){
            System.out.println(col.get(i).toString());
        }
    }

}
