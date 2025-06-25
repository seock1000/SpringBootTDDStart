package commerce.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
public class SecurityConfiguration {

    @Bean
    Pbkdf2PasswordEncoder passwordEncoder() {
        return Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }

    @Bean
    JwtKeyHolder jwtKeyHolder(@Value("${security.jwt.secret}") String secret) {
        SecretKey key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        return new JwtKeyHolder(key);
    }

    @Bean
    DefaultSecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable) // CSRF 보호 비활성화
            .authorizeHttpRequests(requests -> requests
                .requestMatchers("/seller/signup").permitAll() // /seller/signup 경로는 인증 없이 접근 허용
                .requestMatchers("/seller/issueToken").permitAll()
                .requestMatchers("/shopper/signup").permitAll() // /shopper/signup 경로는 인증 없이 접근 허용
                .requestMatchers("/shopper/issueToken").permitAll()
                .requestMatchers("/seller/me").permitAll()
            )
            .build();
    }
}
