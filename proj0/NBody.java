
public class NBody{
	/** return a double corredsponding to the radius of the universe. */
	public static double readRadius(String fileName){
		In in = new In(fileName);
		int planetsNumber = in.readInt();
		double universeRadius = in.readDouble();
		return universeRadius;
	}
	
	public static Body[] readBodies(String fileName){
		In in = new In(fileName);
		int planetsNumber = in.readInt(); 				// 1st line: The total number of stars in this .txt file.
		double uniniverseRadius = in.readDouble();		// 2nd line: Universe radius.
		Body allBodies[] = new Body[planetsNumber];		// Create a stars array.
		int i = 0;
		while (i < planetsNumber){
			double xxPos = in.readDouble();				// read its x-position from this .txt file.
			double yyPos = in.readDouble();				// read its y-position from this .txt file.
			double xxVel = in.readDouble(); 			// read x-direction velocity.
			double yyVel = in.readDouble();				// read y-direction velocity.
			double mass = in.readDouble();				// read its mass.
			String planetName = in.readString();		// read its corredsponding image file names.
			allBodies[i] = new Body(xxPos, yyPos, xxVel, yyVel, mass, planetName); // Construct Body.
			i += 1;
		}
		return allBodies;
	}
	
	public static void main(String[] args){
		// read total time, delta time and corredsponding .txt files from command line.
		double T = Double.parseDouble(args[0]);
		double dt = Double.parseDouble(args[1]);
		String filename = args[2];
		double universeRadius = NBody.readRadius(filename);
		Body stars[] = readBodies(filename);
		int starsNum = stars.length;
		
		StdDraw.setScale(-2 * universeRadius, 2 * universeRadius);
		StdDraw.clear();
		StdDraw.picture(0, 0, "images/starfield.jpg");
		for(Body star: stars){
			star.draw();
		}
		double xForces[] = new double[starsNum];
		double yForces[] = new double[starsNum];
		// Animation
		StdDraw.enableDoubleBuffering();	// This technique used to prevent flickering in the animation.
		for (double time = 0.0; time < T; time += dt){
			for (int index = 0; index < starsNum; index++){
				// Calculate net x and y forces for each body.
				xForces[index] = stars[index].calcNetForceExertedByX(stars);
				yForces[index] = stars[index].calcNetForceExertedByY(stars);
			}
			for (int index = 0; index < starsNum; index++){	
				// Update position and velocity for each body.
				stars[index].update(dt, xForces[index], yForces[index]);
			}
			// draw universe background
			StdDraw.picture(0, 0, "images/starfield.jpg");
			
			// draw all planets
			for (int index = 0; index < starsNum; index++){
				stars[index].draw();
			}
			StdDraw.show();
			StdDraw.pause(10);
		}
 
		
	}
}