package db.queries;

import db.ServerDB;
import java.util.ArrayList;
import util.Result;

public class RecipeQueries {
    public static Result<ArrayList<entities.Recipe>> getRecipes(ServerDB server, int start, int stop) {
        return Result.failure("Not Implemented");
    }
}
