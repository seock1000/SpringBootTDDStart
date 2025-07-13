package test.commerce.api.seller.products.id;

import commerce.view.SellerProductView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import test.commerce.api.CommerceApiTest;
import test.commerce.api.TestFixture;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@CommerceApiTest
@DisplayName("GET /seller/products/{id}")
public class GET_specs {

    @Test
    void 올바른_접근_토큰을_사용하면_200_OK_응답을_반환한다(
        @Autowired TestFixture fixture // Client 역할
    ) {
         // Arrange
         fixture.createSellerThenSetAsDefaultUser();
         UUID id = fixture.registerProduct();

         // Act
            ResponseEntity<?> response = fixture.client().getForEntity(
                "/seller/products/" + id,
                SellerProductView.class
            );

         // Assert
         assertThat(response.getStatusCode().value()).isEqualTo(200);
    }
}
