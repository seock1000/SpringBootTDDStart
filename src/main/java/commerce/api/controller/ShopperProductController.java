package commerce.api.controller;

import commerce.ProductRepository;
import commerce.result.PageCarrier;
import commerce.view.ProductView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record ShopperProductController(
    ProductRepository productRepository
) {

    @GetMapping("/shopper/products")
    public PageCarrier<ProductView> getProducts() {
        ProductView[] items = productRepository.findAll()
            .stream()
        .map(product -> {
            return new ProductView(
                product.getId(),
                null,
                null,
                null,
                null,
                null,
                0
            );
        })
            .toArray(ProductView[]::new);
        return new PageCarrier<>(items, null);
    }
}
