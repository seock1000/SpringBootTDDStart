package commerce.api.controller;

import commerce.Product;
import commerce.ProductRepository;
import commerce.result.PageCarrier;
import commerce.view.ProductView;
import commerce.view.SellerView;
import jakarta.persistence.EntityManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.function.Function;

import static java.util.Comparator.comparing;

@RestController
public record ShopperProductController(
    ProductRepository productRepository,
    EntityManager em
) {

    @GetMapping("/shopper/products")
    public PageCarrier<ProductView> getProducts() {
        String query = """
            SELECT new commerce.api.controller.ProductSellerTuple(p, s)
            FROM Product p
            JOIN Seller s ON p.sellerId = s.id
            ORDER BY p.dataKey DESC
            """;
        ProductView[] items = em.createQuery(query, ProductSellerTuple.class)
            .getResultList()
            .stream()
            .map(ProductSellerTuple::toView)
            .toArray(ProductView[]::new);

        return new PageCarrier<>(items, null);
    }

}
