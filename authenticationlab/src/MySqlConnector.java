import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.ResultSetMetaData;


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
	public String signMessage(String username, String MessageHash) {
		String signature = null;
		try {
			CallableStatement cStmt = conn.prepareCall("{? = call authenticate_message(?,?)}");
			cStmt.registerOutParameter(1,java.sql.Types.VARCHAR);

			cStmt.setString(2, username);
			cStmt.setString(3, MessageHash);
			cStmt.execute();
			signature = cStmt.getString(1);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return signature;
	}
	
	public AccessControlContainer generateAccessControlFromList() throws SQLException
	{
		CallableStatement cStmt = conn.prepareCall("{call get_useraccess()}");
		HashMap<String, Long> data = new HashMap<String, Long>();
		if (!cStmt.execute())
			return new PrinterAccessControl(data);
		
		java.sql.ResultSet res = cStmt.getResultSet();
		while (res.next()) {
	        String username = res.getString("username");
	        long accesslevel = res.getLong("accesslevel");
	        data.put(username, accesslevel);
	      }
		return new PrinterAccessControl(data);
	}
	
	public AccessControlContainer generateAccessControlFromRoles() throws SQLException
	{		
		CallableStatement cStmt = conn.prepareCall("{call get_useraccess_roles()}");
		HashMap<String, Long> data = new HashMap<String, Long>();
		if (!cStmt.execute())
			return new PrinterAccessControl(data);
		
		HashMap<Integer, Long> rolePermissions = new HashMap<Integer, Long>();
		ArrayList<Pair<String,Integer>> users = new ArrayList<>();
		java.sql.ResultSet res = cStmt.getResultSet();
		while (res.next()) {
	        String username = res.getString("username");
	        int roleid = res.getInt("roleid");
	        long accesslevel = res.getLong("accesslevel");
	        int subaccessOf = res.getInt("subaccessOf");
		    addPermission(rolePermissions, roleid, accesslevel);
		    addPermission(rolePermissions, subaccessOf, accesslevel);
	        users.add(new Pair<String, Integer>(username, roleid));
	        
	        data.put(username, accesslevel);
	      }
		
		for(Pair<String, Integer> user : users)
			data.put(user.getVal1(), rolePermissions.get(user.getVal2()));
		return new PrinterAccessControl(data);
	}
	
	private void addPermission(HashMap<Integer, Long> perms, int id, long perm)
	{
		if (perms.containsKey(id))
		{
			long curPerm = perms.get(id);
			perms.remove(id);
			perms.put(id, curPerm | perm);
		}
		else
			perms.put(id, perm);
	}
	
}
