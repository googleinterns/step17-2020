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
import java.util.*;
import java.util.stream.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class RecommendationEngineTest {
  private final Drink DRINKA = new Drink("latte", "123", 4.8, 12, null);
  private final Drink DRINKB = new Drink("cold brew", "123", 4.8, 12, null);
  private final Drink DRINKC = new Drink("latte", "456", 4.2, 12, null);
  private final Drink DRINKD = new Drink("cold brew", "456", 4.2, 12, null);
  private final Drink DRINKE = new Drink("latte", "789", 1.5, 12, null);
  private final Drink DRINKF = new Drink("cold brew", "789", 1.5, 12, null);
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
    drinkList.add(DRINKA);
    drinkList.add(DRINKB);
    drinkList.add(DRINKC);
    drinkList.add(DRINKD);
    drinkList.add(DRINKE);
    drinkList.add(DRINKF);
    List<String> idList = new ArrayList<>();
    idList = drinkList.stream().map(Drink::getStore).collect(Collectors.toList());
    Assert.assertEquals(storeIdAB, RecommendationEngine.getBestShop(idList, "latte", drinkList));
    Assert.assertEquals(
        storeIdAB, RecommendationEngine.getBestShop(idList, "cold brew", drinkList));
    drinkList.clear();
  }

  @Test
  public void testGetScore() {
    Assert.assertEquals(75.8, RecommendationEngine.getScore(DRINKA), DELTA);
    Assert.assertEquals(75.8, RecommendationEngine.getScore(DRINKB), DELTA);
    Assert.assertEquals(71.6, RecommendationEngine.getScore(DRINKC), DELTA);
    Assert.assertEquals(71.6, RecommendationEngine.getScore(DRINKD), DELTA);
    Assert.assertEquals(52.7, RecommendationEngine.getScore(DRINKE), DELTA);
    Assert.assertEquals(52.7, RecommendationEngine.getScore(DRINKE), DELTA);
  }

  @Test
  public void testGetDrink() {
    drinkList.add(DRINKA);
    drinkList.add(DRINKB);
    drinkList.add(DRINKC);
    drinkList.add(DRINKD);
    drinkList.add(DRINKE);
    drinkList.add(DRINKF);
    Assert.assertEquals(DRINKA, RecommendationEngine.getDrink(storeIdAB, "latte", drinkList).get());
    drinkList.clear();
  }
}
