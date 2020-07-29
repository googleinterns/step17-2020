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

import java.io.IOException;
import java.util.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// import org.json.simple.JSONArray;

// Servlet that handles comments
@WebServlet("/sort")
public class SortServlet extends HttpServlet {

  /** This method takes input from the comment box and stores it with the rest of the comments */
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Set<Drink> drinksSet = Drink.searchForDrink();
    // List<List<String>> coffeeShops = new ArrayList<>();
    // JSONArray array2D = (JSONArray) request.getParameter("coffeeshop");
    // if (Array2D != null) {
    //   int len = Array2D.length();
    //   for (int i = 0; i < len; i++) {
    //     JSONArray array1D = array2D.get(i);
    //     List<String> coffeeShop = new ArrayList<>();
    //     for (int j = 0; j < array1D.length(); j++) {
    //       coffeeShop.add(array1D.get(i).toString());
    //     }
    //   }
    //   coffeeShops.add(coffeeShop);
    // }
    // if (request.getParameter("byDistance").isEmpty()) {
    // } else {
    // }

    // Gson gson = new Gson();

    // response.setContentType("application/json;");
    // response.getWriter().println(gson.toJson(coffeeShops));
  }
}
