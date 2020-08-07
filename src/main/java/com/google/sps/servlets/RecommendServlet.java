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

import com.google.gson.Gson;
import com.google.sps.data.*;
import java.io.IOException;
import java.util.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Servlet that handles recommendations
// This servlet will be used for order functionality
// so that when a user is requesting a beverage,
// this will return the best store to get this beverage from
// by using RecommendationEngine functions
@WebServlet("/recommend")
public class RecommendServlet extends HttpServlet {

  @Override
  /**
   * This method takes input from the beverageRequested box and puts it through the TYDRecommended
   * function TYDRecommended looks through stores near the user and looks for the best store to get
   * the user specified beverage from (via a weighting formula)
   */
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    Gson gson = new Gson();

    String userInputBeverages = request.getParameter("beverageRequested");
    String[] beverages = userInputBeverages.split(",");
    List<Map<String, String>> coffeeShops =
        gson.fromJson(request.getParameter("coffeeshop"), ArrayList.class);

    List<String> listStoreIds = new ArrayList<>(); // todo: get this list from the places api
    List<Drink> drinkList = new ArrayList<>();
    HashMap<String, String> beverageToStoreId = new HashMap<>();

    // adds all drinks from the storeid's we want into a list
    for (Map<String, String> storeToId : coffeeShops) {
      for (Drink drink : DrinkDAO.getDrinksByStore(storeToId.get("store"))) {
        listStoreIds.add(storeToId.get("store"));
        drinkList.add(drink);
      }
    }

    // puts the beverage into a hashmap which maps to its bestshop storeid
    for (String beverage : beverages) {
      beverage = beverage.trim();
      beverageToStoreId.put(
          beverage, RecommendationEngine.getBestShop(listStoreIds, beverage, drinkList));
    }

    StringBuilder jsonBuilder = new StringBuilder();
    beverageToStoreId.forEach(
        (key, value) ->
            jsonBuilder.append("Your " + key + " is coming from store: " + value).append("\n"));
    response.getWriter().println(gson.toJson(jsonBuilder.toString()));
  }
}
