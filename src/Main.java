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
    public Main(){
        this.scanner = new Scanner(System.in);
        this.user = null;
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
            performAction();
        }


    }

    public void displayMenu(){
        //todo
        System.out.println("TODO");
        System.out.println("Say something.");
    }

    public void performAction(){
        //todo
        System.out.println("TODO");
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
<<<<<<< HEAD
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
=======
        this.user = new User(n,cupboardID); //TODO
>>>>>>> parent of c2d817e... adding method with comment

    }




}
