package fr.tbr.iamcore.services.impl.filestore;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import fr.tbr.iamcore.datamodel.Identity;
import fr.tbr.iamcore.services.IdentityDAO;
import fr.tbr.iamcore.services.exception.IMCoreException;
import fr.tbr.iamcore.services.util.IdentityUtility;

public class IdentityDAOFileStoreImpl implements IdentityDAO {

	public static final String DEFAULT_FILE_STORE = "identities/identities.txt";
	public static final String DEFAULT_FIELD_SEPARATOR = ",";
	public static final String DEFAULT_NEW_LINE = System
			.getProperty("line.separator");;

	private File fileStore;
	private String FIELD_SEPARATOR;
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
	private int idCount;

	/**
	 *  Creates with default file store location "identities/identities.txt";
	 */
	public IdentityDAOFileStoreImpl() {
		this(DEFAULT_FILE_STORE);
	}

	/**
	 * 
	 * @param isAppend if true appends the data to existing file otherwise create new one
	 */
	public IdentityDAOFileStoreImpl(boolean isAppend) {
		this(DEFAULT_FILE_STORE, isAppend);
	}

	/**
	 * 
	 * @param fileStorePath Path of IDentity DAO file store
	 */
	public IdentityDAOFileStoreImpl(String fileStorePath) {
		this(fileStorePath, true);
	}

	/**
	 * 
	 * @param fileStorePath Path of IDentity DAO file store
	 * @param isAppend if true appends the data to existing file otherwise create new one
	 */
	public IdentityDAOFileStoreImpl(String fileStorePath, boolean isAppend) {
		fileStore = new File(fileStorePath);
		FIELD_SEPARATOR = DEFAULT_FIELD_SEPARATOR;
		try {
			IdentityUtility.createFileStore(fileStore);
			if (!isAppend) {
				fileStore.delete();
				fileStore.createNewFile();
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
					"Error while initializing file store.");
		}
	}

	@Override
	public boolean save(Identity identity) {
		boolean result = false;
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(
					fileStore, true)));

			identity.setId(idCount++);
			writer.print(serielizeIdentity(identity));
			writer.println();
			writer.flush();
			writer.close();
			result = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public boolean update(Identity identityToEdit) {
		boolean isEdited = false;
		boolean isIdentityEdited = false;

		List<Identity> identities = getAllEntities();
		if (identities != null) {
			for (Identity identity : identities) {
				if (identity.equals(identityToEdit)) {
					identity.setFirstName(identityToEdit.getFirstName());
					identity.setLastName(identityToEdit.getLastName());
					identity.setEmail(identityToEdit.getEmail());
					identity.setBirthDate(identityToEdit.getBirthDate());
					isIdentityEdited = true;
					break;
				}
			}
		}

		if (isIdentityEdited) {
			if (serielizeIdentities(identities)) {
				isEdited = true;
			}
		}
		return isEdited;
	}

	@Override
	public boolean delete(Identity identityToDelete) {
		boolean isDeleted = false;
		boolean isIdentityFound = false;

		List<Identity> identities = getAllEntities();
		if (identities != null) {
			for (Identity identity : identities) {
				if (identity.equals(identityToDelete)) {
					identityToDelete = identity;
					isIdentityFound = true;
					break;
				}
			}
		}

		if (isIdentityFound) {
			identities.remove(identityToDelete);
			if (serielizeIdentities(identities)) {
				isDeleted = true;
			}
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
		List<Identity> identities = new ArrayList<Identity>();
		Scanner scanner = null;
		try {
			scanner = new Scanner(fileStore);

			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				Identity identity = deserielizeIdentity(line);
				if (identity != null) {
					identities.add(identity);
				}
			}

		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} finally {
			scanner.close();
		}
		return identities;
	}

	private Identity deserielizeIdentity(String line) {
		Identity identity = null;
		if (line != null) {
			String[] fileds = line.split(FIELD_SEPARATOR);
			if (fileds != null && fileds.length == 5) {
				int id = Integer.parseInt(fileds[0]);
				Date birthDate = null;
				if (!"null".equals(fileds[4])) {
					try {
						birthDate = simpleDateFormat.parse(fileds[4]);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				identity = new Identity(id, fileds[1], fileds[2], fileds[3],
						birthDate);
				try {
					birthDate = simpleDateFormat.parse((fileds[3]));
				} catch (ParseException e) {
				}
				identity.setBirthDate(birthDate);
			}
		}
		return identity;
	}

	/**
	 * Converts Identity instance to line of string
	 * 
	 * @param identity
	 * @return
	 */
	private String serielizeIdentity(Identity identity) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(identity.getId());
		buffer.append(FIELD_SEPARATOR);
		buffer.append(identity.getFirstName());
		buffer.append(FIELD_SEPARATOR);
		buffer.append(identity.getLastName());
		buffer.append(FIELD_SEPARATOR);
		buffer.append(identity.getEmail());
		buffer.append(FIELD_SEPARATOR);
		if (identity.getBirthDate() != null) {
			buffer.append(simpleDateFormat.format(identity.getBirthDate()));
		} else {
			buffer.append(identity.getBirthDate());
		}
		return buffer.toString();
	}

	/**
	 * Serielizes list of identities to file sysytem
	 * 
	 * @param identityList
	 * @return
	 */
	private boolean serielizeIdentities(List<Identity> identityList) {
		boolean isSearialized = false;
		PrintWriter writer = null;
		if (identityList != null && !identityList.isEmpty()) {
			try {
				writer = new PrintWriter(new BufferedWriter(new FileWriter(
						fileStore, false)));
				for (Identity identity : identityList) {
					writer.print(serielizeIdentity(identity));
					writer.println();
				}
				writer.flush();
				writer.close();
				isSearialized = true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return isSearialized;
	}

	@Override
	public Identity get(int id) throws IMCoreException {
		List<Identity> identities = getAllEntities();
		if (identities != null) {
			for (Identity identity : identities) {
				if (identity.getId() == id) {
					return identity;
				}
			}
		}
		return null;
	}
}
