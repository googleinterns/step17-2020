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
import java.util.HashMap;

public class Drink {

  private final String name;
  private final String id;
  public HashMap<String, Point2D> storeIDToAvgRating;

  Drink(String name, String id) {
    this.name = name;
    this.id = id;
    this.storeIDToAvgRating = new HashMap<>();
  }

  // TODO: COMPLETE THIS METHOD
  public Drink searchForDrink(String name) {
    /*
     for (min(all stores y returned by Google Map, HashSet of storeID)) {
    Check if the HashSet (or Google Map result) contains y
        If yes {
         Get y location;
         Calculate distance/ Retrieving rating
        }
       }
     */
    return null;
  }

  // TODO: DATASTORE

  public double updateAverageRating(String id, double newRating) {
    Point2D ratings = storeIDToAvgRating.get(id);
    double numRatings = ratings.getX();
    double averageRating = ratings.getY();
    double errorReturnValue = -Double.MAX_VALUE;

    if (newRating == 0.0) {
      return errorReturnValue;
    }

    if (numRatings == 0.0) {
      ratings.setLocation(1.0, newRating);
      storeIDToAvgRating.put(id, ratings);
      return newRating;
    }

    averageRating = ((averageRating * numRatings) + newRating) / (numRatings + 1);
    averageRating = roundToOneDecimalPlace(averageRating);
    ratings.setLocation(numRatings + 1, averageRating);

    return averageRating;
  }

  public String getName() {
    return this.name;
  }

  public String getID() {
    return this.id;
  }

  public double roundToOneDecimalPlace(double num) {
    if (num < 1.0) return 0.0;

    return Math.round(num * 10) / 10.0;
  }
}
