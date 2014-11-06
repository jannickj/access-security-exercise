
public interface MessageAuthenticator {

	boolean checkHash(String username, long nonce, String MessageHash, String FullHash);
}
