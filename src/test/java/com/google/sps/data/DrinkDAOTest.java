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
import java.util.Set;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class DrinkDAOTest {
  private final String DRINK_A = "black coffee";
  private final String DRINK_B = "latte";

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
  public void testGetDrinksByName() {
    Drink a = DrinkDAO.saveDrink(DRINK_A, ratingA, storeIDA);
    Drink b = DrinkDAO.saveDrink(DRINK_A, ratingB, storeIDB);
    Drink c = DrinkDAO.saveDrink(DRINK_B, ratingC, storeIDC);
    Drink d = DrinkDAO.saveDrink(DRINK_B, ratingD, storeIDD);

    List<Drink> drinks = DrinkDAO.getDrinksByName(DRINK_A);
    Assert.assertEquals(drinks.size(), 2);
    for (Drink drink : drinks) {
      Assert.assertTrue(drink.getName().equals(DRINK_A));
    }
    drinks = DrinkDAO.getDrinksByName(DRINK_B);
    Assert.assertEquals(drinks.size(), 2);
    for (Drink drink : drinks) {
      Assert.assertTrue(drink.getName().equals(DRINK_B));
    }
  }

  @Test
  public void testSearchForDrinkByRating() {
    Drink a = DrinkDAO.saveDrink(DRINK_A, ratingA, storeIDA);
    Drink b = DrinkDAO.saveDrink(DRINK_A, ratingB, storeIDB);
    Drink c = DrinkDAO.saveDrink(DRINK_A, ratingC, storeIDC);
    Drink d = DrinkDAO.saveDrink(DRINK_A, ratingD, storeIDD);
    Drink e = DrinkDAO.saveDrink(DRINK_A, ratingE, storeIDE);
    Drink f = DrinkDAO.saveDrink(DRINK_B, ratingA, storeIDA);
    Drink g = DrinkDAO.saveDrink(DRINK_B, ratingB, storeIDB);

    List<Drink> drinks = Drink.searchForDrinkByRating(DRINK_A);
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

    drinks = Drink.searchForDrinkByRating(DRINK_B);
    resultA = drinks.get(0);
    resultB = drinks.get(1);
    Assert.assertEquals(a.getStore(), resultA.getStore());
    Assert.assertEquals(b.getStore(), resultB.getStore());
  }

  @Test
  public void testSearchForDrink() {
    Drink a = DrinkDAO.saveDrink(DRINK_A, ratingA, storeIDA);
    Drink b = DrinkDAO.saveDrink(DRINK_A, ratingB, storeIDB);
    Drink c = DrinkDAO.saveDrink(DRINK_B, ratingC, storeIDC);
    Drink d = DrinkDAO.saveDrink(DRINK_B, ratingD, storeIDD);

    Set<Drink> drinks = Drink.searchForDrink(DRINK_A);
    for (Drink drink : drinks) {
      Assert.assertTrue(drink.getName().equals(DRINK_A));
      Assert.assertTrue((drink.getStore().equals(storeIDA)) || (drink.getStore().equals(storeIDB)));
    }
    drinks = Drink.searchForDrink(DRINK_B);
    for (Drink drink : drinks) {
      Assert.assertTrue(drink.getName().equals(DRINK_B));
      Assert.assertTrue((drink.getStore().equals(storeIDC)) || (drink.getStore().equals(storeIDD)));
    }
  }

  @Test
  public void testUpdateAverageRating() {
    Drink a = DrinkDAO.saveDrink(DRINK_A, ratingA, storeIDA);
    for (int i = 0; i < 10; i++) {
      a.updateAverageRating(NEW_RATING);
    }
    // 10 ratings of 5.0 and 1 rating of 2.5
    double expected = 4.7727;
    Assert.assertEquals(expected, a.getRating(), EPSILON);
  }
}
