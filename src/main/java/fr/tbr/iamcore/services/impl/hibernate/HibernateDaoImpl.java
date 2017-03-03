package fr.tbr.iamcore.services.impl.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.persister.collection.CollectionPropertyNames;
import org.springframework.beans.factory.annotation.Autowired;

import fr.tbr.iamcore.datamodel.Identity;
import fr.tbr.iamcore.services.IdentityDAO;
import fr.tbr.iamcore.services.exception.IMCoreException;

public class HibernateDaoImpl implements IdentityDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Override
	public boolean save(Identity identity) throws IMCoreException {
		Session session = getSessionFactory().openSession();
		Transaction transaction = session.getTransaction();
		transaction.begin();
		try {
			session.save(identity);
			// int id = (int) session.save(identity);
			// identity.setId(id);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			transaction.commit();
			session.close();
		}
		return true;
	}

	@Override
	public boolean update(Identity identity) throws IMCoreException {
		Session session = getSessionFactory().openSession();
		Identity identityFrmDb;

		Transaction trx = session.getTransaction();
		trx.begin();
		try {
			identityFrmDb = (Identity) session.load(Identity.class,
					identity.getId());
			identityFrmDb.setFirstName(identity.getFirstName());
			identityFrmDb.setLastName(identity.getLastName());
			identityFrmDb.setEmail(identity.getEmail());
			identityFrmDb.setBirthDate(identity.getBirthDate());
			identityFrmDb.setAttributes(identity.getAttributes());
			session.update(identityFrmDb);
			trx.commit();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			session.close();
		}
		return true;
	}

	@Override
	public boolean delete(Identity identity) throws IMCoreException {
		Session session = getSessionFactory().openSession();
		Identity identityFrmDb = null;
		Transaction trx = session.getTransaction();
		trx.begin();
		try {
			identityFrmDb = (Identity) session.load(Identity.class,
					identity.getId());
			session.delete(identityFrmDb);
			trx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			session.close();
		}
		return true;
	}

	@Override
	public List<Identity> search(Identity identityToSearch)
			throws IMCoreException {
		List<Identity> identities = new ArrayList<Identity>();
		Session session = getSessionFactory().openSession();
		Criteria criteria = session.createCriteria(Identity.class);
		if (identityToSearch != null) {
			if (identityToSearch.getFirstName() != null
					&& !identityToSearch.getFirstName().isEmpty()) {
				criteria.add(Restrictions.ilike("firstName",
						identityToSearch.getFirstName(), MatchMode.ANYWHERE));
			}

			if (identityToSearch.getLastName() != null
					&& !identityToSearch.getLastName().isEmpty()) {
				criteria.add(Restrictions.ilike("lastName",
						identityToSearch.getLastName(), MatchMode.ANYWHERE));
			}
			if (identityToSearch.getEmail() != null
					&& !identityToSearch.getEmail().isEmpty()) {
				criteria.add(Restrictions.ilike("email",
						identityToSearch.getEmail(), MatchMode.ANYWHERE) );
			}

			if (identityToSearch.getBirthDate() != null) {
				criteria.add(Restrictions.eq("birthDate",
						identityToSearch.getBirthDate()));
			}
			Map<String,String> attributeMap = identityToSearch.getAttributes();
			if (attributeMap!= null && !attributeMap.isEmpty()) {
				criteria.createAlias("attributes", "a");
				Disjunction disjunction = Restrictions.disjunction();
				
				for (String key:attributeMap.keySet()) {
					//criteria.add(Restrictions.eq("a." + CollectionPropertyNames.COLLECTION_ELEMENTS, map.get(key)));
					disjunction.add(Restrictions.ilike("a." + CollectionPropertyNames.COLLECTION_ELEMENTS,
							attributeMap.get(key), MatchMode.ANYWHERE));
				}
				criteria.add(disjunction);
			
			}
		

			identities = criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY).list();
		}
		return identities;
	}

	@Override
	public List<Identity> getAllEntities() throws IMCoreException {
		Session session = getSessionFactory().openSession();
		List<Identity> list = session.createCriteria(Identity.class).setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY).list();
		session.close();
		return list;
	}

	@Override
	public Identity get(int id) throws IMCoreException {
		Session session = getSessionFactory().openSession();
		Identity identity;
		try {
			identity = (Identity) session.get(Identity.class, id);
			// session.get(arg0, arg1)
		} finally {
			session.close();
		}
		return identity;
	}
}
