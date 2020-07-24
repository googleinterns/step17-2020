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
  private final long drinkID;
  private HashMap<String, Point2D> storeIDtoAvgRating;

  Drink(String name, long drinkID) {
    this.name = name;
    this.drinkID = drinkID;
    this.storeIDtoAvgRating = new HashMap<>();
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

  /* TODO: COMPLETE THIS METHOD
  public double updateAverageRating(String store) {
    double averageRating = 0.0;

    for (int i = 0; i < storeIDtoAvgRating.size(); i++) averageRating += 1;

    return 0.0;
  }
  */

  public String getName() {
    return this.name;
  }

  public long getID() {
    return this.drinkID;
  }

  public double roundToOneDecimalPlace(double num) {
    return Math.round(num * 10) / 10.0;
  }
}
