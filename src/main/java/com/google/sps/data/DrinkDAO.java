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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.gson.*;
import java.util.ArrayList;
import java.util.List;

public class DrinkDAO {

  private static DatastoreService drinkDataStore = DatastoreServiceFactory.getDatastoreService();

  private static List<Drink> getDrinks(Query query) {
    PreparedQuery results = drinkDataStore.prepare(query);
    List<Drink> drinks = new ArrayList<>();

    for (Entity drinkEntity : results.asIterable()) {
      double numRatings = (double) drinkEntity.getProperty("numRatings");
      double avgRating = (double) drinkEntity.getProperty("rating");
      String storeID = (String) drinkEntity.getProperty("store");
      String name = (String) drinkEntity.getProperty("name");

      drinks.add(new Drink(name, storeID, avgRating, numRatings, drinkEntity));
    }

    return drinks;
  }

  public static Drink saveDrink(String name, double newRating, String storeID) {
    Query query =
        new Query("Drink")
            .addFilter("store", Query.FilterOperator.EQUAL, storeID)
            .addFilter("name", Query.FilterOperator.EQUAL, name);
    List<Drink> drinks = getDrinks(query);

    // Check if the drink in that store is already saved in datastore
    // If not, add a new entity
    if (drinks.isEmpty()) {
      Entity drinkEntity = new Entity("Drink");

      drinkEntity.setProperty("name", name);
      drinkEntity.setProperty("rating", newRating);
      drinkEntity.setProperty("numRatings", 1.0);
      drinkEntity.setProperty("store", storeID);

      drinkDataStore.put(drinkEntity);
      return new Drink(name, storeID, newRating, 1.0, drinkEntity);

      // If yes, update the average rating of the drink
    } else if (drinks.size() == 1) {
      Drink drink = drinks.get(0);
      drinkDataStore.put(drink.updateAverageRating(newRating));
      return drink;

    } else {
      throw new RuntimeException("Should not have multiple drink entities for 1 drink in 1 store");
    }
  }

  public static List<Drink> getDrinksByStore(String storeID) {
    Query query = new Query("Drink").addFilter("store", Query.FilterOperator.EQUAL, storeID);
    return getDrinks(query);
  }

  public static List<Drink> getDrinksByName(String name) {
    Query query = new Query("Drink").addFilter("name", Query.FilterOperator.EQUAL, name);
    return getDrinks(query);
  }
}
