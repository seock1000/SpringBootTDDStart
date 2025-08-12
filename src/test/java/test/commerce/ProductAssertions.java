package test.commerce;

import commerce.command.RegisterProductCommand;
import commerce.view.ProductView;
import commerce.view.SellerProductView;
import commerce.view.SellerView;
import org.assertj.core.api.ThrowingConsumer;

import java.math.BigDecimal;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductAssertions {

    public static ThrowingConsumer<? super SellerProductView> isDerivedFrom(RegisterProductCommand command) {
        return product -> {
            assertThat(product.name()).isEqualTo(command.name());
            assertThat(product.description()).isEqualTo(command.description());
            assertThat(product.priceAmount())
                .matches(equals(command.priceAmount()));
            assertThat(product.imageUri()).isEqualTo(command.imgUri());
            assertThat(product.stockQuantity()).isEqualTo(command.stockQuantity());
        };
    }

    public static ThrowingConsumer<? super ProductView> isViewDerivedFrom(RegisterProductCommand command) {
        return view -> {
            assertThat(view.id()).isNotNull();
            assertThat(view.name()).isEqualTo(command.name());
            assertThat(view.description()).isEqualTo(command.description());
            assertThat(view.priceAmount())
                .matches(equals(command.priceAmount()));
            assertThat(view.imageUri()).isEqualTo(command.imgUri());
        };
    }

    private static Predicate<? super BigDecimal> equals(BigDecimal expected) {
        return actual -> actual.compareTo(expected) == 0;
    }
}
