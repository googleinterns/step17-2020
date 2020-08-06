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

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.simple.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public final class TSPTest {
  private final int size = 6;
  private final double[][] adjacencyMatrix = new double[size][size];
  private final double EPSILON = 0.001;
  private final double userLat = 42.254336;
  private final double userLng = -83.7124096;
  private final double starbucksLat = 42.274905;
  private final double starbucksLng = -83.734014;
  private final double bearclawLat = 42.2596352;
  private final double bearclawLng = -83.712071;

  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  @Before
  public void setUp() {
    helper.setUp();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  // Graph in this test is from https://www.mathworks.com/help/bioinfo/ref/graphshortestpath.html
  @Test
  public void testTSPAlgorithm() {
    adjacencyMatrix[3][0] = 0.45;
    adjacencyMatrix[5][1] = 0.41;
    adjacencyMatrix[1][2] = 0.51;
    adjacencyMatrix[4][2] = 0.32;
    adjacencyMatrix[5][2] = 0.29;
    adjacencyMatrix[2][3] = 0.15;
    adjacencyMatrix[4][3] = 0.36;
    adjacencyMatrix[0][4] = 0.21;
    adjacencyMatrix[1][4] = 0.32;
    adjacencyMatrix[0][5] = 0.99;
    adjacencyMatrix[3][5] = 0.38;
    boolean[] v = new boolean[size];
    double ans = Double.MAX_VALUE;
    double min = Double.MAX_VALUE;
    for (int i = 1; i < size; i++) {
      List<Integer> bestPath = new ArrayList<>();
      List<Integer> currPath = new ArrayList<>();
      ans = TSP.travelingSalesman(adjacencyMatrix, v, i, size, 1, 0, ans, bestPath, currPath);
      if (ans < min) {
        min = ans;
      }
    }
    Assert.assertEquals(min, 1.2, EPSILON);
  }

  @Test
  public void testHaversineDistance() {
    // Case 1: Two points are the exact same points
    double distance = TSP.haversineDistance(userLat, userLng, userLat, userLng);
    Assert.assertEquals(distance, 0, EPSILON);
    distance = TSP.haversineDistance(userLat, userLng, starbucksLat, starbucksLng);
    Assert.assertEquals(distance, 2.896858, EPSILON);
    distance = TSP.haversineDistance(bearclawLat, bearclawLng, starbucksLat, starbucksLng);
    Assert.assertEquals(distance, 2.478540, EPSILON);
    double reverse = TSP.haversineDistance(starbucksLat, starbucksLng, bearclawLat, bearclawLng);
    Assert.assertEquals(distance, reverse, EPSILON);
  }

  @Test
  public void testAdjacencyMatrix() {
    JSONObject jsonObject1 = new JSONObject();
    jsonObject1.put("address", "2460 Washtenaw Ave, Ann Arbor, MI 48104, United States");
    jsonObject1.put("distance", "0.1796649032567423");
    jsonObject1.put("lat", "42.2596352");
    jsonObject1.put("lng", "-83.712071");
    jsonObject1.put("name", "Bearclaw Coffee Co");
    jsonObject1.put("store", "ChIJC0rOXQKvPIgRUWwvH7uoU5k");
    JSONObject jsonObject2 = new JSONObject();
    jsonObject2.put("address", "1214 S University Ave, Ann Arbor, MI 48104, United States");
    jsonObject2.put("distance", "2.4537345227595146");
    jsonObject2.put("lat", "42.274905");
    jsonObject2.put("lng", "-83.734014");
    jsonObject2.put("name", "Starbucks");
    jsonObject2.put("store", "ChIJkzy_TkSuPIgRO7_JrkzTTI0");
    JSONArray array = new JSONArray();
    array.add(jsonObject1);
    array.add(jsonObject2);
    Gson gson = new Gson();
    List<Map<String, String>> coffeeShops = gson.fromJson(array.toString(), ArrayList.class);
    double[][] adjacencyMatrix = TSP.constructAdjacencyMatrix(coffeeShops, userLat, userLng);
    Assert.assertEquals(adjacencyMatrix[1][2], adjacencyMatrix[2][1], EPSILON);
    Assert.assertEquals(adjacencyMatrix[1][2], 2.478540, EPSILON);
    Assert.assertEquals(adjacencyMatrix[0][0], 0, EPSILON);
    Assert.assertEquals(adjacencyMatrix[1][1], 0, EPSILON);
    Assert.assertEquals(adjacencyMatrix[2][2], 0, EPSILON);
  }
}
