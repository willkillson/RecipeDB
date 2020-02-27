import db.Queries;
import db.ServerDB;
import entities.Cupboard;
import entities.Ingredient;
import entities.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import util.Result;

public class Main {

    //vars
    private Scanner scanner;
    private ServerDB server;

    public static void main(String[] args) {
        new Main(args).run();
    }

    /**
     * Constructor
     */
    public Main(String[] args) {
        this.scanner = new Scanner(System.in);

        if(args.length < 5){
            System.out.println(
                "Error: Main <ip> <database name> <database driver> <username> <password>");
            System.exit(0);
        }

        Result<ServerDB.Config> maybeConfig = ServerDB.Config.create()
            .setIp(args[0])
            .setDatabaseName(args[1])
            .setDriver(args[2])
            .setUsername(args[3])
            .setPassword(args[4])
            .build();

        if (maybeConfig.isFailure()) {
            System.out.println(maybeConfig.error());
            System.exit(0);
        }

        try {
            this.server = ServerDB.create(maybeConfig.value());
            Queries.checkConnection(server);
        } catch (SQLException e) {
            System.out.println("Error connecting to the database");
            e.printStackTrace();
            System.exit(-1);
        } catch (ClassNotFoundException e) {
            System.out.println("Invalid database driver");
            System.exit(-1);
        }
    }

    public static HashMap<Integer, String> menuOptions;
    static {
        menuOptions = new HashMap<>();
        menuOptions.put(0, "Logout");
        menuOptions.put(1, "Show entities.Cupboard");
        menuOptions.put(2, "Show Recipe Information");
    }

    public void run() {
        while (true) {
            User user = getUser();
            System.out.println("Welcome " + user.getEmail());
            int choice = -1;
            do {
                choice = displayMenu();
                if (choice != 0) {
                    switch (choice) {
                        case (1) :
                            showCupboard(user);
                            break;
                        default:
                            System.out.println("ERROR: That selection has not been implemented.");
                    }
                }
            } while(choice != 0);
        }
    }


    /**
     * TODO: NEED TO COMPLETE THIS MENU
     */
    public int displayMenu() {
        int choice;
        do {
            System.out.println("***Menu Items***");
            for(Map.Entry<Integer, String> entry : menuOptions.entrySet()) {
                System.out.println(entry.getKey() + ") " + entry.getValue());
            }
            choice = scanner.nextInt();

            if (!menuOptions.containsKey(choice)) {
                System.out.println("ERROR: Please select a valid option!");
            }
        } while (!menuOptions.containsKey(choice));
        return choice;
    }

    public User getUser() {
        User user = null;
        do {
            // Get credentials

            /*   // add/remove slash to /* to toggle comment block
            System.out.print("UserName:");
            String name = scanner.nextLine();
            System.out.print("Password:");
            String password = scanner.nextLine();
            /*/
            String name = "38CA10BA";//todo remove this for auth
            String password = "03D64A6A";//automatic login
            //*/

            // Auth user
            Result<User> maybeUser = Queries.verifyUser(server, name, password);

            if (maybeUser.isFailure()) {
                System.out.println(maybeUser.error());
            } else {
                user = maybeUser.value();
            }
        } while (user == null); // exists on existing user
        return user;
    }

    void showCupboard(User user) {
        Result<Cupboard> maybeCupboard = Queries.getCupboard(server, user);
        if (maybeCupboard.isSuccess()) {
            Cupboard cupboard = maybeCupboard.value();
            if (cupboard.size() == 0) {
                System.out.println("entities.Cupboard is empty");
            } else {
                System.out.println("Ingredients");
                System.out.println("--------------");
                for (Ingredient ingredient : cupboard.getIngredients()) {
                    System.out.println(ingredient.getName());
                }
            }
        } else {
            System.out.println(maybeCupboard.error());
        }
    }
}
