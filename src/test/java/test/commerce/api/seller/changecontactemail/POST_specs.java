package test.commerce.api.seller.changecontactemail;

import commerce.command.ChangeContactEmailCommand;
import commerce.view.SellerMeView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import test.commerce.InvalidEmailSource;
import test.commerce.api.CommerceApiTest;
import test.commerce.api.TestFixture;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static test.commerce.EmailGenerator.generateEmail;

@CommerceApiTest
@DisplayName("POST /seller/changeContactEmail")
public class POST_specs {

    @Test
    void 올바르게_요청하면_204_NO_CONTENT_응답을_반환한다(
        @Autowired TestFixture fixture
        ) {
        // Arrange
        fixture.createSellerThenSetAsDefaultUser();
        String newEmail = generateEmail();

        // Act
        ResponseEntity<Void> response = fixture.client().postForEntity(
            "/seller/changeContactEmail",
            new ChangeContactEmailCommand(newEmail),
            Void.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(204);
    }

    @ParameterizedTest
    @InvalidEmailSource
    void contactEmail_속성이_올바르게_지정되지_않으면_400_BAD_REQUEST_응답을_반환한다(
        String invalidEmail,
        @Autowired TestFixture fixture
        ) {
        // Arrange
        fixture.createSellerThenSetAsDefaultUser();

        // Act
        ResponseEntity<Void> response = fixture.client().postForEntity(
            "/seller/changeContactEmail",
            new ChangeContactEmailCommand(invalidEmail),
            Void.class
        );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void 문의_이메일_주소를_올바르게_변경한다(
        @Autowired TestFixture fixture
    ) {
        // Arrange
        fixture.createSellerThenSetAsDefaultUser();
        String newEmail = generateEmail();

        // Act
        fixture.client().postForEntity(
            "/seller/changeContactEmail",
            new ChangeContactEmailCommand(newEmail),
            Void.class
        );

        // Assert
        ResponseEntity<SellerMeView> response = fixture.client().getForEntity(
            "/seller/me",
            SellerMeView.class
        );
        assertThat(requireNonNull(response.getBody()).contactEmail()).isEqualTo(newEmail);
    }
}
