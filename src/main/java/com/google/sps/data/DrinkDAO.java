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

  private static DatastoreService drinkDAO = getDatastore();

  private static DatastoreService getDatastore() {
    return DatastoreServiceFactory.getDatastoreService();
  }

  public static Drink createDrink(
      String name, double avgRating, double numRatings, String storeID) {
    Entity drinkEntity = new Entity("Drink");

    drinkEntity.setProperty("name", name);
    drinkEntity.setProperty("rating", avgRating);
    drinkEntity.setProperty("numRatings", numRatings);
    drinkEntity.setProperty("store", storeID);

    drinkDAO.put(drinkEntity);

    return new Drink(name, storeID, avgRating, numRatings, drinkEntity);
  }

  public static List<Drink> getDrinksByStore(String storeID) {
    Query query = new Query("Drink").addFilter("store", Query.FilterOperator.EQUAL, storeID);
    PreparedQuery results = getDatastore().prepare(query);

    List<Drink> drinksByStore = new ArrayList<>();

    for (Entity drinkEntity : results.asIterable()) {
      double numRatings = (double) drinkEntity.getProperty("numRatings");
      double avgRating = (double) drinkEntity.getProperty("rating");
      String name = (String) drinkEntity.getProperty("name");

      drinksByStore.add(new Drink(name, storeID, avgRating, numRatings, drinkEntity));
    }

    return drinksByStore;
  }

  public static List<Drink> getDrinksByName(String name) {
    Query query = new Query("Drink").addFilter("name", Query.FilterOperator.EQUAL, name);
    PreparedQuery results = getDatastore().prepare(query);

    List<Drink> drinkByName = new ArrayList<>();

    for (Entity drinkEntity : results.asIterable()) {
      double numRatings = (double) drinkEntity.getProperty("numRatings");
      double avgRating = (double) drinkEntity.getProperty("rating");
      String storeID = (String) drinkEntity.getProperty("store");

      drinkByName.add(new Drink(name, storeID, avgRating, numRatings, drinkEntity));
    }

    return drinkByName;
  }

  public static void updateEntity(Entity drinkEntity, double numRatings, double rating) {
    drinkEntity.setProperty("rating", rating);
    drinkEntity.setProperty("numRatings", numRatings);
  }
}
