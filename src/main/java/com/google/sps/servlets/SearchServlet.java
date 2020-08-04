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
import com.google.sps.data.Drink;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Servlet that handles search for drinks
@WebServlet("/search")
public class SearchServlet extends HttpServlet {

  /** This method takes input from the search bar and returns relevant stores' information */
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get drink name entered in the search bar
    String name = request.getParameter("drink");
    Gson gson = new Gson();
    // Convert Json stringified JavaScript array into an ArrayList of nearby coffee shops
    List<Map<String, String>> coffeeShops =
        gson.fromJson(request.getParameter("coffeeshop"), ArrayList.class);

    if (request.getParameter("filter").equals("By Distance")) {
      searchDrinkByDistance(coffeeShops, name);
      response.setContentType("application/json;");
      response.getWriter().println(gson.toJson(coffeeShops));

    } else {
      List<Map<String, String>> intersection = searchDrinkByRating(coffeeShops, name);
      response.setContentType("application/json;");
      response.getWriter().println(gson.toJson(intersection));
    }
  }

  private void searchDrinkByDistance(List<Map<String, String>> coffeeShops, String name) {
    Set<Drink> drinksSet = Drink.searchForDrink(name);
    // A set of store IDs that contains the drink
    Set<String> stores = drinksSet.stream().map(Drink::getStore).collect(Collectors.toSet());

    for (Iterator<Map<String, String>> itr = coffeeShops.iterator(); itr.hasNext(); ) {
      Map<String, String> coffeeShop = itr.next();

      // Check if the nearby coffee shop is in the set of stores returned by searchForDrink
      if (!stores.contains(coffeeShop.get("store"))) {
        itr.remove();
      }
    }
    return;
  }

  private List<Map<String, String>> searchDrinkByRating(
      List<Map<String, String>> coffeeShops, String name) {
    // Create a intersection between search results and nearby coffee shops
    List<Map<String, String>> intersection = new ArrayList<>();
    List<Drink> drinks = Drink.searchForDrinkByRating(name);

    // Iterate through the search results to see if each store is in the list of nearby coffee
    // shops
    for (Iterator<Drink> drinkItr = drinks.iterator(); drinkItr.hasNext(); ) {
      Drink drink = drinkItr.next();

      for (Iterator<Map<String, String>> itr = coffeeShops.iterator(); itr.hasNext(); ) {
        Map<String, String> coffeeShop = itr.next();

        if (coffeeShop.get("store").equals(drink.getStore())) {
          coffeeShop.put(
              "rating", Double.toString(Drink.roundToOneDecimalPlace(drink.getRating())));
          intersection.add(coffeeShop);
          break;
        }
      }
    }
    return intersection;
  }
}
