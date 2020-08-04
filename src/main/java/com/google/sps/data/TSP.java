// Copyright 2019 Google LLC
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

import java.util.ArrayList;
import java.util.List;

/** Represents a comment in store or user page. */
public class TSP {

  private Map<String, int> storeIdToIndex = new HashMap<>();
  private List<List<double>> adjacencyMatrix = new ArrayList<>();

  public static void constructAdjacencyMatrix(
      List<Map<String, String>> coffeeShops, double userLat, double userLng) {
    for (int i = 0; i < coffeeShops.size(); i++) {
      double lat2 = coffeeShops.get(i).get("lat");
      double lon2 = coffeeShops.get(i).get("lng");
      adjacencyMatrix.get(0).set(i + 1, haversineDistance(userLat, userLng, lat2, lon2));
    }
    for (int i = 0; i < coffeeShops.size(); i++) {
      for (int j = 0; j < coffeeShops.size(); j++) {
        double lat1 = coffeeShops.get(i).get("lat");
        double lon1 = coffeeShops.get(i).get("lng");
        double lat2 = coffeeShops.get(j).get("lat");
        double lon2 = coffeeShops.get(j).get("lng");
        adjacencyMatrix.get(0).set(i + 1, haversineDistance(lat1, lng1, lat2, lon2));
      }
    }
    return;
  }

  /**
   * /* Calculates the straight line distance between two /* sets of latitutes and longitutes /* A
   * breakdown of the haversine formula can be found at /*
   * http://www.movable-type.co.uk/scripts/latlong.html
   */
  public static double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
    int R = 6371; // Radius of the earth in km
    double dLat = degToRad(lat2 - lat1);
    double dLon = degToRad(lon2 - lon1);
    double a =
        Math.sin(dLat / 2) * Math.sin(dLat / 2)
            + Math.cos(degToRad(lat1))
                * Math.cos(degToRad(lat2))
                * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    double distance = R * c; // Distance in km
    return distance;
  }

  public static double degreeToRadian(double degree) {
    return degree * (Math.PI / 180);
  }

  /* Function to find the minimum weight */
  public static int travelingSalesman(
      int[][] adjacencyMatrix,
      boolean[] visited,
      int currPos,
      int n,
      int count,
      int cost,
      int ans) {

    // If last node is reached
    // keep the minimum value out of the total cost
    // of traversal and "ans"
    // Finally return to check for more possible values
    if (count == (n - 1)) {
      ans = Math.min(ans, cost + adjacencyMatrix[0][currPos]);
      return ans;
    }

    // BACKTRACKING STEP
    // 0 is the user location, which will never be considered as part of the path
    // since the for loop starts iterating from 1
    visited[0] = true;
    for (int i = 1; i < n; i++) {
      if (visited[i] == false && adjacencyMatrix[currPos][i] > 0) {

        // Mark as visited
        visited[i] = true;
        ans =
            tsp(adjacencyMatrix, visited, i, n, count + 1, cost + adjacencyMatrix[currPos][i], ans);

        // Mark ith node as unvisited
        visited[i] = false;
      }
    }
    return ans;
  }
}
