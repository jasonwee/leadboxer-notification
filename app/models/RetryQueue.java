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

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.Model;
import com.avaje.ebean.PagedList;
import com.avaje.ebean.annotation.DbJson;
import com.fasterxml.jackson.databind.JsonNode;

import play.data.validation.Constraints;

/**
 *
 * @author jason
 *
 */
@Entity
public class RetryQueue extends Model {

   @Id
   public UUID id;

   @Constraints.Required
   public int maxRetry = 3;

   @Constraints.Required
   public int retry;

   @Constraints.Required
   public Long notificationId;

   @DbJson
   public JsonNode hitJson;

   public int getMaxRetry() {
      return maxRetry;
   }

   public void setMaxRetry(int maxRetry) {
      this.maxRetry = maxRetry;
   }

   public int getRetry() {
      return retry;
   }

   public void setRetry(int retry) {
      this.retry = retry;
   }

   public Long getNotificationId() {
      return notificationId;
   }

   public void setNotificationId(Long notificationId) {
      this.notificationId = notificationId;
   }

   public JsonNode getHitJson() {
      return hitJson;
   }

   public void setHitJson(JsonNode hitJson) {
      this.hitJson = hitJson;
   }

   public static Find<UUID, RetryQueue> find = new Find<UUID, RetryQueue>() {

   };

   public static PagedList<RetryQueue> page(int page, int pageSize, String sortBy, String order, String filter) {
      //return find.where().ilike("", "%" + filter + "%").orderBy(sortBy + " " + order).setFirstRow(page).setMaxRows(pageSize).findPagedList();
      return find.where().orderBy(sortBy + " " + order).setFirstRow(page).setMaxRows(pageSize).findPagedList();
   }

}
