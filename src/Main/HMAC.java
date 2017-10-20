package Main;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/*
 * http://tech-gym.com/2012/11/android/1055.html
 */

public class HMAC {
	public static final String ALGO = "hmacSHA256";
	public static String get(String data, String key) {
        try {
                SecretKeySpec sk = new SecretKeySpec(key.getBytes(), ALGO);
                Mac mac = Mac.getInstance(ALGO);
                mac.init(sk);

                byte[] mac_bytes = mac.doFinal(data.getBytes());

                StringBuilder sb = new StringBuilder(2 * mac_bytes.length);
                for(byte b: mac_bytes) {
                        sb.append(String.format("%02x", b&0xff) );
                }
                return String.valueOf(sb);
		} catch (Exception e) {}
		return "";
	}
	// public static String get(String str, String secret) {
	// String algo = "HmacSHA256";
	// try {
	// SecretKeySpec sk = new SecretKeySpec(secret.getBytes(), algo);
	// Mac mac = Mac.getInstance(algo);
	// mac.init(sk);
	//
	// byte[] mac_bytes = mac.doFinal(str.getBytes());
	//
	// StringBuilder sb = new StringBuilder(2 * mac_bytes.length);
	// for(byte b: mac_bytes) {
	// sb.append(String.format("%02x", b&0xff) );
	// }
	// return String.valueOf(sb);
	// } catch (Exception e) {}
	// return "";
	// }
}