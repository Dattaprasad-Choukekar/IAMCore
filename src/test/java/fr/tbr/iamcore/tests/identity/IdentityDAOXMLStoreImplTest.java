package fr.tbr.iamcore.tests.identity;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.tbr.iamcore.datamodel.Identity;
import fr.tbr.iamcore.services.IdentityDAO;
import fr.tbr.iamcore.services.exception.IMCoreException;
import fr.tbr.iamcore.services.impl.xmlstore.IdentityDaoXMLStoreImpl;

public class IdentityDAOXMLStoreImplTest {
	public static final String TEST_FAILED = "Test Failed";

	private static IdentityDAO identityDAO;

	@Test
	public void testCreateIdentityDAOXMLStoreImpl() {
		try {
			IdentityDAO identityDAO = new IdentityDaoXMLStoreImpl();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(TEST_FAILED);
		}
	}

	@Test
	public void testCreateIdentityDAOXMLStoreImplAppendFalse() {
		try {
			IdentityDAO identityDAO = new IdentityDaoXMLStoreImpl(false);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(TEST_FAILED);
		}
	}

	@Test
	public void testCreateIdentityDAOXMLStoreImplNonDefault() {
		try {
			String tmpFile = "isGreat";
			IdentityDAO identityDAO = new IdentityDaoXMLStoreImpl(true,
					tmpFile);
			identityDAO = new IdentityDaoXMLStoreImpl(false, tmpFile);
			new File(tmpFile).delete();
			
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(TEST_FAILED);
		}
	}

	@Test
	public void testGetAllEntities() throws IMCoreException {
		if (identityDAO.getAllEntities() == null
				|| identityDAO.getAllEntities().isEmpty()) {
			System.out.println(TEST_FAILED);
		}
	}

	@BeforeClass
	public static void testSaveIdentities() throws IMCoreException {
		identityDAO = new IdentityDaoXMLStoreImpl(false);
		Identity identity = new Identity("Datta", "Choukekar", "abc@gmail.com",
				new Date());
		if (!identityDAO.save(identity)) {
			throw new RuntimeException("Test failed");
		}
		identity = new Identity("Datta", "Choukekar2", "abc2@gmail.com");
		if (!identityDAO.save(identity)) {
			throw new RuntimeException("Test failed");
		}
		identity = new Identity("Francois", "Hollande", "francaois@gmail.com",
				new Date());
		if (!identityDAO.save(identity)) {
			throw new RuntimeException("Test failed");
		}

		identity = new Identity("Charlse", "Gaule", "charles@gmail.com");
		if (!identityDAO.save(identity)) {
			throw new RuntimeException("Test failed");
		}

		identity = new Identity("Jack", "Sparrow", "blackpearl@gmail.com");
		if (!identityDAO.save(identity)) {
			throw new RuntimeException("Test failed");
		}

		identity = new Identity("James", "Bond", "007@gmail.com");
		if (!identityDAO.save(identity)) {
			throw new RuntimeException("Test failed");
		}

	}

	@Test
	public void testUpdateIdentity() throws IMCoreException {
		Identity identity = new Identity("Datta", null, null, null);
		List<Identity> listIdentity = identityDAO.search(identity);
		if (listIdentity.isEmpty()) {
			throw new RuntimeException("Test failed");
		}
		identity = listIdentity.get(0);
		identity.setEmail("updatedemail@com");
		identity.setBirthDate(new Date());
		if (!identityDAO.update(identity)) {
			throw new RuntimeException("Test failed");
		}
	}

	@Test
	public void testDeleteIdentity() throws IMCoreException {
		Identity identity = new Identity("James", null, null, null);
		List<Identity> listIdentity = identityDAO.search(identity);
		if (listIdentity.isEmpty()) {
			throw new RuntimeException("Test failed");
		}
		identity = listIdentity.get(0);
		if (!identityDAO.delete(identity)) {
			throw new RuntimeException("Test failed");
		}
	}
	
	@AfterClass
	public static void clean() {
	/*	File fileStore = new File(IdentityDaoXMLStoreImpl.DEFAULT_XLM_FILE_STORE);
		if (fileStore.isDirectory()) {
			new File(fileStore.list()[0]).delete();
		}
		fileStore.getParentFile().delete();*/
		
	}
}
