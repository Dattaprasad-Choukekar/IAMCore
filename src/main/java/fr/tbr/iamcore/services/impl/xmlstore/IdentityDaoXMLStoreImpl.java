package fr.tbr.iamcore.services.impl.xmlstore;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.tbr.iamcore.datamodel.Identity;
import fr.tbr.iamcore.services.IdentityDAO;
import fr.tbr.iamcore.services.exception.IMCoreException;
import fr.tbr.iamcore.services.util.IdentityUtility;

public class IdentityDaoXMLStoreImpl implements IdentityDAO {
	public static final String DEFAULT_XLM_FILE_STORE = "identities/identities.xml";
	public static final String DEFAULT_NEW_LINE = System
			.getProperty("line.separator");
	private String identitiesElementName = "Identities";

	private File xmlFileStore;
	private IdentityXMLHelper identityXMLHelper;

	private int idCount;

	public IdentityDaoXMLStoreImpl() {
		this(true, DEFAULT_XLM_FILE_STORE);
	}

	public IdentityDaoXMLStoreImpl(boolean isAppend) {
		this(isAppend, DEFAULT_XLM_FILE_STORE);
	}

	public IdentityDaoXMLStoreImpl(boolean isAppend, String xlmFileStorePath) {

		xmlFileStore = new File(xlmFileStorePath);
		identityXMLHelper = new IdentityXMLHelper(xmlFileStore,
				new SimpleDateFormat());
		try {
			IdentityUtility.createXMLFileStore(xmlFileStore,
					identitiesElementName);

			if (!isAppend) {
				if (xmlFileStore.exists()) {
					xmlFileStore.delete();
				}
				IdentityUtility.createXMLFileStore(xmlFileStore,
						identitiesElementName);
				// IdentityUtility.formatXMLFile(xmlFileStore,
				// identitiesElementName);
				idCount = 1;
			} else {
				// Get last id
				List<Identity> allEntitiesList = getAllEntities();
				if (allEntitiesList.isEmpty()) {
					idCount = 1;
				} else {
					Identity lastIdentity = allEntitiesList.get(allEntitiesList
							.size() - 1);
					idCount = lastIdentity.getId() + 1;
				}
			}
		} catch (IOException e) {
			throw new ExceptionInInitializerError(
					"Error while initializing xml file store.");
		}

	}

	@Override
	public boolean save(Identity identity) {
		boolean isSaved = false;
		try {
			identity.setId(idCount++);
			identityXMLHelper.saveIdentityToXML(identity);
			isSaved = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error while saving identity to XML: "
					+ xmlFileStore.getAbsolutePath());
		}
		return isSaved;
	}

	@Override
	public boolean update(Identity identity) {
		boolean isUpdated = false;
		try {
			identityXMLHelper.updateIdentityToXML(identity);
			isUpdated = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error while updating identitiy: "
					+ identity + " to XML: " + xmlFileStore.getAbsolutePath());
		}
		return isUpdated;
	}

	@Override
	public boolean delete(Identity identity) {
		boolean isDeleted = false;
		try {
			identityXMLHelper.deleteIdentityToXML(identity);
			isDeleted = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error while reading delting identity:"
					+ identity + " from XML: " + xmlFileStore.getAbsolutePath());
		}
		return isDeleted;
	}

	@Override
	public List<Identity> search(Identity identityToSearch) {
		List<Identity> resultIdentityList = new ArrayList<Identity>();
		List<Identity> identities = getAllEntities();
		if (identities != null) {
			for (Identity identity : identities) {
				if (identity.getFirstName().equals(
						identityToSearch.getFirstName())) {
					// resultIdentity =
					// deserielizeIdentity(serielizeIdentity(identity));
					resultIdentityList.add(identity);
				}
			}
		}
		return resultIdentityList;
	}

	@Override
	public List<Identity> getAllEntities() {
		try {
			return identityXMLHelper.readAllIdentiesFromXML();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
			throw new RuntimeException(
					"Error while reading identities from XML: "
							+ xmlFileStore.getAbsolutePath());
		}
	}

	@Override
	public Identity get(int id) throws IMCoreException {
		List<Identity> allEntities = new ArrayList<Identity>();
		try {
			allEntities = identityXMLHelper.readAllIdentiesFromXML();

		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
			throw new RuntimeException(
					"Error while reading identities from XML: "
							+ xmlFileStore.getAbsolutePath());
		}
		for (Identity identity : allEntities) {
			if (identity.getId() == id) {
				return identity;
			}
		}

		return null;
	}
}