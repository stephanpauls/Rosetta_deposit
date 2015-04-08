package com.exlibris.dps.sdk.dgtconv;
import java.sql.*;
import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

public class TestDBOracle {

	private Connection conn = null;
    private final String url = "jdbc:oracle:thin:@aleph08.libis.kuleuven.be:1521:dtl3";
	private static Logger logger = Logger.getLogger(TestDBOracle.class);
	
	private String clob="";
	private String label="";
	private String pA="";
	private String pB="";
	private String pC="";
	private String ingest="";
	private String status ="EMPTY";
	
	  public TestDBOracle()
	  {
		  try{
		    Class.forName("oracle.jdbc.driver.OracleDriver");
		  } catch (ClassNotFoundException e) {
			  logger.debug(e.getMessage());
		  }

		  
		  try {
		    conn = DriverManager.getConnection(url,FlowExample.prop.getProperty("DB_USER_ID"),FlowExample.prop.getProperty("DB_PASSWORD"));
		    conn.setAutoCommit(false);
		  } catch (SQLException e){
			  logger.debug(e.getMessage());
		  }
	  }
	  
	  public void getConnection()
	  {
/*		  
		  closeConn();
		  try{
		    Class.forName("oracle.jdbc.driver.OracleDriver");
		  } catch (ClassNotFoundException e) {
			  logger.debug(e.getMessage());
		  }
*/
		  
		  try {
			if (conn == null) { 
				conn = DriverManager.getConnection(url,"D31_rep00","D31_rep00");
				conn.setAutoCommit(false);
			}
		  } catch (SQLException e){
			  logger.debug(e.getMessage());
		  }
	  }

	public String getPIDOfFile(String filename,String pid) {
	  
		  Statement stmt = null;
		  ResultSet rset = null;
		  String filePid = "";
		  try{
			getConnection();
		    stmt = conn.createStatement();
		String query =  "select c.pid from hdestreamref strref inner join hdecontrol c on c.id = strref.id where c.id in (select distinct r.targetcontrol from hdecontrol c inner join hderelation r on r.control = c.id  where c.pid ='"+pid+"') and  strref.filename ='"+filename+"'";
	    rset = stmt.executeQuery(query);
	    while (rset.next()) {
	    	filePid = rset.getString(1);
		    break;
	    }
	    rset.close();
	    stmt.close();
	    
	  } catch (SQLException e){
		  logger.debug(e.getMessage());
		  closeConn();
	  }
	  return filePid;

	}
  public String getFilename(String pid) {
	  
	  Statement stmt = null;
	  ResultSet rset = null;
	  String fileName = "";
	  try{
		getConnection();
	    stmt = conn.createStatement();
	    
	    String query =  "select strref.filename from hdestreamref strref inner join hdecontrol c on c.id = strref.id where c.pid ='"+pid+"'";
	    rset = stmt.executeQuery(query);
	    while (rset.next()) {
	    	fileName = rset.getString(1);
		    break;
	    }
	    rset.close();
	    stmt.close();
	    
	  } catch (SQLException e){
		  logger.debug(e.getMessage());
		  closeConn();
	  }
	  if (fileName == null || fileName == "") return fileName;
	  Integer lastSlashInd = fileName.lastIndexOf('/');
	  if (lastSlashInd != -1) {
		  fileName = fileName.substring(lastSlashInd+1);
	  }
      logger.debug(fileName);
      return fileName;

  }
  
  String getFilepath(String pid) {
	  Statement stmt = null;
	  ResultSet rset = null;
	  String filePath = "";
	  try{
		getConnection();
	    stmt = conn.createStatement();
	    
	    String query =  "select st.root || strref.internalpath from hdestreamref strref inner join hdecontrol c on c.id = strref.id inner join hrestorage st on st.appid = strref.storageid where c.pid = '"+pid+"'";
	    rset = stmt.executeQuery(query);
	    while (rset.next()) {
	    	filePath = rset.getString(1);
		    break;
	    }
	    rset.close();
	    stmt.close();
	    
	  } catch (SQLException e){
		  logger.debug(e.getMessage());
	  }
      logger.debug(filePath);
      return filePath;	  
  }
  
 public String getAccessRights(String pid) {
	  Statement stmt = null;
	  ResultSet rset = null;
	  clob = "";
	  try{
		getConnection();
	    stmt = conn.createStatement();
	    
	    String query =  "select m.value from hdecontrol c " +
	    		"inner join  hdepidmid pm on c.pid = pm.pid inner join hdemetadata m on m.mid = pm.mid and m.mdid = 15  " +
	    		"where c.pid = '"+pid+"'";	    
	    
	    rset = stmt.executeQuery(query);
	    while (rset.next()) {
	    	clob = rset.getString(1);
		    break;
	    }
	    rset.close();
	    stmt.close();
	    
	  } catch (SQLException e){
		  logger.debug(e.getMessage());
	  }
      return clob;	  
 }
  
 public String getDublinCore(String pid) {
	  Statement stmt = null;
	  ResultSet rset = null;
	  clob = "";
	  try{
		getConnection();
	    stmt = conn.createStatement();
	    
	    String query =  "select m.value from hdecontrol c " +
	    		"inner join  hdepidmid pm on c.pid = pm.pid inner join hdemetadata m on m.mid = pm.mid and m.mdid = 10  " +
	    		"where c.pid = '"+pid+"'";	    
	    
	    rset = stmt.executeQuery(query);
	    while (rset.next()) {
	    	clob = rset.getString(1);
		    break;
	    }
	    rset.close();
	    stmt.close();
	    
	  } catch (SQLException e){
		  logger.debug(e.getMessage());
	  }
     return clob;	  
}
  
  public void getFileParameters(String pid) {
	  Statement stmt = null;
	  ResultSet rset = null;
	  String filePath = "";
	  try{
		getConnection();
	    stmt = conn.createStatement();
	    
	    String query =  "select c.label,c.partitiona,c.partitionb,nvl(c.partitionc,' '),c.ingestid from hdecontrol c where c.pid = '"+pid+"'";	    
	    
	    rset = stmt.executeQuery(query);
	    while (rset.next()) {
	    	label = rset.getString(1);
	    	pA = rset.getString(2);
	    	pB = rset.getString(3);
	    	pC = rset.getString(4);
	    	ingest = rset.getString(5);
		    break;
	    }
	    rset.close();
	    stmt.close();
	    
	  } catch (SQLException e){
		  logger.debug(e.getMessage());
	  }
      logger.debug(filePath);
      return;	  
  }
  
  
  public boolean isChildObject(String pid){
	  Statement stmt = null;
	  ResultSet rset = null;
	  try{
		getConnection();
	    stmt = conn.createStatement();
	    String query =  "select distinct r.type from hdecontrol c inner join hderelation r on r.targetcontrol = c.id where r.type = 2  and c.partitionb != 'DO_MIGRATIE_TEXTURA' and c.pid = '"+pid+"'";	    
	    
	    rset = stmt.executeQuery(query);
	    while (rset.next()) {
		    stmt.close();
		    rset.close();
		    return true;
	    }
	    rset.close();
	    stmt.close();
		return false;
	  } catch (SQLException e){
		  logger.info(e.getMessage());
		  return true;
	  }
  }	  
  
  public String getFilelabel(String pid) {
	  Statement stmt = null;
	  ResultSet rset = null;
	  String filePath = "";
	  try{
		getConnection();
	    stmt = conn.createStatement();
	    
	    
	    String query =  "select c.label from hdecontrol c where c.pid = '"+pid+"'";	    
//	    String query =  "select case when instr(label,'_') = 0 then label else trim(substr(label,1,(instr(label,'_'))+3)) end from hdecontrol where pid = '"+pid+"'";	    

	    rset = stmt.executeQuery(query);
	    while (rset.next()) {
	    	filePath = rset.getString(1);
		    break;
	    }
	    rset.close();
	    stmt.close();
	  } catch (SQLException e){
		  logger.debug(e.getMessage());
	  }
      logger.debug(filePath);
      return filePath;	  
  }
  
  public String getComplexElementThumbnail(String pid) {

	  Statement stmt = null;
	  ResultSet rset = null;
	  String pidThumbnail = "";
	  try{
		getConnection();
	    stmt = conn.createStatement();
	    
	    String query =  "select cc.pid from hderelation r inner join hdecontrol c on r.targetcontrol = c.id " +
	    		"inner join hdecontrol cc on r.control = cc.id " +
	    		"where c.pid = '"+pid+"' ";
	    rset = stmt.executeQuery(query);
	    while (rset.next()) {
	    	pidThumbnail = rset.getString(1);
		    break;
	    }
	    rset.close();
	    stmt.close();
	  } catch (SQLException e){
		  logger.debug(e.getMessage());
	  }
	  return pidThumbnail;
  }
  
  public Integer setDone (String columnName,String pid) {
	  Statement stmt = null;
	  int update = 0;
	  try{
		getConnection();
	    stmt = conn.createStatement();
	    
	    String query =  "update hdecontrol set "+columnName+ "= "+columnName+" || ' DONE' " +
	    		"where pid = '"+pid+"' ";
	    update = stmt.executeUpdate(query);
	    conn.commit();
	    stmt.close();
	  } catch (SQLException e){
		  logger.debug(e.getMessage());
		  try {
		  conn.rollback();
		  } catch (SQLException e2){
			  logger.debug(e2.getMessage());
		  }
	  }
	  return update;	  
  }
  
  public Integer setDone (String columnName,String pid,String sip) {
	  Statement stmt = null;
	  int update = 0;
	  try{
		getConnection();
	    stmt = conn.createStatement();
	    
	    String query =  "update hdecontrol set "+columnName+ "= "+columnName+" || ' DONE ' || " +sip +
	    		" where pid = '"+pid+"' ";
	    update = stmt.executeUpdate(query);
	    conn.commit();
	    stmt.close();
	  } catch (SQLException e){
		  logger.debug(e.getMessage());
		  try {
		  conn.rollback();
		  } catch (SQLException e2){
			  logger.debug(e2.getMessage());
		  }
	  }
	  return update;	  
  }
  public String getParentCollSip(String pid) {
	  
	  String sip= "0";
	  Statement stmt = null;
	  ResultSet rset = null;	  
	  try{
		getConnection();
	    stmt = conn.createStatement();
	    

	  String query =  "select SUBSTR(partitionb,instr(partitionb,'DONE ')+5) from hdecontrol ctl where ctl.id in " +
  "(select r.control from hderelation r inner join hdecontrol c on c.id = r.targetcontrol " + 
  " where c.pid = '"+pid+"' ) and (ctl.partitionb like 'DO_COLL_MIGRATIE_TEXTURA%' or ctl.partitionb like 'DO_COLL_DO_MIGRATIE_TEXTURA%')" ;
	    rset = stmt.executeQuery(query);
	    while (rset.next()) {
	    	 sip = rset.getString(1);
		    break;
	    }
	    rset.close();
	    stmt.close();
	  } catch (SQLException e){
		  logger.debug(e.getMessage());
	  }
	  return sip;
	  
}
  
public boolean IsDMDIDOnFileLevel(String pid,String dmdid) {
	  Integer nrPid= 0;
	  boolean fileLevelFlag = false;
	  Statement stmt = null;
	  ResultSet rset = null;	  
	  try{
		getConnection();
	    stmt = conn.createStatement();
	    String query =  "select count(distinct pm.pid) from hdecontrol c " +
	    		"inner join hderelation r on r.control = c.id " +
	    		"inner join hdecontrol ct on ct.id = r.targetcontrol " +
	    		"inner join hdepidmid pm on pm.pid = ct.pid " +
	    		"inner join hdemetadata m on m.mid = pm.mid " +
	    		"where c.pid = '"+pid+"' " +
	    		"and m.mdid = 10 " +
	    		"and m.mid = '"+dmdid+"'";
	    rset = stmt.executeQuery(query);
	    while (rset.next()) {
	    	nrPid = rset.getInt(1);
	    	fileLevelFlag = nrPid == 0  ? false : true;
		    break;
	    }
	    rset.close();
	    stmt.close();
	    
	  } catch (SQLException e){
		  logger.debug(e.getMessage());
	  }
	  return fileLevelFlag;
}
  
public void closeConn() {
	try{
		conn.close();
	} catch (SQLException e) {
		logger.debug(e.getMessage());
	}
}

public Connection getConn() {
	return conn;
}

public void setConn(Connection conn) {
	this.conn = conn;
}
 
public String getClob() {
	return clob;
}


public String getLabel() {
	return label;
}


public String getpA() {
	return pA;
}


public String getpB() {
	return pB;
}


public String getpC() {
	return pC;
}


public String getStatus() {
	return status;
}


public void setStatus(String status) {
	this.status = status;
}


public String getIngest() {
	return ingest;
}



}