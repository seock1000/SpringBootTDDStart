package commerce.querymodel;

import commerce.Product;
import commerce.query.GetSellerProducts;
import commerce.view.ArrayCarrier;
import commerce.view.SellerProductView;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static java.util.Comparator.comparing;

@RequiredArgsConstructor
public class GetSellerProductsQueryProcessor {

    private final Function<UUID, List<Product>> getProductsOfSeller;


    public ArrayCarrier<SellerProductView> process(GetSellerProducts query) {
        SellerProductView[] items = getProductsOfSeller.apply(query.sellerId())
            .stream()
            .sorted(comparing(Product::getRegisteredTimeUtc).reversed())
            .map(ProductMapper::convertToView)
            .toArray(SellerProductView[]::new);
        return new ArrayCarrier<>(items);
    }
}
