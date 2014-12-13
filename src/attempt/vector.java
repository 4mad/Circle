//David Govorko, 12/12/2014
package attempt;

//Depreciated since 2d is all that is necessary
public class vector {

	double x,y,z;
				
	public vector() {
	  x=0;
	  y=0;
	  z=0;
	  		
	}
	
	public vector(double X, double Y, double Z) {
		x = X;
		y = Y;
		z = Z;
					
	}
	
	public double dist(vector a) {
		return Math.sqrt(Math.pow(this.x-a.x,2)+Math.pow(this.y-a.y,2) + Math.pow(this.z-a.z,2));	
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

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}
		
	public String toString() {
		return "Vector: " + " <" + getX() + "," + getY() + "," + getZ() + ">";
	}
	
	public double dot(vector a)	{
		return this.x*a.getX() + this.y*a.getY() + this.z*a.getZ();
	}

	public vector add(vector a)	{
		double i = this.x + a.getX();
		double j = this.y + a.getY();
		double k = this.z + a.getZ();
		return new vector(i,j,k); 
	}
	
	public vector subtract(vector a) {
		double i = this.x - a.getX();
		double j = this.y - a.getY();
		double k = this.z - a.getZ();
		return new vector(i,j,k);	 
	}
	
	public double magnitude() {
		return Math.sqrt(Math.pow(this.x,2)+Math.pow(this.y,2)+Math.pow(this.z,2) );

	}
		
	public vector cross(vector a) {
			double i = this.y*a.getZ() - this.z*a.getY();
			double j = this.z*a.getX() - this.x*a.getZ();
			double k = this.x*a.getY() - this.y*a.getX();
			return new vector(i,j,k);			
	}

	public double angle() {
		return  Math.acos(this.x/this.magnitude());
	}
	
	public double otherAngle() {
		return Math.abs(Math.PI/2.0 - this.angle()); 
	}
	
	public vector unitize()	{
		return new vector(this.x/this.magnitude(),this.y/this.magnitude(),this.z/this.magnitude());			
	}
	
	public vector multi(double a) {
		return new vector(a*this.x,a*this.y,a*this.z);
	}
	
	public vector deepCopy() {
		return new vector(x,y,z);
	}
	
	public vector integizer() {
		return new vector(Math.floor(this.x),Math.floor(this.y),Math.floor(this.z));
	}
	
	public double compareV(vector comp) {
		double xD = this.x - comp.x;
		double yD = this.y - comp.y;
		double zD = this.z - comp.z;
		
		return Math.sqrt(xD*xD + yD*yD + zD*zD);
	}
	
}
