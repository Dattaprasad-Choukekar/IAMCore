package fr.tbr.iamcore.services.impl.xmlstore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;

import fr.tbr.iamcore.datamodel.Identity;

public class IdenityXMLHelperOld {
	public static String identityElement = "Identity";
	public static String firstNameElement = "FirstName";
	public static String idElement = "Id";
	public static String lastNameElement = "LastName";
	public static String emailIdElement = "EmailId";
	public static String birthDateElement = "BirthDate";

	private DateFormat dateFormat;
	private XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
	private TransformerFactory transformerFactory = TransformerFactory.newInstance();
	private File xmlFileStore;

	public IdenityXMLHelperOld(File xmlFileStore) {
		this.xmlFileStore = xmlFileStore;
		dateFormat = new SimpleDateFormat();
	}

	public IdenityXMLHelperOld(File xmlFileStore, DateFormat dateFormat) {
		this.xmlFileStore = xmlFileStore;
		this.dateFormat = dateFormat;
	}

	public boolean saveEntityToXML() {
		Transformer transformer;
		return false;
	}
	public List<Identity> readAllEntitiesFromXML() {
		List<Identity> allEntities = new ArrayList<Identity>();
		XMLEventReader eventReader;
		try {

			boolean isIdentity = false;
			boolean isId = false;
			boolean isFirstName = false;
			boolean isLastName = false;
			boolean isEmail = false;
			boolean isBirthDate = false;

			eventReader = xmlInputFactory.createXMLEventReader(new FileReader(
					xmlFileStore));

			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();
				Identity identity = null;
				switch (event.getEventType()) {
				case XMLStreamConstants.START_ELEMENT:
					StartElement startElement = event.asStartElement();
					String elementName = startElement.getName().getLocalPart();

					if (identityElement.equals(elementName)) {
						isIdentity = true;
						identity = new Identity();
						System.out.println("Start of " + identityElement);
					}
					if (idElement.equals(elementName)) {
						isId = true;
					}
					if (firstNameElement.equals(elementName)) {
						isFirstName = true;
					}
					if (lastNameElement.equals(elementName)) {
						isLastName = true;
					}
					if (emailIdElement.equals(elementName)) {
						isEmail = true;
					}
					if (birthDateElement.equals(elementName)) {
						isBirthDate = true;
					}
					break;
				case XMLStreamConstants.CHARACTERS:
					Characters characters = event.asCharacters();
					if (isId) {
						identity.setId(Integer.parseInt(characters.getData()));
						isId = false;
					}
					if (isFirstName) {
						identity.setFirstName(characters.getData());
						isFirstName = false;
					}
					if (isLastName) {
						identity.setLastName(characters.getData());
						isLastName = false;
					}
					if (isEmail) {
						identity.setEmail(characters.getData());
						isEmail = false;
					}
					if (isBirthDate) {
						identity.setBirthDate(dateFormat.parse(characters
								.getData()));
						isBirthDate = false;
					}
					break;
				case XMLStreamConstants.END_ELEMENT:
					EndElement endElement = event.asEndElement();
					if (endElement.getName().getLocalPart()
							.equals(identityElement)) {
						allEntities.add(identity);
						System.out.println(identity);
						System.out.println("End of " + identityElement);
					}
					break;
				}
			}
		} catch (FileNotFoundException | XMLStreamException | ParseException e) {
			e.printStackTrace();
			throw new RuntimeException(
					"Error while retrieving Identities from XML file store:"
							+ xmlFileStore.getAbsolutePath());
		}

		return allEntities;
	}
}
