package attempt;

public class CircleGen {

	double radius;
	Vector2d center;
	
	public CircleGen(){
		radius = 100;
		center = new Vector2d(0,0);
	}
	
	public CircleGen(double radiusLength, Vector2d centerPoint){
		radius = radiusLength;
		center = centerPoint;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public Vector2d getCenter() {
		return center;
	}

	public void setCenter(Vector2d center) {
		this.center = center;
	}
	
}
