//David Govorko, 12/12/2014 
package attempt;

//import java.util.*;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
//Change Log
//Storage is now just points not slope
//As a result Adding is just adding and checking for ~same points
//Added Least Square Line Estimation
//Pathfinding cleaned, cannot handle identical collisions, but still steps 1-3 and ~4(lines only)
//Added rendering of line of best fit
//Adding is now a boolean
//Direction vectors now part of circle and polygon object
//Movement is now a method in circle and polygon
//


public class NowAddPathFinding {
	//TOO DOO make an user interface
	//TOO DOO VOO DOO WHOO DOO VOO DOO I DOO VOO DOO U DOO TOO? Estimates shape and avoids it live 
	//Make a world generator
	//Make a map discovery program
	//make a classification program (circle, line, etc...) include kasa circle guesser
	//some kind of way to get rid of floating point error 10^-15 == 0
	//guess corners too
	//Create an arch object later
	//make the auto renders show suggestions of weather to update or not, visual feedback on changes
	//Mouse interface for that?
	//figure out if whole line or just that explored region should be investigated.
	//this is the start of major things such as different storages for different types of borders
	//also pathfinder complications and connecting different identified objects. MAP BUILDING w'ell call it
	
	//Error on bounce is do to position refresh rate, decrease Vector2d size, ??I Forgot what this means. halp
	//increase number of nanoseconds each frame waits ??Again i forgot
	
	//**World Dimensions**\\
	static final int worldWidth = 600;
	static final int worldHeight = 640;
	static final Vector2d worldDim = new Vector2d(worldWidth,worldHeight);
	
						//What i'm doing now
	//Figure out why Pathfinder won't change return angle 
	//expand on pathfinding to recognize circles in addition to arches
	//??WHYYYYYY IS COLLISON DATA POINT 1 WRONG???
	//Change Square start to change with pathfinder
	//make sure to use .GEt and .Set instead of .x and .y and general clean up of initiated variables

	//**Feedback Timers**\\
	int shutUp = 0;
	
	//**GEOMETRY**\\
	//center of the square
	Vector2d squareStart = new Vector2d(150, 320);//this needs to change whenever pathfinder starts new path
	Vector2d SquareCenter = new Vector2d(squareStart.getX(), squareStart.getY());
	Vector2d sDir = new Vector2d((Math.random()*-0.5), (Math.random()*-0.5));
	double angles = 0;
	RegPolyGen square = new RegPolyGen(4, SquareSide, angles, sDir, SquareCenter); //angle is always in rads	
	//center of the circle and direction
	Vector2d CircleCenter = new Vector2d(450, 320);
	Vector2d CircleDirection = new Vector2d(0, 0);
	static final double radius = 100;
	static final double SquareSide = 50;
	CircleGen circle = new CircleGen(radius, CircleDirection, CircleCenter);
	//collision point
	Vector2d CollisionPoint = new Vector2d(0, 0);
	//Border size
	Vector2d BorderSize = new Vector2d(66, 640);
	//Offset for anything drawn on second "world" ///////////////////////FIX THAT TODAY
	Vector2d BorderOrigin = new Vector2d(worldDim.getX(), 0);	
	//Virtual lines at the borders of the map
	LineGen line1 = new LineGen(new Vector2d(0,0), new Vector2d(0, worldDim.getY()));//0,0 going up
	LineGen line2 = new LineGen(new Vector2d(0,0), new Vector2d(worldDim.getX(), 0));//0,0 going right
	LineGen line3 = new LineGen(new Vector2d(worldDim.getX(), worldDim.getY()),
			new Vector2d(0, -worldDim.getY()));//topRight corner down
	LineGen line4 = new LineGen(new Vector2d(worldDim.getX(), worldDim.getY()), 
			new Vector2d(-worldDim.getX(), 0));//topRight corner left
	
	Vector2d secondWorldShift = new Vector2d(BorderOrigin.getX() + BorderSize.getX(), 0);	
	RenderA renderA = new RenderA();
	
	//**GAME TIMING**\\
	//time of last frame
	long lastFrame;
	//fps
	int fps;
	//previous fps
	long lastFPS;
	//frame rate cap
	private int speed = 200;
	
	//**DATA STORAGE**\\
	int store = 0;
	Vector2d[] storage = new Vector2d[100];
	CircleGen kasaCircle;
	LineGen lsLine;
	Collision collider = new Collision();
	
	//**Path Finder**\\
	int numCollisions = 0;
	double reverseSteps = 0;

	public void start() {

		//Generic screen creation
		try {
			Display.setDisplayMode(new DisplayMode(worldWidth*2 + 66, worldHeight)); //it takes ints
			Display.create();
		} catch (LWJGLException e)  {
			e.printStackTrace();
			System.exit(0);
		}

		//initGl is its own method now see initGL() method
		initGL();

		//keeps all objects moving at constant speed(fps)
		getDelta(); //initialize lastFrame before refreshing starts
		lastFPS = getTime(); //Initialize FPS

		while(!Display.isCloseRequested()) {// this is where the magic behind the curtains happens
			int delta = getDelta();

			update(delta); //house cleaning, keayboard, fps, input

			renderGL();//where the visual magic happens

			Display.update();
			Display.sync(speed);//default limits to 200 FPS
		}
		Display.destroy();
	}

	public void initGL() {
		//generic clears screen and initiates the screen
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, worldDim.getX() + BorderOrigin.getX(), 0, worldDim.getY(), 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

	public int getDelta() {
		//calculates number of milliseconds have passed since lastFrame and returns that value as delta
		long time = getTime(); //gets time in milliseconds from CPU
		int delta = (int) (time - lastFrame);
		lastFrame = time;

		return delta;
	}

	public long getTime() {
		//gets time in milliseconds from pc
		return (Sys.getTime()*1000) / Sys.getTimerResolution(); 
	}

	public void update(int delta) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) square.getCenter().setX(square.getCenter().getX() - Math.random()*0.5*delta);
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) square.getCenter().setX(square.getCenter().getX() + Math.random()*0.5*delta);
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) square.getCenter().setY(square.getCenter().getY() - Math.random()*0.5*delta);
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) square.getCenter().setY(square.getCenter().getY() + Math.random()*0.5*delta);
		if (Keyboard.isKeyDown(Keyboard.KEY_PERIOD)) speed += 2;
		if (Keyboard.isKeyDown(Keyboard.KEY_COMMA)) speed -= 2;

		collision();

		square.move();
		circle.move();
		
		updateFPS(); //Fps Counter/displayer
	}

	public void updateFPS()	{
		//calculates fps 
		if (getTime()-lastFPS > 1000) {
			Display.setTitle("Le titre FPS " + fps);
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}

	public void renderGL() {

		//Clears screen and depth buffer
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		//GL11.glGetString(GL_VERSION);

		GL11.glPointSize(1.0f);	
		GL11.glLineWidth(1.0f);
		GL11.glColor3f(1.0f,0.75f,0.0f);
		renderA.polygonV2d(square); //The Square

		//GL11.glColor3f(0.0f,0.0f,1.0f);
		//GL11.glPointSize(3.0f);//The square's corners		
		//renderA.pointsOffset(square.generateVerts(), square.getCenter());

		GL11.glColor3f(0.0f,0.0f,1.0f);
		renderA.circleV2d(circle.getCenter(), circle.getRadius());//The Circle

		GL11.glColor3f(0.0f,1.0f,0.5f);
		renderA.rectangleOriginV2d(BorderOrigin, BorderSize);//The Border

		GL11.glPointSize(2.0f);		
		GL11.glColor3f(1.0f,1.0f,1.0f);//Collision points
		renderA.pointsOffset(storage, secondWorldShift, store);

		GL11.glLineWidth(2.5f); 		
		GL11.glColor3f(0.0f,1.0f,0.0f);//circle to square collision line
		renderA.lineV2d(square.getCenter(), CollisionPoint);

		GL11.glColor3f(1.0f,0.0f,0.0f);//velocity vector
		renderA.line(square.getCenter().getX(), square.getCenter().getY(), 
				square.getCenter().getX() + square.getDirection().getX()*512, square.getCenter().getY() + square.getDirection().getY()*512);

		if (Keyboard.isKeyDown(Keyboard.KEY_K) && store > 3) {
			kasaCircle = kasaCircleGuess(storage, store);
			if (shutUp%64==0) System.out.println("KASADATA CONFIRMATION LINE");
			shutUp++;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_G) && store > 3) {
			GL11.glColor3f(1.0f,1.0f,1.0f);//kasa circle
			renderA.circleV2d(kasaCircle.getCenter().add(secondWorldShift), kasaCircle.getRadius());
			if (shutUp%64==0) System.out.println("KASACIRCLE DRAWING CONFIRMATION LINE BUT WHERE IS CIRCLE");
			shutUp++;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_F) && store > 3) {
			GL11.glColor3f(1.0f,1.0f,1.0f);//Line of best Fit
			GL11.glLineWidth(2.5f);
			renderA.line(lsLine.getOrigin().getX() + BorderOrigin.getX() + BorderSize.getX(), lsLine.getOrigin().getY(), 
					lsLine.getVector().getX() + lsLine.getOrigin().getX() + BorderOrigin.getX() + BorderSize.getX(), lsLine.getVector().getY() + lsLine.getOrigin().getY());
			//System.out.println(lsLine.getVector());
		}

		square.setAngleRads(square.getAngleRads() + 0.001);
	}

	public void collision()	{
		double D = square.getCenter().dist(circle.getCenter());
		double AB =  (square.getLengthSide()/2*Math.sqrt(2) + circle.getRadius()); //stays here
		//Is using distance as first check necessary, or should collision always be detected? 
		if (line1.linePointDist(square.getCenter()) < square.getLongDistAcross() & collider.LinePoly(line1, square) != null) {
			System.out.println(square.getDirection());
			pathFinder(adding(collider.LinePoly(line1, square), store));
		}
		else if (line2.linePointDist(square.getCenter()) < square.getLongDistAcross() & collider.LinePoly(line2, square) != null) {
			pathFinder(adding(collider.LinePoly(line2, square), store));
		}
		else if (line3.linePointDist(square.getCenter()) < square.getLongDistAcross() & collider.LinePoly(line3, square) != null) {
			pathFinder(adding(collider.LinePoly(line3, square), store));
		}
		else if (line4.linePointDist(square.getCenter()) < square.getLongDistAcross() & collider.LinePoly(line4, square) != null) {
			pathFinder(adding(collider.LinePoly(line4, square), store));
		}			
		else if (D < (AB + square.getLongDistAcross() + 300)) { // the 300 needs to be based on speed of moving polygon(s), or else a fast moving polygon will skip this check	

			CollisionPoint = collider.CircleSqaure(square, circle);

			if (CollisionPoint.dist(circle.getCenter()) <= circle.getRadius()) {
				pathFinder(adding(CollisionPoint, store));
			}
		}
		//else if (printer%128==0) System.out.println(S);
		pathFinder(false);
	}

	public CircleGen kasaCircleGuess(Vector2d[] data, int iterate) {
		double aM,bM,rK = 0,A,B,C,D,E;//http://people.cas.uab.edu/~mosya/cl/CircleFitByKasa.cpp
		double sumX2 = 0,sumX = 0, sumY2 = 0,sumY = 0,sumXY = 0,sumXY2 = 0,sumX3 = 0,sumX2Y = 0,sumY3 = 0;
		CircleGen kasaData = new CircleGen();

		for (int n = 0; n < iterate; n++){
			sumX2 +=  Math.pow(data[n].getX(),2);
			sumX += data[n].getX();
			sumY2 +=  Math.pow(data[n].getY(),2);
			sumY += data[n].getY();
			sumXY += data[n].getX()*data[n].getY();
			sumXY2 += data[n].getX()* Math.pow(data[n].getY(),2);
			sumX3 +=  Math.pow(data[n].getX(),3);
			sumX2Y += data[n].getY()* Math.pow(data[n].getX(),2);
			sumY3 +=  Math.pow(data[n].getY(),3);	 
		}

		A = iterate*sumX2-sumX*sumX;
		B = iterate*sumXY-sumX*sumY;
		C = iterate*sumY2-sumY*sumY;
		D =  (0.5*(iterate*sumXY2-sumX*sumY2+iterate*sumX3-sumX*sumX2));
		E =  (0.5*(iterate*sumX2Y-sumX2*sumY+iterate*sumY3-sumY*sumY2));
		System.out.println("A    : " + A);
		System.out.println("B    : " + B);
		System.out.println("C    : " + C);
		System.out.println("D    : " + D);
		System.out.println("E    : " + E);

		aM = (D*C-B*E)/(A*C-B*B);
		bM = (A*E-B*D)/(A*C-B*B);

		for (int n = 0; n < iterate; n++)
			rK += (Math.pow(data[n].getX()-aM, 2)+Math.pow(data[n].getY()-bM, 2))/iterate;
		rK = Math.sqrt(rK);
		System.out.println(rK + "   should be  " + circle.getRadius());
		System.out.println(aM + "   should be  " + circle.getCenter().getX());
		System.out.println(bM + "   should be  " + circle.getCenter().getY());

		kasaData.setRadius(rK);
		kasaData.setCenter(new Vector2d(aM,bM));

		// input  apache math library for covaraiance and see if it is faster.
		return kasaData;
	}
	

	public LineGen leastSquareLineGuess(Vector2d[] data, int iterate){
		double mX = 0,mY = 0,sumX2 = 0,sumXY = 0;//hotmath.com/hotmath_help/topics/line-of-best-fit.html

		for(int n = 0; n < iterate; n++){
			mX += data[n].getX();
			mY += data[n].getY();
		}

		for(int n = 0; n < iterate; n++){
			sumX2 +=  Math.pow(data[n].getX() - mX,2);
			sumXY += (data[n].getX() - mX)*(data[n].getY() - mY);
		}
		
		return new LineGen(data[0].integizerCeil(), new Vector2d(sumX2, sumXY));//min and max points don't matter yet
	}

	public boolean adding(Vector2d CollisionObject, int iterate) {
		boolean hit = false;
		if (iterate > 0) {
			for(int IT = 0; IT < iterate; IT++) {
				if (CollisionObject.dist(storage[IT]) < 1.5) {
					System.out.println("There shan't be any repetition in this data structure!!!"); 
					IT = iterate;
					store--;
					hit = false;
				}else {
					storage[iterate] = CollisionObject.deepCopy();
					hit =  true;
				}
			}
			store++;
		} else {
			storage[iterate] = CollisionObject.deepCopy();
			store++;
			hit = true;
		}
		for(int IT = 0; IT < iterate; IT++) System.out.println(storage[IT]);
		return hit;
	}
	
	/*
	public void pathFinder(){
		S.x *= -1;
		S.y *= -1;
		double slopeC;
		double slopeA;
		//find slope of center square to data points and compare with the current slope
		//then change and test accordingly
		for (int it = 0; it < (store-1); it++){
			slopeC  = (storage[it].getY() - SquareCenter.getY())/(storage[it].getX() - SquareCenter.getX());
			slopeA = S.getY()/S.getX();
			if (Math.abs(slopeA) - Math.abs(slopeC) <= 0.5) {
				System.out.println("I'm not going back to that point!");
				it = 0;
				S.x *= 0.9 + 0.2*Math.random();
				S.y *= 0.9 + 0.2*Math.random();
			}
		}
		//System.out.println(S);
	} */
	
	//make it so that the shape avoids repeats and if repeat does happen just retry
	//stop rotate when reversing?
	public void pathFinder(boolean collision){//reorder for speed
		if (collision & numCollisions < 3) {//collision and calc how far back to move
			System.out.println("num" + numCollisions);
			numCollisions++;	
			square.setDirection(square.getDirection().scalarMulti(-1));//reverse needs to take into account spinning collisions and weird shapes plus hitting other shapes
			reverseSteps = Math.min(square.getLongDistAcross(),square.getCenter().dist(squareStart))/square.getDirection().magnitude();
		} else if (collision & numCollisions == 3){
			System.out.println(13);
			numCollisions = 0;
			square.setDirection(square.getDirection().scalarMulti(-1));//reverse needs to take into account spinning collisions and weird shapes plus hitting other shapes
			reverseSteps = 0;
			lsLine = leastSquareLineGuess(storage, store);
			System.out.println(lsLine.getOrigin());
		} else if(reverseSteps > 0) {//moving back
			System.out.println(2);
			reverseSteps--;
		} else if(reverseSteps < 0) {//calc direction forward to go to
			System.out.println(3);
			reverseSteps = 0;
			square.setDirection(square.getDirection().scalarMulti(-1));
			double tempAngle = Math.atan(square.getShortDistAcross()/Math.min(square.getLongDistAcross(),square.getCenter().dist(squareStart)));
			square.getDirection().rotate(tempAngle*(2*Math.random()-1));
			System.out.println(square.getDirection());
		}
	}

	public static void main(String[] args) {
		NowAddPathFinding dis = new NowAddPathFinding();
		dis.start();
	}
}