package ass2.game;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

import ass2.math.Vector3;
import ass2.math.Vector4f;

public class LSystemTrees extends GameObject implements Drawable {

	private String initialValue;
	private String aValue;
	private String bValue;
	private int numGenerations;
	
	private String generatedString;
	private Texture treeTexture;
	
	private Material material;
	
	public LSystemTrees(GameObject parent, double x, double y, double z, String initialValue, String aValue, String bValue, int numGenerations) {
		super(parent);
		this.initialValue = initialValue;
		this.aValue = aValue;
		this.bValue = bValue;
		this.numGenerations = numGenerations;
		this.generatedString = generateString();
		
		this.transform.position = new Vector3(x, y, z);
		material = new Material();
		material.diffuse = new Vector4f(.5f, 0.35f, 0.05f, 0.0f);
	}
	
	private String generateString(){
		String currString = initialValue;
		System.out.println("GENERATING STRING");
		System.out.println(aValue);
		System.out.println(bValue);
		System.out.println(this.numGenerations);
		for(int i=0;i<this.numGenerations;i++){
			currString = currString.replaceAll("A", aValue);
			System.out.println(i);
			currString = currString.replaceAll("B", bValue);
			//string replace A value with [><SDAJSDHKUJNAL] string replace B
			//System.out.println(currString);
		}
		return currString;
	}
	
	public Vector3 getPosition() {
		return transform.position.clone();
	}
	
	private void drawBranch(GL2 gl, int branchDepth){
		
		double initialBranchRadius = 0.03;
		double branchRadiusReduction = 0.003;
		double branchLength = 0.2;
		double currentBranchRadius = initialBranchRadius - branchRadiusReduction*branchDepth;
		
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		//gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glPushMatrix();
		gl.glRotated(-90, 1, 0, 0);
        GLU glu = new GLU();
        GLUquadric quadric = glu.gluNewQuadric();
        
        //gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
		//gl.glColor3f(0, 0, 0);
		//gl.glBindTexture(GL2.GL_TEXTURE_2D, treeTexture.getTextureId());
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, new float[]{material.ambient.x, material.ambient.y, material.ambient.z}, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, new float[]{material.diffuse.x, material.diffuse.y, material.diffuse.z}, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, new float[]{material.specular.x, material.specular.y, material.specular.z}, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, new float[]{material.phong.x, material.phong.y, material.phong.z}, 0);
		//gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
		//gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
		//gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
		//gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);;
		glu.gluCylinder(quadric, currentBranchRadius, currentBranchRadius, branchLength, 7, 7);
		//gl.glBindTexture(GL2.GL_TEXTURE_2D, treeTexture.getTextureId());
		//gl.glDisable(GL2.GL_TEXTURE_2D);
		

		//glu.gluDeleteQuadric(quadric); //I think these need to be deleted after use????
		
		gl.glPopMatrix();
		//translate so we don't draw branches on top of each other
		gl.glTranslated(0, branchLength, 0);
	}

	@Override
	public void initialize(GL2 gl) {
		// TODO Auto-generated method stub
		this.treeTexture = new Texture(gl, "src/ass2/textures/treeTexture.bmp", true);
		
	}

	@Override
	public void dispose(GL2 gl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(GL2 gl) {
		// TODO Auto-generated method stub
		int branchDepth = 0;
		double angle = 15;
		//what am I doing
		
        gl.glMatrixMode(GL2.GL_MODELVIEW); // is this all I need to do?
        //gl.glPushMatrix();
        //gl.glLoadIdentity();

        // find local coordinates
             
//        gl.glTranslated(myTranslation[0], myTranslation[1], 0);
//        gl.glRotated(myRotation, 0, 0, 1);
//        gl.glScaled(myScale, myScale, 1);
		
		for (int i =0; i<generatedString.length(); i++){
			if (generatedString.charAt(i) == 'f'){
     		    //f : create branch
				//draw cylinder???????????
				drawBranch(gl ,branchDepth);
				
			} else if (generatedString.charAt(i) == '^') {
  			    //^ rotate up in the y-axis
				//around the y axis clockwise???????
				gl.glRotated(-angle, 0, 0, 1);
				
			} else if (generatedString.charAt(i) == 'v') {
				// v rotate down in the y-axis
				gl.glRotated(angle, 0, 0, 1);
				
			} else if (generatedString.charAt(i) == '<') {
				//twist left in the z-axis
				gl.glRotated(-angle, 0, 1, 0);
				
			} else if (generatedString.charAt(i) == '>') {
				//twist right in the z-axis
				gl.glRotated(angle, 0, 1, 0);
				
			} else if (generatedString.charAt(i) == '+') {
				//- rotate the branch left in x-axis
				gl.glRotated(angle, 1, 0, 0);
				
			} else if (generatedString.charAt(i) == '-') {
				//+ rotate the branch right in x-axis 
				gl.glRotated(-angle, 1, 0, 0);
				
			} else if (generatedString.charAt(i) == '[') {
				//push matrix
				gl.glPushMatrix();
				branchDepth++;
			} else if (generatedString.charAt(i) == ']') {
				//pop matrix
				gl.glPopMatrix();
				branchDepth--;
			} else {
				//System.out.println(generatedString); //printing this out to see if I have missed anything
			}
//			[ and ] : define a set of local area/branch. The definitions of the area are put inside the [ and ] 
//			+ and - : rotate the branch right/left in x-axis 
//			^ and v : rotate the branch up/down in y-axis 
//			<> : twist the branch left/right in z-axis
			//gl.glPopMatrix();
		}
		
		
	}
	
	
	
	
	
	
	
	

}
