package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;



public class DBConnection {
	
	private static final String DB_TABLE = "comments";
	private final String name;
	private Connection connect;
	private Statement statement;
	private ResultSet resultSet;
	private PreparedStatement preparedStatement;
	
	
	public DBConnection (String name){
		this.name=name;
	}
		
	
	/**
	 * Abre la conexion a la base de datos
	 * @return 
	 * @throws Exception
	 */
	public boolean connect() throws Exception {
		try {
			//Cargar el driver MYSQL
			Class.forName("com.mysql.jdbc.Driver");
			// jdbc:mysql://ip database // database ?
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/"+ name +"?"
							+ "user=root&password=");
			// Statements allow to issue SQL queries to the Database
			statement= connect.createStatement();
		}catch (Exception e){
			close();
			throw e;
		}
		return true;
	}

	@SuppressWarnings("rawtypes")
	public ArrayList<Map> select(int id) throws SQLException{
		String selectSQL = "SELECT id, myuser, email, webpage, datum, summary, comments FROM "+DB_TABLE+" WHERE id = ?";
		ArrayList<Map> map = null;
		
		try{
			preparedStatement = connect
						.prepareStatement(selectSQL);
			preparedStatement.setInt(1, id);   // el primer ? se cambia por id
			resultSet = preparedStatement.executeQuery();
			map= resultSetToCollection(resultSet);
		} catch (SQLException e){
			close();
			throw e;
		}
		return map;
	}
	
	public void deleteAll() throws SQLException{
		try{
			preparedStatement = connect
					.prepareStatement("trucate"+DB_TABLE);
			preparedStatement.executeUpdate();
		}catch (SQLException e){
			close();
			throw e;
		}
	}
	
	
	public static ArrayList<Map> resultSetToCollection(ResultSet resultSet) throws SQLException {
		
		ArrayList<Map> list = new ArrayList<Map> ();
		
		while (resultSet.next()){
			
			int id = resultSet.getInt("id");
			String user = resultSet.getString("myuser");
			String email = resultSet.getString("email");
			String website = resultSet.getString("webpage");
			String summary = resultSet.getString("summary");
			Date date = resultSet.getDate("datum");
			String comment = resultSet.getString("comments");
			
			HashMap<String,String> hashMap = new HashMap<String,String>();
			hashMap.put("id", String.valueOf(id));
			hashMap.put("user", user);
			hashMap.put("email", email);
			hashMap.put("webpage", website);
			hashMap.put("summary", summary);
			hashMap.put("date", date.toString());
			hashMap.put("comments", comment);	
			
			list.add(hashMap);
		}
		return list;
	}
	
	/* 
	public void delete (int id) throws SQLException{
		try {
			preparedStatement = connect
					.prepareStatement(delete from "+DB_TABLE+" where id= ?");
			preparedStatement.setInt(1,  id);
					
		}
		
	}
	
	*/
	
	
	// you need to close the resultSet
		
	public void close() {
		try{
			if (resultSet !=null){
				resultSet.close();
				resultSet=null;
			}
			
			if (statement !=null){
				statement.close();
				statement=null;
			}
			
			if (connect !=null){
				connect.close();
				connect=null;
			}
		}catch (Exception e){
		
		}
		
	}

	/**
	 * 		INSERT INTO tabla valores (default, ?, ?, ?, ?, ?, ?)
	 * 
	 * @param user
	 * @param email
	 * @param summary
	 * @param Comment
	 * @throws SQLException 
	 */
	
	
	public int insert ( String user, String email, String webpage ,String summary,String comment) throws SQLException {
		int lastInsertedId = -1;
		String strSQL = "insert into "+ DB_TABLE +" values (default, ?, ?, ?, ?, ?, ?)";
		
		try{
			// Statement.RETURN_GENERATED_KEYS   = retorna el ID del añadido
			preparedStatement = connect
					 	.prepareStatement(strSQL, Statement.RETURN_GENERATED_KEYS);
		
			preparedStatement.setString		(1, user);
			preparedStatement.setString		(2, email);
			preparedStatement.setString		(3, webpage);
			preparedStatement.setDate		(4, new java.sql.Date(System.currentTimeMillis()) );
			preparedStatement.setString		(5, summary);
			preparedStatement.setString		(6, comment);
			
			preparedStatement.executeUpdate();
			ResultSet rs = preparedStatement.getGeneratedKeys();
			
			if (rs.next())
				lastInsertedId = rs.getInt(1);
				
		}catch (SQLException e){
			close();
			throw e;
		}
		
		return lastInsertedId;
	}
		
	public void update(){
	}
	
	public static void writeResultSet(HashMap<String,String> hashMap ){
        System.out.print("\tid: " 		+ hashMap.get("id"));
        System.out.print("\tUser: " 	+ hashMap.get("user"));
        System.out.print("\tWebsite: " 	+ hashMap.get("webpage"));
        System.out.print("\tsummary: " 	+ hashMap.get("summary"));
        System.out.print("\tDate: " 	+ hashMap.get("date"));
        System.out.print("\tComment: " 	+ hashMap.get("comments"));
        System.out.print("\t\n");
    }	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
	


