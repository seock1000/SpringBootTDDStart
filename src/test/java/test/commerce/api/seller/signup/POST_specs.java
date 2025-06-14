package test.commerce.api.seller.signup;

import commerce.CommerceApiApp;
import commerce.command.CreateSellerCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
    classes = CommerceApiApp.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@DisplayName("POST /seller/signup")
public class POST_specs {

    @Test
    void 올바르게_요청하면_204_NO_CONTENT_응답을_반환한다(
        @Autowired TestRestTemplate client // Client 역할
    ) {
        // Arrange
        CreateSellerCommand command = new CreateSellerCommand(
            "seller@test.com",
        "seller",
        "password"
        );

        // Act
        ResponseEntity<Void> response = client.postForEntity(
            "/seller/signup",
            command, // 요청 본문
            Void.class // 응답 본문
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(204);
    }
}
