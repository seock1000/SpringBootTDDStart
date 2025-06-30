package test.commerce.api.shopper.me;

import commerce.command.CreateSellerCommand;
import commerce.command.CreateShopperCommand;
import commerce.query.IssueShopperToken;
import commerce.result.AccessTokenCarrier;
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
    void 올바른_접근_토큰을_사용하면_200_OK_응답을_반환한다(
        @Autowired TestFixture fixture // Client 역할
    ) {
        // Arrange
        String email = generateEmail();
        String password = generatePassword();

        fixture.createShopper(email, generateUsername(), password);
        String token = fixture.issueShopperToken(email, password); ;

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
    void 접근_토큰을_사용하지_않으면_401_UNAUTHORIZED_응답을_반환한다(
        @Autowired TestFixture fixture // Client 역할
    ) {
        // Arrange
        String email = generateEmail();
        String password = generatePassword();

        fixture.createShopper(email, generateUsername(), password);

        // Act
        ResponseEntity<ShopperMeView> response = fixture.client().exchange(
            get("/shopper/me")
                .build(),
            ShopperMeView.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(401);
    }

    @Test
    void 서로_다른_구매자의_식별자는_서로_다르다(
        @Autowired TestFixture fixture // Client 역할
    ) {
        // Arrange
        String email1 = generateEmail();
        String password1 = generatePassword();
        String email2 = generateEmail();
        String password2 = generatePassword();

        fixture.createShopper(email1, generateUsername(), password1);
        fixture.createShopper(email2, generateUsername(), password2);

        String token1 = fixture.issueShopperToken(email1, password1);
        String token2 = fixture.issueShopperToken(email2, password2);

        // Act
        ResponseEntity<ShopperMeView> response1 = fixture.client().exchange(
            get("/shopper/me")
                .header("Authorization", "Bearer " + token1)
                .build(),
            ShopperMeView.class
        );

        ResponseEntity<ShopperMeView> response2 = fixture.client().exchange(
            get("/shopper/me")
                .header("Authorization", "Bearer " + token2)
                .build(),
            ShopperMeView.class
        );

        // Assert
        assertThat(response1.getBody().id()).isNotEqualTo(response2.getBody().id());
    }

}
