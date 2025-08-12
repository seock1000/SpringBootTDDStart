package commerce.api.controller;

import commerce.Product;
import commerce.ProductRepository;
import commerce.result.PageCarrier;
import commerce.view.ProductView;
import commerce.view.SellerView;
import jakarta.persistence.EntityManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

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
        String query = """
            SELECT new commerce.api.controller.ProductSellerTuple(p, s)
            FROM Product p
            JOIN Seller s ON p.sellerId = s.id
            WHERE :cursor IS NULL OR p.dataKey <= :cursor
            ORDER BY p.dataKey DESC
            """;

        int pageSize = 10;

        List<ProductSellerTuple> results = em
            .createQuery(query, ProductSellerTuple.class)
            .setParameter("cursor", decodeCursor(continuationToken))
            .setMaxResults(pageSize + 1)
            .getResultList();

        ProductView[] items = results
            .stream()
            .limit(pageSize)
            .map(ProductSellerTuple::toView)
            .toArray(ProductView[]::new);

        Long next = results.getLast().product().getDataKey();

        return new PageCarrier<>(items, encodeCursor(next));
    }

    private Long decodeCursor(String continuationToken) {
        if (continuationToken == null) {
            return null;
        }
        byte[] data = Base64.getUrlDecoder().decode(continuationToken);
        return Long.parseLong(new String(data, UTF_8));
    }

    private String encodeCursor(Long cursor) {
        byte[] data = cursor.toString().getBytes(UTF_8);
        return Base64.getUrlEncoder().encodeToString(data);
    }
}
