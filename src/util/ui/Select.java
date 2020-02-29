package util.ui;

import java.util.ArrayList;
import java.util.Scanner;
import util.Result;

public class Select<T> {
    public static <T> SelectAction<T> show(Scanner scanner, ArrayList<T> choices,
                                           boolean hasPrevious, boolean hasBack, boolean hasNext) {
        Result<SelectAction<T>> action = null;
        do {
            if (action != null && action.isFailure()) {
                System.out.println(action.error());
            }
            System.out.println("Select an option or action below");
            System.out.println("---------------------------------");
            System.out.println("Items");
            System.out.println("---------------------------------");
            for (int i = 0; i < choices.size(); ++i) {
                System.out.println(i + ") " + choices.get(i));
            }
            if (hasBack || hasNext || hasPrevious) {
                System.out.println("---------------------------------");
                System.out.println("Actions");
                System.out.println("---------------------------------");
            }
            if (hasPrevious) {
                System.out.println("p) Go to Previous page");
            }
            if (hasNext) {
                System.out.println("n) Go to Next Page");
            }
            if (hasBack) {
                System.out.println("b) Go Back to Previous Screen");
            }
            action = parseChoice(scanner.nextLine(), choices, hasPrevious, hasBack, hasNext);
        } while (action.isFailure());
        return action.value();
    }

    public static <T> Result<SelectAction<T>> parseChoice(String input, ArrayList<T> choices,
                                                          boolean hasPrevious, boolean hasBack,
                                                          boolean hasNext) {
        String trimmed = input.trim();
        if (trimmed.length() == 0) {
            return Result.failure("Black lines are not a valid input");
        }
        char first = trimmed.charAt(0);
        if (first == 'b') {
            return (hasBack)
                ? Result.success(SelectAction.Back())
                : Result.failure("Back is not allowed here");
        }
        if (first == 'p') {
            return (hasPrevious)
                ? Result.success(SelectAction.Previous())
                : Result.failure("Previous is not allowed here");
        }
        if (first == 'n') {
            return (hasNext)
                ? Result.success(SelectAction.Next())
                : Result.failure("Next is not allowed here");
        }

        try {
            int parsed = Integer.parseInt(input);
            T value = choices.get(parsed);
            SelectAction<T> action = SelectAction.Selected(value);
            return (parsed < 0 || parsed >= choices.size())
                ? Result.failure("'' is not a valid selection")
                : Result.success(action);
        } catch (NumberFormatException e) {
            return Result.failure("'" + input + "' is not a valid action");
        }
    }
}
