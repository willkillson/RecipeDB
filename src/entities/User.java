package entities;

public class User {
    private String userId;
    private String email;
    private String cupboardId;
    private String cartID;

    public User(String userId, String email, String cupboardId, String cartID) {
        this.userId = userId;
        this.email = email;
        this.cupboardId = cupboardId;
        this.cartID = cartID;
    }

    public String getCartId() {
        return cartID;
    }

    public String getCupboardId() {
        return cupboardId;
    }

    public String getEmail() {
        return email;
    }

    public String getUserId() {
        return userId;
    }


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
