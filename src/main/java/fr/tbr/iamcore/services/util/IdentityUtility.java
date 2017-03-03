package fr.tbr.iamcore.services.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Contains utility methods.
 * 
 * @author Dattaprasad
 *
 */
public class IdentityUtility {
	public static void createFileStore(File fileStore) throws IOException {
		createStore(fileStore, false, null);
	}

	public static boolean checkNotEmpty(String param) {
		return param != null && !param.isEmpty() ? false : true;
	}

	/**
	 * Creates file store in file system
	 * @param fileStore
	 * @param isXML true if file store is xml
	 * @param identitiesElementName if file store is xml provides the identity element name
	 * @throws IOException
	 */
	private static void createStore(File fileStore, boolean isXML,
			String identitiesElementName) throws IOException {
		if (!fileStore.exists()) {
			try {
				File parent = fileStore.getParentFile();
				if (parent != null && !parent.exists()) {
					parent.mkdirs();
				}
				fileStore.createNewFile();
				if (isXML) {
					formatXMLFile(fileStore, identitiesElementName);
				}
			} catch (IOException e) {
				System.out
						.println("Something gone wrong in the file creation, see the following stack trace");
				e.printStackTrace();
				throw e;
			}
		}
		System.out.println("File store path is : "
				+ fileStore.getAbsolutePath());

	}
	/**
	 * Creates XML file store.
	 * @param fileStore
	 * @param identitiesElementName
	 * @throws IOException
	 */
	public static void createXMLFileStore(File fileStore,
			String identitiesElementName) throws IOException {
		createStore(fileStore, true, identitiesElementName);
	}

	/**
	 * Creates xml file with given element name.
	 * 
	 * @param xmlFileStore
	 * @param identitiesElementName
	 * @throws IOException
	 */
	public static void formatXMLFile(File xmlFileStore,
			String identitiesElementName) throws IOException {
		PrintWriter writer = new PrintWriter(xmlFileStore);
		writer.print("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><"
				+ identitiesElementName
				+ ">"
				+ "</"
				+ identitiesElementName
				+ ">");
		writer.flush();
		writer.close();

	}
}
