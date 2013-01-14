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
import javax.crypto.Cipher;
import javax.crypto.spec.PBEParameterSpec;

/**
 * Outils de cryptage/décryptage symétrique avec clé
 *
 * @author JALOUZET Jérémie / OUADGHIRI Mohammed
 */
public class Cryptage {

    private static final String KEYSTORE_PASSWORD = "dfh10ZR49";
    private static final String PASSWORD = "7e3Gz0167";
    private static final String SALT = "lzer43QEtg";

    private static Key getKey() throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException, CertificateException, IOException {
        KeyStore ks = KeyStore.getInstance("BKS");
        ks.load(Strangers.getKeyStore(), KEYSTORE_PASSWORD.toCharArray());
        return ks.getKey("strangers", PASSWORD.toCharArray());
    }

    public static String crypter(String property) throws GeneralSecurityException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException, CertificateException, IOException {
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
        pbeCipher.init(Cipher.ENCRYPT_MODE, getKey(), new PBEParameterSpec(base64Decode(SALT), 20));
        return base64Encode(pbeCipher.doFinal(property.getBytes()));
    }

    public static String decrypter(String property) throws GeneralSecurityException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException, CertificateException, IOException {
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
        pbeCipher.init(Cipher.DECRYPT_MODE, getKey(), new PBEParameterSpec(base64Decode(SALT), 20));
        return new String(pbeCipher.doFinal(base64Decode(property)));
    }

    private static String base64Encode(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private static byte[] base64Decode(String property) throws IOException {
        return Base64.decode(property, Base64.DEFAULT);
    }
}
