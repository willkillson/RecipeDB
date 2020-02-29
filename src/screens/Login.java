package screens;

import db.Queries;
import db.ServerDB;
import entities.User;
import util.Result;

public class Login {

    public static User getUser(ServerDB server) {
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
}