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

public class CommentDatabase {

  private static DatastoreService getDatastore() {
    return DatastoreServiceFactory.getDatastoreService();
  }

  public static Comment createComment(
      long rating, String drink, String content, String store, String email) {
    Entity commentEntity = new Entity("Comment");

    commentEntity.setProperty("rating", rating);
    commentEntity.setProperty("drink", drink);
    commentEntity.setProperty("content", content);
    commentEntity.setProperty("store", store);
    commentEntity.setProperty("email", email);
    getDatastore().put(commentEntity);
    long commentID = commentEntity.getKey().getId();
    return new Comment(commentID, rating, drink, content);
  }

  public static Comment getCommentByID(long commentID) {
    Query query =
        new Query("Comment")
            .addFilter(
                "__key__", Query.FilterOperator.EQUAL, KeyFactory.createKey("Comment", commentID));
    Entity commentEntity = getDatastore().prepare(query).asSingleEntity();

    if (commentEntity == null) {
      return null;
    }

    long rating = (long) commentEntity.getProperty("rating");
    String drink = (String) commentEntity.getProperty("drink");
    String content = (String) commentEntity.getProperty("content");

    return new Comment(commentID, rating, drink, content);
  }

  public static List<Comment> getCommentByStore(String storeID) {
    Query query = new Query("Comment").addFilter("store", Query.FilterOperator.EQUAL, storeID);
    PreparedQuery results = getDatastore().prepare(query);

    List<Comment> commentByStore = new ArrayList<>();

    for (Entity entity : results.asIterable()) {
      long id = entity.getKey().getId();
      long rating = (long) entity.getProperty("rating");
      String drink = (String) entity.getProperty("drink");
      String content = (String) entity.getProperty("content");

      commentByStore.add(new Comment(id, rating, drink, content));
    }

    return commentByStore;
  }

  public static List<Comment> getCommentByEmail(String email) {
    Query query = new Query("Comment").addFilter("email", Query.FilterOperator.EQUAL, email);
    PreparedQuery results = getDatastore().prepare(query);

    List<Comment> commentByEmail = new ArrayList<>();

    for (Entity entity : results.asIterable()) {
      long id = entity.getKey().getId();
      long rating = (long) entity.getProperty("rating");
      String drink = (String) entity.getProperty("drink");
      String content = (String) entity.getProperty("content");

      commentByEmail.add(new Comment(id, rating, drink, content));
    }

    return commentByEmail;
  }
}
