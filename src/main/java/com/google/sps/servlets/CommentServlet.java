// Copyright 2020 Google LLC
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

package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Servlet that handles comments
@WebServlet("/make-comment")
public class CommentServlet extends HttpServlet {
  /* This method takes comments, timestamps and the id of comments and creates a Task object.
   * It then converts the Task object to a json.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("Task").addSort("timestamp", SortDirection.DESCENDING);

    DatastoreService savedComments = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = savedComments.prepare(query);

    List<Task> tasks = new ArrayList<>();

    for (Entity entity : results.asIterable()) {
      long id = entity.getKey().getId();
      long timestamp = (long) entity.getProperty("timestamp");
      String comment = (String) entity.getProperty("comment");

      Task task = new Task(id, timestamp, comment);
      tasks.add(task);
    }

    Gson gson = new Gson();

    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(tasks));
  }

  // This method takes input from the comment box and stores it with the rest of the comments
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String comment = getParameter(request, "text-input", "");
    long timestamp = System.currentTimeMillis();

    Entity taskEntity = new Entity("Task");
    taskEntity.setProperty("comment", comment);
    taskEntity.setProperty("timestamp", timestamp);

    DatastoreService savedComments = DatastoreServiceFactory.getDatastoreService();
    savedComments.put(taskEntity);

    response.sendRedirect("/index.html");
  }

  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    return (value == null) ? defaultValue : value;
  }
}

// The Task class is used to store and group information that will be used for comments
class Task {
  private final long id;
  private final long timestamp;
  private final String comment;

  public Task(long id, long timestamp, String comment) {
    this.id = id;
    this.timestamp = timestamp;
    this.comment = comment;
  }
}
