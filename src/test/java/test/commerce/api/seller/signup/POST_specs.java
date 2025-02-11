package test.commerce.api.seller.signup;

import commerce.CommerceApiApp;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
    classes = CommerceApiApp.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@DisplayName("POST /seller/signUp")
public class POST_specs {
}
