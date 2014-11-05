import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.apache.commons.codec.digest.DigestUtils;


public class AuthMsgHasher {

	
	
	public static String generateMsgHash(int id, long nonce, Serializable[] msg)
	{
		try 
		{
			ByteArrayOutputStream data = new ByteArrayOutputStream();
			ObjectOutputStream serializer = new ObjectOutputStream(data);
			serializer.writeLong(id);
			serializer.writeLong(nonce);
			for(int i=0;i<msg.length;i++)
				serializer.writeObject(msg[i]);
			serializer.flush();
			return DigestUtils.sha512Hex(data.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static String generateUserHash(String username, String password, long salt, String oldHash)
	{
		String saltedPassword = DigestUtils.sha512Hex(password+Long.toString(salt));
		return DigestUtils.sha512Hex(oldHash+username+saltedPassword);		
	}
	
}
