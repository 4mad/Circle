package attempt;

public class PointDir {
	
	public Vector2d Point;
	public Vector2d Direction;
	
	public PointDir(){
		Point = new Vector2d();
		Direction = new Vector2d();
	}
	
	public PointDir(Vector2d point, Vector2d direction){
		Point = point;
		Direction = direction;
	}

	public Vector2d getPoint() {
		return Point;
	}

	public void setPoint(Vector2d point) {
		Point = point;
	}

	public Vector2d getDirection() {
		return Direction;
	}

	public void setDirection(Vector2d direction) {
		Direction = direction;
	}

	@Override
	public String toString() {
		return "PointDir [Point=" + Point + ", Direction=" + Direction + "]";
	}
	
}
