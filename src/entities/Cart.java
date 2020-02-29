package entities;

import java.util.ArrayList;

/**
 * Cart of the user. Each user has one and only one.
 */
public class Cart {
    private String cartId;
    private ArrayList<Ingredient> ingredients;

    public Cart(String cartId, ArrayList<Ingredient> ingredients) {
        this.cartId = cartId;
        this.ingredients = ingredients;
    }

    public Iterable<Ingredient> getIngredients() {
        return ingredients;
    }

    public String getCartId() {
        return cartId;
    }

    /**
     * @return number of ingredients in the cart
     */
    public int size() {
        return ingredients.size();
    }
}
