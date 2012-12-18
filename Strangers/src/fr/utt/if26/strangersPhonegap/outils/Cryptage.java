package fr.utt.if26.strangersPhonegap.outils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Outils de cryptage/décryptage symétrique avec clé
 *
 * @author JALOUZET Jérémie / OUADGHIRI Mohammed
 */
public class Cryptage {

    private static byte[] key = null;
    private static final String key_clear = "0645312789";
    private final static String ALGORITHM_SECURERANDOM = "SHA1PRNG";
    private final static String ALGORITHM_CIPHER = "AES";

    private static byte[] getKey() throws NoSuchAlgorithmException {
        if (key == null) {
            key = generateKey();
        }
        return key;
    }

    private static byte[] generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM_CIPHER);
        SecureRandom secureRandom = SecureRandom.getInstance(ALGORITHM_SECURERANDOM);
        secureRandom.setSeed(key_clear.getBytes());
        keyGenerator.init(128, secureRandom);
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

    public static String crypter(String chaine) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(ALGORITHM_CIPHER);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(getKey(), ALGORITHM_CIPHER));
        return new String(cipher.doFinal(chaine.getBytes()));
    }

    public static String decrypter(String chaine) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(ALGORITHM_CIPHER);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(getKey(), ALGORITHM_CIPHER));
        return new String(cipher.doFinal(chaine.getBytes()));
    }
}
