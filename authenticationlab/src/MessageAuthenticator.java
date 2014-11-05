
public interface MessageAuthenticator {

	boolean checkHash(String username, String MessageHash, String FullHash);
}
