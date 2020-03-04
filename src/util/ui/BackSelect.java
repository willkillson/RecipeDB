package util.ui;

import java.util.ArrayList;
import java.util.Scanner;

public class BackSelect {
    public static <T> SelectAction<T> show(Scanner scanner, ArrayList<T> items) {
        return Select.show(scanner, items, false, true, false);
    }

    public static <T> SelectAction<T> show(Scanner scanner, ArrayList<T> items, String prompt) {
        return Select.show(scanner, items, prompt, false, true, false);
    }
}
