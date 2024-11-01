package kz.symtech.antifraud.models.utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

public class HashPasswordUtils {
    private static final Random RANDOM = new SecureRandom();
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 512;
    private static final String PASSWORD_REGEX = "[A-Za-z0-9]";

    private static byte[] getNextSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return salt;
    }

    private static byte[] generateSalt() {
        byte[] byteSalt = getNextSalt();
        byte[] desalt = Base64.getEncoder().encode(byteSalt);
        String saltString = new String(desalt);
        return saltString.getBytes();
    }

    public static String getSalt() {
        return new String(generateSalt());
    }

    public static String generateRandomKey() {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        byte[] hashBytes = messageDigest.digest(String.valueOf(RANDOM.nextInt()).getBytes());

        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : hashBytes) {
            stringBuilder.append(String.format("%02x", b));
        }
        return stringBuilder.toString();
    }

    public static String generatePassword(int length) {
        return RANDOM.ints(length, 'A', 'z' + 1)
                .mapToObj(c -> (char) c)
                .filter(c -> Character.toString(c).matches(PASSWORD_REGEX))
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }


    private static byte[] hash(char[] password, byte[] salt) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        Arrays.fill(password, Character.MIN_VALUE);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Error while hashing a password.");
        } finally {
            spec.clearPassword();
        }
    }

    public static String hashPass(String password, String salt) {
        byte[] saltByte = salt.getBytes();
        char[] chPass = password.toCharArray();
        byte[] fistHash = hash(chPass, saltByte);
        byte[] baseHash = Base64.getEncoder().encode(fistHash);

        return new String(baseHash);
    }

}
