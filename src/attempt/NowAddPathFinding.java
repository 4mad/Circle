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
	//As a result adding is just adding and checking for ~same points
	//

public class NowAddPathFinding {
	//TOO DOO make an interface, randomizer for angle of bounce|Should i wrap storage's elements into new 2vectors object, or new data structure???
	//TOO DOO VOO DOO WHOO DOO VOO DOO I DOO VOO DOO U DOO TOO? Estimates shape and avoids it live 
	//Make a world generator
	//make a classification program (circle, line, etc...) include kasa circle guesser
	//some kind of way to get rid of floating point error 10^-15 == 0
	//guess corners too
	//Create an arch object later

	//Error on bounce is do to position refresh rate, decrease Vector2d size, ??I Forgot what this means. halp
	//increase number of nanoseconds each frame waits ??Again i forgot
	static final int worldWidth = 600;
	static final int worldHeight = 640;

	static final Vector2d worldDim = new Vector2d(worldWidth,worldHeight);

								//What i'm doing now
	//find pathfinding error
	//make adding return boolean and put it into pathFinding not in collision
	//adding a line of best fit
	//make a catch for the pathfinder that quits it if 50 checks have happened in a row
	//make sure to use .GEt and .Set instead of .x and .y and general clean up of initiated variables

	int shutUp = 0;
	int printer;
	private Vector2d squareStart = new Vector2d(150, 320);
	
	private int speed = 200;
	//**GEOMETRY**\\
	//center of our square
	Vector2d SquareCenter = new Vector2d(squareStart.getX(), squareStart.getY());
	//center of our circle
	Vector2d CircleCenter = new Vector2d(450, 320);
	//collision point
	Vector2d CollisionPoint = new Vector2d(0, 0);
	//Border size
	Vector2d BorderSize = new Vector2d(66,640);
	//Offset for anything drawn on second "world"
	Vector2d BorderOrigin = new Vector2d(worldDim.getX(),0);

	//width,height,radius
	static final double radius = 100;
	static final double SquareSide = 50;
	//double h = 50;
	//double w = 50; These are for rectangles
	double angles = 0;

	RegPolyGen square = new RegPolyGen(4, SquareSide, angles, SquareCenter); //angle is always in rads	

	CircleGen circle = new CircleGen(radius, CircleCenter);

	LineGen line1 = new LineGen(new Vector2d(0,0), new Vector2d(0, worldDim.getY()));//0,0 going up
	LineGen line2 = new LineGen(new Vector2d(0,0), new Vector2d(worldDim.getX(), 0));//0,0 going right
	LineGen line3 = new LineGen(new Vector2d(worldDim.getX(), worldDim.getY()),
			new Vector2d(0, -worldDim.getY()));//topRight corner down
	LineGen line4 = new LineGen(new Vector2d(worldDim.getX(), worldDim.getY()), 
			new Vector2d(-worldDim.getX(), 0));//topRight corner left

	RenderA renderA = new RenderA();
	//**SQUARE DIRECTION**\\
	double dx = (Math.random()*-0.5);
	double dy = (Math.random()*-0.5);//fix this to be only in S.set and get

	Vector2d S = new Vector2d(dx,dy);
	//**GAME TIMING**\\
	//time of last frame
	long lastFrame;
	//fps
	int fps;
	//previous fps
	long lastFPS;
	//**DATA STORAGE**\\
	int store = 0;
	Vector2d[] storage = new Vector2d[100];
	CircleGen kasaCircle;
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
		long time = getTime(); //gets time in milliseconds from pc
		int delta = (int) (time - lastFrame);
		lastFrame = time;

		return delta;
	}

	public long getTime() {
		//gets time in milliseconds from pc
		return (Sys.getTime()*1000) / Sys.getTimerResolution(); 
	}

	public void update(int delta) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) SquareCenter.x -= Math.random()*0.5 * delta;
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) SquareCenter.x += Math.random()*0.5 * delta;
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) SquareCenter.y -= Math.random()*0.5 * delta;
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) SquareCenter.y += Math.random()*0.5 * delta;
		if (Keyboard.isKeyDown(Keyboard.KEY_PERIOD)) speed += 2;
		if (Keyboard.isKeyDown(Keyboard.KEY_COMMA)) speed -= 2;

		collision();

		//square.setCenter(square.getCenter().add(S));
		SquareCenter.y = SquareCenter.y + S.y;
		SquareCenter.x = SquareCenter.x + S.x;

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
		renderA.pointsOffset(storage, new Vector2d(BorderOrigin.getX() + BorderSize.getX(), 0), store);

		GL11.glLineWidth(2.5f); 		
		GL11.glColor3f(0.0f,1.0f,0.0f);//circle to square collision line
		renderA.lineV2d(square.getCenter(), CollisionPoint);

		GL11.glColor3f(1.0f,0.0f,0.0f);//velocity vector
		renderA.line(square.getCenter().getX(), square.getCenter().getY(), 
				square.getCenter().getX() + S.x*512, square.getCenter().getY() + S.y*512);

		if (Keyboard.isKeyDown(Keyboard.KEY_K) && store > 3) {
			kasaCircle = kasaCircleGuess(storage,store);
			if (shutUp%64==0) System.out.println("KASADATA CONFIRMATION LINE");
			shutUp++;
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_G) && store > 3) {
			GL11.glColor3f(1.0f,1.0f,1.0f);//kasa circle
			renderA.circle(kasaCircle.getCenter().getX() + BorderOrigin.getX() + BorderSize.getX(),
					kasaCircle.getCenter().getY() + BorderOrigin.getY(), kasaCircle.getRadius());
			if (shutUp%64==0) System.out.println("KASACIRCLE DRAWING CONFIRMATION LINE BUT WHERE IS CIRCLE");
			shutUp++;
		}

		square.setAngleRads(square.getAngleRads() + 0.001);
	}

	public void collision()	{
		double D = square.getCenter().dist(circle.getCenter());
		double AB =  (square.getLengthSide()/2*Math.sqrt(2) + circle.getRadius()); //stays here
		//use polygon distance across tool
		//reorder below based on probability 
		double longDist = square.getLongDistAcross();
		if (line1.linePointDist(square.getCenter()) < longDist & collider.LinePoly(line1, square) != null) {
			pathFinder(true);
			adding(collider.LinePoly(line1, square), store);
		}
		else if (line2.linePointDist(square.getCenter()) < longDist & collider.LinePoly(line2, square) != null) {
			pathFinder(true);
			adding(collider.LinePoly(line2, square), store);
		}
		else if (line3.linePointDist(square.getCenter()) < longDist & collider.LinePoly(line3, square) != null) {
			pathFinder(true);
			adding(collider.LinePoly(line3, square), store);
		}
		else if (line4.linePointDist(square.getCenter()) < longDist & collider.LinePoly(line4, square) != null) {
			pathFinder(true);
			adding(collider.LinePoly(line4, square), store);
		}			
		else if (D < (AB + longDist + 300)) { // the 300 needs to be based on speed of moving polygon(s), or else a fast moving polygon will skip this check	

			CollisionPoint = collider.CircleSqaure(square, circle);

			if (CollisionPoint.dist(circle.getCenter()) <= circle.getRadius()) {
				adding(CollisionPoint, store);
				pathFinder(true);
			}

			//else if (printer%64==0) System.out.println("Zone " + D); 

			/* 0.15 error 730 fps -- little to no clipping
			The code above is more precise 
			If it can be made to work with any polygon then better alternative */

			/* 0.21 error 740 fps -- up to 1% clipping
			Code bellow has varying precision from bad to better then top code but possibly faster
			At the moment it is future proof with regards to polygon */

			/*Dir = new Vector2d(SquareCenter.x-CircleCenter.x,SquareCenter.y-CircleCenter.y,0);
			double theta = 0;
			if (Math.abs(Dir.x) > Math.abs(Dir.y)) {
				theta =  (Math.PI/2-Math.atan(Math.abs(Dir.x)/Math.abs(Dir.y)));
				CollisionPoint.setX( CircleCenter.x + Math.abs(Dir.x)/Dir.x*(Math.abs(Dir.x) - w/2));
				CollisionPoint.setY( CircleCenter.y +  (Math.abs(Dir.y)/Dir.y*(Math.abs(Dir.y) - h/2*Math.tan(theta))));	
			} else if (Math.abs(Dir.x) < Math.abs(Dir.y)) {
				theta =  (Math.PI/2-Math.atan(Math.abs(Dir.y)/Math.abs(Dir.x)));
				CollisionPoint.setX( CircleCenter.x +  (Math.abs(Dir.x)/Dir.x*(Math.abs(Dir.x) - w/2*Math.tan(theta))));
				CollisionPoint.setY( CircleCenter.y + Math.abs(Dir.y)/Dir.y*(Math.abs(Dir.y) - h/2));				
			} else if (Math.abs(Dir.x) == Math.abs(Dir.y)) {
				CollisionPoint.setX( CircleCenter.x +  (Math.abs(Dir.x)/Dir.x*(Math.abs(Dir.x) - w/2*Math.sqrt(2))));
				CollisionPoint.setY( CircleCenter.y +  (Math.abs(Dir.y)/Dir.y*(Math.abs(Dir.y) - h/2*Math.sqrt(2))));
			}
			if (CollisionPoint.dist(CircleCenter)<= radius) {
				S.x *= -1;
				S.y *= -1;
				adding(CollisionPoint,Dir,store);
			}
			//else if (printer%64==0) System.out.println("Zone " + D);*/ 
		}
		//else if (printer%128==0) System.out.println(S);
		printer++;		
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

	public void adding(Vector2d CircleCenterB, int iterate) {
		if (iterate > 0) {
			for(int IT = 0; IT < iterate; IT++) {
				if (CircleCenterB.dist(storage[IT]) < 2) {
					System.out.println("There shan't be any repetition in this data structure!!!"); 
					IT = iterate;
					store--;
				}else {
					storage[iterate] = CircleCenterB.deepCopy();
				}
			}
			store++;
		} else {
			storage[iterate] = CircleCenterB.deepCopy();
			store++;
		}
		//for(int IT = 0; IT < iterate; IT++) System.out.println(storage[IT]);
	}

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
		System.out.println(S);
	}
	
	public Vector2d pathFinder(boolean collision){//reorder for speed
		if (collision & numCollisions == 0 & reverseSteps == 0) {//collision and calc how far back to move
			numCollisions += 1;
			S.setX(-1*S.getX());
			S.setY(-1*S.getY());//reverse needs to take into account spinning collisions and weird shapes plus hitting other shapes
			reverseSteps = Math.min(square.getLongDistAcross(),square.getCenter().dist(squareStart))/S.magnitude();
			return S;
			//what about going back and holding down on gin back only
		} else if(numCollisions == 1 & reverseSteps > 0) {//moving back
			reverseSteps -= 1;
			return S;
		}else if(numCollisions == 1 & reverseSteps < 0) {//calc direction forward to go to
			reverseSteps = 0;
			S.setX(-1*S.getX());
			S.setY(-1*S.getY());
			double tempAngle = Math.atan(square.getShortDistAcross()/Math.min(square.getLongDistAcross(),square.getCenter().dist(squareStart)));
			S.rotate(tempAngle*(2*Math.random()-1));
			return S;
		}else if(numCollisions == 1 & reverseSteps == 0){//need it? Go back
			return S;
		}else if (collision & numCollisions == 1 & reverseSteps == 0 ){
			System.out.println("second collision");
			numCollisions += 1;
			S.setX(-1*S.getX());
			S.setY(-1*S.getY());//reverse needs to take into account spinning collisions and weird shapes plus hitting other shapes
			reverseSteps = Math.min(square.getLongDistAcross(),square.getCenter().dist(squareStart))/S.magnitude();
			return S;
		} else if(numCollisions == 2 & reverseSteps > 0) { 
			reverseSteps -= 1;
			return S;
		}else if(numCollisions == 2 & reverseSteps < 0) {
			reverseSteps = 0;
			S.setX(-1*S.getX());
			S.setY(-1*S.getY());
			double tempAngle = Math.atan(square.getShortDistAcross()/Math.min(square.getLongDistAcross(),square.getCenter().dist(squareStart)));
			S.rotate(tempAngle*(2*Math.random()-1));
			return S;
		}else if(numCollisions == 2 & reverseSteps == 0){//need it?
			return S;
		}else if (collision & numCollisions == 2 & reverseSteps == 0 ){
			numCollisions += 1;
			S.setX(-1*S.getX());
			S.setY(-1*S.getY());//reverse needs to take into account spinning collisions and weird shapes plus hitting other shapes
			reverseSteps = Math.min(square.getLongDistAcross(),square.getCenter().dist(squareStart))/S.magnitude();
			return S;
		}
		
		return S;
	}

	public static void main(String[] args) {
		NowAddPathFinding dis = new NowAddPathFinding();
		dis.start();
	}
}