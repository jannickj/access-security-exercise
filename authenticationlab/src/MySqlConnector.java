import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;


public class MySqlConnector implements MessageAuthenticator, SaltContainer {
	
	public MySqlConnector(Connection sqlConnection) {
		super();
		this.conn = sqlConnection;
	}

	private Connection conn;
	
	@Override
	public String getSalt(String username) {
		
		String salt = null;
		try {
			CallableStatement cStmt = conn.prepareCall("{? = call get_salt(?)}");
			cStmt.registerOutParameter(1,java.sql.Types.VARCHAR);
		
			cStmt.setString(2, username);
			cStmt.execute();
			salt = cStmt.getString(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return salt;
	}

	@Override
	public boolean checkHash(String username, long nonce, String MessageHash, String FullHash) {
		boolean authenticated = false;
		try {
			CallableStatement cStmt = conn.prepareCall("{? = call authenticate_message(?,?)}");
			cStmt.registerOutParameter(1,java.sql.Types.VARCHAR);

			cStmt.setString(2, username);
			cStmt.setString(3, MessageHash);
			cStmt.execute();
			String userHash = cStmt.getString(1);
			String noncedHash = AuthMsgHasher.generatedNoncedHash(nonce, userHash);
			authenticated = FullHash.equals(noncedHash);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return authenticated;
	}

}
