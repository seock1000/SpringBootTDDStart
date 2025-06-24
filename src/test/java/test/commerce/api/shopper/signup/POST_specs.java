package test.commerce.api.shopper.signup;

import commerce.command.CreateShopperCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import test.CommerceApiTest;

import static org.assertj.core.api.Assertions.assertThat;
import static test.commerce.EmailGenerator.generateEmail;
import static test.commerce.PasswordGenerator.generatePassword;
import static test.commerce.UsernameGenerator.generateUsername;

@CommerceApiTest
@DisplayName("/shopper/signup")
public class POST_specs {

    @Test
    void 올바르게_요청하면_204_NO_CONTENT_응답을_반환한다(
        @Autowired TestRestTemplate client // Client 역할
    ) {
        // Arrange
        CreateShopperCommand command = new CreateShopperCommand(
            generateEmail(),
            generateUsername(),
            generatePassword()
        );

        // Act
        ResponseEntity<Void> response = client.postForEntity(
            "/shopper/signup",
            command, // 요청 본문
            Void.class // 응답 본문
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(204);
    }

    @Test
    void email_속성이_지정되지_않으면_400_BAD_REQUEST_응답을_반환한다(
        @Autowired TestRestTemplate client // Client 역할
    ) {
        // Arrange
        CreateShopperCommand command = new CreateShopperCommand(
            null, // email 속성 없음
            generateUsername(),
            generatePassword()
        );

        // Act
        ResponseEntity<Void> response = client.postForEntity(
            "/shopper/signup",
            command, // 요청 본문
            Void.class // 응답 본문
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }
}
