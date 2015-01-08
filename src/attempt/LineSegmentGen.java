package attempt;

public class LineSegmentGen {
	//maybe put distance function here? Now how do you combine them?
	//work on tangent lines(end1 and end2 figure out) and PointDist(end1 and end2 limit distances) and pointProjection
		Vector2d originTemp;
		Vector2d end1;
		Vector2d end2;
		LineGen line;
		
		public LineSegmentGen() {
			end1 = new Vector2d(0,0);
			end2 = new Vector2d(10,10);
			originTemp = end1;
			line = new LineGen(originTemp, new Vector2d(10,10));
		}
		
		public LineSegmentGen(Vector2d endPoint1, Vector2d endPoint2, Vector2d direction) {
			end1 = endPoint1;
			end2 = endPoint2;
			originTemp = originCalc(end1, end2, direction);
			line = new LineGen(originTemp, direction);
		}
		
		public LineSegmentGen(Vector2d endPoint1, Vector2d endPoint2, Vector2d direction, Vector2d originPickEnd1or2) {
			end1 = endPoint1;
			end2 = endPoint2;
			originTemp = originPickEnd1or2;
			line = new LineGen(originTemp, direction);
		}
		
		public LineSegmentGen(Vector2d[] endPoints, Vector2d direction) {
			Vector2d[] temp = minMax(endPoints);
			end1 = temp[0];
			end2 = temp[1];
			originTemp = originCalc(end1, end2, direction);
			line = new LineGen(originTemp, direction);
		}
		
		public LineSegmentGen(Vector2d[] endPoints, Vector2d direction, Vector2d originPickEndPoint) {
			Vector2d[] temp = minMax(endPoints);
			end1 = temp[0];
			end2 = temp[1];
			originTemp = originPickEndPoint;
			line = new LineGen(originTemp, direction);
		}

		public Vector2d getOriginTemp() {
			return originTemp;
		}

		public void setOriginTemp(Vector2d origin) {
			this.originTemp = origin;
		}

		public Vector2d getEnd1() {
			return end1;
		}

		public void setEnd1(Vector2d end1) {
			this.end1 = end1;
		}

		public Vector2d getEnd2() {
			return end2;
		}

		public void setEnd2(Vector2d end2) {
			this.end2 = end2;
		}

		public LineGen getLine() {
			return line;
		}

		public void setLine(LineGen line) {
			this.line = line;
		}

		//public LineGen tangent(){
		//	return new LineSegmentGen(this.getOrigin(), this.getVector().tangent());
		//}
		
	//	public double linePointDist(Vector2d point){
	//		Vector2d pv = point.subtract(this.getOrigin());
	//		return pv.dot(this.getVector().tangent())/this.getVector().magnitude(); 
	//	}
		
	//	public double linePointProj(Vector2d point){
	//		Vector2d pv = point.subtract(this.getOrigin());
	//		return pv.dot(this.getVector())/this.getVector().magnitude(); 
	//	}
		
		public Vector2d originCalc(Vector2d end1, Vector2d end2, Vector2d direction){
			if (end1.subtract(end2).angleBetween(direction) < Math.PI/2) return end2;
			return end1;
		}
		
		public Vector2d[] minMax(Vector2d[] point){
			Vector2d end1 = point[0], end2 = point[1];
			for (int n = 0; n < point.length-1; n++)
				for (int i = n + 1; i < point.length; i++)
					if(point[n].dist(point[i]) > end1.dist(end2)){
						end1 = point[n];
						end2 = point[i];
					}
			
			Vector2d[] results = {end1, end2};
			return results;			
		}
		
}		