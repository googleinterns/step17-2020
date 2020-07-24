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
/*
package com.google.sps.data;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class DrinkDAOTest {

  private final String DRINK_NAME = "black coffee";
  private final String DRINK_NAME_B = "latte";
  private final String DRINK_NAME_C = "water";
  private final String DRINK_NAME_D = "frappacino";

  private static final double EPSILON = 0.001;

  private static final double RATING = 5.0;
  private static final double NUM_RATINGS = 1.0;
  private static final double NEW_RATING = 1.0;
  private static final long STORE_ID_A = 1;
  private static final long STORE_ID_B = 2;
  private static final long STORE_ID_C = 3;
  private static final long STORE_ID_D = 4;
  private static final long STORE_ID_E = 5;

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
  public void testUpdateAverageRating() {
    Drink drink = DrinkDAO.saveDrink(DRINK_NAME, RATING, NUM_RATINGS, STORE_ID_A);
    double actual = drink.updateAverageRating(STORE_ID_A, NEW_RATING);
    double expected = 3.0;
    Assert.assertEquals(actual, expected, EPSILON);
  }

  @Test
  public void testGetStoresWithDrink() {
    Drink drinkA = DrinkDAO.saveDrink(DRINK_NAME, RATING, NUM_RATINGS, STORE_ID_A);
    Drink drinkB = DrinkDAO.getDrinkByID(drinkA.getID());
    Assert.assertEquals(drinkA.getName(), drinkB.getName());
  }
}
*/
