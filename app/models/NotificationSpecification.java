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
	
	@Constraints.Required
	public String datasetId;
	
	@Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date nsAdded;
	// TODO, we should change from date to either of the following but need to test if it work with mysql
	// java.time.LocalDateTime
	// org.joda.time.DateTime

	@Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date lastSend;
	
	// n=30
	// n=86400
	@Constraints.Required
	public String sendCondition;

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

	public String getSendCondition() {
		return sendCondition;
	}

	public void setSendCondition(String sendCondition) {
		this.sendCondition = sendCondition;
	}

	@Override
	public String toString() {
		return "NotificationSpecification [id=" + id + ", nKey=" + nKey + ", nValue=" + nValue + ", emailRecipients="
				+ emailRecipients + ", datasetId=" + datasetId + ", nsAdded=" + nsAdded + ", lastSend=" + lastSend
				+ ", sendCondition=" + sendCondition + "]";
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
	
	public static List<NotificationSpecification> byDataset(String datasetId) {
		return find.where().eq("datasetId", datasetId).findList();
	}
	
	public static List<NotificationSpecification> distinctDatasets() {
		return find.where().setDistinct(true).select("datasetId").findList();
	}
	
	// TODO how to test this other than going through controller? we need a unit test
	// findUnique() throws NonUniqueResultException, should we handle this?
	public static NotificationSpecification getNotificationSpecification(String datasetId, String key, String value) {
		return find.where().eq("datasetId", datasetId).eq("nKey", key).eq("nValue", value).findUnique();
	}

	
}
