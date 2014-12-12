package attempt;
//Depreciated since Vector2d and PointDir has "replaced" it
public class PVector {

	vector V;
	point P;
	
	public	PVector()
	{
		V = new vector(0,0,0);
		P = new point(0,0);
	}
	
	public vector getV() {
		return V;
	}

	public void setV(vector v) {
		V = v;
	}

	public point getP() {
		return P;
	}

	public void setP(point p) {
		P = p;
	}

	public PVector(point b, vector a)
	{
		V = a;
		P = b;	
		
	}
	
	public String toString()
	{
				
		return "PV: " + this.P + " + " + this.V; 
	}
		
}
