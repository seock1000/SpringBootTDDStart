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
        String username = generateUsername();

        var command = new CreateShopperCommand(email, username, password);
        fixture.client().postForEntity("/shopper/signup", command, Void.class);

        var carrier = fixture.client().postForObject(
            "/shopper/issueToken",
            new IssueShopperToken(email, password),
            AccessTokenCarrier.class // 응답 본문
        );

        // Act
        ResponseEntity<ShopperMeView> response = fixture.client().exchange(
            get("/shopper/me")
                .header("Authorization", "Bearer " + carrier.accessToken())
                .build(),
            ShopperMeView.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

}
