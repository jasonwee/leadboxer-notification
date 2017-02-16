package models;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.joda.time.DateTime;

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
	
	@Constraints.Required
	public String datasetId;
	
	// TODO tested this does not work.
	@Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date nsAdded;

	// TODO tested this does not work.
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

	public String getDatasetId() {
		return datasetId;
	}

	public void setDatasetId(String datasetId) {
		this.datasetId = datasetId;
	}

	public Date getNsAdded() {
		return nsAdded;
	}

	public void setNsAdded(Date nsAdded) {
		this.nsAdded = nsAdded;
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
				+ emailRecipients + ", datasetId=" + datasetId + ", nsAdded=" + nsAdded + ", lastSend=" + lastSend
				+ "]";
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
				.setFirstRow(page)
				.setMaxRows(pageSize)
				.findPagedList();
	}
	
	

}
