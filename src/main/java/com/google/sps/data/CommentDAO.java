// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.gson.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDAO {

  private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  /**
   * This function takes in a PreparedQuery object and converts each entity in the into a comment
   * object
   */
  private static List<Comment> convertQueryEntityToComment(PreparedQuery results) {
    List<Comment> comments = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
      long id = entity.getKey().getId();
      long rating = (long) entity.getProperty("rating");
      String drink = (String) entity.getProperty("drink");
      String content = (String) entity.getProperty("content");
      String store = (String) entity.getProperty("store");
      String email = (String) entity.getProperty("email");
      String storeInfo = (String) entity.getProperty("storeInfo");

      comments.add(new Comment(id, rating, drink, content, store, email, storeInfo));
    }
    return comments;
  }

  public static Comment storeComment(
      long rating, String drink, String content, String store, String email, String storeInfo) {
    Entity commentEntity = new Entity("Comment");

    commentEntity.setProperty("rating", rating);
    commentEntity.setProperty("drink", drink);
    commentEntity.setProperty("content", content);
    commentEntity.setProperty("store", store);
    commentEntity.setProperty("email", email);
    commentEntity.setProperty("storeInfo", storeInfo);
    datastore.put(commentEntity);
    long commentID = commentEntity.getKey().getId();
    return new Comment(commentID, rating, drink, content, store, email, storeInfo);
  }

  public static Comment getCommentByID(long commentID) {
    Query query =
        new Query("Comment")
            .addFilter(
                "__key__", Query.FilterOperator.EQUAL, KeyFactory.createKey("Comment", commentID));
    Entity commentEntity = datastore.prepare(query).asSingleEntity();

    if (commentEntity == null) {
      return null;
    }

    long rating = (long) commentEntity.getProperty("rating");
    String drink = (String) commentEntity.getProperty("drink");
    String content = (String) commentEntity.getProperty("content");
    String store = (String) commentEntity.getProperty("store");
    String email = (String) commentEntity.getProperty("email");
    String storeInfo = (String) commentEntity.getProperty("storeInfo");

    return new Comment(commentID, rating, drink, content, store, email, storeInfo);
  }

  public static List<Comment> getCommentByStore(String storeID) {
    Query query = new Query("Comment").addFilter("store", Query.FilterOperator.EQUAL, storeID);
    PreparedQuery results = datastore.prepare(query);
    return convertQueryEntityToComment(results);
  }

  public static List<Comment> getCommentByEmail(String email) {
    Query query = new Query("Comment").addFilter("email", Query.FilterOperator.EQUAL, email);
    PreparedQuery results = datastore.prepare(query);
    return convertQueryEntityToComment(results);
  }
}
