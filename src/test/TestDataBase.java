package test;

import org.junit.Test;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import main.DBConnection;

public class TestDataBase {
	
	private static final String TEST_USER = "TestSelectUser";
	private static final String TEST_EMAIL = "TestSelectEmail";
	private static final String TEST_WEBPAGE = "TestSelectWebpage";
	private static final String TEST_SUMMARY = "TestSelectSummary";
	private static final String TEST_COMMENT = "TestSelectComment";
	
	
	@Test
	public void testConnection(){
		
		boolean result = false;
		DBConnection connector = new DBConnection("dbtest2");
		
		try{
			result = connector.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			connector.close();
		}
	Assert.assertEquals(true, result);
	}

	
	@Test
	public void testInsert(){
		
		// boolean result = false;
		DBConnection connector = new DBConnection("dbtest2");
		int id = -1;
		try {
			connector.connect();
			id= connector.insert("TestUser", "TestEmail", "TestWebpage","TestSummary", "TestComments");
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			connector.close();
		}
		
		Assert.assertEquals(true, id>-1);						
	}
	
	@Test
	public void testSelect(){
		DBConnection connector = new DBConnection("dbtest2");
		int id = -1;
		ArrayList<Map> arrayResult =null;
		
		try{
			connector.connect();
			id= connector.insert(
					TEST_USER,
					TEST_EMAIL,
					TEST_WEBPAGE,
					TEST_SUMMARY,
					TEST_COMMENT);
						
			arrayResult = connector.select(id);
			
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			connector.close();		
		}
		
		Assert.assertNotNull(arrayResult);
		Assert.assertEquals(1, arrayResult.size());
		
		@SuppressWarnings("unchecked")
		HashMap<String, String> hashMap =  (HashMap<String, String>) arrayResult.get(0); 

		Assert.assertEquals(TEST_USER, hashMap.get("user")); 
		Assert.assertEquals(TEST_EMAIL, hashMap.get("email")); 
		Assert.assertEquals(TEST_WEBPAGE, hashMap.get("webpage")); 
		Assert.assertEquals(TEST_SUMMARY, hashMap.get("summary")); 
		Assert.assertEquals(TEST_COMMENT, hashMap.get("comments"));

	    DBConnection.writeResultSet(hashMap);
	}
	
	
	
	
	
	
	
	
	
	
}
