package test.commerce.api.shopper.signup;

import commerce.Shopper;
import commerce.ShopperRepository;
import commerce.command.CreateShopperCommand;
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
        @Autowired TestRestTemplate client // Client 역할
    ) {
        // Arrange
        CreateShopperCommand command = new CreateShopperCommand(
            email, // 잘못된 형식의 email
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

    @Test
    void username_속성이_지정되지_않으면_400_BAD_REQUEST_응답을_반환한다(
        @Autowired TestRestTemplate client // Client 역할
    ) {
        // Arrange
        CreateShopperCommand command = new CreateShopperCommand(
            generateEmail(),
            null, // username 속성 없음
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


    @ParameterizedTest
    @ValueSource(strings = {
        "",
        "sh",
        "shopper ",
        "shopper@",
        "shopper.",
        "shopper!",
    })
    void username_속성이_올바른_형식을_따르지_않으면_400_BAD_REQUEST_응답을_반환한다(
        String username,
        @Autowired TestRestTemplate client // Client 역할
    ) {
        // Arrange
        CreateShopperCommand command = new CreateShopperCommand(
            generateEmail(),
            username, // 잘못된 형식의 username
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


    // red-green-refactor 범위를 벗어나는 테스트(바로 성공)
    // 일부러 정규식을 망가뜨리는 등 수정한 뒤 테스트하여 실패 케이스를 확인하여 신뢰도를 얻을 수 있음
    @ParameterizedTest
    @ValueSource(strings = {
        "abcdefghijklmnopqrstuvwxyz",
        "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
        "0123456789",
        "shopper-",
        "shopper_",
    })
    void username_속성이_올바른_형식을_따르면_204_NO_CONTENT_응답을_반환한다(
        String username,
        @Autowired TestRestTemplate client // Client 역할
    ) {
        // Arrange
        CreateShopperCommand command = new CreateShopperCommand(
            generateEmail(),
            username, // 올바른 형식의 username
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
    void password_속성이_지정되지_않으면_400_BAD_REQUEST_응답을_반환する(
        @Autowired TestRestTemplate client // Client 역할
    ) {
        // Arrange
        CreateShopperCommand command = new CreateShopperCommand(
            generateEmail(),
            generateUsername(),
            null // password 속성 없음
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

    @ParameterizedTest
    @MethodSource("test.commerce.TestDataSource#invalidPasswords")
    void password_속성이_올바른_형식을_따르지_않으면_400_BAD_REQUEST_응답을_반환한다(
        String password,
        @Autowired TestRestTemplate client // Client 역할
    ) {
        // Arrange
        CreateShopperCommand command = new CreateShopperCommand(
            generateEmail(),
            generateUsername(),
            password // 잘못된 형식의 password
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

    @Test
    void 이미_존재하는_이메일_주소로_요청하면_400_BAD_REQUEST_응답을_반환한다(
        @Autowired TestRestTemplate client // Client 역할
    ) {
        // Arrange
        String email = generateEmail();
        String username = generateUsername();
        String password = generatePassword();

        // 이미 존재하는 이메일로 회원가입 요청
        client.postForEntity(
            "/shopper/signup",
            new CreateShopperCommand(email, username, password),
            Void.class // 응답 본문
        );

        // Act
        ResponseEntity<Void> response = client.postForEntity(
            "/shopper/signup",
            new CreateShopperCommand(email, generateUsername(), generatePassword()),
            Void.class // 응답 본문
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void 이미_존재하는_사용자_이름으로_요청하면_400_BAD_REQUEST_응답을_반환한다(
        @Autowired TestRestTemplate client // Client 역할
    ) {
        // Arrange
        String email = generateEmail();
        String username = generateUsername();
        String password = generatePassword();

        // 이미 존재하는 사용자명으로 회원가입 요청
        client.postForEntity(
            "/shopper/signup",
            new CreateShopperCommand(email, username, password),
            Void.class // 응답 본문
        );

        // Act
        ResponseEntity<Void> response = client.postForEntity(
            "/shopper/signup",
            new CreateShopperCommand(generateEmail(), username, generatePassword()),
            Void.class // 응답 본문
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void 비밀번호를_올바르게_암호화_한다(
        @Autowired TestRestTemplate client,
        @Autowired ShopperRepository shopperRepository,
        @Autowired PasswordEncoder passwordEncoder
        ) {
        // Arrange
        var command = new CreateShopperCommand(
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
        Shopper shopper = shopperRepository
            .findAll()
            .stream()
            .filter(it -> it.getEmail().equals(command.email()))
            .findFirst()
            .orElseThrow();
        String actual = shopper.getHashedPassword();
        assertThat(actual).isNotNull();
        assertThat(passwordEncoder.matches(command.password(), actual)).isTrue();
    }
}
