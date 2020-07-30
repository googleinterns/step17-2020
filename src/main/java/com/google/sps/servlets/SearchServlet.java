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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// import org.json.simple.JSONArray;

// Servlet that handles search for drinks
@WebServlet("/search")
public class SearchServlet extends HttpServlet {

  /** This method takes input from the comment box and stores it with the rest of the comments */
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String drink = request.getParameter("drink");
    Gson gson = new Gson();
    List<HashMap<String, String>> coffeeShops =
        gson.fromJson(request.getParameter("coffeeshop"), ArrayList.class);

    if (request.getParameter("filter") == "By closest location") {
      Set<Drink> drinksSet = Drink.searchForDrink(drink);
      Set<String> stores =
          drinksSet.stream().map(Drink::getStore).collect(Collectors.toCollection(HashSet::new));
      System.out.println(drink);
      System.out.println("PRINTING THE LIST RETURNED BY DRINKDAO");
      System.out.println(drinksSet);
      ListIterator<HashMap<String, String>> itr = coffeeShops.iterator();
      for (ListIterator<HashMap<String, String>> itr = coffeeShops.iterator(); itr.hasNext(); ) {
        HashMap<String, String> coffeeShop = coffeeShops.next();
        if (!stores.contains(coffeeShop.get("store"))) {
          itr.remove();
        }
      }
      response.setContentType("application/json;");
      response.getWriter().println(gson.toJson(coffeeShops));
    } else {
      List<Drink> drinks = Drink.searchForDrinkByRating(drink);
      System.out.println(drink);
      System.out.println("PRINTING THE LIST RETURNED BY DRINKDAO");
      System.out.println(drinks);
    }
  }
}
