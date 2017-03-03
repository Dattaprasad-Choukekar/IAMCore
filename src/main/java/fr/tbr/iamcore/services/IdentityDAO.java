package fr.tbr.iamcore.services;

import java.util.List;

import fr.tbr.iamcore.datamodel.Identity;
import fr.tbr.iamcore.services.exception.IMCoreException;

public interface IdentityDAO {
	/**
	 * Saves identity to persistant storage
	 * @param identity
	 * @return
	 * @throws IMCoreException
	 */
	public boolean save(Identity identity) throws IMCoreException;

	/**
	 * Update the identity in the persistant storage with given id
	 * @param identity
	 * @return
	 * @throws IMCoreException
	 */
	public boolean update(Identity identity) throws IMCoreException;

	/**
	 * Deletes the identity with given id from persistant storage
	 * @param identity
	 * @return
	 * @throws IMCoreException
	 */
	public boolean delete(Identity identity) throws IMCoreException;

	/**
	 * Searches the Identity in persistance stoarge
	 * @param identity
	 * @return
	 * @throws IMCoreException
	 */
	public List<Identity> search(Identity identity) throws IMCoreException;

	/**
	 * Gets all the identities from persistant storage
	 * @return
	 * @throws IMCoreException
	 */
	public List<Identity> getAllEntities() throws IMCoreException;
	
	/**
	 * Gets the Identity with given id
	 * @param id
	 * @return
	 * @throws IMCoreException
	 */
	public Identity get(int id) throws IMCoreException;
}
