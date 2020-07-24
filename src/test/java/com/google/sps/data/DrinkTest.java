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

import java.awt.geom.Point2D;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class DrinkTest {
  private final String DRINK_NAME = "Black Coffee";
  private final String ID_A = "Starbucks";
  private final String ID_B = "Dunkin Donuts";
  private final String ID_C = "Roasting Buddies";
  private final String ID_D = "McDonalds";
  private final String ID_E = "Krispy Kreme";
  private final String ID_F = "Burger King";
  private final String ID_G = "Some Other Place";
  private final String id = "id";
  private final double epsilon = 0.01;
  private final double ERROR_RETURN_VALUE = -Double.MAX_VALUE;
  private Drink drink;

  @Before
  public void setUp() {
    drink = new Drink(DRINK_NAME, id);

    Point2D ratingsA = new Point2D.Double(1.0, 5.0);
    Point2D ratingsB = new Point2D.Double(29.0, 2.5);
    Point2D ratingsC = new Point2D.Double(47.0, 3.1);
    Point2D ratingsD = new Point2D.Double(95678.0, 1.9);
    Point2D ratingsE = new Point2D.Double(1567.0, 4.4);
    Point2D ratingsF = new Point2D.Double(0.0, 0.0);
    Point2D ratingsG = new Point2D.Double(56.0, 3.5);

    drink.storeIDToAvgRating.put(ID_A, ratingsA);
    drink.storeIDToAvgRating.put(ID_B, ratingsB);
    drink.storeIDToAvgRating.put(ID_C, ratingsC);
    drink.storeIDToAvgRating.put(ID_D, ratingsD);
    drink.storeIDToAvgRating.put(ID_E, ratingsE);
    drink.storeIDToAvgRating.put(ID_F, ratingsF);
    drink.storeIDToAvgRating.put(ID_G, ratingsG);
  }

  @Test
  public void testDrinkName() {
    Assert.assertEquals(drink.getName(), DRINK_NAME);
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

  @Test
  public void testRoundToOneDecimalPlaceD() {
    double num = -1.937;
    double actual = drink.roundToOneDecimalPlace(num);
    double expected = 0.0;
    Assert.assertEquals(expected, actual, epsilon);
  }

  @Test
  public void testRoundToOneDecimalPlaceE() {
    double num = 0.0;
    double actual = drink.roundToOneDecimalPlace(num);
    double expected = 0.0;
    Assert.assertEquals(expected, actual, epsilon);
  }

  @Test
  public void testUpdateAverageRatingA() {
    double newRating = 1.0;
    double actual = drink.updateAverageRating(ID_A, newRating);
    double expected = 3.0;
    Assert.assertEquals(expected, actual, epsilon);
  }

  @Test
  public void testUpdateAverageRatingB() {
    double newRating = 5.0;
    double actual = drink.updateAverageRating(ID_B, newRating);
    double expected = 2.6;
    Assert.assertEquals(expected, actual, epsilon);
  }

  @Test
  public void testUpdateAverageRatingC() {
    double newRating = 2.0;
    double actual = drink.updateAverageRating(ID_C, newRating);
    double expected = 3.1;
    Assert.assertEquals(expected, actual, epsilon);
  }

  @Test
  public void testUpdateAverageRatingD() {
    double newRating = 3.0;
    double actual = drink.updateAverageRating(ID_D, newRating);
    double expected = 1.9;
    Assert.assertEquals(expected, actual, epsilon);
  }

  @Test
  public void testUpdateAverageRatingE() {
    double newRating = 4.0;
    double actual = drink.updateAverageRating(ID_E, newRating);
    double expected = 4.4;
    Assert.assertEquals(expected, actual, epsilon);
  }

  @Test
  public void testUpdateAverageRatingF() {
    double newRating = 1.0;
    double actual = drink.updateAverageRating(ID_F, newRating);
    double expected = 1.0;
    Assert.assertEquals(expected, actual, epsilon);
  }

  @Test
  public void testUpdateAverageRatingG() {
    double newRating = 0.0;
    double actual = drink.updateAverageRating(ID_G, newRating);
    double expected = ERROR_RETURN_VALUE;
    Assert.assertEquals(expected, actual, epsilon);
  }
}
