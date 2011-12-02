package model;

import static org.junit.Assert.*;

import org.junit.Test;

public class ConditionTest {

	@Test
	public void testInvalidCondition() {
		int exceptionThrownCount = 0;
		
		try {
			new Condition(0, -1);
		} catch (IllegalArgumentException e) {
			assertEquals("Maximum value less than minimum value", e.getMessage());
			exceptionThrownCount++;
		}
		
		try {
			new Condition(0, 5, 6);
		} catch (IllegalArgumentException e) {
			assertEquals("Starting value not in min/max range", e.getMessage());
			exceptionThrownCount++;
		}
		
		assertEquals(2, exceptionThrownCount);
	}
	
	@Test
	public void testIncrease() {
		// increase by zero shouldn't increase
		Condition c = new Condition(0, 500);
		c.increase(0);
		assertTrue(c.getCurrent() == 500);

		// increase beyond max shouldn't increase
		c = new Condition(1, 500);
		c.increase(1);
		assertTrue(c.getCurrent() == 500);
		
		c = new Condition(1, 500, 1);
		c.increase(1);
		assertTrue(c.getCurrent() == 2);
		
		c = new Condition(-500, 1);
		c.increase(1);
		assertTrue(c.getCurrent() == 1);
		
		c = new Condition(-500, 1, -500);
		c.increase(1);
		assertTrue(c.getCurrent() == -499);
		
		c = new Condition(1, 5);
		c.increase(-1);
		assertTrue(c.getCurrent() == 5);
	}
	
	@Test
	public void testDecrease() {
		// decrease by zero shouldn't decrease
		Condition c = new Condition(0, 500);
		c.decrease(0);
		assertTrue(c.getCurrent() == 500);

		// decrease below min shouldn't decrease
		c = new Condition(1, 500, 1);
		c.decrease(1);
		assertTrue(c.getCurrent() == 1);
		
		c = new Condition(1, 500, 500);
		c.decrease(1);
		assertTrue(c.getCurrent() == 499);
		
		c = new Condition(-500, 1, -500);
		c.decrease(1);
		assertTrue(c.getCurrent() == -500);
		
		c = new Condition(-500, 1, -500);
		c.decrease(1);
		assertTrue(c.getCurrent() == -500);
		
		// decreasing by a negative number shouldn't change anything
		c = new Condition(1, 5);
		c.decrease(-1);
		assertTrue(c.getCurrent() == 5);
	}
}
