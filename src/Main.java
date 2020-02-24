import java.sql.SQLException;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    //vars
    private Scanner scanner;
    private User user;
    private ServerDB server;

    enum menu_encoder{
        show_cupboard,
        show_recipe_information
    }
    HashMap<String, menu_encoder> selector;


    public static void main(String[] args) {

        new Main(args).run();

    }

    /**
     * Constructor
     */
    public Main(String[] args){
        this.scanner = new Scanner(System.in);
        this.user = null;

        //build menu encoder
        this.selector = new HashMap<>();

        selector.put("show_cupboard", menu_encoder.show_cupboard);
        selector.put("show_recipe_information", menu_encoder.show_recipe_information);

        for(int i = 0;i< args.length;i++){
            System.out.println("args["+i+"]: "+args[i]);
        }

        if(args.length<4){
            System.out.println("Error: not enough arguments");
            System.exit(0);
        }

        this.server = new ServerDB(args[0],args[1],args[2],args[3],args[4]);


    }

    public void run(){





        while(true){

/*            System.out.print("UserName:");
            String name = scanner.nextLine();
            System.out.print("Password:");
            String password = scanner.nextLine();*/
            String name = "38CA10BA";//todo remove this for auth
            String password = "03D64A6A";//automatic login
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
                this.showCupboard();
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
        try{
            server.connect();
            server.ps = server.conn.prepareStatement("select userID, cupboardID, cartID\n" +
                    "from USER\n" +
                    "where USER.userID=?\n" +
                    "AND USER.password = ?;");
            server.ps.setString(1,n);
            server.ps.setString(2,pwd);

            server.rs = server.ps.executeQuery();
            server.rs.next();
            String userId = server.rs.getString("userID");
            String cupboardID = server.rs.getString("cupboardID");
            String cartID = server.rs.getString("cartID");

            if(userId==null){
                System.out.println("Error: invalid username/password");
                return;
            }
            if(cupboardID==null){
                System.out.println("Error: invalid username/password");
                return;
            }
            if(cartID==null){
                System.out.println("Error: invalid username/password");
                return;
            }

            this.user = new User(userId,cupboardID,cartID);


        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            try{
                this.server.close();
            }
            catch (SQLException se) {
                se.printStackTrace();
            }

        }

    }

    void showCupboard(){

        try{
            server.connect();
            server.ps = server.conn.prepareStatement("select STORES.ingredientID\n" +
                    "from USER, STORES\n" +
                    "where USER.userID=?" +
                    "AND USER.cupboardID = ?" +
                    "AND STORES.cupboardID = USER.cupboardID;");

            server.ps.setString(1,user.userName);
            server.ps.setString(2,user.cupboardId);

            server.rs = server.ps.executeQuery();


            String __Ingredient = "ingredientID";
            String _Ingredient = String.format("%-10s", __Ingredient);

            System.out.println(_Ingredient);

            while (server.rs.next()) {
                _Ingredient = String.format("%-10s", server.rs.getString("ingredientID"));
                System.out.println(_Ingredient);
            }


        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            try{
                this.server.close();
            }
            catch (SQLException se) {
                se.printStackTrace();
            }

        }
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
