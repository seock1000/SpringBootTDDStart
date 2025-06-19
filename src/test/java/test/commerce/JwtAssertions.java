package test.commerce;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtAssertions {

    public static void conformsToJwtFormat(String s) {
        String[] parts = s.split("\\.");
        assertThat(parts).hasSize(3);
        assertThat(parts[0]).matches(JwtAssertions::isBase64UrlEncodedJson);
        assertThat(parts[1]).matches(JwtAssertions::isBase64UrlEncodedJson);
        assertThat(parts[2]).matches(JwtAssertions::isBase64UrlEncoded);
    }

    private static boolean isBase64UrlEncodedJson(String s2) {
        try {
            new ObjectMapper().readTree(Base64.getUrlDecoder().decode(s2));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isBase64UrlEncoded(String s3) {
        try {
            Base64.getUrlDecoder().decode(s3);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
