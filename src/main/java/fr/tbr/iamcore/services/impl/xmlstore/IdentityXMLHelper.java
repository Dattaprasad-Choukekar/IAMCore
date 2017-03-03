package fr.tbr.iamcore.services.impl.xmlstore;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import fr.tbr.iamcore.datamodel.Identity;

public class IdentityXMLHelper {
	public static String identityElementName = "Identity";
	public static String firstNameElementName = "FirstName";
	public static String idElementName = "Id";
	public static String lastNameElementName = "LastName";
	public static String emailIdElementName = "EmailId";
	public static String birthDateElementName = "BirthDate";

	private DateFormat dateFormat;
	private File xmlFileStore;
	private DocumentBuilderFactory docFactory = DocumentBuilderFactory
			.newInstance();
	private TransformerFactory transformerFactory = TransformerFactory
			.newInstance();

	public IdentityXMLHelper(File xmlFileStore) {
		this.xmlFileStore = xmlFileStore;
		dateFormat = new SimpleDateFormat();
	}

	public IdentityXMLHelper(File xmlFileStore, DateFormat dateFormat) {
		this.xmlFileStore = xmlFileStore;
		this.dateFormat = dateFormat;
	}
	
	public void deleteIdentityToXML(Identity identity) throws Exception {
		DocumentBuilder docBuilder;
		docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(xmlFileStore);
		Node identities = doc.getFirstChild();

		NodeList identityNodeList = identities.getChildNodes();
		for (int i = 0; i < identityNodeList.getLength(); i++) {
			Node identityNode = identityNodeList.item(i);
			if (identityNode.getNodeType() == Node.ELEMENT_NODE) {
				Element identityElement = (Element) identityNode;
				Identity identityDelete = deserializeIdentity(identityElement);
				if (identityDelete != null) {
					if (identityDelete.getId() == identity.getId()) {
						identities.removeChild(identityElement);
					}
				}
			}
		}

		Transformer transformer = transformerFactory.newTransformer();
		transformer.transform(new DOMSource(doc),
				new StreamResult(xmlFileStore));
	}

	public void saveIdentityToXML(Identity identity) throws Exception {
		DocumentBuilder docBuilder;
		docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(xmlFileStore);
		Node identities = doc.getFirstChild();

		Element identityElement = serializeIdentity(identity, doc);
		identities.appendChild(identityElement);

		Transformer transformer = transformerFactory.newTransformer();
		transformer.transform(new DOMSource(doc),
				new StreamResult(xmlFileStore));

	}

	public void updateIdentityToXML(Identity identity) throws Exception {
		DocumentBuilder docBuilder;
		docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(xmlFileStore);
		Node identities = doc.getFirstChild();

		NodeList identityNodeList = identities.getChildNodes();
		for (int i = 0; i < identityNodeList.getLength(); i++) {
			Node identityNode = identityNodeList.item(i);
			if (identityNode.getNodeType() == Node.ELEMENT_NODE) {
				Element identityElement = (Element) identityNode;
				Identity identityToUpdate = deserializeIdentity(identityElement);
				if (identityToUpdate != null) {
					if (identityToUpdate.getId() == identity.getId()) {
						identities.removeChild(identityElement);
						identityElement = serializeIdentity(identity, doc);
						identities.appendChild(identityElement);
					}
				}
			}
		}

		Transformer transformer = transformerFactory.newTransformer();
		transformer.transform(new DOMSource(doc),
				new StreamResult(xmlFileStore));
	}

	public List<Identity> readAllIdentiesFromXML()
			throws ParserConfigurationException, SAXException, IOException {
		List<Identity> allEntities = new ArrayList<Identity>();
		DocumentBuilder docBuilder;
		docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(xmlFileStore);
		Node identities = doc.getFirstChild();
		NodeList identityNodeList = identities.getChildNodes();
		for (int i = 0; i < identityNodeList.getLength(); i++) {
			Node identityNode = identityNodeList.item(i);
			if (identityNode.getNodeType() == Node.ELEMENT_NODE) {
				Element identityElement = (Element) identityNode;
				Identity identity = deserializeIdentity(identityElement);
				if (identity != null) {
					allEntities.add(identity);
				}
			}
		}

		return allEntities;
	}

	private Element serializeIdentity(Identity identity, Document document) {
		Element identityElement = document.createElement(identityElementName);

	/*	Element idElement = document.createElement(idElementName);
		idElement.setTextContent(Integer.toString(identity.getId()));
		identityElement.appendChild(idElement);

		Element firstNameElement = document.createElement(firstNameElementName);
		firstNameElement.setTextContent(identity.getFirstName());
		identityElement.appendChild(firstNameElement);

		Element lastNameElement = document.createElement(lastNameElementName);
		lastNameElement.setTextContent(identity.getLastName());
		identityElement.appendChild(lastNameElement);

		Element emailIdElement = document.createElement(emailIdElementName);
		emailIdElement.setTextContent(identity.getEmail());
		identityElement.appendChild(emailIdElement);

		Element brithDateElement = document.createElement(birthDateElementName);
		if (identity.getBirthDate() != null) {
			brithDateElement.setTextContent(dateFormat.format(identity
					.getBirthDate()));
		}
		identityElement.appendChild(brithDateElement);*/

		return identityElement;
	}

	private Identity deserializeIdentity(Element identityElement) {
		Identity identity = null;
		if (identityElement != null
				&& identityElementName.equals(identityElement.getNodeName())) {
			identity = new Identity();
			NodeList childList = identityElement.getChildNodes();
			for (int i = 0; i < childList.getLength(); i++) {
				Node childNode = childList.item(i);
				if (childNode.getNodeType() == Node.ELEMENT_NODE) {
					Element childElement = (Element) childNode;
					String nodeName = childElement.getNodeName();
					String nodeText = "";//childElement.getTextContent();

					if (idElementName.equals(nodeName)) {
						identity.setId(Integer.parseInt(nodeText));
					} else if (firstNameElementName.equals(childElement
							.getNodeName())) {
						identity.setFirstName(nodeText);
					} else if (lastNameElementName.equals(childElement
							.getNodeName())) {
						identity.setLastName(nodeText);
					} else if (emailIdElementName.equals(childElement
							.getNodeName())) {
						identity.setEmail(nodeText);
					} else if (birthDateElementName.equals(childElement
							.getNodeName())) {
						try {
							if (nodeText != null && nodeText.length() != 0) {
								identity.setBirthDate(dateFormat
										.parse(nodeText));
							}
						} catch (ParseException e) {
							e.printStackTrace();
							throw new RuntimeException(
									"Error while reading entities from XML"
											+ xmlFileStore.getAbsolutePath());
						}
					}
				}
			}
		}
		return identity;
	}
}
