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
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/store-rating")
public class StoreRatingServlet extends HttpServlet {

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

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    Gson gson = new Gson();
    String json = gson.toJson(votes);
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String rating = request.getParameter("store-rating");
    int currentVotes = votes.containsKey(rating) ? votes.get(rating) : 0;
    votes.put(rating, currentVotes + 1);

    response.sendRedirect("coffeeshop.html");
  }
}
