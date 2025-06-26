package test.commerce.api.seller.signup;

import commerce.Seller;
import commerce.SellerRepository;
import commerce.command.CreateSellerCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import test.commerce.api.CommerceApiTest;

import static org.assertj.core.api.Assertions.assertThat;
import static test.commerce.EmailGenerator.generateEmail;
import static test.commerce.PasswordGenerator.generatePassword;
import static test.commerce.UsernameGenerator.generateUsername;

@CommerceApiTest
@DisplayName("POST /seller/signup")
public class POST_specs {

    @Test
    void 올바르게_요청하면_204_NO_CONTENT_응답을_반환한다(
        @Autowired TestRestTemplate client // Client 역할
    ) {
        // Arrange
        CreateSellerCommand command = new CreateSellerCommand(
            generateEmail(),
            generateUsername(),
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

    @Test
    void email_속성이_지정되지_않으면_400_BAD_REQUEST_응답을_반환한다(
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        CreateSellerCommand command = new CreateSellerCommand(
            null, // email 속성 없음
            generateUsername(),
            "password"
        );

        // Act
        ResponseEntity<Void> response = client.postForEntity(
            "/seller/signup",
            command, // 요청 본문
            Void.class // 응답 본문
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "invalid-email",
        "invalid-email@",
        "invalid-email@test",
        "invalid-email@test.",
        "invalid-email@.com",
    })
    void email_속성이_올바른_형식을_따르지_않으면_400_BAD_REQUEST_응답을_반환한다(
        String email,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        CreateSellerCommand command = new CreateSellerCommand(
            email, // 잘못된 형식의 email
            generateUsername(),
            "password"
        );

        // Act
        ResponseEntity<Void> response = client.postForEntity(
            "/seller/signup",
            command, // 요청 본문
            Void.class // 응답 본문
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void username_속성이_지정되지_않으면_400_BAD_REQUEST_응답을_반환한다(
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        CreateSellerCommand command = new CreateSellerCommand(
            generateEmail(),
            null, // username 속성 없음
            "password"
        );
        // Act
        ResponseEntity<Void> response = client.postForEntity(
            "/seller/signup",
            command, // 요청 본문
            Void.class // 응답 본문
        );
        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "",
        "se",
        "seller ",
        "seller@",
        "seller.",
        "seller!",
    })
    void username_속성이_올바른_형식을_따르지_않으면_400_BAD_REQUEST_응답을_반환한다(
        String username,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        CreateSellerCommand command = new CreateSellerCommand(
            generateEmail(),
            username, // 잘못된 형식의 username
            "password"
        );
        // Act
        ResponseEntity<Void> response = client.postForEntity(
            "/seller/signup",
            command, // 요청 본문
            Void.class // 응답 본문
        );
        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "seller",
        "FDIFOEIFIEJFSJF",
        "01010142412",
        "seller-",
        "seller_",
    })
    void username_속성이_올바른_형식을_따르면_204_NO_CONTENT_응답을_반환한다(
        String username,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        CreateSellerCommand command = new CreateSellerCommand(
            generateEmail(),
            username, // 올바른 형식의 username
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

    @Test
    void password_속성이_지정되지_않으면_400_BAD_REQUEST_응답을_반환한다(
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        CreateSellerCommand command = new CreateSellerCommand(
            generateEmail(),
            generateUsername(),
            null // password 속성 없음
        );
        // Act
        ResponseEntity<Void> response = client.postForEntity(
            "/seller/signup",
            command, // 요청 본문
            Void.class // 응답 본문
        );
        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @ParameterizedTest
    @MethodSource("test.commerce.TestDataSource#invalidPasswords")
    void password_속성이_올바른_형식을_따르지_않으면_400_BAD_REQUEST_응답을_반환한다(
        String password,
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        CreateSellerCommand command = new CreateSellerCommand(
            generateEmail(),
            generateUsername(),
            password // 잘못된 형식의 password
        );
        // Act
        ResponseEntity<Void> response = client.postForEntity(
            "/seller/signup",
            command, // 요청 본문
            Void.class // 응답 본문
        );
        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void 이미_존재하는_이메일_주소로_요청하면_400_BAD_REQUEST_응답을_반환한다(
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        String email = generateEmail();
        CreateSellerCommand command = new CreateSellerCommand(
            email,
            generateUsername(),
            "password"
        );
        client.postForEntity("/seller/signup", command, Void.class);

        // Act
        ResponseEntity<Void> response = client.postForEntity(
            "/seller/signup",
            new CreateSellerCommand(email, generateUsername(), "password"), // 요청 본문
            Void.class // 응답 본문
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void 이미_존재하는_사용자_이름으로_요청하면_400_BAD_REQUEST_응답을_반환한다(
        @Autowired TestRestTemplate client
    ) {
        // Arrange
        String username = generateUsername();
        CreateSellerCommand command = new CreateSellerCommand(
            generateEmail(),
            username,
            "password"
        );
        client.postForEntity("/seller/signup", command, Void.class);

        // Act
        ResponseEntity<Void> response = client.postForEntity(
            "/seller/signup",
            new CreateSellerCommand(generateEmail(), username, "password"), // 요청 본문
            Void.class // 응답 본문
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    /**
     * 비밀번호의 암호화 여부는 클라이언트 측에서 확인할 수 없으므로,
     * 서버 측에서 비밀번호가 올바르게 암호화되었는지 확인하는 테스트입니다.
     * 때문에 구현에 의존하는 테스트가 될 수 있습니다.
     */
    @Test
    void 비밀번호를_올바르게_암호화_한다(
        @Autowired TestRestTemplate client,
        @Autowired SellerRepository sellerRepository,
        @Autowired PasswordEncoder encoder
    ) {
        // Arrange
        CreateSellerCommand command = new CreateSellerCommand(
            generateEmail(),
            generateUsername(),
            generatePassword()
        );

        // Act
        ResponseEntity<Void> response = client.postForEntity(
            "/seller/signup",
            command, // 요청 본문
            Void.class // 응답 본문
        );

        // Assert
        Seller seller = sellerRepository.findAll()
            .stream()
            .filter(it -> it.getEmail().equals(command.email()))
            .findFirst()
            .orElseThrow();
        String actual = seller.getHashedPassword();
        assertThat(actual).isNotNull();
        assertThat(encoder.matches(command.password(), actual)).isTrue();
    }
}
