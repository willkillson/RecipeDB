package util;

/**
 * Literal results from the queries.
 *
 * @param <T> the type of value held in the result
 */
public class Result<T> {
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

    /**
     * @return an error message from the specific result
     */
    public String error() {
        return error;
    }

    public static <T> Result<T> success(T value) {
        return new Result<T>(true, value, null);
    }

    /**
     * @return a false (null-typed) result with a pre-made error message
     */
    public static <T> Result<T> failure(String error) {
        return new Result<T>(false, null, error);
    }
}
