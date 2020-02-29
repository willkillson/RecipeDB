package util.ui;

import java.util.ArrayList;
import java.util.Scanner;

public class SimpleSelect {
    public static <T> SelectAction<T> show(Scanner scanner, ArrayList<T> items, int backIndex) {
        SelectAction<T> action = Select.show(scanner, items,
            false, false, false);
        return (items.indexOf(action.getSelected()) == backIndex)
            ? SelectAction.Back() : action;
    }
}
