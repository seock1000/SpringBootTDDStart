package commerce.api.controller;

import commerce.Product;
import commerce.ProductRepository;
import commerce.result.PageCarrier;
import commerce.view.ProductView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;

import static java.util.Comparator.comparing;

@RestController
public record ShopperProductController(
    ProductRepository productRepository
) {

    @GetMapping("/shopper/products")
    public PageCarrier<ProductView> getProducts() {
        ProductView[] items = productRepository.findAll()
            .stream()
            .sorted(comparing(Product::getDataKey).reversed())
            .map(product ->
                new ProductView(
                    product.getId(),
                    null,
                    product.getName(),
                    product.getImageUri(),
                    product.getDescription(),
                    product.getPriceAmount(),
                    product.getStockQuantity()
                )
            )
            .toArray(ProductView[]::new);
        return new PageCarrier<>(items, null);
    }
}
