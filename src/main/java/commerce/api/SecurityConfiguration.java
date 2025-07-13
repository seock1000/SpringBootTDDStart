package commerce.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import static org.springframework.security.oauth2.core.authorization.OAuth2AuthorizationManagers.hasScope;

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
    JwtDecoder jwtDecoder(JwtKeyHolder jwtKeyHolder) {
        return NimbusJwtDecoder.withSecretKey(jwtKeyHolder.key()).build();
    }

    @Bean
    DefaultSecurityFilterChain securityFilterChain(
        HttpSecurity http,
        JwtDecoder jwtDecoder) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable) // CSRF 보호 비활성화
            .oauth2ResourceServer(c -> c.jwt(jwt -> jwt.decoder(jwtDecoder))) // JWT 디코더 설정
            .authorizeHttpRequests(requests -> requests
                .requestMatchers("/seller/signup").permitAll() // /seller/signup 경로는 인증 없이 접근 허용
                .requestMatchers("/seller/issueToken").permitAll()
                .requestMatchers("/seller/**").access(hasScope("seller"))
                .requestMatchers("/shopper/signup").permitAll() // /shopper/signup 경로는 인증 없이 접근 허용
                .requestMatchers("/shopper/issueToken").permitAll()
                .anyRequest().authenticated() // 나머지 경로는 인증 필요
            )
            .build();
    }
}
