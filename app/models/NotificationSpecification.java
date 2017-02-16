package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.Model;
import com.avaje.ebean.PagedList;

import play.data.format.Formats;
import play.data.validation.Constraints;

@Entity
public class NotificationSpecification extends Model {

	@Id
	public Long id;
	
	@Constraints.Required
	public String nKey;
	
	// value is a reserved keyword, so prepend n
	@Constraints.Required
	public String nValue;
	
	@Constraints.Required
	public String emailRecipients;
	
	@Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date ndAdded;
	
	@Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date lastSend;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getnKey() {
		return nKey;
	}

	public void setnKey(String nKey) {
		this.nKey = nKey;
	}

	public String getnValue() {
		return nValue;
	}

	public void setnValue(String nValue) {
		this.nValue = nValue;
	}

	public String getEmailRecipients() {
		return emailRecipients;
	}

	public void setEmailRecipients(String emailRecipients) {
		this.emailRecipients = emailRecipients;
	}

	public Date getNdAdded() {
		return ndAdded;
	}

	public void setNdAdded(Date ndAdded) {
		this.ndAdded = ndAdded;
	}

	public Date getLastSend() {
		return lastSend;
	}

	public void setLastSend(Date lastSend) {
		this.lastSend = lastSend;
	}

	@Override
	public String toString() {
		return "NotificationSpecification [id=" + id + ", nKey=" + nKey + ", nValue=" + nValue + ", emailRecipients="
				+ emailRecipients + ", ndAdded=" + ndAdded + ", lastSend=" + lastSend + "]";
	}

	public static Find<Long, NotificationSpecification> find = new Find<Long, NotificationSpecification>() {};
	
	public static List<NotificationSpecification> findAll() {
		return NotificationSpecification.find.orderBy("id").findList();
	}

	public static PagedList<NotificationSpecification> page(int page, int pageSize, String sortBy, String order, String filter) {
		return 
				find.where()
				.ilike("nValue", "%" + filter + "%")
				.orderBy(sortBy + " " + order)
				.setFirstRow(pageSize)
				.setMaxRows(pageSize)
				.findPagedList();
	}
	
	

}
