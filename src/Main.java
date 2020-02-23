import java.util.Scanner;

public class Main {

    //vars
    private Scanner scanner;
    private boolean isVerifiedUser;
    private String userName;
    private String cupboardId;

    public static void main(String[] args) {

        new Main().run();

    }

    /**
     * Constructor
     */
    public Main(){
        this.scanner = new Scanner(System.in);

    }

    public void run(){

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

    /**
     *
     * @param n
     * @param pwd
     * @return
     */
    boolean verifyUser(String n, String pwd){
        /*
                        Sql

            Find the user with name.
            Check if the password is pwd
            return true/false depending.

         */

        //TODO

        return false;
    }




}
