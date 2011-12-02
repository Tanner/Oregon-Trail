package model.worldMap;

import static org.junit.Assert.*;

import org.junit.Test;

public class LocationNodeTest {

	@Test
	public void test() {
		LocationNode testNode1 = new LocationNode("first test location", 10, 10, 0, 0, 3, 24, 50, 920, 500);
		LocationNode testNode2 = new LocationNode("second test location", 100, 100, 0, 0, 3, 20, 50, 920, 500);
		LocationNode testNode3 = new LocationNode("third test location", 500, -200, 0, 0, 3, 15, 50, 920, 500);
		LocationNode testNode4 = new LocationNode("fourth test location", 1400, -300 , 0, 0, 3, 10, 50, 920, 500);

		assertTrue(testNode1.getHasInTrail() == false);
		assertTrue(testNode2.getHasInTrail() == false);
		assertTrue(testNode3.getHasInTrail() == false);
		assertTrue(testNode4.getHasInTrail() == false);
		
		//testing that private method convert to map coords works correctly

		assertTrue("expect 70, got " + Double.toString(testNode1.getPlayerMapX()),testNode1.getPlayerMapX() == (double)70);
		assertTrue("expect 160, got " + Double.toString(testNode2.getPlayerMapX()),testNode2.getPlayerMapX() == (double)160);
		assertTrue("expect 560, got " + Double.toString(testNode3.getPlayerMapX()),testNode3.getPlayerMapX() == (double)560);
		assertTrue("expect 960, got " + Double.toString(testNode4.getPlayerMapX()),testNode4.getPlayerMapX() == (double)1050);
		
		assertTrue("expect 47.02380952380949, got " + Double.toString(testNode1.getPlayerMapY()), testNode1.getPlayerMapY() == (double)47.02380952380949);
		assertTrue("expect 20.238095238095212, got " + Double.toString(testNode2.getPlayerMapY()), testNode2.getPlayerMapY() == (double)20.238095238095212);
		assertTrue("expect 401.1904761904762, got " + Double.toString(testNode3.getPlayerMapY()), testNode3.getPlayerMapY() == (double)401.1904761904762);
		assertTrue("expect 565.0, got " + Double.toString(testNode4.getPlayerMapY()), testNode4.getPlayerMapY() == (double)565.0);

		//testing that trail adding works - adding testNode 2, 3, 4 to be destinations of trails from testNode1
		
		TrailEdge townTrail1 = new TrailEdge(testNode2,testNode1,0);
		TrailEdge townTrail2 = new TrailEdge(testNode3,testNode1,0);
		TrailEdge townTrail3 = new TrailEdge(testNode4,testNode1,0);
		
		testNode1.setTrails(3);
		assertTrue("Expected 3 trails", testNode1.getTrails() == 3);
		
		testNode1.addTrail(townTrail1);
		testNode1.addTrail(townTrail2);
		testNode1.addTrail(townTrail3);
		//now, make sure these trails are actually available
		assertTrue(testNode1.getOutBoundTrailByIndex(0).getOrigin() == testNode1);
		assertTrue(testNode1.getOutBoundTrailByIndex(0).getOrigin() == townTrail1.getOrigin());
		assertTrue("GOT " + testNode1.getOutBoundTrailByIndex(0).getDestination() + " EXPECTED " + townTrail1.getDestination(),testNode1.getOutBoundTrailByIndex(0).getDestination() == townTrail1.getDestination());
		assertTrue("GOT " + testNode1.getOutBoundTrailByIndex(0).getDestination()+ " should be on the trail but isn't",testNode1.getOutBoundTrailByIndex(0).getDestination().getOnTheTrail());

		assertTrue(testNode1.getOutBoundTrailByIndex(1).getOrigin() == testNode1);
		assertTrue(testNode1.getOutBoundTrailByIndex(1).getOrigin() == townTrail1.getOrigin());
		assertTrue("GOT " + testNode1.getOutBoundTrailByIndex(1).getDestination() + " EXPECTED  " + townTrail2.getDestination(),testNode1.getOutBoundTrailByIndex(1).getDestination() == townTrail2.getDestination());
		assertTrue("GOT " + testNode1.getOutBoundTrailByIndex(1).getDestination()+ " should be on the trail but isn't",testNode1.getOutBoundTrailByIndex(1).getDestination().getOnTheTrail());

		assertTrue(testNode1.getOutBoundTrailByIndex(2).getOrigin() == testNode1);
		assertTrue(testNode1.getOutBoundTrailByIndex(2).getOrigin() == townTrail1.getOrigin());
		assertTrue("GOT " + testNode1.getOutBoundTrailByIndex(2).getDestination() + " EXPECTED " + townTrail3.getDestination(),testNode1.getOutBoundTrailByIndex(2).getDestination() == townTrail3.getDestination());
		assertTrue("GOT " + testNode1.getOutBoundTrailByIndex(2).getDestination()+ " should be on the trail but isn't",testNode1.getOutBoundTrailByIndex(2).getDestination().getOnTheTrail());
		
		
	}

}
/*




*/