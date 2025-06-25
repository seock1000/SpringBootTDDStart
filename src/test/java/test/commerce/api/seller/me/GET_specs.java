package test.commerce.api.seller.me;

import commerce.command.CreateSellerCommand;
import commerce.query.IssueSellerToken;
import commerce.result.AccessTokenCarrier;
import commerce.view.SellerMeView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import test.CommerceApiTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.RequestEntity.get;
import static test.commerce.EmailGenerator.generateEmail;
import static test.commerce.PasswordGenerator.generatePassword;
import static test.commerce.UsernameGenerator.generateUsername;

@CommerceApiTest
@DisplayName("GET /seller/me")
public class GET_specs {

    @Test
    void 올바른_접근_토큰을_사용하면_200_OK_응답을_반환한다(
        @Autowired TestRestTemplate client // Client 역할
        ) {
        // Arrange
        String email = generateEmail();
        String password = generatePassword();
        String username = generateUsername();

        var command = new CreateSellerCommand(email, username, password);
        client.postForEntity("/seller/signup", command, Void.class);
        AccessTokenCarrier carrier = client.postForObject(
            "/seller/issueToken",
            new IssueSellerToken(email, password),
            AccessTokenCarrier.class // 응답 본문
        );
        String token = carrier.accessToken();

        // Act
        ResponseEntity<SellerMeView> response = client.exchange(
            get("/seller/me")
                .header("Authorization", "Bearer " + token)
                .build(),
            SellerMeView.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }
}
