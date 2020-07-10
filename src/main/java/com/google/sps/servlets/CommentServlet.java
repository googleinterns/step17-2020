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
import com.google.sps.data.Comment;
import com.google.sps.data.CommentDatabase;
import java.io.IOException;
import java.util.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Servlet that handles comments
@WebServlet("/comment")
public class CommentServlet extends HttpServlet {
  /* This method takes comments, timestamps and the id of comments and creates a Task object.
   * It then converts the Task object to a json.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    List<Comment> comments = CommentDatabase.getCommentByStore(getParameter(request, "store", ""));
    System.out.println(comments);

    // for (Entity entity : results.asIterable()) {
    //   long id = entity.getKey().getId();
    //   long timestamp = (long) entity.getProperty("timestamp");
    //   String comment = (String) entity.getProperty("comment");

    //   Task task = new Task(id, timestamp, comment);
    //   tasks.add(task);
    // }

    Gson gson = new Gson();

    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(comments));
  }

  // This method takes input from the comment box and stores it with the rest of the comments
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    System.out.println(getParameter(request, "drink", ""));
    System.out.println(getParameter(request, "rating", ""));
    String drink = getParameter(request, "drink", "");
    drink = drink.toLowerCase();
    long rating = Integer.parseInt(getParameter(request, "rating", ""));
    String content = getParameter(request, "content", "");
    String store = getParameter(request, "store", "");

    Comment comment = CommentDatabase.createComment(rating, drink, content, store);
    System.out.printf("%s %s\n", comment.getDrink(), comment.getContent());

    // response.sendRedirect("/coffeeshop.html");
  }

  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    return (value == null) ? defaultValue : value;
  }
}
