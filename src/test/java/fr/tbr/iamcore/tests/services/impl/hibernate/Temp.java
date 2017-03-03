package fr.tbr.iamcore.tests.services.impl.hibernate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.tbr.iamcore.datamodel.Identity;
import fr.tbr.iamcore.services.IdentityDAO;
import fr.tbr.iamcore.services.exception.IMCoreException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/hibernateDaoImplTestConfig.xml" })
public class Temp {

	@Autowired
	private IdentityDAO identityDAO;
	@Test
	public void test() throws IMCoreException {
		Identity identity = new Identity("Datta", "Choukekar", "abc@gmail.com",
				new Date());
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("Age", "10");
		map.put("City", "Mumbai");
		
		identity.setAttributes(map);
		
		identityDAO.save(identity);
		
		identity = new Identity("Datta", "Choukekar1", "abc@gmail.com",
				new Date());
		
		map = new HashMap<String, String>();
		map.put("Age", "10");
		map.put("City", "Pune");
		
		identity.setAttributes(map);
		
		identityDAO.save(identity);
		
		
		
		
		
		System.out.println(identity.getId());
		
		identity = identityDAO.get(identity.getId());
		
		System.out.println(identity.getAttributes());
		
		
		identity = new Identity("Datta", null, null, null);
		
		map = new HashMap<String, String>();
		map.put("City", "Pune");
		
		
		identityDAO.search(identity);
		
		
	}
}
