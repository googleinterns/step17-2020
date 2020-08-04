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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Drink implements Comparable<Drink> {

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

  @Override
  public int compareTo(Drink d) {
    return new Double(avgRating).compareTo(d.avgRating);
  }

  @Override
  public String toString() {
    return String.valueOf(avgRating);
  }

  public static List<Drink> searchForDrinkByRating(String name) {
    List<Drink> drinks = DrinkDAO.getDrinksByName(name);
    Collections.sort(drinks, Collections.reverseOrder());
    return drinks;
  }

  public static Set<Drink> searchForDrink(String name) {
    List<Drink> drinks = DrinkDAO.getDrinksByName(name);
    Set<Drink> drinksSet = new HashSet<Drink>();
    drinksSet.addAll(drinks);
    return drinksSet;
  }

  public Entity updateAverageRating(double newRating) {
    this.avgRating = ((this.avgRating * this.numRatings) + newRating) / (this.numRatings + 1);
    this.numRatings++;
    updateEntity();
    return this.drinkEntity;
  }

  public void updateEntity() {
    drinkEntity.setProperty("rating", this.avgRating);
    drinkEntity.setProperty("numRatings", this.numRatings);
  }

  public String getName() {
    return this.name;
  }

  public String getStore() {
    return this.storeID;
  }

  public double getNumRatings() {
    return this.numRatings;
  }

  public double getRating() {
    return this.avgRating;
  }

  public static double roundToOneDecimalPlace(double num) {
    return Math.round(num * 10) / 10.0;
  }
}
