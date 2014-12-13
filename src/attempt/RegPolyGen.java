//David Govorko, 12/12/2014
package attempt;

public class RegPolyGen {	
	//add a max distance across polygon measurement tool
	//also a min distance across polygon measurement tool
	int numberSides;
	double lengthSide;
	double angleRads;
	double longDistAcross;
	double shortDistAcross;
	Vector2d center;
	Vector2d direction;
	Vector2d[] vertexes;
	//include vector2D in RegPolygon class

	public RegPolyGen() {
		numberSides = 3;
		lengthSide = 10;	
		angleRads = 0;
		center = new Vector2d(0,0);
		direction = new Vector2d(0,0);
		vertexes = this.generateVerts();
		longDistAcross = this.longestDist();
		shortDistAcross = this.shortestDist();
	}

	public Vector2d getDirection() {
		return direction;
	}

	public void setDirection(Vector2d direction) {
		this.direction = direction;
	}

	public double getAngleRads() {
		return angleRads;
	}

	public void setAngleRads(double angleRads) {
		this.angleRads = angleRads;
	}

	public Vector2d getCenter() {
		return center;
	}

	public void setCenter(Vector2d center) {
		this.center = center;
	}

	public RegPolyGen(int numofSides, double lengthofSide, double angleSizeRad, Vector2d centerPoint) {
		numberSides = numofSides;
		lengthSide = lengthofSide;		
		angleRads = angleSizeRad;
		center = centerPoint;
		vertexes = this.generateVerts();
		longDistAcross = this.longestDist();
		shortDistAcross = this.shortestDist();
		direction = new Vector2d(0,0);
	}
	
	public RegPolyGen(int numofSides, double lengthofSide, double angleSizeRad, Vector2d directionVector, Vector2d centerPoint) {
		numberSides = numofSides;
		lengthSide = lengthofSide;		
		angleRads = angleSizeRad;
		center = centerPoint;
		vertexes = this.generateVerts();
		longDistAcross = this.longestDist();
		shortDistAcross = this.shortestDist();
		direction = directionVector;
	}

	public double getLongDistAcross() {
		return longDistAcross;
	}

	public double getShortDistAcross() {
		return shortDistAcross;
	}

	public Vector2d[] getVertexes() {
		return vertexes;
	}

	public void setVertexes(Vector2d[] vertexes) {
		this.vertexes = vertexes;
	}

	public int getNumberSides() {
		return numberSides;
	}

	public void setNumberSides(int numberSides) {
		this.numberSides = numberSides;
	}

	public double getLengthSide() {
		return lengthSide;
	}

	public void setLengthSide(double lengthSide) {
		this.lengthSide = lengthSide;
	}	
	
	public void move(){
		this.center = this.center.add(this.direction);
	}

	public Vector2d[] generateVerts(){
		Vector2d[] verts = new Vector2d[this.getNumberSides()];//make it so that it starts in "regular" position
		double interiorlength = this.getLengthSide()/Math.sqrt(2-2*Math.cos(2*Math.PI/this.getNumberSides()));
		for(int i = 1; i <= this.getNumberSides() ; i++){
			//This splits a circle into all the angles needed to make the vertexes, and rotates the object to the generic "start" position + angle given
			verts[i-1] = new Vector2d(interiorlength*Math.cos((2*Math.PI)/this.getNumberSides()*i - Math.PI*(this.getNumberSides() - 2)/(2*this.getNumberSides()) + angleRads), 
					interiorlength*Math.sin((2*Math.PI)/this.getNumberSides()*i - Math.PI*(this.getNumberSides() - 2)/(2*this.getNumberSides()) + angleRads));
		}
		return verts;
	}

	public double longestDist(){//can be faster?
		double longest = -1;

		for (int i = 0; i < this.getVertexes().length; i++)
			for (int k = this.getVertexes().length - 1; k > i; k--)
				if (this.getVertexes()[i].dist(this.getVertexes()[k]) > longest)
					longest = this.getVertexes()[i].dist(this.getVertexes()[k]);

		return longest;
	}

	public double shortestDist(){//can be faster?
		double shortest = Double.MAX_VALUE;

		for (int i = 0; i < this.getVertexes().length; i++)
			for (int k = this.getVertexes().length - 1; k > i; k--)
				if (this.getVertexes()[i].dist(this.getVertexes()[k]) < shortest)
					shortest = this.getVertexes()[i].dist(this.getVertexes()[k]);

		return shortest;
	}

}
