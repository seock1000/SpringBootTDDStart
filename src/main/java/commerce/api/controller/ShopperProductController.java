package commerce.api.controller;

import commerce.ProductRepository;
import commerce.query.GetProductPage;
import commerce.querymodel.GetProductPageQueryProcessor;
import commerce.result.PageCarrier;
import commerce.view.ProductView;
import jakarta.persistence.EntityManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Comparator.comparing;

@RestController
public record ShopperProductController(
    ProductRepository productRepository,
    EntityManager em
) {

    @GetMapping("/shopper/products")
    public PageCarrier<ProductView> getProducts(
        @RequestParam(value = "continuationToken", required = false) String continuationToken
    ) {
        var processor = new GetProductPageQueryProcessor(em);
        var query = new GetProductPage(continuationToken);
        return processor.process(query);
    }
}
