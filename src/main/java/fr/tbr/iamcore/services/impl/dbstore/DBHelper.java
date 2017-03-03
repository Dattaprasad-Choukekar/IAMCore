package fr.tbr.iamcore.services.impl.dbstore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import fr.tbr.iamcore.services.exception.IMCoreException;
/**
 * This class provides methods to manage database operations.
 * @author Dattaprasad
 *
 */
public class DBHelper {
	private final Logger logger = Logger.getLogger(DBHelper.class.getName());

	private String driver = "org.apache.derby.jdbc.ClientDriver";
	private String connectionURL = "jdbc:derby://localhost:1527/imcoredb";
	private String identityDb = "Identities";
	private String identityTb = "IDENTITY_TB";

	/**
	 * 
	 * @param driver driver name 
	 * @param connectionURL connection URL
	 * @param tableName Name of table
	 * @throws IMCoreException
	 */
	public DBHelper(String driver, String connectionURL, String tableName)
			throws IMCoreException {
		try {
			Class.forName(driver);
		} catch (java.lang.ClassNotFoundException e) {
			logger.log(Level.SEVERE, "Unable to load database driver", e);
			throw new IMCoreException("Unable to load database driver", e);
		}
		this.connectionURL = connectionURL;
		this.identityTb = tableName;
	}

	/**
	 * Returns a jdbc connection connection.
	 * @return
	 * @throws SQLException
	 */
	public Connection getDbConnection() throws SQLException {
		return DriverManager.getConnection(connectionURL);
	}

	/**
	 * Drops the identity table and create a new one
	 * @return
	 * @throws SQLException
	 */
	public Connection createTable() throws SQLException {

		Connection conn = null;
		try {
			conn = getDbConnection();
			Statement stmnt = conn.createStatement();
			try {
				stmnt.execute("drop table " + identityTb);
				stmnt.close();
			} catch (SQLException e) {
				// May be table does not exist hence ignore
			}
			stmnt = conn.createStatement();
			stmnt.execute("CREATE TABLE "
					+ identityTb
					+ " ( ID INT NOT NULL GENERATED ALWAYS AS IDENTITY, FIRSTNAME VARCHAR(70), LASTNAME VARCHAR(80), "
					+ "EMAIL VARCHAR(100),BIRTHDATE DATE)");
			stmnt.close();

			conn.commit();

		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Unable to create table " + identityTb, e);
			throw e;
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}
		}
		return DriverManager.getConnection(connectionURL);
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getConnectionURL() {
		return connectionURL;
	}

	public void setConnectionURL(String connectionURL) {
		this.connectionURL = connectionURL;
	}

	public String getIdentityDb() {
		return identityDb;
	}

	public void setIdentityDb(String identityDb) {
		this.identityDb = identityDb;
	}

	public String getIdentityTb() {
		return identityTb;
	}

	public void setIdentityTb(String identityTb) {
		this.identityTb = identityTb;
	}

}
