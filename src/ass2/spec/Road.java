package ass2.spec;

import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

import ass2.game.Drawable;
import ass2.game.GameObject;
import ass2.game.Material;
import ass2.game.Texture;
import ass2.math.Vector4f;

/**
 * COMMENT: Comment Road 
 *
 * @author malcolmr
 */
public class Road extends GameObject implements Drawable {

	private ArrayList<Double> myPoints;
	private ArrayList<double[]> bezierPoints;
	private ArrayList<double[]> vertexMesh;
	private double myWidth;
	private double myAltitude;
	private int numSegments = 20;
	private Texture roadTexture;
	private Material material;

	/**
	 * Create a new road starting at the specified point
	 */
	public Road(GameObject parent, double width, double x0, double y0) {
		super(parent);
		myWidth = width;
		myPoints = new ArrayList<Double>();
		myPoints.add(x0);
		myPoints.add(y0);
		bezierPoints = new ArrayList<double[]>();
		vertexMesh = new ArrayList<double[]>();
		generateBezierPoints();
		generateVertexMesh();
		//System.out.println("segments size = "+size());
		//printMyPoints();
		printVertexMesh();
	}

	/**
	 * Create a new road with the specified spine
	 *
	 * @param width
	 * @param spine
	 */
	public Road(GameObject parent, double width, double[] spine, double altitude) {
		super(parent);
		myWidth = width;
		this.myAltitude = altitude;
		myPoints = new ArrayList<Double>();
		for (int i = 0; i < spine.length; i++) {
			myPoints.add(spine[i]);
		}
		bezierPoints = new ArrayList<double[]>();
		vertexMesh = new ArrayList<double[]>();
		generateBezierPoints();
		generateVertexMesh();
		//System.out.println("segments size = "+size());
		//printMyPoints();
		printVertexMesh();
		System.out.println("myWidth: "+this.myWidth);
	}

	//Generate a points of the triangles to be drawn
	private void generateBezierPoints() {
		//generate all segments
		double[] currPoint; 
		for (int i=0; i<this.numSegments; i++){
			double temp = (double)i/(double)this.numSegments;
			currPoint = point(temp);
			bezierPoints.add(currPoint);
			System.out.println("Bezier Points: "+currPoint[0]+" "+currPoint[1]);
		}
		System.out.println("myWidth: "+this.myWidth);
	}
	
	private void generateVertexMesh (){
		double[][] currCorners;
		//double[][] tempCorners;
		double dx;
		double dz;
		for (int i =0; i<this.bezierPoints.size(); i++){
			if (i == this.bezierPoints.size()-1) {
				//special case for last point
				currCorners = calculateCorners(bezierPoints.get(i-1),bezierPoints.get(i));////get the old corners
				dx = bezierPoints.get(i)[0] - bezierPoints.get(i-1)[0]; //get old dx
				dz = bezierPoints.get(i)[1] - bezierPoints.get(i-1)[1]; //get old dx
				
				currCorners[0][0] += dx;
				currCorners[0][2] += dz;
				
				currCorners[1][0] += dx;
				currCorners[1][2] += dz;
				
				System.out.println("TEst, dx: "+dx+", dy: "+dz);
				vertexMesh.add(currCorners[0]);
				vertexMesh.add(currCorners[1]);
			} else {
				//general case
				currCorners = calculateCorners(bezierPoints.get(i),bezierPoints.get(i+1));
				vertexMesh.add(currCorners[0]);
				vertexMesh.add(currCorners[1]);
			}
		}
	}

	private double[][] calculateCorners(double[] pOne, double[] pTwo) {
		
		//getting vector between points
		double dx = pTwo[0] - pOne[0];
		double dz = pTwo[1] - pOne[1];
		
		//creating unit vector
		double vectorMagnitude = Math.sqrt((dx*dx)+(dz*dz));
		dx = dx/vectorMagnitude;
		dz = dz/vectorMagnitude;
		
		//scaling by road width
		
		dx *= (double)this.myWidth/(double)2;
		dz *= (double)this.myWidth/(double)2;
		
		double[] cornerOne = new double[3];
		double[] cornerTwo = new double[3];
		
		//System.out.println("dx:" + dx + " dz:" + dz);
		//getting first corner.
		cornerOne[0] = -dz; // adding the vector to the point pOne to get the corner
		cornerOne[0] += pOne[0];
		cornerOne[1] = this.myAltitude;
		cornerOne[2] = dx + pOne[1];
		System.out.println("corner 1: "+cornerOne[0]+", "+cornerOne[1]+", "+cornerOne[2]);
		
		cornerTwo[0] = dz+ pOne[0]; // adding the vector to the point pOne to get the other corner
		cornerTwo[1] = this.myAltitude;
		cornerTwo[2] = -dx;
		cornerTwo[2] += pOne[1];
		System.out.println("corner 2: "+cornerTwo[0]+", "+cornerTwo[1]+", "+cornerTwo[2]);
		
		double[][] corners = new double[2][3];
		corners[0] = cornerOne;
		corners[1] = cornerTwo;
		return corners;
	}
	
	private void printMyPoints () {
		System.out.println("Printing myPoints: ");
		for (int i=0; i<this.myPoints.size();i++){
			System.out.println("index "+i+": "+myPoints.get(i));
		}
	}
	
	private void printVertexMesh () {
		System.out.println("Printing vertexMesh: ");
		for (int i=0; i<this.vertexMesh.size();i+=1){
			System.out.println("index "+i+": "+vertexMesh.get(i)[0]+", "+vertexMesh.get(i)[1]+", "+vertexMesh.get(i)[2]);
		}
		
	}
	
	private double distanceBetweenPoints(double[] pOne, double[] pTwo){
		double dx = pTwo[0] - pOne[0];
		double dz = pTwo[1] - pOne[1];
		double vectorMagnitude = Math.sqrt((dx*dx)+(dz*dz));
		return vectorMagnitude;
	}
	
	/**
	 * The width of the road.
	 *
	 * @return
	 */
	public double width() {
		return myWidth;
	}

	/**
	 * Add a new segment of road, beginning at the last point added and ending at (x3, y3).
	 * (x1, y1) and (x2, y2) are interpolated as bezier control points.
	 *
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x3
	 * @param y3
	 */
	public void addSegment(double x1, double y1, double x2, double y2, double x3, double y3) {
		myPoints.add(x1);
		myPoints.add(y1);
		myPoints.add(x2);
		myPoints.add(y2);
		myPoints.add(x3);
		myPoints.add(y3);
	}

	/**
	 * Get the number of segments in the curve
	 *
	 * @return
	 */
	public int size() {
		return myPoints.size() / 6;
	}

	/**
	 * Get the specified control point.
	 *
	 * @param i
	 * @return
	 */
	public double[] controlPoint(int i) {
		double[] p = new double[2];
		p[0] = myPoints.get(i*2);
		p[1] = myPoints.get(i*2+1);
		return p;
	}

	/**
	 * Get a point on the spine. The parameter t may vary from 0 to size().
	 * Points on the kth segment take have parameters in the range (k, k+1).
	 *
	 * @param t
	 * @return
	 */
	public double[] point(double t) {
		int i = (int)Math.floor(t);
		t = t - i;

		i *= 6;

		double x0 = myPoints.get(i++);
		double y0 = myPoints.get(i++);
		double x1 = myPoints.get(i++);
		double y1 = myPoints.get(i++);
		double x2 = myPoints.get(i++);
		double y2 = myPoints.get(i++);
		double x3 = myPoints.get(i++);
		double y3 = myPoints.get(i++);

		double[] p = new double[2];

		p[0] = b(0, t) * x0 + b(1, t) * x1 + b(2, t) * x2 + b(3, t) * x3;
		p[1] = b(0, t) * y0 + b(1, t) * y1 + b(2, t) * y2 + b(3, t) * y3;
		//System.out.println("in function print p0 "+p[0]+", t = "+t);
		//System.out.println("in function print p0 "+p[1]+", t = "+t);
		return p;
	}

	/**
	 * Calculate the Bezier coefficients
	 *
	 * @param i
	 * @param t
	 * @return
	 */
	private double b(int i, double t) {

		switch(i) {

		case 0:
			return (1-t) * (1-t) * (1-t);

		case 1:
			return 3 * (1-t) * (1-t) * t;

		case 2:
			return 3 * (1-t) * t * t;

		case 3:
			return t * t * t;
		}

		// this should never happen
		throw new IllegalArgumentException("" + i);
	}
	
	

	@Override
	public void initialize(GL2 gl) {
		roadTexture = new Texture(gl, getClass().getResourceAsStream("/textures/roadTexture2.jpg"), true);
		gl.glEnable(GL2.GL_TEXTURE_2D);
		material = new Material();
		material.diffuse = new Vector4f(0.5f, 0.5f, 0.5f, 1.0f);
		
	}

	@Override
	public void dispose(GL2 gl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(GL2 gl) {
		
		//double textureLength = (double)this.vertexMesh.size()/(double)4;
		//gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glEnable(GL.GL_TEXTURE_2D);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, new float[]{material.ambient.x, material.ambient.y, material.ambient.z}, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, new float[]{material.diffuse.x, material.diffuse.y, material.diffuse.z}, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, new float[]{material.specular.x, material.specular.y, material.specular.z}, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, new float[]{material.phong.x, material.phong.y, material.phong.z}, 0);
		
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);

		gl.glBindTexture(GL2.GL_TEXTURE_2D, roadTexture.getTextureId());
		gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
		
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);

		gl.glEnable(GL2.GL_POLYGON_OFFSET_POINT);
		gl.glEnable(GL2.GL_POLYGON_OFFSET_LINE);
		gl.glEnable(GL2.GL_POLYGON_OFFSET_FILL);
		gl.glPolygonOffset(-1.0f, -1.0f);

		gl.glBegin(GL2.GL_TRIANGLE_STRIP);
		final double textureScale = 0.25;
		double spineDist=0;
		int bezierCount=0;
		for (int i = this.vertexMesh.size() - 2; i >= 0; i -= 2){
    		if(bezierCount+1<this.bezierPoints.size()){
    			spineDist+= distanceBetweenPoints(this.bezierPoints.get(bezierCount), this.bezierPoints.get(bezierCount+1));
    		}
			
			gl.glTexCoord2d(0.0, spineDist * textureScale);
			gl.glNormal3d(0.0, 1.0, 0.0);
    		gl.glVertex3d(this.vertexMesh.get(i)[0], this.vertexMesh.get(i)[1], this.vertexMesh.get(i)[2]);
    		
			gl.glTexCoord2d(1.0, spineDist * textureScale);
			gl.glNormal3d(0.0, 1.0, 0.0);
			gl.glVertex3d(this.vertexMesh.get(i+1)[0], this.vertexMesh.get(i+1)[1], this.vertexMesh.get(i+1)[2]);

    		bezierCount++;
		}
		gl.glEnd();

		gl.glPolygonOffset(0.0f, 0.0f);
		gl.glDisable(GL2.GL_POLYGON_OFFSET_POINT);
		gl.glDisable(GL2.GL_POLYGON_OFFSET_LINE);
		gl.glDisable(GL2.GL_POLYGON_OFFSET_FILL);

//		gl.glDisable(GL2.GL_LIGHTING);
//		gl.glBegin(GL2.GL_LINES);
//		for (int i=0;i+2<this.vertexMesh.size(); i+=2){
//			gl.glColor3f(0f,1f,0f);
//			gl.glVertex3d(this.vertexMesh.get(i)[0], this.vertexMesh.get(i)[1], this.vertexMesh.get(i)[2]);
//			gl.glColor3f(0f,1f,0f);
//			gl.glVertex3d(this.vertexMesh.get(i+2)[0], this.vertexMesh.get(i+2)[1], this.vertexMesh.get(i+2)[2]);
//		}
//		gl.glEnd();
//		//System.out.print("Display");
//		gl.glBegin(GL2.GL_LINES);
//		for (int i=1;i+2<this.vertexMesh.size(); i+=2){
//			gl.glColor3f(1f,0f,0f);
//			gl.glVertex3d(this.vertexMesh.get(i)[0], this.vertexMesh.get(i)[1], this.vertexMesh.get(i)[2]);
//			gl.glColor3f(1f,0f,0f);
//			gl.glVertex3d(this.vertexMesh.get(i+2)[0], this.vertexMesh.get(i+2)[1], this.vertexMesh.get(i+2)[2]);
//		}
//		gl.glEnd();
//		gl.glEnable(GL2.GL_LIGHTING);
		
		gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
	}


}
