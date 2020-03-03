package entities;

import java.util.ArrayList;

/**
 * Cupboard of the user. Each user has one and only one.
 */
public class Cupboard {
    private String cupboardId;
    private ArrayList<Ingredient> ingredients;

    public Cupboard(String cupboardId, ArrayList<Ingredient> ingredients) {
        this.cupboardId = cupboardId;
        this.ingredients = ingredients;
    }

    public Iterable<Ingredient> getIngredients() {
        return ingredients;
    }

    public String getCupboardId() {
        return cupboardId;
    }

    public ArrayList<String> getIngredientNames() {
        ArrayList<String> retArray = new ArrayList<>();
        for (int i = 0; i < ingredients.size(); i++) {
            retArray.add(this.ingredients.get(i).getName());
        }
        return retArray;
    }

    public Ingredient getIngredient(int index) {
        return this.ingredients.get(index);
    }

    /**
     * Returns whether the cupboard contains a certain ingredient
     *
     * @param oIngredient
     * @return
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
