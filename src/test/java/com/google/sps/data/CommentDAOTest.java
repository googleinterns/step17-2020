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

package com.google.sps.data;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class CommentDAOTest {
  private static final long RATING_A = 4;
  private static final String DRINK_A = "Vanilla Latte";
  private static final String CONTENT_A = "Very good.";
  private static final String STORE_A = "21a0b251c9b8392186142c798263e289fe45b4aa";
  private static final String EMAIL_A = "testA@example.com";
  private static final long RATING_B = 3;
  private static final String DRINK_B = "Cold Brew";
  private static final String CONTENT_B = "Good.";
  private static final String EMAIL_B = "testB@example.com";
  private static final long RATING_C = 5;
  private static final String DRINK_C = "Water";
  private static final String CONTENT_C = "Excellent.";
  private static final String STORE_C = "21a0b251c9b8392186142c798263e289fe45b4ab";

  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  @Before
  public void setUp() {
    helper.setUp();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void testGetCommentByID() {
    Comment commentA = CommentDAO.storeComment(RATING_A, DRINK_A, CONTENT_A, STORE_A, EMAIL_A);
    Comment commentFromDB = CommentDAO.getCommentByID(commentA.getId());
    Assert.assertEquals(commentA.getRating(), commentFromDB.getRating());
    Assert.assertEquals(commentA.getDrink(), commentFromDB.getDrink());
    Assert.assertEquals(commentA.getContent(), commentFromDB.getContent());
  }

  @Test
  public void testGetCommentByStore() {
    Comment commentA = CommentDAO.storeComment(RATING_A, DRINK_A, CONTENT_A, STORE_A, EMAIL_A);
    Comment commentB = CommentDAO.storeComment(RATING_B, DRINK_B, CONTENT_B, STORE_A, EMAIL_A);
    Comment commentC = CommentDAO.storeComment(RATING_C, DRINK_C, CONTENT_C, STORE_C, EMAIL_A);
    Comment commentD = CommentDAO.storeComment(RATING_C, DRINK_C, CONTENT_C, STORE_C, EMAIL_A);
    List<Comment> comments = CommentDAO.getCommentByStore(STORE_A);
    Assert.assertEquals(comments.size(), 2);
    for (Comment comment : comments) {
      Assert.assertEquals(comment.getStore(), STORE_A);
    }
  }

  @Test
  public void testGetCommentByEmail() {
    Comment commentA = CommentDAO.storeComment(RATING_A, DRINK_A, CONTENT_A, STORE_A, EMAIL_A);
    Comment commentB = CommentDAO.storeComment(RATING_B, DRINK_B, CONTENT_B, STORE_A, EMAIL_B);
    Comment commentC = CommentDAO.storeComment(RATING_C, DRINK_C, CONTENT_C, STORE_C, EMAIL_B);
    List<Comment> comments = CommentDAO.getCommentByEmail(EMAIL_B);
    Assert.assertEquals(comments.size(), 2);
    for (Comment comment : comments) {
      Assert.assertEquals(comment.getEmail(), EMAIL_B);
    }
  }

  @Test
  public void testGetCommentWithDuplicateComment() {
    Comment commentA = CommentDAO.storeComment(RATING_A, DRINK_A, CONTENT_A, STORE_A, EMAIL_A);
    Comment commentB = CommentDAO.storeComment(RATING_A, DRINK_A, CONTENT_A, STORE_A, EMAIL_A);
    List<Comment> commentByStore = CommentDAO.getCommentByStore(STORE_A);
    Assert.assertEquals(commentByStore.size(), 2);
    for (Comment comment : commentByStore) {
      Assert.assertEquals(comment.getStore(), STORE_A);
    }
    List<Comment> commentByEmail = CommentDAO.getCommentByEmail(EMAIL_A);
    Assert.assertEquals(commentByEmail.size(), 2);
    for (Comment comment : commentByEmail) {
      Assert.assertEquals(comment.getEmail(), EMAIL_A);
    }
  }
}
