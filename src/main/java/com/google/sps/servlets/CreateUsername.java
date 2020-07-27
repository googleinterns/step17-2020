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

package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.gson.Gson;
import com.google.sps.data.*;
import java.io.IOException;
import java.util.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// this servlet will be used to create usernames for users
@WebServlet("/create-username")
public class CreateUsername extends HttpServlet {
  // This method takes input from the username box and stores it in datastore
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    Gson gson = new Gson();
    // look through all entities with "username" property through query

    String username = getParameter(request, "username", "");
    String email = getParameter(request, "email", "");

    Query query = new Query("User").addFilter("username", Query.FilterOperator.EQUAL, username);
    PreparedQuery results = getDatastore().prepare(query);
    for (Entity entity : results.asIterable()) {

      if (entity.getProperty("username").equals(username)) {
        String json = gson.toJson("This username has already been taken!");

        response.getWriter().println(json);
        return;
      }
    }
    User user = createUser(email, username);
    System.out.println("username created " + user.getEmail() + " " + user.getUsername());
    String json = gson.toJson("Your username was created, " + username);
    response.getWriter().println(json);
  }

  public static User createUser(String email, String username) {
    Entity userEntity = new Entity("User");
    userEntity.setProperty("email", email);
    userEntity.setProperty("username", username);
    getDatastore().put(userEntity);
    long id = userEntity.getKey().getId();
    return new User(id, email, username);
  }

  private static DatastoreService getDatastore() {
    return DatastoreServiceFactory.getDatastoreService();
  }

  public boolean isValidUsername(String input, HashSet set) {
    // retrieve a set of all usernames from datastore
    if (set.contains(input)) {
      return false;
    }
    return true;
  }

  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    return (value == null) ? defaultValue : value;
  }
}
