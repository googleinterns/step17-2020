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
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class RecommendTest {
  private Drink DRINKA = new Drink("latte", "12345", 4.2, 12, null);
  private Drink DRINKB = new Drink("cold brew", "54321", 4.8, 12, null);
  private Drink DRINKC = new Drink("latte", "12345", 4.2, 12, null);
  private Drink DRINKD = new Drink("cold brew", "54321", 4.8, 12, null);

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
    List<Drink> drinkList = new ArrayList<Drink>();
    drinkList.add(DRINKA);
    drinkList.add(DRINKB);
    drinkList.add(DRINKC);
    drinkList.add(DRINKD);
    List<String> idList = new ArrayList<String>();
    for (Drink drink : drinkList) {
      idList.add(drink.getStore());
    }
    Assert.assertEquals(
        Recommended.getBestShop(idList, "latte"), Recommended.getBestShop(idList, "cold brew"));
  }

  @Test
  public void testGetScore() {

    Assert.assertEquals(Recommended.getScore(DRINKA), Recommended.getScore(DRINKC));
    Assert.assertEquals(Recommended.getScore(DRINKB), Recommended.getScore(DRINKD));
  }
}
