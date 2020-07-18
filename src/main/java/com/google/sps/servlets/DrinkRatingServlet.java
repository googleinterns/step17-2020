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

// Servlet to process users rating a drink from a coffee shop
@WebServlet("/drink-rating")
public class DrinkRatingServlet extends HttpServlet {

  private final String maxRating = "/5.0";
  private List<String> knownDrinks = new ArrayList<>();
  public Map<String, Integer> votes = createMap();

  private Map<String, Integer> createMap() {
    Map<String, Integer> map = new LinkedHashMap<>();

    map.put("1", 0);
    map.put("2", 0);
    map.put("3", 0);
    map.put("4", 0);
    map.put("5", 0);

    return map;
  }

  private double getAverageRating(Map<String, Integer> votes) {

    int totalRatings = 0;
    int totalVotes = 0;

    for (int i = 1; i <= votes.size(); i++) {
      String str = Integer.toString(i);
      totalRatings = i * votes.get(str);
      totalVotes += votes.get(str);
    }

    double average = (double) (totalRatings / totalVotes);
    average = Math.round(average * 10) / 10.0;
    return average;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    Query query = new Query("Drink").addSort("averageRating", SortDirection.DESCENDING);

    DatastoreService savedRatings = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = savedRatings.prepare(query);

    List<Drink> drinks = new ArrayList<>();

    for (Entity entity : results.asIterable()) {
      double averageRating = (double) entity.getProperty("averageRating");
      String name = (String) entity.getProperty("name");

      if (!knownDrinks.contains(name)) {
        String str = name + " : " + Double.toString(averageRating) + maxRating;
        Drink drink = new Drink(votes, name, averageRating, str);
        drinks.add(drink);
      }
    }
    response.setContentType("application/json");
    Gson gson = new Gson();
    String json = gson.toJson(drinks);
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    String name = getParameter(request, "drink", "");
    name = name.toLowerCase();
    String rating = getParameter(request, "drink-rating", "");

    int currentVotes = votes.containsKey(rating) ? votes.get(rating) : 0;
    votes.put(rating, currentVotes + 1);
    double averageRating = getAverageRating(votes);

    Entity drinkEntity = new Entity("Drink");
    drinkEntity.setProperty("name", name);
    drinkEntity.setProperty("averageRating", averageRating);

    DatastoreService savedComments = DatastoreServiceFactory.getDatastoreService();
    savedComments.put(drinkEntity);

    // Respond with the result.
    response.sendRedirect("coffeeshop.html");
  }

  /**
   * @return the request parameter, or the default value if the parameter was not specified by the
   *     client
   */
  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
}

class Drink {

  Map<String, Integer> votes;
  private final String name;
  String ratingString;
  double averageRating;

  Drink(Map<String, Integer> votes, String name, double averageRating, String ratingString) {

    this.name = name;
    this.averageRating = averageRating;
    this.votes = votes;
    this.ratingString = ratingString;
  }
}
