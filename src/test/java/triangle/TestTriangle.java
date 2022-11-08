package triangle;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestTriangle {
	
	@Test
	public void testScalene() {
		assertEquals(Triangle.SCALENE, Triangle.classifyTriangle(2, 6, 7));
	}
	
	@Test
	public void testEquilateral() {
		assertEquals(Triangle.EQUALATERAL, Triangle.classifyTriangle(2, 2, 2));
	}	
}
