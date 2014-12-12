package attempt;

public class Vector2d {

	double x,y;
				
	public Vector2d() {
	  x=0;
	  y=0;		
	}
	
	public Vector2d(double X, double Y) {
		x = X;
		y = Y;		
	}
	
	public double dist(Vector2d a) {
		return Math.sqrt(Math.pow(this.x-a.x,2)+Math.pow(this.y-a.y,2));	
	}
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public String toString() {
		return "Vector2d: " + " <" + getX() + "," + getY() + ">";
	}
	
	public double dot(Vector2d a)	{
		return this.x*a.getX() + this.y*a.getY();
	}

	public Vector2d add(Vector2d a)	{
		double i = this.x + a.getX();
		double j = this.y + a.getY();
		return new Vector2d(i, j); 
	}
	
	public Vector2d subtract(Vector2d a) {
		double i = this.x - a.getX();
		double j = this.y - a.getY();
		return new Vector2d(i, j);	 
	}
	
	public double magnitude() {
		return Math.sqrt(Math.pow(this.x,2) + Math.pow(this.y,2));
	}
		
	public double cross(Vector2d a) {
			return this.x*a.getY() - a.getX()*this.y;			
	}

	public double angle() {
		return  Math.acos(this.x/this.magnitude());
	}
	
	public double otherAngle() {
		return Math.abs(Math.PI/2.0 - this.angle()); 
	}
	
	public Vector2d unitize()	{
		if (this.x == 0 & this.y == 0) return this;
		return new Vector2d(this.x/this.magnitude(), this.y/this.magnitude());			
	}
	
	public Vector2d scalarMulti(double a) {
		return new Vector2d(a*this.x, a*this.y);
	}
	
	public double angleBetween(Vector2d a){
		return Math.acos(this.dot(a)/(this.magnitude()*a.magnitude()));
	}
	
	public Vector2d tangent(){
		return new Vector2d(-this.y, this.x);
	}
	
	public Vector2d deepCopy() {
		return new Vector2d(x, y);
	}
	
	public Vector2d integizerFloor() {
		return new Vector2d(Math.floor(this.x), Math.floor(this.y));
	}
	
	public Vector2d integizerCeil() {
		return new Vector2d(Math.ceil(this.x), Math.ceil(this.y));
	}
	
	public double distV(Vector2d comp) {
		double xD = this.x - comp.x;
		double yD = this.y - comp.y;		
		return Math.sqrt(xD*xD + yD*yD);
	}
	
	public Vector2d rotate(double angle)
	{
		double xPrime = this.x*Math.cos(angle) - this.y*Math.sin(angle);
		double yPrime = this.x*Math.sin(angle) + this.y*Math.cos(angle);
		return new Vector2d(xPrime, yPrime);
	}
}
