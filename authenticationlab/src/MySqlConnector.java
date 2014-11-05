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
	public long getSalt(String username) {
		
		long salt = 0;
		try {
			CallableStatement cStmt = conn.prepareCall("{? = call get_salt(?)}");
			cStmt.registerOutParameter(1,java.sql.Types.BIGINT);
		
			cStmt.setString(2, username);
			cStmt.execute();
			salt = cStmt.getLong(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return salt;
	}

	@Override
	public boolean checkHash(String username, String MessageHash,
			String FullHash) {
		boolean authenticated = false;
		try {
			CallableStatement cStmt = conn.prepareCall("{? = call authenticate_message(?,?,?)}");
			cStmt.registerOutParameter(1,java.sql.Types.BIGINT);
		
			cStmt.setString(2, username);
			cStmt.setString(3, MessageHash);
			cStmt.setString(4, FullHash);
			cStmt.execute();
			authenticated = cStmt.getBoolean(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return authenticated;
	}

}
