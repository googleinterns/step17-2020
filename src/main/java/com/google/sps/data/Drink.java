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

import com.google.appengine.api.datastore.Entity;
import com.google.gson.*;
import java.util.List;

public class Drink {

  private final String name;
  private final String storeID;
  private double avgRating;
  private double numRatings;
  Entity drinkEntity;

  Drink(String name, String storeID, double avgRating, double numRatings, Entity drinkEntity) {
    this.name = name;
    this.storeID = storeID;
    this.avgRating = avgRating;
    this.numRatings = numRatings;
    this.drinkEntity = drinkEntity;
  }

  /*
  public HashMap<Long, Point2D> searchForDrink(String name) {
    Drink drink = DrinkDAO.getDrinksByName(this.nameToDrinkID.get(name));

    return drink.storeIDToAvgRating;
  }*/

  public double updateAverageRating(String store, String name, double newRating) {
    List<Drink> drinks = DrinkDAO.getDrinksByStore(store);
    double errorReturnValue = -Double.MAX_VALUE;

    if (drinks == null || newRating == 0.0) {
      return errorReturnValue;
    }

    Drink drink = null;

    for (Drink d : drinks) {
      if (name.equals(d.name)) {
        drink = d;
        break;
      }
    }

    drink.avgRating = ((drink.avgRating * drink.numRatings) + newRating) / (drink.numRatings + 1);
    drink.numRatings++;
    DrinkDAO.updateEntity(drinkEntity, drink.numRatings, drink.avgRating);

    return drink.avgRating;
  }

  public String getName() {
    return this.name;
  }

  public String getStore() {
    return this.storeID;
  }

  public double roundToOneDecimalPlace(double num) {
    if (num < 1.0) return 0.0;

    return Math.round(num * 10) / 10.0;
  }
}
