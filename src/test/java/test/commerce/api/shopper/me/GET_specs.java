package test.commerce.api.shopper.me;

import commerce.view.ShopperMeView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import test.commerce.api.CommerceApiTest;
import test.commerce.api.TestFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.RequestEntity.get;
import static test.commerce.EmailGenerator.generateEmail;
import static test.commerce.PasswordGenerator.generatePassword;
import static test.commerce.UsernameGenerator.generateUsername;

@CommerceApiTest
@DisplayName("GET /shopper/me")
public class GET_specs {

    @Test
    void 올바르게_요청하면_200_OK_상태코드를_반환한다(
        @Autowired TestFixture fixture
    ) {
        // Arrange
        String email = generateEmail();
        String password = generatePassword();

        fixture.createShopper(email, generateUsername(), password);
        String token = fixture.issueShopperToken(email, password);

        // Act
        ResponseEntity<ShopperMeView> response = fixture.client().exchange(
            get("/shopper/me")
                .header("Authorization", "Bearer " + token)
                .build(),
            ShopperMeView.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void 접근_토큰을_사용하지_않으면_401_Unauthorized_상태코드를_반환한다(
        @Autowired TestFixture fixture
    ) {
        // Act
        ResponseEntity<ShopperMeView> response = fixture.client().exchange(
            get("/shopper/me").build(),
            ShopperMeView.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(401);
    }
}
