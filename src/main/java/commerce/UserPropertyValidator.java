package commerce;

public class UserPropertyValidator {

    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

    public static boolean isEmailValid(String email) {
        return email != null && email.matches(EMAIL_REGEX);
    }
}
