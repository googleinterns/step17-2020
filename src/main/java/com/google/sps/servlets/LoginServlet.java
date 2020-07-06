// // Copyright 2019 Google LLC
// //
// // Licensed under the Apache License, Version 2.0 (the "License");
// // you may not use this file except in compliance with the License.
// // You may obtain a copy of the License at
// //
// //     https://www.apache.org/licenses/LICENSE-2.0
// //
// // Unless required by applicable law or agreed to in writing, software
// // distributed under the License is distributed on an "AS IS" BASIS,
// // WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// // See the License for the specific language governing permissions and
// // limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login") // users are sent here when they are not logged in
public class LoginServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html");
    UserService userService = UserServiceFactory.getUserService();
    boolean isLoggedIn = userService.isUserLoggedIn();

    if (isLoggedIn) {
      System.out.println("logged in");
      String userEmail = userService.getCurrentUser().getEmail();
      String redirectUrlAfterLogOut = "/";
      String logoutUrl = userService.createLogoutURL(redirectUrlAfterLogOut);

      response.getWriter().println("<p>Hello " + userEmail + "!</p>");
      response.getWriter().println("<p>Logout <a href=\"" + logoutUrl + "\">here</a>.</p>");
    } else {
      System.out.println("not logged in");
      String redirectUrlAfterLogIn = "/";
      String loginUrl = userService.createLoginURL(redirectUrlAfterLogIn);

      response.getWriter().println("<p>Hello stranger.</p>");
      response.getWriter().println("<p>Login <a href=\"" + loginUrl + "\">here</a>.</p>");
    }
  }
  //   public void doGet(HttpServletRequest request, HttpServletResponse response) throws
  // IOException {
  //     response.setContentType("application/json");
  //     boolean isLoggedIn;

  //     UserService userService = UserServiceFactory.getUserService();
  //     isLoggedIn=userService.isUserLoggedIn();

  //     Gson gson = new Gson();
  //     String json = gson.toJson(isLoggedIn);
  //     response.getWriter().println(json);
  //   }

}
