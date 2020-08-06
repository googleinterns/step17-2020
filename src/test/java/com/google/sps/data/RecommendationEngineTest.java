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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class RecommendationEngineTest {
  private static final Drink DRINK_A = new Drink("latte", "123", 4.8, 12, null);
  private static final Drink DRINK_B = new Drink("cold brew", "123", 4.8, 12, null);
  private static final Drink DRINK_C = new Drink("latte", "456", 4.2, 12, null);
  private static final Drink DRINK_D = new Drink("cold brew", "456", 4.2, 12, null);
  private static final Drink DRINK_E = new Drink("latte", "789", 1.5, 12, null);
  private static final Drink DRINK_F = new Drink("cold brew", "789", 1.5, 12, null);
  private List<Drink> drinkList = new ArrayList<>();
  private static final double DELTA = .0000001;
  private String storeIdAB = "123";

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
  public void testGetBestShop() {
    drinkList.add(DRINK_A);
    drinkList.add(DRINK_B);
    drinkList.add(DRINK_C);
    drinkList.add(DRINK_D);
    drinkList.add(DRINK_E);
    drinkList.add(DRINK_F);
    List<String> idList = drinkList.stream().map(Drink::getStore).collect(Collectors.toList());

    Assert.assertEquals(storeIdAB, RecommendationEngine.getBestShop(idList, "latte", drinkList));
    Assert.assertEquals(
        storeIdAB, RecommendationEngine.getBestShop(idList, "cold brew", drinkList));
    drinkList.clear();
  }

  @Test
  public void testGetScore() {
    Assert.assertEquals(75.8, RecommendationEngine.getScore(DRINK_A), DELTA);
    Assert.assertEquals(75.8, RecommendationEngine.getScore(DRINK_B), DELTA);
    Assert.assertEquals(71.6, RecommendationEngine.getScore(DRINK_C), DELTA);
    Assert.assertEquals(71.6, RecommendationEngine.getScore(DRINK_D), DELTA);
    Assert.assertEquals(52.7, RecommendationEngine.getScore(DRINK_E), DELTA);
    Assert.assertEquals(52.7, RecommendationEngine.getScore(DRINK_E), DELTA);
  }

  @Test
  public void testGetDrink() {
    drinkList.add(DRINK_A);
    drinkList.add(DRINK_B);
    drinkList.add(DRINK_C);
    drinkList.add(DRINK_D);
    drinkList.add(DRINK_E);
    drinkList.add(DRINK_F);
    Assert.assertEquals(
        DRINK_A, RecommendationEngine.getDrink(storeIdAB, "latte", drinkList).get());
    drinkList.clear();
  }
}
