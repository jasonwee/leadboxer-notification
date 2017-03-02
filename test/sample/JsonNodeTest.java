package sample;
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

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.libs.Json;

/**
 * 
 * {
 *        "dataset": {
 *          "datasetId": "a1d90dccc04df83f26553dc753ed41f2",
 *          "enable": false,
 *          "notifications": {
 *            "most_likely_company": [
 *              "google",
 *              "leadboxer"
 *            ],
 *            "li_industry": [
 *              "Defense & Space",
 *              "foo"
 *            ]
 *          }
 *        }
 *      }
 *      
 * @author jason
 *
 */
public class JsonNodeTest {
   
   /**
    * to see the output, uncomment the @
    * 
     * {
     *        "dataset": {
     *          "datasetId": "a1d90dccc04df83f26553dc753ed41f2",
     *          "enable": false,
     *          "notifications": {
     *            "most_likely_company": [
     *              "google",
     *              "leadboxer"
     *            ],
     *            "li_industry": [
     *              "Defense & Space",
     *              "foo"
     *            ]
     *          }
     *        }
     *      }
    * @throws JsonProcessingException
    */
   //@Test
   public void formAndDisplayIdentJsonForDataset() throws JsonProcessingException {
      ObjectNode root = Json.newObject();
      ObjectNode aDataset = root.putObject("dataset")
            .put("datasetId", "a1d90dccc04df83f26553dc753ed41f2")
            .put("enable", true);
      ObjectNode notifications = aDataset.putObject("notifications");
      notifications.putArray("most_likely_company").add("google").add("leadboxer");
      notifications.put("most_likely_company", "google");
      notifications.putArray("li_industry").add("Defense & Space").add("foo");
      notifications.putArray("original_url").add("https://www.google.com").add("https://www.leadboxer.com");
         
      
      ObjectMapper mapper = new ObjectMapper();
      mapper.enable(SerializationFeature.INDENT_OUTPUT);
      String ident = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
      System.out.println(ident);
   }
   
   /**
    * to see the output, uncomment the @
    *        {
    *  "datasets": [
    *    {
    *      "datasetId": "a1d90dccc04df83f26553dc753ed41f2",
    *      "enable": false,
    *      "notifications": {
    *        "most_likely_company": [
    *          "google",
    *          "leadboxer"
    *        ],
    *        "li_industry": "Defense & Space1",
    *        "original_url": [
    *          "http://www.leadboxer.com/feature",
    *          "http://www.leadboxer.com/pricing"
    *        ]
    *      }
    *    },
    *    {
    *      "datasetId": "e2d1c24722e8a52390a42be6e89f7a65",
    *      "enable": false,
    *      "notifications": {
    *        "most_likely_company": [
    *          "google",
    *          "leadboxer",
    *          "Bae Systems"
    *        ],
    *        "li_industry": "financial2",
    *        "original_url": [
    *          "http://www.opentracker.net/feature",
    *          "http://www.opentracker.net/pricing"
    *        ]
    *      }
    *    }
    *  ]
    *}
    *
    * @throws JsonProcessingException
    */
   //@Test
   public void formAndDisplayIdentJsonForDatasets() throws JsonProcessingException {
      
      ObjectMapper om = new ObjectMapper();
      ObjectNode root = Json.newObject();
      ArrayNode datasets = root.putArray("datasets");
      
      // a1d90dccc04df83f26553dc753ed41f2
      ObjectNode dataset = om.createObjectNode();
      dataset.put("datasetId", "a1d90dccc04df83f26553dc753ed41f2");
      dataset.put("enable", true);
      ObjectNode notifications = dataset.putObject("notifications");
      notifications.putArray("most_likely_company").add("google").add("leadboxer");
      notifications.put("li_industry", "Defense & Space");
      notifications.putArray("original_url").add("http://www.leadboxer.com/feature").add("http://www.leadboxer.com/pricing");
      datasets.add(dataset);
      
      // e2d1c24722e8a52390a42be6e89f7a65
      dataset = om.createObjectNode();
      dataset.put("datasetId", "e2d1c24722e8a52390a42be6e89f7a65");
      dataset.put("enable", true);
      notifications = dataset.putObject("notifications");
      notifications.putArray("most_likely_company").add("google").add("leadboxer").add("Bae Systems");
      notifications.put("li_industry", "financial2");
      notifications.putArray("original_url").add("http://www.opentracker.net/feature").add("http://www.opentracker.net/pricing");
      datasets.add(dataset);
      
      System.out.println(datasets.findParents("datasetId"));
      
      
      System.out.println(datasets.findValues("datasetId"));
      
      System.out.println(datasets.findValuesAsText("datasetId").contains("a1d90dccc04df83f26553dc753ed41f2"));
      
      
      ObjectMapper mapper = new ObjectMapper();
      mapper.enable(SerializationFeature.INDENT_OUTPUT);
      String ident = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
      System.out.println(ident);
   }

}

