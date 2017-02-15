package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.data.format.Formats;
import play.data.validation.Constraints;

import com.avaje.ebean.Model;
import com.avaje.ebean.PagedList;


@Entity
public class Computer extends Model {
	
	@Id
	public Long id;
	
	@Constraints.Required
	public String name;
	
	@Formats.DateTime(pattern = "yyyy-MM-dd")
	public Date introduced;
	
	@Formats.DateTime(pattern = "yyyy-MM-dd")
	public Date discontinued;
	
	@ManyToOne
	public Company company;
	
	// generic query helper for entity Computer with id Long 
	public static Find<Long, Computer> find = new Find<Long, Computer>(){};
	
	public static PagedList<Computer> page(int page, int pageSize, String sortBy, String order, String filter) {
		return 
				find.where()
				.ilike("name", "%" + filter + "%")
				.orderBy(sortBy + " " + order)
				.fetch("company")
				.setFirstRow(pageSize)
				.setMaxRows(pageSize)
				.findPagedList();
	}
	
	

}
