package commerce;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

public interface ShopperRepository extends JpaRepository<Shopper, Long> {
    Optional<Shopper> findByEmail(String email);

    Optional<Shopper> findById(UUID id);
}
