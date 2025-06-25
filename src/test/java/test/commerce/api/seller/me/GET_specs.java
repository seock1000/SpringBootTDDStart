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

    @Test
    void 접근_토큰을_사용하지_않으면_401_Unauthorized_응답을_반환한다(
        @Autowired TestRestTemplate client // Client 역할
    ) {
        // Act
        ResponseEntity<SellerMeView> response = client.exchange(
            get("/seller/me").build(),
            SellerMeView.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(401);
    }

    @Test
    void 서로_다른_판매자의_식별자는_서로_다르다(
        @Autowired TestRestTemplate client // Client 역할
    ) {
        // Arrange
        String email1 = generateEmail();
        String password1 = generatePassword();
        String username1 = generateUsername();

        var command1 = new CreateSellerCommand(email1, username1, password1);
        client.postForEntity("/seller/signup", command1, Void.class);
        AccessTokenCarrier carrier1 = client.postForObject(
            "/seller/issueToken",
            new IssueSellerToken(email1, password1),
            AccessTokenCarrier.class // 응답 본문
        );
        String token1 = carrier1.accessToken();

        String email2 = generateEmail();
        String password2 = generatePassword();
        String username2 = generateUsername();

        var command2 = new CreateSellerCommand(email2, username2, password2);
        client.postForEntity("/seller/signup", command2, Void.class);
        AccessTokenCarrier carrier2 = client.postForObject(
            "/seller/issueToken",
            new IssueSellerToken(email2, password2),
            AccessTokenCarrier.class // 응답 본문
        );
        String token2 = carrier2.accessToken();

        // Act
        ResponseEntity<SellerMeView> response1 = client.exchange(
            get("/seller/me")
                .header("Authorization", "Bearer " + token1)
                .build(),
            SellerMeView.class
        );

        ResponseEntity<SellerMeView> response2 = client.exchange(
            get("/seller/me")
                .header("Authorization", "Bearer " + token2)
                .build(),
            SellerMeView.class
        );

        // Assert
        assertThat(response1.getBody().id()).isNotEqualTo(response2.getBody().id());
    }

    @Test
    void 같은_판매자의_식별자는_항상_같다(
        @Autowired TestRestTemplate client // Client 역할
    ) {
        // Arrange
        String email = generateEmail();
        String password = generatePassword();
        String username = generateUsername();

        var command = new CreateSellerCommand(email, username, password);
        client.postForEntity("/seller/signup", command, Void.class);
        AccessTokenCarrier carrier1 = client.postForObject(
            "/seller/issueToken",
            new IssueSellerToken(email, password),
            AccessTokenCarrier.class // 응답 본문
        );
        AccessTokenCarrier carrier2 = client.postForObject(
            "/seller/issueToken",
            new IssueSellerToken(email, password),
            AccessTokenCarrier.class // 응답 본문
        );
        String token1 = carrier1.accessToken();
        String token2 = carrier2.accessToken();

        // Act
        ResponseEntity<SellerMeView> response1 = client.exchange(
            get("/seller/me")
                .header("Authorization", "Bearer " + token1)
                .build(),
            SellerMeView.class
        );

        ResponseEntity<SellerMeView> response2 = client.exchange(
            get("/seller/me")
                .header("Authorization", "Bearer " + token2)
                .build(),
            SellerMeView.class
        );

        // Assert
        assertThat(response1.getBody().id()).isEqualTo(response2.getBody().id());
    }
}
