package fr.tbr.iamcore.tests.services.impl.dbstore;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.tbr.iamcore.services.exception.IMCoreException;
import fr.tbr.iamcore.services.impl.dbstore.DBHelper;
import fr.tbr.iamcore.services.util.IdentityUtility;

public class DBHelperTest {
	private final Logger logger = Logger
			.getLogger(DBHelperTest.class.getName());
	private static String dbDriver;
	private static String connectionURL;
	private static String tableName;

	public static void main(String[] args) {

	}

	@BeforeClass
	public static void setUp() {
		String dbConfigFilePath = "./config/inmemoryDbConfig.properties";
		Properties props = new Properties();
		try {
			FileInputStream fis = new FileInputStream(dbConfigFilePath);
			props.load(fis);
		} catch (IOException e) {
			throw new RuntimeException(
					"Error while reading DB configuration file :"
							+ dbConfigFilePath, e);
		}
		dbDriver = props.getProperty("dbDriver");
		connectionURL = props.getProperty("connectionURL");
		tableName = props.getProperty("tableName");
		if (IdentityUtility.checkNotEmpty(dbDriver)
				|| IdentityUtility.checkNotEmpty(connectionURL)
				|| IdentityUtility.checkNotEmpty(tableName)) {
			throw new RuntimeException("Invalid DB configuration : \n" + props);
		}
	}

	@Test
	public void initializeDBhelperTest() throws IMCoreException {
		DBHelper dbHelper = new DBHelper(dbDriver, connectionURL, tableName);
	}

	@Test
	public void getDbConnectionTest() throws SQLException, IMCoreException {
		DBHelper dbHelper = new DBHelper(dbDriver, connectionURL, tableName);
		Connection conn = null;
		try {
			conn = dbHelper.getDbConnection();
			Assert.assertNotNull("Unable to get DB connection", conn);
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Unable to get DB connection", e);
			throw e;
		}

	}

	@Test
	public void createTableTest() throws IMCoreException, SQLException {
		DBHelper dbHelper = new DBHelper(dbDriver, connectionURL, tableName);
		dbHelper.createTable();
	}
}
