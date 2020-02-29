import db.Queries;
import db.ServerDB;
import entities.User;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import screens.Login;
import screens.ManageCupboard;
import screens.ManageRecipe;
import util.Result;
import util.SelectAction;
import util.SimpleSelect;

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

        if (args.length < 5) {
            System.out.println(
                "\nERROR: Insufficient number of arguments provided.\n"
                + "Please use the following for reference:\n"
                + "Main <ip> <database name> <database driver> <username> <password>");
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
            System.out.println("\nERROR: Connection failed.\n"
            		+ "Please check the url, username, and password.");
            System.exit(-1);
        } catch (ClassNotFoundException e) {
            System.out.println("\nERROR: Invalid database driver.");
            System.exit(-1);
        }
    }

    public static ArrayList<String> menuOptions;

    static {
        menuOptions = new ArrayList<>();
        menuOptions.add("Logout");
        menuOptions.add("Manage Cupboard");
        menuOptions.add("Manage Recipes");
    }

    public void run() {
        while (true) {
            User user = Login.getUser(server);
            System.out.println("Welcome " + user.getEmail());

            SelectAction<String> selected = null;
            do {
                // the zero is the index of the exit in menuOptions
                selected = SimpleSelect.show(scanner, menuOptions, 0);
                if (selected.isSelected()) { // valid selection / not back
                    // get selected index
                    String selectionText = selected.getSelected();
                    int index = menuOptions.indexOf(selectionText);
                    switch (index) {
                        case (1):
                            ManageCupboard.view(scanner, server, user);
                            break;
                        case (2):
                            ManageRecipe.view(scanner, server, user);
                            break;
                        default:
                            System.out.println("ERROR: That selection has not been implemented.");
                    }
                }
            } while (!selected.isBack()); // exit on back
        }
    }
}
