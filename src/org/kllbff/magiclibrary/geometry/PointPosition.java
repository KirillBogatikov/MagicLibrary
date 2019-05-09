package org.kllbff.magiclibrary.geometry;

/**
 * <h3>Represents position of some point on the shape</h3>
 * <p>Any point can locates in shape, on it's border or outside</p>
 * 
 * @author Kirill Bogatikov
 * @version 1.0
 * @since 1.0
 */
public enum PointPosition {
    INSIDE, OUTSIDE,
    /* used only for shapes */
    ON_BORDER, IN_VERTEX
}
