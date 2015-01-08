//David Govorko, 12/12/2014
package attempt;

public class LineGen {
	//maybe put distance function here?
	Vector2d origin;
	Vector2d vector;
	
	public LineGen() {
		origin = new Vector2d(0,0);
		vector = new Vector2d(10,10);		
	}
	
	public LineGen(Vector2d point, Vector2d direction) {
		origin = point;
		vector = direction;		
	}

	public Vector2d getOrigin() {
		return origin;
	}

	public void setOrigin(Vector2d origin) {
		this.origin = origin;
	}

	public Vector2d getVector() {
		return vector;
	}

	public void setVector(Vector2d vector) {
		this.vector = vector;
	}
	
	public LineGen tangent(){
		return new LineGen(this.getOrigin(), this.getVector().tangent());
	}
	
	public double linePointDist(Vector2d point){
		Vector2d pv = point.subtract(this.getOrigin());
		return pv.dot(this.getVector().tangent())/this.getVector().magnitude(); 
	}
	
	public double linePointProj(Vector2d point){
		Vector2d pv = point.subtract(this.getOrigin());
		return pv.dot(this.getVector())/this.getVector().magnitude(); 
	}
}
