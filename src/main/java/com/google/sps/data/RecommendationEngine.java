package com.google.sps.data;

import java.util.*;

/*This class holds the TYDRecommended algorithm which will give the
    user a recommended store to get their desired beverage from.
*/
public class RecommendationEngine {

  public static Optional<Drink> getDrink(String storeId, String drinkName, List<Drink> drinkList) {
    return drinkList.stream().filter(drink -> drink.getName().equals(drinkName)).findFirst();
  }
  // given a coffeeshop, return its tydrecommended score
  public static double getScore(Drink drink) {
    int numComments = (int) drink.getNumRatings();
    double avgRating = drink.getRating();
    double timeMinutes = getWalkingDistanceMins();
    double commentScore, ratingScore, distanceScore;
    //      /30             /35         /35

    // get commentScore
    if (numComments >= 100) { // 100+
      commentScore = 30;
    } else if (numComments >= 75) { // 75-99
      commentScore = 25;
    } else if (numComments >= 50) { // 50-74
      commentScore = 20;
    } else if (numComments >= 25) { // 25-49
      commentScore = 15;
    } else {
      commentScore = (numComments * .6);
    }

    // get ratingScore
    ratingScore = avgRating * 7;

    // get distanceScore
    if (timeMinutes <= 4.0) { // 0-4 mins
      distanceScore = 35;
    } else {
      distanceScore = 35 - timeMinutes;
    }

    return (commentScore + ratingScore + distanceScore);
  }
  // this function returns the highest scored store from a list of stores
  public static String getBestShop(
      List<String> listStoreIds, String inputBeverage, List<Drink> drinkList) {
    double bestScore = 0.0;
    String highestScoredStore = "";
    for (String storeId : listStoreIds) {
      Optional<Drink> drink = getDrink(storeId, inputBeverage, drinkList);
      if (drink.isPresent()) {
        double currentShopScore = getScore(drink.get());
        if (currentShopScore > bestScore) {
          highestScoredStore = storeId;
          bestScore = currentShopScore;
        }
      }
    }

    if (!highestScoredStore.equals("")) {
      return highestScoredStore;
    }
    return "No Results";
  }

  public static int getWalkingDistanceMins() {
    // TODO: return duration of walk in minutes
    // calculate distance with store functions and compare with user info
    return 1; // example return for testing purposes
  }
}
