/**
 * 
 */
package fr.tbr.iamcore.datamodel;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

/**
 * @author Tom
 *
 */
@Entity
@Table(name = "identities")
public class Identity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "identities_id")
	private int id = -1;

	@Column(name = "identities_firstname")
	private String firstName;
	
	@Column(name = "identities_lastname")
	private String lastName;

	@Column(name = "identities_email")
	private String email;

	@Column(name = "identities_birthdate")
	private Date birthDate;
	
	@ElementCollection(fetch  = FetchType.EAGER)
	@MapKeyColumn(name="name")
    @Column(name="value")
    @CollectionTable(name="identity_attributes", joinColumns=@JoinColumn(name="identities_id"))
	@Cascade(value=org.hibernate.annotations.CascadeType.ALL)
	private Map<String,String> attributes = new  HashMap<String,String>() ;

	
	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	public Identity() {

	}

	public Identity(String firstName, String lastName, String email) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public Identity(String firstName, String lastName, String email,
			Date birthDate) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.birthDate = birthDate;
	}

	public Identity(int id, String firstName, String lastName, String email,
			Date birthDate) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.birthDate = birthDate;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the birthDate
	 */
	public Date getBirthDate() {
		return birthDate;
	}

	/**
	 * @param birthDate
	 *            the birthDate to set
	 */
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	@Override
	public String toString() {
		return "Identity [id = " + id + ", " + "firstName=" + firstName
				+ ", lastName=" + lastName + ", email=" + email
				+ ", birthDate=" + birthDate + "]\n" + attributes;
	}

	@Override
	public boolean equals(Object objectToCheck) {
		if (objectToCheck != null && objectToCheck instanceof Identity) {
			Identity identityToCheck = (Identity) objectToCheck;
			/*
			 * if (identityToCheck.getFirstName() != null &&
			 * identityToCheck.getLastName() != null) { if
			 * (identityToCheck.getFirstName().equals(this.firstName) &&
			 * identityToCheck.getLastName().equals(this.lastName)) { return
			 * true; } }
			 */
			if (this.id == identityToCheck.id) {
				return true;
			}
		}
		return false;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
