package fr.utt.if26.strangersPhonegap.outils;

import android.util.Base64;
import fr.utt.if26.strangersPhonegap.Strangers;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/**
 * Outils de cryptage/décryptage symétrique avec clé
 *
 * @author JALOUZET Jérémie / OUADGHIRI Mohammed
 */
public class Cryptage {

    private static final String KEYSTORE_PASSWORD = "dfh10ZR49";
    private static final String PASSWORD = "7e3Gz0167";
    private static final byte[] SALT = {
        (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,
        (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,};

    /*
     * Toujours des problèmes avec le keystore, et AES. Cette méthode n'est donc pas utilisée pour le moment.
     */
    private static Key getKey_keystore() throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException, CertificateException, IOException {
        KeyStore ks = KeyStore.getInstance("BKS");
        ks.load(Strangers.getKeyStore(), KEYSTORE_PASSWORD.toCharArray());
        return ks.getKey("strangers", PASSWORD.toCharArray());
    }

    private static Key getKey() throws InvalidKeySpecException, NoSuchAlgorithmException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        return keyFactory.generateSecret(new PBEKeySpec(PASSWORD.toCharArray()));
    }

    public static String crypter(String property) throws GeneralSecurityException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException, CertificateException, IOException {
        Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
        cipher.init(Cipher.ENCRYPT_MODE, getKey(), new PBEParameterSpec(SALT, 20));
        return base64Encode(cipher.doFinal(property.getBytes()));
    }

    public static String decrypter(String property) throws GeneralSecurityException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException, CertificateException, IOException {
        Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
        cipher.init(Cipher.DECRYPT_MODE, getKey(), new PBEParameterSpec(SALT, 20));
        return new String(cipher.doFinal(base64Decode(property)));
    }

    private static String base64Encode(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private static byte[] base64Decode(String property) throws IOException {
        return Base64.decode(property, Base64.DEFAULT);
    }
}
