package util.ui;

import java.util.ArrayList;
import java.util.Scanner;

public class PaginatedSelect {
    public static <T> SelectAction<T> show(Scanner scanner, ArrayList<T> items, boolean hasPrevious, boolean hasNext) {
        return Select.show(scanner, items, hasPrevious, true, hasNext);
    }
}
