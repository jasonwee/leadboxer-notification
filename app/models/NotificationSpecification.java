package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.Model;

import play.data.format.Formats;
import play.data.validation.Constraints;

@Entity
public class NotificationSpecification extends Model {
	
	@Id
	public Long id;
	
	@Constraints.Required
	public String nKey;
	
	@Constraints.Required
	public String nValue;
	
	@Constraints.Required
	public String emailRecipients;
	
	@Formats.DateTime(pattern = "yyyy-MM-dd")
	public Date added;
	
	public static Find<Long, NotificationSpecification> find = new Find<Long, NotificationSpecification>() {};
	
	public static List<NotificationSpecification> findAll() {
		return NotificationSpecification.find.orderBy("id").findList();
	}
	
	

}
