/**
 * @author Roy Lin
 */
public class Body{
	/** The properties of the planet. */
	public double xxPos; // Its current x position.
	public double yyPos; // Its current y position.
	public double xxVel; // Its current velocity in the x direction.
	public double yyVel; // Its current velocity in the y direction.
	public double mass;  // Its mass.
	public String imgFileName;  // The name of the file that correspond to the image that depicts the body.
	public static final double G = 6.67e-11; // Gravitational constant
	private static final String preFile = "images/"; // images are in images/ file.
	/** Constructor */
	public Body(double xP, double yP, double xV, double yV, double m, String img){
		xxPos = xP;
		yyPos = yP;
		xxVel = xV;
		yyVel = yV;
		mass = m;
		imgFileName = img;
	}
	
	public Body(Body b){
		this.xxPos = b.xxPos;
		this.yyPos = b.yyPos;
		this.xxVel = b.xxVel;
		this.yyVel = b.yyVel;
		this.mass = b.mass;
		this.imgFileName = b.imgFileName;
	}
	
	/** Calculate the square of the distance between two bodies. */
	public double calcDistanceSquare(Body b){
		double distanceX = this.xxPos - b.xxPos;
		double distanceY = this.yyPos - b.yyPos;
		double distance_Square = distanceX * distanceX + distanceY * distanceY;
		return distance_Square;
	}
	/** Calculate the distance between two Bodies. */
	public double calcDistance(Body b){
		return Math.sqrt(this.calcDistanceSquare(b));
	}
	
	/** Calculate the total force exerted on this Body */
	public double calcForceExertedBy(Body b){
		double distance_Square = this.calcDistanceSquare(b);
		return Body.G * this.mass * b.mass / distance_Square;
	}
	
	/** Calculate the force at X and Y direction. */
	public double calcForceExertedByX(Body b){
		double distanceX = b.xxPos - this.xxPos;
		double distance = this.calcDistance(b);
		double netForce = this.calcForceExertedBy(b);
		return netForce * distanceX / distance;
	}
	
	public double calcForceExertedByY(Body b){
		double distanceY = b.yyPos - this.yyPos;
		double distance = this.calcDistance(b);
		double netForce = this.calcForceExertedBy(b);
		return netForce * distanceY / distance;
	}
	
	/** Calculate the net force at X and Y direction exerted by all other bodies*/
	public double calcNetForceExertedByX(Body[] allBodys){
		double netForceByX = 0.0;
		for (Body b : allBodys){
			if (!this.equals(b)){ // Not the same body.
				netForceByX += this.calcForceExertedByX(b);
			}
		}
		return netForceByX;
	}
	
	public double calcNetForceExertedByY(Body[] allBodys){
		double netForceByY = 0.0;
		for (Body b : allBodys){
			if (!this.equals(b)){ // Not the same body.
				netForceByY += this.calcForceExertedByY(b);
			}
		}
		return netForceByY;
	}
	
	/** Update the state of the Body when it is exerted by some force. */
	public void update(double deltaTime, double forceX, double forceY){
		// Calculate the acceleration using x and y forces.
		double accX = forceX / this.mass;
		double accY = forceY / this.mass;
		// Calculate the new velocity by using the acceleration and current velocity.
		this.xxVel += deltaTime * accX;
		this.yyVel += deltaTime * accY;
		// Calculate the new position by using the new velocity.
		this.xxPos += deltaTime * this.xxVel;
		this.yyPos += deltaTime * this.yyVel;
	}
	
	public void draw(){
		StdDraw.picture(this.xxPos, this.yyPos, preFile + this.imgFileName);
	}
}