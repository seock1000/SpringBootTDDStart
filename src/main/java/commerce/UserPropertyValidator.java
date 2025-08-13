package commerce;

public class UserPropertyValidator {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    private static final String USERNAME_REGEX = "^[a-zA-Z0-9_-]{3,}$";

    public static boolean isEmailValid(String email) {
        return email != null && email.matches(EMAIL_REGEX);
    }

    public static boolean isUsernameValid(String username) {
        return username != null && username.matches(USERNAME_REGEX);
    }

    public static boolean isPasswordValid(String password) {
        return password != null
            && password.length() >= 8
            && !contains4SequentialCharacters(password);
    }

    private static boolean contains4SequentialCharacters(String password) {
        for(int i = 0; i < password.length() - 3; i++) {
            char c1 = password.charAt(i);
            char c2 = password.charAt(i + 1);
            char c3 = password.charAt(i + 2);
            char c4 = password.charAt(i + 3);
            if (c1 + 1 == c2 && c2 + 1 == c3 && c3 + 1 == c4) {
                return true;
            }
        }
        return false;
    }
}
