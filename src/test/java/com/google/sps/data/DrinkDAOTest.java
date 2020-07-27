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
public final class DrinkDAOTest {
  private final String DRINK_NAME = "black coffee";

  private final String storeIDA = "A";
  private final String storeIDB = "B";
  private final String storeIDC = "C";
  private final String storeIDD = "D";
  private final String storeIDE = "E";

  private double numRatingsA = 29.0;
  private double numRatingsB = 16.0;
  private double numRatingsC = 48.0;
  private double numRatingsD = 5.0;
  private double numRatingsE = 33.0;

  private double ratingA = 2.5;
  private double ratingB = 1.6;
  private double ratingC = 4.1;
  private double ratingD = 5.0;
  private double ratingE = 3.2;

  private final double NEW_RATING = 5.0;
  private final double EPSILON = 0.001;

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
  public void testSearchForDrink() {
    Drink a = DrinkDAO.saveDrink(DRINK_NAME, ratingA, numRatingsA, storeIDA);
    Drink b = DrinkDAO.saveDrink(DRINK_NAME, ratingB, numRatingsB, storeIDB);
    Drink c = DrinkDAO.saveDrink(DRINK_NAME, ratingC, numRatingsC, storeIDC);
    Drink d = DrinkDAO.saveDrink(DRINK_NAME, ratingD, numRatingsD, storeIDD);
    Drink e = DrinkDAO.saveDrink(DRINK_NAME, ratingE, numRatingsE, storeIDE);

    List<Drink> drinks = Drink.searchForDrink(DRINK_NAME);
    Drink resultD = drinks.get(0);
    Drink resultC = drinks.get(1);
    Drink resultE = drinks.get(2);
    Drink resultA = drinks.get(3);
    Drink resultB = drinks.get(4);

    Assert.assertEquals(d.getStore(), resultD.getStore());
    Assert.assertEquals(c.getStore(), resultC.getStore());
    Assert.assertEquals(e.getStore(), resultE.getStore());
    Assert.assertEquals(a.getStore(), resultA.getStore());
    Assert.assertEquals(b.getStore(), resultB.getStore());
  }

  @Test
  public void testUpdateAverageRating() {
    Drink a = DrinkDAO.saveDrink(DRINK_NAME, ratingA, numRatingsA, storeIDA);
    a.updateAverageRating(NEW_RATING);
    double expected = 2.5833;
    Assert.assertEquals(expected, a.getRating(), EPSILON);
  }
}
