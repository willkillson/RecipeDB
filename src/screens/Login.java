package screens;

import static db.queries.UserQueries.verifyUser;


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

            //TODO      We will be adding a select user feature here, that will allow us to
            //TODO      select a user, and so everything after this will modify the user we
            //TODO      choose.

            //TODO      There will not be a login feature.

            // Auth user
            Result<User> maybeUser = verifyUser(server, name, password);

            if (maybeUser.isFailure()) {
                System.out.println(maybeUser.error());
            } else {
                user = maybeUser.value();
            }
        } while (user == null); // exists on existing user
        return user;
    }
}
