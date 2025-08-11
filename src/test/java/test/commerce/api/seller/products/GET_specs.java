package test.commerce.api.seller.products;

import commerce.view.ArrayCarrier;
import commerce.view.SellerProductView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import test.commerce.api.CommerceApiTest;
import test.commerce.api.TestFixture;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.RequestEntity.get;

@CommerceApiTest
@DisplayName("GET /seller/products")
public class GET_specs {

    @Test
    void 올바르게_요청하면_200_OK_응답을_반환한다(
        @Autowired TestFixture fixture
    ) {
        // Arrange
        fixture.createSellerThenSetAsDefaultUser();

        // Act
        ResponseEntity<ArrayCarrier<SellerProductView>> response =
            fixture.client().exchange(
                get("/seller/products").build(),
                new ParameterizedTypeReference<>() {}
            );

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    void 판매자가_등록한_모든_상품을_반환한다(
        @Autowired TestFixture fixture
    ) {
        // Arrange
        fixture.createSellerThenSetAsDefaultUser();
        List<UUID> expects = fixture.registerProducts();

        // Act
        ResponseEntity<ArrayCarrier<SellerProductView>> response =
            fixture.client().exchange(
                get("/seller/products").build(),
                new ParameterizedTypeReference<>() {}
            );

        // Assert
        ArrayCarrier<SellerProductView> actual = response.getBody();
        assertThat(actual).isNotNull();
        assertThat(actual.items())
            .extracting(SellerProductView::id)
            .containsAll(expects);
    }
}
