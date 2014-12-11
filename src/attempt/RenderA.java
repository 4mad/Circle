package attempt;

import org.lwjgl.opengl.GL11;

//This class just does the rendering geometry, no colors or fancy effects.
//Currently Circle, line, point, rectangle, and convex Polygons are supported.
//Should eventually be able to render weird triangles, ellipses, and tackle more complex shapes, also more diverse inputs.
//??Compare drawing a list of vertexes to actually doing geometric calculations.

public class RenderA {

	//circle with all data given as doubles 	??Make a circle with variable precision?(#250 changes)
	public void circle(double CenterX, double CenterY, double Radius) {
		GL11.glPushMatrix();
		GL11.glBegin(GL11.GL_LINE_LOOP);
		for(int i = 0; i<250; i++) {
			double angle = (i*2*Math.PI/250);
			GL11.glVertex2d(CenterX + (Math.cos(angle) * Radius), CenterY + (Math.sin(angle) * Radius)); 
		}
		GL11.glEnd();
		GL11.glPopMatrix();
	}

	//circle with center given as Vector and radius as double 	??Make a circle with variable precision?
	public void circleV2d(Vector2d center, double Radius) {
		GL11.glPushMatrix();
		GL11.glBegin(GL11.GL_LINE_LOOP);
		for(int i = 0; i<250; i++) {
			double angle = (i*2*Math.PI/250);
			GL11.glVertex2d(center.getX() + (Math.cos(angle) * Radius), center.getY() + (Math.sin(angle) * Radius)); 
		}
		GL11.glEnd();
		GL11.glPopMatrix();
	}

	//Rectangle made by vectors, origin is the bottom left point ??make it so vector2d and double are mixed
	public void rectangleOriginV2d(Vector2d origin, Vector2d size ) {
		//moving object
		GL11.glPushMatrix();

		//drawing the square
		GL11.glBegin(GL11.GL_QUADS);	
		GL11.glVertex2d(origin.getX() , origin.getY());
		GL11.glVertex2d(origin.getX() + size.getX(), origin.getY());
		GL11.glVertex2d(origin.getX() + size.getX(), origin.getY() + size.getY());
		GL11.glVertex2d(origin.getX(), origin.getY() + size.getY());
		//end
		GL11.glEnd();
		GL11.glPopMatrix();
	}

	//rectangle made by doubles for origin and length/width. origin is the bottom left corner
	public void rectangleOrigin(double originX, double originY, double sizeX, double sizeY) {
		//moving object
		GL11.glPushMatrix();

		//drawing the square
		GL11.glBegin(GL11.GL_QUADS);	
		GL11.glVertex2d(originX , originY);
		GL11.glVertex2d(originX+sizeX , originY);
		GL11.glVertex2d(originX+sizeX , originY+sizeY);
		GL11.glVertex2d(originX , originY+sizeY);
		//end
		GL11.glEnd();
		GL11.glPopMatrix();
	}

	//rectangle made by vectors for center and size.
	public void rectangleCenterV2d(Vector2d center, Vector2d size ) {
		//moving object
		GL11.glPushMatrix();

		//drawing the square
		GL11.glBegin(GL11.GL_QUADS);	
		GL11.glVertex2d(center.getX() - size.getX()/2, center.getY() - size.getY()/2);
		GL11.glVertex2d(center.getX() + size.getX()/2, center.getY() - size.getY()/2);
		GL11.glVertex2d(center.getX() + size.getX()/2, center.getY() + size.getY()/2);
		GL11.glVertex2d(center.getX() - size.getX()/2, center.getY() + size.getY()/2);
		//end
		GL11.glEnd();
		GL11.glPopMatrix();
	}

	//Polygon made by a list of Vertexes
	public void polygonV2d(Vector2d[] vertexes){
		//moving object
		GL11.glPushMatrix();

		//Drawing the Polygon
		GL11.glBegin(GL11.GL_POLYGON);
		for (int i = 0; i < vertexes.length; i++)
			GL11.glVertex2d(vertexes[i].getX() , vertexes[i].getY());
		//end
		GL11.glEnd();
		GL11.glPopMatrix();
	}

	//Polygon made by a list of Vertexes and an offset
	public void polygonV2d(Vector2d[] vertexes, Vector2d Offset){
		//moving object
		GL11.glPushMatrix();

		//Drawing the Polygon
		GL11.glBegin(GL11.GL_POLYGON);
		for (int i = 0; i < vertexes.length; i++)
			GL11.glVertex2d(vertexes[i].getX()+Offset.getX(), vertexes[i].getY()+Offset.getY());
		//end
		GL11.glEnd();
		GL11.glPopMatrix();
	}

	//Line made by 2 vertexes
	public void lineV2d(Vector2d origin, Vector2d end){
		//moving object
		GL11.glPushMatrix();

		//Drawing the Line
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2d(origin.getX() , origin.getY());
		GL11.glVertex2d(end.getX() , end.getY());
		//end
		GL11.glEnd();
		GL11.glPopMatrix();		
	}

	//Line made by 2 sets of doubles
	public void line(double originX, double originY, double endX, double endY){
		//moving object
		GL11.glPushMatrix();

		//Drawing the Line
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex2d(originX, originY);
		GL11.glVertex2d(endX, endY);
		//end
		GL11.glEnd();
		GL11.glPopMatrix();		
	}

	//Points made by list of Vertexes
	public void points(Vector2d[] vertexes){
		//moving object
		GL11.glPushMatrix();

		//Drawing the Polygon
		GL11.glBegin(GL11.GL_POINTS);
		for (int i = 0; i < vertexes.length; i++)
			GL11.glVertex2d(vertexes[i].getX(), vertexes[i].getY());
		//end
		GL11.glEnd();
		GL11.glPopMatrix();
	}

	//Points made by list of Vertexes and offset by a Vector  
	public void pointsOffset(Vector2d[] vertexes, Vector2d offset){
		//moving object
		GL11.glPushMatrix();

		//Drawing the Points with an offset
		GL11.glBegin(GL11.GL_POINTS);
		for (int i = 0; i < vertexes.length; i++)
			GL11.glVertex2d(vertexes[i].getX() + offset.getX(), vertexes[i].getY() + offset.getY());
		//end
		GL11.glEnd();
		GL11.glPopMatrix();
	}

	//OIUTDATED Points made by list of Vertexes and offset by a Vector using the OUTDATED POINTDIR
	public void pointsDirOffset(PointDir[] vertexes, Vector2d offset, int max){
		//moving object
		GL11.glPushMatrix();

		//Drawing the Points with an offset
		GL11.glBegin(GL11.GL_POINTS);
		for (int i = 0; i < max; i++)
			GL11.glVertex2d(vertexes[i].getPoint().getX() + offset.getX(), vertexes[i].getPoint().getY() + offset.getY());
		//end
		GL11.glEnd();
		GL11.glPopMatrix();
	}

	//Points made by list of Vertexes and offset by a Vector  
	public void pointsOffset(Vector2d[] vertexes, Vector2d offset, int max){
		//moving object
		GL11.glPushMatrix();

		//Drawing the Points with an offset
		GL11.glBegin(GL11.GL_POINTS);
		for (int i = 0; i < max; i++)
			GL11.glVertex2d(vertexes[i].getX() + offset.getX(), vertexes[i].getY() + offset.getY());
		//end
		GL11.glEnd();
		GL11.glPopMatrix();
	}

	//Points made by a of Vector
	public void point(Vector2d vertex){
		//moving object
		GL11.glPushMatrix();

		//Drawing the Points with an offset
		GL11.glBegin(GL11.GL_POINT);
		GL11.glVertex2d(vertex.getX(), vertex.getY());
		//end
		GL11.glEnd();
		GL11.glPopMatrix();
	}

	//to calculate the center from vertexes???????
	//Polygon made by a list of Vertexes rotated by this angle
	public void polygonV2d(Vector2d[] vertexes, double angle){//This need much more thinking
		//moving object
		GL11.glPushMatrix();

		//GL11.glTranslated(SquareCenter.x,SquareCenter.y,0);
		GL11.glRotated(angle, 0, 0, 1);
		//Drawing the Polygon
		GL11.glBegin(GL11.GL_POLYGON);
		for (int i = 0; i < vertexes.length; i++)
			GL11.glVertex2d(vertexes[i].getX(), vertexes[i].getY());
		//end
		GL11.glEnd();
		GL11.glPopMatrix();
	}

	//Polygon made by a list of Vertexes and a Center then rotated
	public void polygonV2d(RegPolyGen r){
		//moving object
		GL11.glPushMatrix();
		GL11.glTranslated(r.getCenter().getX(),r.getCenter().getY(),0);
		GL11.glRotated(Math.toDegrees(r.getAngleRads()), 0, 0, 1); 
		//Drawing the Polygon
		GL11.glBegin(GL11.GL_LINE_LOOP); //LINE_LOOP does outline, POLYGON does filled
		for (int i = 0; i < r.getVertexes().length; i++)
			GL11.glVertex2d(r.getVertexes()[i].getX(), r.getVertexes()[i].getY());
		//end
		GL11.glEnd();
		GL11.glPopMatrix();
	}

}
