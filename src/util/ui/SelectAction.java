package util.ui;

public class SelectAction<T> {

    public enum Action {
        Previous,
        Back,
        Next,
        Selected
    }

    private Action action;

    private T selected;

    private SelectAction(Action action) {
        this.action = action;
    }

    private SelectAction(T selected) {
        this.action = Action.Selected;
        this.selected = selected;
    }

    public boolean isBack() {
        return action == Action.Back;
    }

    public boolean isNext() {
        return action == Action.Next;
    }

    public boolean isPrevious() {
        return action == Action.Previous;
    }

    public boolean isSelected() {
        return action == Action.Selected;
    }

    public T getSelected() {
        return selected;
    }

    public static <T> SelectAction<T> Back() {
        return new SelectAction<>(Action.Back);
    }

    public static <T> SelectAction<T> Previous() {
        return new SelectAction<>(Action.Previous);
    }

    public static <T> SelectAction<T> Next() {
        return new SelectAction<>(Action.Next);
    }

    public static <T> SelectAction<T> Selected(T value) {
        return new SelectAction<>(value);
    }
}
