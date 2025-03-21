package commerce.api.controller;

import commerce.result.PageCarrier;
import commerce.view.ProductView;
import jakarta.persistence.EntityManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public record ShopperProductsController(EntityManager entityManager) {

    @GetMapping("/shopper/products")
    PageCarrier<ProductView> getProducts() {
        String queryString = """
            SELECT new commerce.api.controller.ProductSellerTuple(p, s)
            FROM Product p
            JOIN Seller s ON p.sellerId = s.id
            ORDER BY p.dataKey DESC
            """;

        ProductView[] items = entityManager
            .createQuery(queryString, ProductSellerTuple.class)
            .getResultList()
            .stream()
            .map(ProductSellerTuple::toView)
            .toArray(ProductView[]::new);

        return new PageCarrier<>(items, null);
    }
}
