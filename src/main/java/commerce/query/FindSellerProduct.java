package commerce.query;

import java.util.UUID;

public record FindSellerProduct(
    UUID productId,
    UUID sellerId
) {
}
