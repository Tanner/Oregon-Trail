package model;

import static org.junit.Assert.*;

import org.junit.Test;

public class ConditionTest {

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

}
