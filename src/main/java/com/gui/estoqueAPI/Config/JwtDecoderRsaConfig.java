package com.gui.estoqueAPI.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.stream.Collectors;

@Configuration
public class JwtDecoderRsaConfig {

    @Bean
    JwtDecoder jwtDecoder(ResourceLoader resourceLoader,
                          @Value("${security.jwt.public-key-path}") String publicKeyPath) throws Exception {

        Resource resource = resourceLoader.getResource(publicKeyPath);

        String pem = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)
        ).lines().collect(Collectors.joining("\n"));

        String content = pem
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(content);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);

        RSAPublicKey publicKey =
                (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(spec);

        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }
}