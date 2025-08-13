package commerce.querymodel;

import commerce.Product;
import commerce.query.FindSellerProduct;
import commerce.view.SellerProductView;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@RequiredArgsConstructor
public class FindSellerProductQueryProcessor {

    private final Function<UUID, Optional<Product>> findProduct;

    public Optional<SellerProductView> process(FindSellerProduct query) {
        return findProduct.apply(query.productId())
            .filter(product -> product.getSellerId().equals(query.sellerId()))
            .map(ProductMapper::convertToView);
    }
}
