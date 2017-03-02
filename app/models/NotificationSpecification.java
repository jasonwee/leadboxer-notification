/*
 * Copyright (c) 2014 - 2017, LeadBoxer and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  LeadBoxer designates this
 * particular file as subject to the "Classpath" exception as provided
 * by LeadBoxer in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact LeadBoxer, Herengracht 182 Amsterdam, Noord-Holland 1016 BR
 * Netherlands or visit www.leadboxer.com if you need additional information or
 * have any questions.
 */

package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.Model;
import com.avaje.ebean.PagedList;

import play.data.format.Formats;
import play.data.validation.Constraints;

/**
 * Notification Specification is user define requirement that when requirement is met, certain actions is taken
 * ns is short for notification specification
 *
 * @author jason
 *
 */
@Entity
public class NotificationSpecification extends Model {

	// uuid http://ebean-orm.github.io/docs/mapping/jpa/id
	// id signify this ns
	@Id
	public Long id;
	
	// the key for the notification specification, currently support most_likely_company, li_industry and original_url
	@Constraints.Required
	@Column(length=64)
	public String nKey;
	
	// value is a reserved keyword, so prepend n
	// value associated with the key mentioned above
	@Constraints.Required
	@Column(length=512)
	public String nValue;
	
	// in mysql , default length is varchar(255)
	// with comma separated
	@Constraints.Required
	public String emailRecipients;
	
	// length of 100 because leadboxer has that defined.
	@Constraints.Required
	@Column(length=100)
	public String datasetId;
	
	@Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(columnDefinition = "datetime") // we use this because mysql 5.1 cannto accept default datetime(6)
	public Date nsAdded;
	// TODO, we should change from date to either of the following but need to test if it work with mysql
	// java.time.LocalDateTime
	// org.joda.time.DateTime

	@Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(columnDefinition = "datetime") // we use this because mysql 5.1 cannto accept default datetime(6)
	public Date lastSend;
	
	/*
	 * logic on how often email send. for instance, one email send to recipient within N minutes or one email send when N of
	 * hits happened.
	 */
	// when n = 30, it means every 30seconds, send once. for n = 86400, means everyday only send once.
	// we use string because we can defined complex condition in the next improvement.
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
	
	/**
	 * find a list of ns by the datasetId
	 *
	 * @param datasetId
	 *
	 * @return
	 */
	public static List<NotificationSpecification> byDataset(String datasetId) {
		return find.where().eq("datasetId", datasetId).findList();
	}
	
	/**
	 * return distinct datasetId currently configured.
	 *
	 * @return a list of datasetId in NotificationSpecification
	 */
	public static List<NotificationSpecification> distinctDatasets() {
		return find.where().setDistinct(true).select("datasetId").findList();
	}
	
	// findUnique() throws NonUniqueResultException, should we handle this?
	public static NotificationSpecification getNotificationSpecification(String datasetId, String key, String value) {
		return find.where().eq("datasetId", datasetId).eq("nKey", key).eq("nValue", value).findUnique();
	}

	
}