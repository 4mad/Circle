//David Govorko, 12/12/2014
package attempt;

public class Collision {
	//Line Segment Collisions need to be added
	//maybe add more in depth circle collision? Is there a RAtio between Dist Vs. Radius length? THen get angle from there and simple rotation to get two collision points on the line and then you can make the arc from that
	public Vector2d CircleSqaure(RegPolyGen sq, CircleGen cir){
		Vector2d Dir = cir.getCenter().subtract(sq.getCenter());

		Vector2d CollisionP = new Vector2d();
		double tempX;
		double tempY;
		double baX = Dir.getX()*Math.cos(-sq.getAngleRads()) - Dir.getY()*Math.sin(-sq.getAngleRads());
		double baY = Dir.getX()*Math.sin(-sq.getAngleRads()) + Dir.getY()*Math.cos(-sq.getAngleRads());
		// make vectors BA and TEMP
		double halfLength = sq.getLengthSide()/2;

		if ( baX < - halfLength) tempX = -halfLength;
		else if (baX >  halfLength) tempX = halfLength;
		else tempX = baX;

		if (baY < - halfLength) tempY = - halfLength;
		else if (baY > halfLength) tempY = halfLength;
		else tempY = baY;
		//can do rectangles too just replace squareSide with height and width
		//also make a tempH and tempW for h/2 and w/2
		//CollisionP.setX(tempX*Math.cos(sq.getAngleRads()) - tempY*Math.sin(sq.getAngleRads()) + sq.getCenter().getX());
		//CollisionP.setY(tempX*Math.sin(sq.getAngleRads()) + tempY*Math.cos(sq.getAngleRads()) + sq.getCenter().getY());
		Vector2d temp = new Vector2d(tempX, tempY);
		CollisionP = temp.rotate(sq.getAngleRads()).add(sq.getCenter());

		return CollisionP;
	}

	public Vector2d LinePoly(LineGen lin, RegPolyGen sq) {//maybe make it detect if a whole side hits? multiple hits == line between two points, need to set up line based world mapping first
		Vector2d collideVert = null;
		double min = lin.linePointDist(sq.getCenter());
		//System.out.println(min);
		Vector2d[] verts = sq.generateVerts();
		if (min == 0) return sq.getCenter();
		for (int i = 0; i < verts.length; i++) {
			//System.out.println(sq.getVertexes()[i]);
			if(min/lin.linePointDist(verts[i].add(sq.getCenter())) < 0){//probably shouldn't generate verts 
				collideVert = verts[i].add(sq.getCenter());
				//System.out.println(lin.linePointDist(sq.getVertexes()[i].add(sq.getCenter())));
				break;
			}
		}
		return collideVert;		
	}

	public Vector2d LineCircle(LineGen lin, CircleGen cir){//does not take into account of overshooting and curve vs line collision which i do not desire to force upon my worst of enemies
		Vector2d collideVert = null;//so only one point generated, which is around area of collision
		double dist = lin.linePointDist(cir.getCenter());
		//System.out.println(dist);
		if (Math.abs(dist) <= cir.getRadius())
			collideVert = cir.getCenter().subtract((lin.getVector().tangent().unitize().scalarMulti(dist)));
		else if (dist == 0) collideVert = cir.getCenter();

		return collideVert;
	}

	public Vector2d[] LineCircleArch(LineGen lin, CircleGen cir, double angleResolution){//angle resolution (0,1]
		Vector2d[] arch = null;//gets the whole arch. 
		Vector2d mid = LineCircle(lin,cir).subtract(cir.getCenter());
		if (mid == null) return arch;
		else if (mid.add(cir.getCenter()).dist(cir.getCenter())== 0){//this probably will have errors with direction
			double offset = lin.tangent().getVector().angle();
			//System.out.println(mid);
			arch = new Vector2d[(int)(1/angleResolution*180 + 1)];
			for (int i = 0; i < arch.length; i++) 
				arch[i] = new Vector2d(cir.getRadius()*Math.cos((i*angleResolution - 90)*Math.PI/180), cir.getRadius()*Math.sin((i*angleResolution - 90)*Math.PI/180)).rotate(offset).add(cir.getCenter());
		}else{//clean it up? It's beautiful
			double edgeDist = Math.sqrt(cir.getRadius()*cir.getRadius() - lin.linePointDist(cir.getCenter())*lin.linePointDist(cir.getCenter()));
			Vector2d edge1 = mid.add(lin.getVector().unitize().scalarMulti(edgeDist));
			double angle = mid.angleBetween(edge1);
			double offset = mid.angle();
			//System.out.println((mid.unitize()));
			arch = new Vector2d[(int)(1/angleResolution*angle*180/Math.PI*2 + 1)];
			for (int i = 0; i < arch.length; i++)
				arch[i] = new Vector2d(cir.getRadius()*Math.cos(i*angleResolution*Math.PI/180 - angle), cir.getRadius()*Math.sin(i*angleResolution*Math.PI/180 - angle)).rotate(offset).add(cir.getCenter());
		}
		return arch;
	}
	
	public Vector2d[] LineCircleArchEdgesOnly(LineGen lin, CircleGen cir){
		Vector2d[] edges = null;//Just gets the edges
		Vector2d mid = LineCircle(lin,cir).subtract(cir.getCenter());
		if (mid == null) return edges;
		else if (mid.add(cir.getCenter()).dist(cir.getCenter())== 0){
			edges = new Vector2d[2];
			edges[0] = cir.getCenter().add(lin.getVector().unitize().scalarMulti(cir.getRadius()));
			edges[1] = cir.getCenter().subtract(lin.getVector().unitize().scalarMulti(cir.getRadius()));
		}else{
			double edgeDist = Math.sqrt(cir.getRadius()*cir.getRadius() - lin.linePointDist(cir.getCenter())*lin.linePointDist(cir.getCenter()));
			edges = new Vector2d[2];
			edges[0] = mid.add(lin.getVector().unitize().scalarMulti(edgeDist));
			edges[1] = mid.subtract(lin.getVector().unitize().scalarMulti(edgeDist));			
		}
		return edges;
	}

}
