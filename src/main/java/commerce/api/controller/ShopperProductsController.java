package commerce.api.controller;

import commerce.Product;
import commerce.ProductRepository;
import commerce.result.PageCarrier;
import commerce.view.ProductView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;

@RestController
public record ShopperProductsController(ProductRepository repository) {

    @GetMapping("/shopper/products")
    PageCarrier<ProductView> getProducts() {
        ProductView[] items = repository
            .findAll()
            .stream()
            .sorted(comparing(Product::getDataKey, reverseOrder()))
            .map(product -> new ProductView(
                product.getId(),
                null,
                null,
                null,
                null,
                null,
                0
            ))
            .toArray(ProductView[]::new);
        return new PageCarrier<>(items, null);
    }
}
