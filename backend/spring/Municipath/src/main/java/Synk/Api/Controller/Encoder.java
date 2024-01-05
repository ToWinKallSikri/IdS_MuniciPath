package Synk.Api.Controller;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class Encoder {
    private static final String AES = "AES";
    private final SecretKeySpec key;

    public Encoder() {
        final byte[] decodedPwd = Base64.getDecoder().decode("PBKDF2PBKDF2PBKDF2PBKD");
        this.key = new SecretKeySpec(decodedPwd, AES);
    }

    public String encode(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public String decode(String encryptedText) throws Exception {
        byte[] decoded = Base64.getDecoder().decode(encryptedText);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted);
    }
}