package test.commerce.api.seller.issuetoken;

import commerce.CommerceApiApp;
import commerce.command.CreateSellerCommand;
import commerce.query.IssueSellerToken;
import commerce.result.AccessTokenCarrier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import test.CommerceApiTest;
import test.commerce.JwtAssertions;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static test.commerce.EmailGenerator.generateEmail;
import static test.commerce.PasswordGenerator.generatePassword;
import static test.commerce.UsernameGenerator.generateUsername;

@CommerceApiTest
@DisplayName("POST /seller/issueToken")
public class POST_specs {

    @Test
    void 올바르게_요청하면_200_OK_상태코드를_반환한다(
        @Autowired TestRestTemplate client // Client 역할
        ) {
        // Arrange
        String email = generateEmail();
        String password = generatePassword();
        client.postForEntity(
            "/seller/signup",
            new CreateSellerCommand(
                email,
                generateUsername(),
                password
            ),
            Void.class // 응답 본문
        );

        // Act
        var response = client.postForEntity(
            "/seller/issueToken",
            new IssueSellerToken(email,password),
            AccessTokenCarrier.class // 응답 본문
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void 올바르게_요청하면_접근_토큰을_반환한다(
        @Autowired TestRestTemplate client // Client 역할
        ) {
        // Arrange
        String email = generateEmail();
        String password = generatePassword();
        client.postForEntity(
            "/seller/signup",
            new CreateSellerCommand(
                email,
                generateUsername(),
                password
            ),
            Void.class // 응답 본문
        );

        // Act
        var response = client.postForEntity(
            "/seller/issueToken",
            new IssueSellerToken(email,password),
            AccessTokenCarrier.class // 응답 본문
        );

        // Assert
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().accessToken()).isNotNull();
    }

    @Test
    void 접근_토큰은_JWT_형식을_따른다(
        @Autowired TestRestTemplate client // Client 역할
    ) {
        // Arrange
        String email = generateEmail();
        String password = generatePassword();
        client.postForEntity(
            "/seller/signup",
            new CreateSellerCommand(email, generateUsername(), password),
            Void.class // 응답 본문
        );

        // Act
        var response = client.postForEntity(
            "/seller/issueToken",
            new IssueSellerToken(email,password),
            AccessTokenCarrier.class // 응답 본문
        );

        // Assert
        String actual = requireNonNull(response.getBody()).accessToken();
        assertThat(actual).satisfies(JwtAssertions::conformsToJwtFormat);
    }

    @Test
    void 존재하지_않는_이메일_주소로_요청하면_400_BAD_REQUEST_응답을_반환한다(
        @Autowired TestRestTemplate client // Client 역할
    ) {
        // Arrange
        String email = generateEmail();
        String password = generatePassword();

        // Act
        var response = client.postForEntity(
            "/seller/issueToken",
            new IssueSellerToken(email, password),
            AccessTokenCarrier.class // 응답 본문
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void 잘못된_비밀번호가_사용되면_400_Bad_Request_상태코드를_반환한다(
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        String email = generateEmail();
        String password = generatePassword();
        String wrongPassword = generatePassword();

        client.postForEntity(
            "/seller/signup",
            new CreateSellerCommand(email, generateUsername(), password),
            Void.class // 응답 본문
        );

        // Act
        var response = client.postForEntity(
            "/seller/issueToken",
            new IssueSellerToken(email, wrongPassword),
            AccessTokenCarrier.class // 응답 본문
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }
}
