package entities;

/**
 * User object to store data for Users's from mySQL queries
 */
public class User {
    private String userId;
    private String email;
    private String cupboardId;
    private String cartID;

    /**
     * User method
     * @param userId
     * @param email
     * @param cupboardId
     * @param cartID
     */
    public User(String userId, String email, String cupboardId, String cartID) {
        this.userId = userId;
        this.email = email;
        this.cupboardId = cupboardId;
        this.cartID = cartID;
    }

    /**
     * getter for cartID
     * @return String
     */
    public String getCartId() {
        return cartID;
    }

    /**
     * getter for cupboardID
     * @return String
     */
    public String getCupboardId() {
        return cupboardId;
    }

    /**
     * getter for email
     * @return String
     */
    public String getEmail() {
        return email;
    }

    /**
     * getter for userID
     * @return String
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Prints a User object to a String
     * @return
     */
    @Override
    public String toString() {
        return "User{" +
            "userId='" + userId + '\'' +
            ", email='" + email + '\'' +
            ", cupboardId='" + cupboardId + '\'' +
            ", cartID='" + cartID + '\'' +
            '}';
    }
}
