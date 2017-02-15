package models;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints;

import com.avaje.ebean.Model;

@Entity
public class Company extends Model {
	
	@Id
	public Long id;
	
	@Constraints.Required
	public String name;
	
	// generic query helper for entity Company with id Long
	public static Find<Long, Company> find = new Find<Long, Company>() {};
	
	public static Map<String, String> options() {
		LinkedHashMap<String, String> options = new LinkedHashMap<String, String>();
		for (Company c: Company.find.orderBy("name").findList()) {
			options.put(c.id.toString(), c.name);
		}
		return options;
	}
}
