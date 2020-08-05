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

import com.google.sps.data.*;
import java.io.IOException;
import java.util.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Servlet that handles recommendations
@WebServlet("/recommend")
public class RecommendServlet extends HttpServlet {

  @Override
  /**
   * This method takes input from the beverageRequested box and puts it through the TYDRecommended
   * function
   */
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String beverage = request.getParameter("beverageRequested");
    System.out.println(beverage);
    List<String> listStoreIds = new ArrayList<>();
    RecommendationEngine.getBestShop(listStoreIds, beverage);
  }
}
