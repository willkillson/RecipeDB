package entities;

import java.util.ArrayList;

/**
 * Cupboard of the user. Each user has one and only one.
 */
public class Cupboard {
    private String cupboardId;
    private ArrayList<Ingredient> ingredients;


    /**
     * Cupboard method
     * @param cupboardId
     * @param ingredients
     */
    public Cupboard(String cupboardId, ArrayList<Ingredient> ingredients) {
        this.cupboardId = cupboardId;
        this.ingredients = ingredients;
    }


    /**
     * getter for ingredients
     * @return
     */
    public Iterable<Ingredient> getIngredients() {
        return ingredients;
    }

    /**
     * getter for cupboardID
     * @return
     */
    public String getCupboardId() {
        return cupboardId;
    }

    /**
     * getter for ingredient names
     * @return ArratList
     */
    public ArrayList<String> getIngredientNames() {
        ArrayList<String> retArray = new ArrayList<>();
        for (int i = 0; i < ingredients.size(); i++) {
            retArray.add(this.ingredients.get(i).getName());
        }
        return retArray;
    }

    /**
     * getter for ingredient
     * @param index
     * @return
     */
    public Ingredient getIngredient(int index) {
        return this.ingredients.get(index);
    }

    /**
     * Returns whether the cupboard contains a certain ingredient.
     *
     * @param oIngredient	to check for
     * @return true or false
     */
    public boolean contains(Ingredient oIngredient) {
        return ingredients.contains(oIngredient);
    }

    /**
     * @return number of ingredients in the cupboard
     */
    public int size() {
        return ingredients.size();
    }
}
