package fr.tbr.iamcore.services.impl.dbstore;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import fr.tbr.iamcore.datamodel.Identity;
import fr.tbr.iamcore.services.IdentityDAO;
import fr.tbr.iamcore.services.exception.IMCoreException;
import fr.tbr.iamcore.services.util.IdentityUtility;
import fr.tbr.iamcore.tests.services.impl.dbstore.DBHelperTest;

public class IdentityDaoDbStoreImpl implements IdentityDAO {

	private final Logger logger = Logger
			.getLogger(DBHelperTest.class.getName());

	@Autowired
	private DBHelper dbHelper;

	private static final String INSERT_IDENTITY = "INSERT INTO IDENTITY_TB (FIRSTNAME, LASTNAME, EMAIL, BIRTHDATE) VALUES(?, ?, ?, ?)";
	private static final String SELECT_ALL_IDENTITIES = "SELECT * FROM IDENTITY_TB";
	private static final String SELECT_IDENTITITY_BY_ID = "SELECT * FROM IDENTITY_TB WHERE ID = ?";
	private static final String SEARCH_IDENTITIES = "SELECT * FROM IDENTITY_TB WHERE ";
	private static final String UPDATE_IDENTITY = "UPDATE IDENTITY_TB SET FIRSTNAME= ?, LASTNAME = ?, EMAIL = ?, BIRTHDATE = ? WHERE ID=?";
	private static final String DELETE_IDENTITY = "DELETE FROM IDENTITY_TB WHERE ID=?";
	private static final String FIRST_NAME = "FIRSTNAME";
	private static final String LAST_NAME = "LASTNAME";
	private static final String EMAIL = "EMAIL";
	private static final String BIRTH_DATE = "BIRTHDATE";

	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 
	 * @param dbConfigFilePath
	 *            Path of db config properties file
	 * @throws IMCoreException
	 */
	public IdentityDaoDbStoreImpl(String dbConfigFilePath)
			throws IMCoreException {
		this(new File(dbConfigFilePath), false);
	}

	/**
	 * 
	 * @param dbConfigFile
	 *            db configuration property file
	 * @param isTruncateOrCreateTable
	 *            if true deletes and creates the IDENTITY table in DB.
	 * @throws IMCoreException
	 */
	public IdentityDaoDbStoreImpl(File dbConfigFile,
			boolean isTruncateOrCreateTable) throws IMCoreException {

		Properties props = new Properties();
		try {
			FileInputStream fis = new FileInputStream(dbConfigFile);
			props.load(fis);
		} catch (IOException e) {
			throw new IMCoreException(
					"Error while reading DB configuration file :"
							+ dbConfigFile.getAbsolutePath(), e);
		}
		String dbDriver = props.getProperty("dbDriver");
		String connectionURL = props.getProperty("connectionURL");
		String tableName = props.getProperty("tableName");
		if (IdentityUtility.checkNotEmpty(dbDriver)
				|| IdentityUtility.checkNotEmpty(connectionURL)
				|| IdentityUtility.checkNotEmpty(tableName)) {
			throw new IMCoreException("Invalid DB configuration : \n" + props);
		}
		dbHelper = new DBHelper(dbDriver, connectionURL, tableName);
		if (isTruncateOrCreateTable) {
			try {
				dbHelper.createTable();
			} catch (SQLException e) {
				throw new IMCoreException(
						"Error while trauncating or creating table", e);
			}
		}

	}

	/**
	 * 
	 * @param dbConfigFile
	 *            db configuration property file path
	 * @param isTruncateOrCreateTable
	 *            if true deletes and creates the IDENTITY table in DB.
	 * @throws IMCoreException
	 */
	public IdentityDaoDbStoreImpl(String dbConfigFilePath,
			boolean isTruncateOrCreateTable) throws IMCoreException {
		this(new File(dbConfigFilePath), isTruncateOrCreateTable);
	}

	/**
	 * Saves identity to database
	 */
	@Override
	public boolean save(Identity identity) throws IMCoreException {
		boolean isSaved = false;
		Connection conn = null;
		try {
			conn = dbHelper.getDbConnection();

			PreparedStatement stmt = conn.prepareStatement(INSERT_IDENTITY);
			stmt.setString(1, identity.getFirstName());
			stmt.setString(2, identity.getLastName());
			stmt.setString(3, identity.getEmail());
			java.sql.Date birthDate = identity.getBirthDate() != null ? new java.sql.Date(
					identity.getBirthDate().getTime()) : null;
			stmt.setDate(4, birthDate);
			stmt.executeUpdate();
			isSaved = true;

			stmt.close();

		} catch (SQLException e) {
			logger.log(Level.SEVERE,
					"Excption while saving Identity to database", e);
			throw new IMCoreException(
					"Excption while saving Identity to database", e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					throw new IMCoreException(
							"Excption while closing connection to database", e);
				}
			}
		}
		return isSaved;
	}

	/**
	 * Updates given identity to database
	 */
	@Override
	public boolean update(Identity identity) throws IMCoreException {
		boolean isUpdated = false;
		if (identity != null) {
			Connection conn = null;
			try {
				conn = dbHelper.getDbConnection();
				PreparedStatement stmt = conn.prepareStatement(UPDATE_IDENTITY);
				stmt.setString(1, identity.getFirstName());
				stmt.setString(2, identity.getLastName());
				stmt.setString(3, identity.getEmail());
				java.sql.Date birthDate = identity.getBirthDate() != null ? new java.sql.Date(
						identity.getBirthDate().getTime()) : null;
				stmt.setDate(4, birthDate);
				stmt.setInt(5, identity.getId());
				int result = stmt.executeUpdate();
				if (result == 1) {
					isUpdated = true;
				}
				stmt.close();
			} catch (SQLException e) {
				logger.log(Level.SEVERE,
						"Excption while updating Identity to database", e);
				throw new IMCoreException(
						"Excption while updating Identity to database", e);
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						throw new IMCoreException(
								"Excption while closing connection to database",
								e);
					}
				}
			}
		}
		return isUpdated;

	}

	/**
	 * Deletes identity from datatabse
	 */
	@Override
	public boolean delete(Identity identity) throws IMCoreException {
		boolean isDeleted = false;
		if (identity != null) {
			Connection conn = null;
			try {
				conn = dbHelper.getDbConnection();
				PreparedStatement stmt = conn.prepareStatement(DELETE_IDENTITY);
				stmt.setInt(1, identity.getId());
				int result = stmt.executeUpdate();
				if (result == 1) {
					isDeleted = true;
				}
				stmt.close();
			} catch (SQLException e) {
				logger.log(Level.SEVERE,
						"Excption while deleting Identity from database", e);
				throw new IMCoreException(
						"Excption while deleting Identity from database", e);
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						throw new IMCoreException(
								"Excption while closing connection to database",
								e);
					}
				}
			}
		}
		return isDeleted;

	}

/**
 * Search the paramaters specified in identityToSearch object and returns the list of identies.
 */
	@Override
	public List<Identity> search(Identity identityToSearch)
			throws IMCoreException {
		List<Identity> identities = new ArrayList<Identity>();
		if (identityToSearch != null) {
			StringBuffer searchIdentitiesSql = new StringBuffer(
					SEARCH_IDENTITIES);
			boolean isAppendAnd = false;
			if (identityToSearch.getFirstName() != null
					&& !identityToSearch.getFirstName().isEmpty()) {
				searchIdentitiesSql.append(FIRST_NAME);
				searchIdentitiesSql.append("=");
				searchIdentitiesSql.append("'");
				searchIdentitiesSql.append(identityToSearch.getFirstName());
				searchIdentitiesSql.append("'");
				isAppendAnd = true;
			}
			if (identityToSearch.getLastName() != null
					&& !identityToSearch.getLastName().isEmpty()) {
				if (isAppendAnd) {
					searchIdentitiesSql.append(" and ");
				}
				searchIdentitiesSql.append(LAST_NAME);
				searchIdentitiesSql.append("=");
				searchIdentitiesSql.append("'");
				searchIdentitiesSql.append(identityToSearch.getLastName());
				searchIdentitiesSql.append("'");
				isAppendAnd = true;
			}
			if (identityToSearch.getEmail() != null
					&& !identityToSearch.getEmail().isEmpty()) {
				if (isAppendAnd) {
					searchIdentitiesSql.append(" and ");
				}
				searchIdentitiesSql.append(EMAIL);
				searchIdentitiesSql.append("=");
				searchIdentitiesSql.append("'");
				searchIdentitiesSql.append(identityToSearch.getEmail());
				searchIdentitiesSql.append("'");
				isAppendAnd = true;
			}

			if (identityToSearch.getBirthDate() != null) {
				if (isAppendAnd) {
					searchIdentitiesSql.append(" and ");
				}
				searchIdentitiesSql.append(BIRTH_DATE);
				searchIdentitiesSql.append("=");
				searchIdentitiesSql.append("'");
				searchIdentitiesSql.append(dateFormat.format(identityToSearch
						.getBirthDate()));
				searchIdentitiesSql.append("'");
				isAppendAnd = true;
			}

			// Check if no critereia is added to where cluase, return all
			// results
			if (SEARCH_IDENTITIES.equals(searchIdentitiesSql.toString())) {
				searchIdentitiesSql.setLength(0);
				searchIdentitiesSql.append(SELECT_ALL_IDENTITIES);
			}

			Connection conn = null;
			try {
				conn = dbHelper.getDbConnection();

				Statement stmt = conn.createStatement();
				ResultSet resultSet = stmt.executeQuery(searchIdentitiesSql
						.toString());
				while (resultSet.next()) {
					Identity identity = deserializeIdentity(resultSet);
					identities.add(identity);
				}
				resultSet.close();
				stmt.close();
				conn.close();

			} catch (SQLException e) {
				logger.log(Level.SEVERE,
						"Excption while retriving Identities from database", e);
				throw new IMCoreException(
						"Excption while retriving Identities from database", e);
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						throw new IMCoreException(
								"Excption while closing connection to database",
								e);
					}
				}
			}
		}
		return identities;
	}

	/**
	 * Returns all the identities from database.
	 */
	@Override
	public List<Identity> getAllEntities() throws IMCoreException {
		List<Identity> identities = new ArrayList<Identity>();
		Connection conn = null;
		try {
			conn = dbHelper.getDbConnection();
			Statement stmt = conn.createStatement();
			ResultSet resultSet = stmt.executeQuery(SELECT_ALL_IDENTITIES);
			while (resultSet.next()) {
				Identity identity = deserializeIdentity(resultSet);
				identities.add(identity);
			}
			resultSet.close();
			stmt.close();
			conn.close();

		} catch (SQLException e) {
			logger.log(Level.SEVERE,
					"Excption while retriving Identities from database", e);
			throw new IMCoreException(
					"Excption while retriving Identities from database", e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					throw new IMCoreException(
							"Excption while closing connection to database", e);
				}
			}
		}
		return identities;
	}

	private Identity deserializeIdentity(ResultSet resultSet)
			throws SQLException {
		Identity identity = new Identity();
		identity.setId(resultSet.getInt(1));
		identity.setFirstName(resultSet.getString(2));
		identity.setLastName(resultSet.getString(3));
		identity.setEmail(resultSet.getString(4));
		java.sql.Date birthDate = resultSet.getDate(5);
		if (birthDate != null) {
			identity.setBirthDate(new Date(birthDate.getTime()));
		}
		return identity;
	}

	/**
	 * Gets identity from database with specified id.
	 */
	@Override
	public Identity get(int id) throws IMCoreException {
		Identity identity = null;
		Connection conn = null;
		try {
			conn = dbHelper.getDbConnection();

			PreparedStatement stmt = conn
					.prepareStatement(SELECT_IDENTITITY_BY_ID);
			stmt.setInt(1, id);
			ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				identity = deserializeIdentity(resultSet);
			} else {
				logger.log(Level.WARNING, "Identity with id not found. Id: "
						+ id);
			}

			resultSet.close();
			stmt.close();

		} catch (SQLException e) {
			logger.log(Level.SEVERE,
					"Excption while saving Identity to database", e);
			throw new IMCoreException(
					"Excption while saving Identity to database", e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					throw new IMCoreException(
							"Excption while closing connection to database", e);
				}
			}
		}
		return identity;

	}
}
