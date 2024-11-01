package kz.symtech.antifraud.authservice.helpers;

import kz.symtech.antifraud.authservice.config.JwtConfig;
import kz.symtech.antifraud.authservice.utils.Base64Util;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Данный класс предоставялет возможность генерации JWT токенов
 */
@Component
@RequiredArgsConstructor
public class JwtKeyProvider {

    private final ResourceLoader resourceLoader;
    private final Base64Util base64Util;
    private final JwtConfig jwtConfig;

    @Getter
    private PrivateKey privateKey;

    @Getter
    private PublicKey publicKey;

    /**
     * Данный метод подгружает публичный и приватный ключи
     */
    @PostConstruct
    public void init() {
        privateKey = readKey(
                jwtConfig.getPrivateKeyPath(),
                "PRIVATE",
                this::privateKeySpec,
                this::privateKeyGenerator
        );

        publicKey = readKey(
                jwtConfig.getPublicKeyPath(),
                "PUBLIC",
                this::publicKeySpec,
                this::publicKeyGenerator
        );
    }

    /**
     * Данный метод читает данные из ключа
     * @param resourcePath
     * @param headerSpec
     * @param keySpec
     * @param keyGenerator
     * @param <T>
     * @return
     */
    private <T extends Key> T readKey(String resourcePath, String headerSpec, Function<String, EncodedKeySpec> keySpec, BiFunction<KeyFactory, EncodedKeySpec, T> keyGenerator) {
        try {
            Resource resource = resourceLoader.getResource(resourcePath);
            InputStream inputStream = resource.getInputStream();
            String keyString = new String(inputStream.readAllBytes());
            keyString = keyString.replace("-----BEGIN " + headerSpec + " KEY-----", "");
            keyString = keyString.replace("-----END " + headerSpec + " KEY-----", "");
            keyString = keyString.replaceAll("\\s+", "");

            return keyGenerator.apply(KeyFactory.getInstance("RSA"), keySpec.apply(keyString));
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Данный метод устанавливает спецификацию
     * приватного ключа
     * @param data
     * @return
     */
    private EncodedKeySpec privateKeySpec(String data) {
        return new PKCS8EncodedKeySpec(base64Util.decode(data));
    }

    /**
     * Данный метод генерирует приватный ключ
     * @param kf
     * @param spec
     * @return
     */
    private PrivateKey privateKeyGenerator(KeyFactory kf, EncodedKeySpec spec) {
        try {
            return kf.generatePrivate(spec);
        } catch(InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Данный метод устанавливает спецификацию публичного ключа
     * @param data
     * @return
     */
    private EncodedKeySpec publicKeySpec(String data) {
        return new X509EncodedKeySpec(base64Util.decode(data));
    }

    /**
     * Данный метод геренирует публичный ключ
     * @param kf
     * @param spec
     * @return
     */
    private PublicKey publicKeyGenerator(KeyFactory kf, EncodedKeySpec spec) {
        try {
            return kf.generatePublic(spec);
        } catch(InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
}
