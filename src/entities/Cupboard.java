package entities;

import java.util.ArrayList;

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

    public int size() {
        return ingredients.size();
    }
}
