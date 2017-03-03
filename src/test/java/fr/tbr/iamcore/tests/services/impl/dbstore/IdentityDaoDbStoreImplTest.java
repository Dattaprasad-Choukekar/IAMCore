package fr.tbr.iamcore.tests.services.impl.dbstore;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.tbr.iamcore.datamodel.Identity;
import fr.tbr.iamcore.services.IdentityDAO;
import fr.tbr.iamcore.services.exception.IMCoreException;
import fr.tbr.iamcore.services.impl.dbstore.DBHelper;
import fr.tbr.iamcore.services.impl.dbstore.IdentityDaoDbStoreImpl;
import fr.tbr.iamcore.services.util.IdentityUtility;

public class IdentityDaoDbStoreImplTest {

	private IdentityDAO identityDAO;
	
	public static final String DB_CONFIG_PATH = "./config/inmemoryDbConfig.properties";

	public static DBHelper dbHelper = null;
	
	public static void main(String[] args) {

	}

	@BeforeClass
	public static void initTestCasesBeforeClass() throws IMCoreException,
			SQLException {

		String dbConfigFilePath = DB_CONFIG_PATH;
		Properties props = new Properties();
		try {
			FileInputStream fis = new FileInputStream(dbConfigFilePath);
			props.load(fis);
		} catch (IOException e) {
			throw new RuntimeException(
					"Error while reading DB configuration file :"
							+ dbConfigFilePath, e);
		}
		String dbDriver = props.getProperty("dbDriver");
		String connectionURL = props.getProperty("connectionURL");
		String tableName = props.getProperty("tableName");
		if (IdentityUtility.checkNotEmpty(dbDriver)
				|| IdentityUtility.checkNotEmpty(connectionURL)
				|| IdentityUtility.checkNotEmpty(tableName)) {
			throw new RuntimeException("Invalid DB configuration : \n" + props);
		}
		
		dbHelper = new DBHelper(dbDriver, connectionURL, tableName);
	}

	@Before
	public void initTestCases() throws IMCoreException, SQLException {
		identityDAO = new IdentityDaoDbStoreImpl(DB_CONFIG_PATH);
		dbHelper.createTable();
	}

	@Test
	public void saveIdentityToDbStoreTest() throws IMCoreException {
		Identity identity = new Identity("Datta", "Choukekar", "abc@gmail.com",
				new Date());
		Assert.assertTrue("Error while saving Identity.",
				identityDAO.save(identity));
		identity = new Identity("Datta", "Choukekar2", "abc2@gmail.com");
		Assert.assertTrue("Error while saving Identity.",
				identityDAO.save(identity));
		identity = new Identity("Francois", "Hollande", "francaois@gmail.com",
				new Date());
		Assert.assertTrue("Error while saving Identity.",
				identityDAO.save(identity));

		identity = new Identity("Charlse", "Gaule", "charles@gmail.com");
		Assert.assertTrue("Error while saving Identity.",
				identityDAO.save(identity));

		identity = new Identity("Jack", "Sparrow", "blackpearl@gmail.com");
		Assert.assertTrue("Error while saving Identity.",
				identityDAO.save(identity));

		identity = new Identity("James", "Bond", "007@gmail.com");
		Assert.assertTrue("Error while saving Identity.",
				identityDAO.save(identity));
	}

	@Test
	public void readAllIdentitiesFromDbStoreTest() throws IMCoreException {
		saveIdentityToDbStoreTest();
		List<Identity> identities = identityDAO.getAllEntities();
		Assert.assertTrue("One or more indentities expected.",
				!identities.isEmpty());

	}

	@Test
	public void searchIdentitiesFromDbStoreTest() throws IMCoreException {
		saveIdentityToDbStoreTest();
		Identity identity = new Identity("Datta", "Choukekar", null, null);
		List<Identity> identitySearchResultList = identityDAO.search(identity);
		Assert.assertTrue("One or more indentities expected.",
				!identitySearchResultList.isEmpty());

	}

	@Test
	public void searchIdentitiesWithParamsTest() throws IMCoreException {
		Calendar calendar = Calendar.getInstance();
		calendar.set(1987, 4, 9);
		Date date = calendar.getTime();

		// Data prparation
		Identity identity = new Identity("George", "Cloney",
				"george@gmail.com", date);
		Assert.assertTrue("Error while saving Identity.",
				identityDAO.save(identity));

		calendar.set(1980, 4, 25);
		date = calendar.getTime();

		identity = new Identity("George", "Clooney", "george@gmail.com", date);
		Assert.assertTrue("Error while saving Identity.",
				identityDAO.save(identity));

		identity = new Identity("George", "Cloney", "george-cloney@gmail.com",
				date);
		Assert.assertTrue("Error while saving Identity.",
				identityDAO.save(identity));
		
		// Tests
		identity = new Identity("George", null,
				null, null);
		List<Identity> identitySearchResultList = identityDAO.search(identity);
		Assert.assertTrue("One or more indentities missing.",
				identitySearchResultList.size() == 3);
		
		identity = new Identity("George", "Clooney",
				null, null);
		identitySearchResultList = identityDAO.search(identity);
		Assert.assertTrue("One or more indentities missing.",
				identitySearchResultList.size() == 1);
		
		identity = new Identity(null, null,
				"george-cloney@gmail.com", null);
		identitySearchResultList = identityDAO.search(identity);
		Assert.assertTrue("One or more indentities missing.",
				identitySearchResultList.size() == 1);
		
		identity = new Identity("George", null,
				null, date);
		identitySearchResultList = identityDAO.search(identity);
		Assert.assertTrue("One or more indentities missing.",
				identitySearchResultList.size() == 2);
	}
	
	@Test
	public void updateIdentitiesFromDbStoreTest() throws IMCoreException {
		saveIdentityToDbStoreTest();
		Identity identity = new Identity("Datta", "Choukekar", null, null);
		List<Identity> identitySearchResultList = identityDAO.search(identity);
		Assert.assertTrue("One or more indentities expected.",
				!identitySearchResultList.isEmpty());
		identity = identitySearchResultList.get(0);
		
		identity.setFirstName("Datta Updated");
		identity.setEmail("Datta Updated@gmail.com");
		
		Assert.assertTrue("Error while updating Identity.",
				identityDAO.update(identity));
		
		identity = new Identity("Datta Updated", null, "Datta Updated@gmail.com", null);
		identitySearchResultList = identityDAO.search(identity);
		Assert.assertTrue("Only one indentitiy expected.",
				identitySearchResultList.size() == 1);

	}
	
	@Test
	public void deleteIdentitiesFromDbStoreTest() throws IMCoreException {
		saveIdentityToDbStoreTest();
		Identity identity = new Identity("Datta", "Choukekar", null, null);
		List<Identity> identitySearchResultList = identityDAO.search(identity);
		Assert.assertTrue("One or more indentities expected.",
				!identitySearchResultList.isEmpty());
		identity = identitySearchResultList.get(0);
		
		Assert.assertTrue("Error while deleting Identity.",
				identityDAO.delete(identity));

		identitySearchResultList = identityDAO.search(identity);
		Assert.assertTrue("Zero entities should be returned.",
				identitySearchResultList.isEmpty());
		
	}
}
