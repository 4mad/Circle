//David Govorko, 12/12/2014
package attempt;

public class CircleGen {

	double radius;
	Vector2d center;
	Vector2d direction;
	
	public CircleGen(){
		radius = 100;
		center = new Vector2d(0, 0);
		direction = new Vector2d(0, 0);
	}
	
	public CircleGen(double radiusLength, Vector2d centerPoint){
		radius = radiusLength;
		center = centerPoint;
		direction = new Vector2d(0, 0);
	}
	
	public CircleGen(double radiusLength, Vector2d directionMovement, Vector2d centerPoint){
		radius = radiusLength;
		center = centerPoint;
		direction = directionMovement;
	}

	public Vector2d getDirection() {
		return direction;
	}

	public void setDirection(Vector2d direction) {
		this.direction = direction;
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
	
	public void move(){
		this.center = this.center.add(this.direction);
	}
	
}
