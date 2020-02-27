package util;

public class Result <T> {
    private T value;
    private String error;
    private boolean success;

    private Result(boolean success, T value, String error) {
        this.success = success;
        this.value = value;
        this.error = error;
    }

    public boolean isFailure() {
        return !success;
    }

    public boolean isSuccess() {
        return success;
    }

    public T value() {
        return value;
    }
    public String error() {
        return error;
    }

    public static Result Success(Object value) {
        return new Result(true, value, null);
    }

    public static Result Failure(String error) {
        return new Result(false, null, error);
    }
}
