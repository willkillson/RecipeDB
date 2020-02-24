import java.util.HashMap;
import java.util.Scanner;

public class Main {

    //vars
    private Scanner scanner;
    private User user;

    enum menu_encoder{
        show_cupboard,
        show_recipe_information
    }
    HashMap<String, menu_encoder> selector;


    public static void main(String[] args) {

        new Main().run();

    }

    /**
     * Constructor
     */
    public Main(){
        this.scanner = new Scanner(System.in);
        this.user = null;

        //build menu encoder
        this.selector = new HashMap<>();

        selector.put("show_cupboard", menu_encoder.show_cupboard);
        selector.put("show_recipe_information", menu_encoder.show_recipe_information);

    }

    public void run(){

        while(true){

            System.out.print("UserName:");
            String name = scanner.nextLine();
            System.out.print("Password:");
            String password = scanner.nextLine();
            verifyUser(name,password);

            if(this.user!=null){
                break;
            }
        }

        while(true){
            displayMenu();
            String out = scanner.nextLine();
            System.out.println();
            performAction(out);
        }


    }

    public void displayMenu(){
        //todo
        System.out.println("***Menu Items***");
        System.out.println("show_cupboard");
        System.out.println("show_recipe_information");
    }

    public void performAction(String a){
        menu_encoder s = this.selector.get(a);

        if(s==null){
            System.out.println("Error: Invalid menu selection.");
            return;
        }
        switch(s){
            case show_cupboard:
            {
                //todo
                System.out.println("TODO: show_cupboard");
                break;
            }
            case show_recipe_information:
            {
                //todo
                System.out.println("TODO: show_recipe_information");
                break;
            }
        }

    }

    /** Initializes the user object
     *
     * @param n
     * @param pwd
     * @return
     */
    void verifyUser(String n, String pwd){
        /*
                        Sql

            Find the user with name.
            Check if the password is pwd.

            Initialize this.user object if
            verification is achieved.

         */
        String cupboardID = "1337";
        this.user = new User(n,cupboardID);
        System.out.println("TODO");//TODO

    }

    void listRecipes(){

        /*

            select *
            from recipes;

         */

    }

    void displayLocalIngredients(){

    }

    void addRecipes(){

        

    }

    void removeRecipe(){}

    void getShoppingList(){

    }

    void getAvailableRecipes(){

         //TODO

    }




}
