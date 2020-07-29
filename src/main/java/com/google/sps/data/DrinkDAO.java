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

  // This method checks if a List containing Drinks with the same name and a List containing
  // Drinks from the same store have a Drink in common, indicating making a new Drink would
  // cause duplicates.
  private static Drink preventDuplicates(List<Drink> drinksByName, List<Drink> drinksByStore) {
    Drink drink = null;

    for (Drink d1 : drinksByName) {
      for (Drink d2 : drinksByStore) {
        String name1 = d1.getName();
        String name2 = d2.getName();

        if (name1.equals(name2)) {
          String store1 = d1.getStore();
          String store2 = d2.getStore();

          if (store1.equals(store2)) {
            drink = d1;
            return drink;
          }
        }
      }
    }
    return drink;
  }

  public static Drink saveDrink(String name, double avgRating, double numRatings, String storeID) {

    List<Drink> drinksByName = getDrinksByName(name);
    List<Drink> drinksByStore = getDrinksByStore(storeID);
    Drink drink = preventDuplicates(drinksByName, drinksByStore);

    // This means that executing the following lines below would create a new duplicate drink
    // Instead of doing that update the existing drink
    if (drink != null) {
      drink.updateAverageRating(avgRating);
      return drink;
    }

    Entity drinkEntity = new Entity("Drink");

    drinkEntity.setProperty("name", name);
    drinkEntity.setProperty("rating", avgRating);
    drinkEntity.setProperty("numRatings", numRatings);
    drinkEntity.setProperty("store", storeID);

    drinkDataStore.put(drinkEntity);

    return new Drink(name, storeID, avgRating, numRatings, drinkEntity);
  }

  public static List<Drink> getDrinksByStore(String storeID) {
    Query query = new Query("Drink").addFilter("store", Query.FilterOperator.EQUAL, storeID);
    return getDrinks(query);
  }

  public static List<Drink> getDrinksByName(String name) {
    Query query = new Query("Drink").addFilter("name", Query.FilterOperator.EQUAL, name);
    return getDrinks(query);
  }

  public static void updateEntity(Entity drinkEntity, double numRatings, double rating) {
    drinkEntity.setProperty("rating", rating);
    drinkEntity.setProperty("numRatings", numRatings);
  }
}
