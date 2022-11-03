package problem;

import java.util.Collections;

public class NoPath extends Path {
	public NoPath() {
		super(Collections.emptyList(), Double.POSITIVE_INFINITY);
	}
}
