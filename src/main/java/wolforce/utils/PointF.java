package wolforce.utils;

import java.awt.geom.Point2D;

public class PointF extends Point2D.Float {

	private static final long serialVersionUID = "1.0".hashCode();

	public PointF(float x, float y) {
		super(x, y);
	}

	public void translate(float dx, float dy) {
		x += dx;
		y += dy;
	}

}
