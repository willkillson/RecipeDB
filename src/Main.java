import java.util.Scanner;

public class Main {

    //vars
    private Scanner scanner;
    private User user;


    public static void main(String[] args) {

        new Main().run();

    }

    /**
     * Constructor
     */
    Main(){
        this.scanner = new Scanner(System.in);
        this.user = null;
    }

    void run(){

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
            performAction();
        }


    }

    void displayMenu(){

        System.out.println("TODO");//todo
    }

    void performAction(){

        System.out.println("TODO");//todo
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


        FROM DELIVERABLE 1:
        Each user will have a unique userID.
        They will use an email, and password used for login.
        Each user will have a cupboardID that uniquely identifies their cupboard.
        Cupboards will store ingredientID’s for each ingredient that a user currently has in their cupboard.

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

    void addRecipes(){

        

    }

    void removeRecipe(){}

    void getShoppingList(){

    }

    void getAvailableRecipes(){

    }




}
