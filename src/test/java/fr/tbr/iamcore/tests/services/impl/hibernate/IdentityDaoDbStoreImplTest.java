package fr.tbr.iamcore.tests.services.impl.hibernate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import fr.tbr.iamcore.datamodel.Identity;
import fr.tbr.iamcore.services.IdentityDAO;
import fr.tbr.iamcore.services.exception.IMCoreException;
import fr.tbr.iamcore.services.impl.hibernate.HibernateDaoImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners(listeners = { IdentityDaoDbStoreImplTest.Setup.class,
		DependencyInjectionTestExecutionListener.class })
@ContextConfiguration(locations = { "/hibernateDaoImplTestConfig.xml" })
public class IdentityDaoDbStoreImplTest {

	public static class Setup extends AbstractTestExecutionListener {
		@Override
		public void beforeTestClass(TestContext testContext) throws Exception {
			HibernateDaoImpl identityDAO = (HibernateDaoImpl) testContext
					.getApplicationContext().getBean("daoImpl");

			Identity identity = new Identity("Datta", "Choukekar",
					"abc@gmail.com", new Date());
			if (!identityDAO.save(identity)) {
				throw new RuntimeException("Test failed");
			}
			identity = new Identity("Datta", "Choukekar2", "abc2@gmail.com");
			if (!identityDAO.save(identity)) {
				throw new RuntimeException("Test failed");
			}
			identity = new Identity("Francois", "Hollande",
					"francaois@gmail.com", new Date());
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
	}

	@Test
	public void saveIdentitiesTest() throws IMCoreException {
		Identity identity = new Identity("Jethrow", "Gibbs",
				"jethrow@ncis.com", new Date());
		Assert.assertTrue(identityDAO.save(identity));
		System.out.println();
	}

	@Test
	public void getAllEntitiesTest() throws IMCoreException {
		List<Identity> identitylist = identityDAO.getAllEntities();
		Assert.assertTrue("Identities cann not be empty", identitylist != null
				&& identitylist.size() != 0);
	}

	@Test
	public void getIdentityTest() throws IMCoreException {
		Identity identity = new Identity("Jethrow", "Gibbs",
				"jethrow@ncis.com", new Date());
		Assert.assertTrue("Identity not saved!", identityDAO.save(identity));
		identity = identityDAO.get(identity.getId());
		Assert.assertTrue(identity != null
				&& identityDAO.get(identity.getId()).getFirstName()
						.equals("Jethrow"));
	}

	@Test
	public void updateIdentityTest() throws IMCoreException {
		Identity identity = new Identity("Jethrow", "Gibbs",
				"jethrow@ncis.com", new Date());
		Assert.assertTrue("Identity not saved!", identityDAO.save(identity));
		identity = identityDAO.get(identity.getId());
		identity.setFirstName("Lee roy Jethrow");
		Assert.assertTrue(identityDAO.update(identity));
		Assert.assertTrue(identity != null
				&& identityDAO.get(identity.getId()).getFirstName() != "Jethrow");
		Assert.assertTrue(identity != null
				&& identityDAO.get(identity.getId()).getFirstName()
						.equals("Lee roy Jethrow"));
	}

	@Test
	public void deleteIdentityTest() throws IMCoreException {
		Identity identity = new Identity("Jethrow", "Gibbs",
				"jethrow@ncis.com", new Date());
		Assert.assertTrue("Identity not saved!", identityDAO.save(identity));
		identity = identityDAO.get(identity.getId());
		identity.setFirstName("Lee roy Jethrow");
		Assert.assertTrue(identityDAO.delete(identity));
		Assert.assertTrue("Identity should be null.",
				identityDAO.get(identity.getId()) == null);
	}

	@Test
	public void searchIdentityTest() throws IMCoreException {
		Identity identity = new Identity("Jethrow", "Gibbs",
				"jethrow@ncis.com", new Date());
		Assert.assertTrue("Identity not saved!", identityDAO.save(identity));
		List<Identity> identityList = identityDAO.search(identity);
		Assert.assertTrue("Search result should not be empty or null.",
				identityList != null && !identityList.isEmpty());
	}

	@Autowired
	private IdentityDAO identityDAO;

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
		identity = new Identity("George", null, null, null);
		List<Identity> identitySearchResultList = identityDAO.search(identity);
		Assert.assertTrue("One or more indentities missing.",
				identitySearchResultList.size() == 3);

		identity = new Identity("George", "Clooney", null, null);
		identitySearchResultList = identityDAO.search(identity);
		Assert.assertTrue("One or more indentities missing.",
				identitySearchResultList.size() == 1);

		identity = new Identity(null, null, "george-cloney@gmail.com", null);
		identitySearchResultList = identityDAO.search(identity);
		Assert.assertTrue("One or more indentities missing.",
				identitySearchResultList.size() == 1);

		identity = new Identity("George", null, null, date);
		identitySearchResultList = identityDAO.search(identity);
		Assert.assertTrue("One or more indentities missing.",
				identitySearchResultList.size() == 2);
		
		identity = new Identity("eor", null, null, date);
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

		identity = new Identity("Datta Updated", null,
				"Datta Updated@gmail.com", null);
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
