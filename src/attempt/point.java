package attempt;

//Depreciated for Vector2d
public class point {

	float x,y,z;
	
	public point(float X,float Y, float Z)
		{
			x = X;
			y = Y;		
			z = Z;
		}
	
	public point(float X,float Y)
	{
		x = X;
		y = Y;		
		z = 0;
	}	
	public float dist(point a)
	{
		return (float) Math.sqrt(Math.pow(this.x-a.x,2)+Math.pow(this.y-a.y,2) + Math.pow(this.z-a.z,2));
		
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}
	
	public String toString()
	{
		return "(" + this.x + " , " + this.y + " , " + this.z + ")";
	}


}
