package test.commerce.api.seller.products;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import test.commerce.api.CommerceApiTest;
import test.commerce.api.TestFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static test.commerce.RegisterProductCommandGenerator.generateRegisterProductCommand;
import static test.commerce.RegisterProductCommandGenerator.generateRegisterProductCommandWithImageUri;

@CommerceApiTest
@DisplayName("POST /seller/products")
public class POST_specs {

    @Test
    void 올바르게_요청하면_201_Created_상태코드를_반환한다(
        @Autowired TestFixture fixture
    ) {
        // Arrange
        fixture.createSellerThenSetAsDefaultUser();

        // Act
        ResponseEntity<Void> response = fixture.client().postForEntity(
            "/seller/products",
            generateRegisterProductCommand(),
            Void.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(201);
    }

    @Test
    void 판매자가_아닌_사용자의_접근_토큰을_사용하면_403_Forbidden_상태코드를_반환한다(
        @Autowired TestFixture fixture
    ) {
        // Arrange
        fixture.createShopperThenSetAsDefaultUser();

        // Act
        ResponseEntity<Void> response = fixture.client().postForEntity(
            "/seller/products",
            generateRegisterProductCommand(),
            Void.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(403);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "invalid-uri",
        "http://",
        "://missing.scheme.com"
    })
    void imageUri_속성이_URI_형식을_따르지_않으면_400_Bad_Request_상태코드를_반환한다(
        String imageUri,
        @Autowired TestFixture fixture
    ) {
        // Arrange
        fixture.createSellerThenSetAsDefaultUser();

        // Act
        ResponseEntity<Void> response = fixture.client().postForEntity(
            "/seller/products",
            generateRegisterProductCommandWithImageUri(imageUri),
            Void.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }
}
