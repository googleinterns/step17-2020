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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class DrinkTest {
  private final String name = "DrinkTest";
  private final long id = 1;
  private final double epsilon = 0.01;
  private Drink drink;

  @Before
  public void setUp() {
    drink = new Drink(name, id);
  }

  @Test
  public void testDrinkName() {
    Assert.assertEquals(drink.getName(), name);
  }

  @Test
  public void testDrinkLong() {
    Assert.assertEquals(drink.getID(), id);
  }

  @Test
  public void testRoundToOneDecimalPlaceA() {
    double num = 1.31;
    double actual = drink.roundToOneDecimalPlace(num);
    double expected = 1.3;
    Assert.assertEquals(expected, actual, epsilon);
  }

  @Test
  public void testRoundToOneDecimalPlaceB() {
    double num = 1.999;
    double actual = drink.roundToOneDecimalPlace(num);
    double expected = 2.0;
    Assert.assertEquals(expected, actual, epsilon);
  }

  @Test
  public void testRoundToOneDecimalPlaceC() {
    double num = 1.937;
    double actual = drink.roundToOneDecimalPlace(num);
    double expected = 1.9;
    Assert.assertEquals(expected, actual, epsilon);
  }
}
