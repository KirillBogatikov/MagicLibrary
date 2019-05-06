package org.kllbff.magiclibrary.geometry;

/**
 * <h4>Represents position of some point on the shape</h4>
 * <p>Any point can locates in shape, on it's border or outside 
 * 
 * @author Kirill Bogatikov
 * @version 1.0
 * @since 1.0
 */
public enum PointPosition {
	INSIDE, OUTSIDE,
	/* used only for lines */
	LEFT, RIGHT, ABOVE, BELOW,
	/* used only for shapes */
	ON_BORDER, IN_VERTEX
}
