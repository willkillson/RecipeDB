package entities;

public class Ingredient {
    private String ingredientId;
    private String name;

    public Ingredient(String ingredientId, String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getIngredientId() {
        return ingredientId;
    }
}
