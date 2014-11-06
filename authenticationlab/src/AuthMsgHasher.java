import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.commons.codec.digest.DigestUtils;


public class AuthMsgHasher {

	
	
	public static String generateMsgHash(int id, Serializable[] msg)
	{
		try 
		{
			ByteArrayOutputStream data = new ByteArrayOutputStream();
			ObjectOutputStream serializer = new ObjectOutputStream(data);
			serializer.writeLong(id);
			for(int i=0;i<msg.length;i++)
				serializer.writeObject(msg[i]);
			serializer.flush();
			return DigestUtils.sha512Hex(data.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static String generateUserHash(String username, String password, String salt, String oldHash)
	{
		String saltedPassword = DigestUtils.sha512Hex(password+salt);
		String userHash = DigestUtils.sha512Hex(oldHash+username+saltedPassword);	
		return userHash;
	}
	
	public static String generatedNoncedHash(long nonce, String hash)
	{
		return DigestUtils.sha512Hex(hash+nonce);
	}
	
	public static String generateFullHash(long nonce, int id, Serializable[] msg, String username, String password, String salt)
	{
		String msgHash = generateMsgHash(id, msg);
		String userHash = generateUserHash(username, password, salt, msgHash);
		String noncedHash = generatedNoncedHash(nonce, userHash);
		return noncedHash;
	}
}
