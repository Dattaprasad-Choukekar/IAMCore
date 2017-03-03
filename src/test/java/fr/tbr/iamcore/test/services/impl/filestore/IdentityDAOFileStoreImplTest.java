package fr.tbr.iamcore.test.services.impl.filestore;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.tbr.iamcore.datamodel.Identity;
import fr.tbr.iamcore.services.IdentityDAO;
import fr.tbr.iamcore.services.exception.IMCoreException;
import fr.tbr.iamcore.services.impl.filestore.IdentityDAOFileStoreImpl;
import fr.tbr.iamcore.services.util.IdentityUtility;

public class IdentityDAOFileStoreImplTest {
	static private final String TEMP_FILE_NAME = "abcd.txt";

	public static void main(String[] args) throws IMCoreException {
	}

	@Test
	public void getAllEntitiesTest() throws IMCoreException {
		IdentityDAO identityDAO = new IdentityDAOFileStoreImpl();
		identityDAO.getAllEntities();
	}

	@Test
	public void defaultIntitializeTest() {
		IdentityDAO identityDAO = new IdentityDAOFileStoreImpl();
	}

	@Test
	public void intitializeTest() {
		IdentityDAO identityDAO = new IdentityDAOFileStoreImpl(TEMP_FILE_NAME);
	}

	@Test
	public void saveIdentityTest() throws IMCoreException {
		IdentityDAO identityDAO = new IdentityDAOFileStoreImpl(TEMP_FILE_NAME);
		Identity identity = new Identity("Datta", "Choukekar", "abc@gmail.com",
				new Date());
		if (!identityDAO.save(identity)) {
			throw new RuntimeException("Test failed");
		}
		identity = new Identity("Francaois", "Hollande", "francaois@gmail.com");
		if (!identityDAO.save(identity)) {
			throw new RuntimeException("Test failed");
		}

		identity = new Identity("Chalrese", "Gaule", "charles@gmail.com",
				new Date());
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

		// ///////////////////////////////////////////////////////////////
		identityDAO = new IdentityDAOFileStoreImpl(TEMP_FILE_NAME, false);
		identity = new Identity("Datta", "Choukekar", "abc@gmail.com");
		if (!identityDAO.save(identity)) {
			throw new RuntimeException("Test failed");
		}
		identity = new Identity("Francaois", "Hollande", "francaois@gmail.com");
		if (!identityDAO.save(identity)) {
			throw new RuntimeException("Test failed");
		}
	}

	@Test
	public void getIdentityTest() throws IMCoreException {

		IdentityDAO identityDAO = new IdentityDAOFileStoreImpl();
		Identity identity = new Identity("Datta", "Choukekar", null, null);
		List<Identity> identitySearchResultList = identityDAO.search(identity);
		if (identitySearchResultList == null
				|| identitySearchResultList.isEmpty()) {
			throw new RuntimeException("Test failed");
		} else {
			System.out.println(identitySearchResultList);
		}
		identity  = identitySearchResultList.get(1);
		Identity identityResult = identityDAO.get(identity.getId());
		if (identityResult != null && identityResult.getFirstName().equals(identity.getFirstName())) {
			System.out.println(identityResult);
		} else {
			throw new RuntimeException("Test failed");
		}

	
	}

	@BeforeClass
	public static void prepareIdentityFile() throws IMCoreException {
		IdentityDAO identityDAO = new IdentityDAOFileStoreImpl(false);
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
	public void editIdentityTest() throws IMCoreException {
		IdentityDAO identityDAO = new IdentityDAOFileStoreImpl();

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
	public void searchIdentityTest() throws IMCoreException {
		IdentityDAO identityDAO = new IdentityDAOFileStoreImpl();
		Identity identity = new Identity("Datta", "Choukekar", null, null);
		List<Identity> identitySearchResultList = identityDAO.search(identity);
		if (identitySearchResultList == null
				|| identitySearchResultList.isEmpty()) {
			throw new RuntimeException("Test failed");
		} else {
			System.out.println(identitySearchResultList);
		}

	}

	@Test
	public void deleteIdentiryTest() throws IMCoreException {
		IdentityDAO identityDAO = new IdentityDAOFileStoreImpl();

		Identity identity = new Identity("Jack", "Sparrow", null, null);
		List<Identity> listIdentity = identityDAO.search(identity);
		if (listIdentity.isEmpty()) {
			throw new RuntimeException("Test failed");
		}
		if (identityDAO.delete(listIdentity.get(0))) {
			System.out.println("Identity deleted." + identity);
		} else {
			throw new RuntimeException("Test failed");
		}
	}
	
	@AfterClass
	public static void clean() {
		File fileStore = new File(TEMP_FILE_NAME);
		if (fileStore.exists() && fileStore.isFile()) {
			fileStore.delete();
		}
		
		fileStore = new File(IdentityDAOFileStoreImpl.DEFAULT_FILE_STORE);
		if (fileStore.exists() && fileStore.isFile()) {
			fileStore.delete();
		}
	}
}
