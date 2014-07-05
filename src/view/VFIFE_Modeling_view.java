package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.util.*;

import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.LineArray;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.Material;
import javax.media.j3d.PointArray;
import javax.media.j3d.PointAttributes;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import model.VFIFE_AppliedLoadStaticForce;
import model.VFIFE_Bar;
import model.VFIFE_CartesianPoint;
import model.VFIFE_Load;
import model.VFIFE_LoadMemberConcentrated;
import model.VFIFE_LoadNode;
import model.VFIFE_Model;
import model.VFIFE_Node;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.Viewer;
import com.sun.j3d.utils.universe.ViewingPlatform;

import control.VFIFEMouseOverBehavior;
import control.VFIFEMousePickBehavior;

public class VFIFE_Modeling_view extends JPanel {

	private static final long serialVersionUID = 344029878504734442L;

	private SimpleUniverse universe = null;
	private BranchGroup scene = null;
	private TransformGroup objTrans = null;
	
	private VFIFEMousePickBehavior mousePickBehavior = null;
	private VFIFEMouseOverBehavior mouseOverBehavior = null;
	
	private VFIFE_Model v5model = null;
	
	public VFIFE_Modeling_view() {	}

	public VFIFE_Modeling_view(VFIFE_Model model) {
		this.v5model = model;
		
		this.setLayout(new BorderLayout());

		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();

		Canvas3D canvas = new Canvas3D(config);
		canvas.setSize(1024, 768);
		canvas.setDoubleBufferEnable(true);
		this.add("Center", canvas);
		
		// set universe with canvas
		universe = new SimpleUniverse(canvas);
		universe.getViewingPlatform().setNominalViewingTransform();
		
		// set viewer anti aliasing
		universe.getViewer().getView().setSceneAntialiasingEnable(true);
		
		scene = createSceneGraph(canvas);
		universe.addBranchGraph(scene);
		
	}

	public void destroy() {
		universe.cleanup();
	}

	public BranchGroup createSceneGraph(Canvas3D canvas) {
		
		// scale array for the max distance among nodes
		ArrayList<Float> arr_scale = new ArrayList<Float>();
		for (VFIFE_Node node : this.v5model.getNodes()) {
			float nodex=(float)(Math.abs(node.getCoord().getCoordinate_x()));
			float nodey=(float)(Math.abs(node.getCoord().getCoordinate_y()));
			float nodez=(float)(Math.abs(node.getCoord().getCoordinate_z()));
			arr_scale.add(nodex);
			arr_scale.add(nodey);
			arr_scale.add(nodez);
		}
		float scale=(float)(1/Collections.max(arr_scale));
		
		BranchGroup objRoot = new BranchGroup();
		objRoot.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		objRoot.setCapability(BranchGroup.ALLOW_DETACH);

		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
				100.0);

		// Create a TG to scale all objects appear in the scene.
		Transform3D t3d = new Transform3D();
		t3d.setScale(scale);
		TransformGroup objScale = new TransformGroup(t3d);
		objRoot.addChild(objScale);

		// Rotate the object to XZ orientation
		Transform3D rotate3d = new Transform3D();
		rotate3d.rotX(-Math.PI/2);
		rotate3d.setTranslation( new Vector3f ( -5.0f, 0.0f, 0.0f ) );
		
		// This TG is used by the mouse manipulators to move the object
		objTrans = new TransformGroup(rotate3d);
		objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		objTrans.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);

		objScale.addChild(objTrans);

		
		// Create the rotate behavior node
		MouseRotate mouseRotate = new MouseRotate(objTrans);
		mouseRotate.setSchedulingBounds(bounds);
		objRoot.addChild(mouseRotate);

		// Create the translate behavior
		MouseTranslate mouseTranslate = new MouseTranslate(objTrans);
		mouseTranslate.setSchedulingBounds(bounds);
		objRoot.addChild(mouseTranslate);

		// Create the zoom behavior node
		MouseZoom mouseZoom = new MouseZoom(objTrans);
		mouseZoom.setSchedulingBounds(bounds);
		objRoot.addChild(mouseZoom);
		
		// Mouse pick
		mousePickBehavior = new VFIFEMousePickBehavior(canvas,objRoot,bounds);
		this.mousePickBehavior.setModel(v5model);
		mousePickBehavior.setPanel(this);//be used to pop up dialogue
		
		mouseOverBehavior = new VFIFEMouseOverBehavior(canvas,objRoot);
		mouseOverBehavior.setSchedulingBounds(bounds);
		objRoot.addChild(mouseOverBehavior);
		

		// Shine it with colored lights.
		Color3f lColor2 = new Color3f(1.0f, 1.0f, 1.0f);
		Vector3f lDir2 = new Vector3f(0.0f, 0.0f, -1.0f);
		DirectionalLight lgt2 = new DirectionalLight(lColor2, lDir2);
		lgt2.setInfluencingBounds(bounds);
		objScale.addChild(lgt2);

		// draw elements here
		if (this.v5model != null) {
			this.drawNodes();
			this.drawBars();
			this.drawLoads();
		}

		objRoot.compile();

		return objRoot;
	}

	// draw node
	public void drawNodes(){
		for (VFIFE_Node node : this.v5model.getNodes()) {
			float pointx = (float) (node.getCoord().getCoordinate_x());
			float pointy = (float) (node.getCoord().getCoordinate_y());
			float pointz = (float) (node.getCoord().getCoordinate_z());
		
			float color[] = { 0.0f, 0.0f, 1.0f, 0.5f, 0.0f, 1.0f, 
					 0.0f, 0.0f, 1.0f, 0.5f, 0.0f, 1.0f, };
			
		    float pvert[] = {pointx, pointy, pointz};
		    PointArray point = new PointArray(20, PointArray.COORDINATES | PointArray.COLOR_4);
	    	point.setCoordinates(0, pvert);
	    	point.setColors(0, color);

			PointAttributes pa = new PointAttributes();
			pa.setPointSize(10.0f);
			pa.setPointAntialiasingEnable(true);
			
			Appearance ap = new Appearance();
			ap.setPointAttributes(pa);
			
			TransformGroup pointGroup = new TransformGroup();  
		    pointGroup.setTransform(new Transform3D());
		    pointGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);  
		    pointGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ); 
		    pointGroup.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
			
			
			Shape3D sh = new Shape3D();
			sh.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
			sh.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
			sh.setUserData(node);
			sh.setGeometry(point);
			sh.setAppearance(ap);
			pointGroup.addChild(sh);
			
			objTrans.addChild(pointGroup);
			
			
			if (node.getRestraint() != null) {
				// TODO USE DIFFERENT SHAPE TO ILLUSTRATE Restraints
				drawConeSimple( pointx, pointy, pointz);
			
			}
			
		}
	}
	
	public void drawBars(){
		
		for(VFIFE_Bar bar : this.v5model.getBars()){
			ArrayList<Float> arr = new ArrayList<Float>();
			for (VFIFE_Node node : bar.getNodes()){
				arr.add((float) (node.getCoord().getCoordinate_x()));
				arr.add((float) (node.getCoord().getCoordinate_y()));
				arr.add((float) (node.getCoord().getCoordinate_z()));
			}
			
			// copy nodes coords into vertex[]
			int size = arr.size();
			float[] vertex = new float[size];
			for (int x = 0; x < size; x++) {
				vertex[x] = arr.get(x);
			}
			
			float color[] = { 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f };
			
			TransformGroup lineGroup = new TransformGroup();  
		    lineGroup.setTransform(new Transform3D());
		    lineGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);  
		    lineGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ); 
		    lineGroup.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
		    
		    
		    LineArray line = new LineArray(20, LineArray.COORDINATES | LineArray.COLOR_3);
			line.setCoordinates(0, vertex);
			line.setColors(0, color);

			LineAttributes la = new LineAttributes();
			la.setLineWidth(1.0f);
			la.setLineAntialiasingEnable(true);
				
			Appearance ap = new Appearance();
			ap.setLineAttributes(la);
				
			Shape3D sh = new Shape3D();
			sh.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
			sh.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
			sh.setUserData(bar);
			sh.setGeometry(line);
			sh.setAppearance(ap);
			
			lineGroup.addChild(sh);

			objTrans.addChild(lineGroup);
			//System.out.println("max is"+Collections.max(arr));
		}
		
	}
	
	public void drawLoads() {
		for (VFIFE_Load force : v5model.getForces()) {
			if (force.getClass().toString().contains("VFIFE_LoadNode")) {
				
				VFIFE_LoadNode v5nodeforce = (VFIFE_LoadNode) force;
				
				// load postion
				VFIFE_Node supportNode = v5nodeforce.getSupporting_node();
				VFIFE_CartesianPoint point = supportNode.getCoord();
				double px = point.getCoordinate_x();
				double py = point.getCoordinate_y();
				double pz = point.getCoordinate_z();
				
				// force vector
				VFIFE_AppliedLoadStaticForce staticforce = (VFIFE_AppliedLoadStaticForce) v5nodeforce.getLoad_values();
				double fx = staticforce.getApplied_force_fx();
				double fy = staticforce.getApplied_force_fy();
				double fz = staticforce.getApplied_force_fz();
			
				// draw Load
				drawArrow(px, py, pz, fx, fy, fz);
				
				continue;
			}
			
			if (force.getClass().toString().contains("VFIFE_LoadMemberConcentrated")) {
				
				VFIFE_LoadMemberConcentrated v5force = (VFIFE_LoadMemberConcentrated) force;
				
				// load postion - distance from start end
				VFIFE_CartesianPoint point = v5force.getLoad_position();
				double distance = point.getCoordinate_x();
				
				VFIFE_Bar supportBar = v5force.getSupporting_member();
				VFIFE_CartesianPoint startPos = supportBar.getNodes().get(0).getCoord();
				VFIFE_CartesianPoint endPos = supportBar.getNodes().get(1).getCoord();
				
				double barLength =  this.getLength(startPos.getCoordinate_x(), startPos.getCoordinate_y(),
						startPos.getCoordinate_z(), endPos.getCoordinate_x(), 
						endPos.getCoordinate_y(), endPos.getCoordinate_z());
				
				double radio = distance/barLength;
				
				// position on bar
				double px = (startPos.getCoordinate_x() + endPos.getCoordinate_x())*radio;
				double py = (startPos.getCoordinate_y() + endPos.getCoordinate_y())*radio;
				double pz = (startPos.getCoordinate_z() + endPos.getCoordinate_z())*radio;
				
				// force vector
				VFIFE_AppliedLoadStaticForce staticforce = (VFIFE_AppliedLoadStaticForce) v5force.getLoad_value();
				double fx = staticforce.getApplied_force_fx();
				double fy = staticforce.getApplied_force_fy();
				double fz = staticforce.getApplied_force_fz();
			
				// draw Load
				drawArrow(px, py, pz, fx, fy, fz);
				
				continue;
			}
		}
	}

	private double getLength(double p1x,double p1y, double p1z, double p2x, double p2y, double p2z){
		double len = 0;
		len += Math.pow(p1x-p2x, 2);
		len += Math.pow(p1y-p2y, 2);
		len += Math.pow(p1z-p2z, 2);
		len = Math.sqrt(len);
		return len;
	}
	
	private void drawConeSimple(float x,float y,float z){	
		
		TransformGroup coneGroup = new TransformGroup();
		coneGroup.setTransform(new Transform3D());
		
		LineArray tri1Line1=new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
		tri1Line1.setCoordinate(0,new Point3f(x, y ,z));
		tri1Line1.setCoordinate(1,new Point3f(x, y-0.289f ,z-0.5f));        
		tri1Line1.setColor(0, new Color3f(0.0f, 1.0f, 0.0f));    
		tri1Line1.setColor(1, new Color3f(0.0f, 1.0f, 0.0f));    
		
		LineArray tri1Line2=new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
		tri1Line2.setCoordinate(0,new Point3f(x, y ,z));
		tri1Line2.setCoordinate(1,new Point3f(x, y+0.289f ,z-0.5f));        
		tri1Line2.setColor(0, new Color3f(0.0f, 1.0f, 0.0f));    
		tri1Line2.setColor(1, new Color3f(0.0f, 1.0f, 0.0f));    
		
		LineArray tri1Line3=new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
		tri1Line3.setCoordinate(0,new Point3f(x, y+0.289f ,z-0.5f));
		tri1Line3.setCoordinate(1,new Point3f(x, y-0.289f ,z-0.5f));        
		tri1Line3.setColor(0, new Color3f(0.0f, 1.0f, 0.0f));    
		tri1Line3.setColor(1, new Color3f(0.0f, 1.0f, 0.0f));    
		
		LineArray tri2Line1=new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
		tri2Line1.setCoordinate(0,new Point3f(x, y ,z));
		tri2Line1.setCoordinate(1,new Point3f(x-0.289f, y ,z-0.5f));        
		tri2Line1.setColor(0, new Color3f(0.0f, 1.0f, 0.0f));    
		tri2Line1.setColor(1, new Color3f(0.0f, 1.0f, 0.0f));    
		
		LineArray tri2Line2=new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
		tri2Line2.setCoordinate(0,new Point3f(x, y ,z));
		tri2Line2.setCoordinate(1,new Point3f(x+0.289f, y ,z-0.5f));        
		tri2Line2.setColor(0, new Color3f(0.0f, 1.0f, 0.0f));    
		tri2Line2.setColor(1, new Color3f(0.0f, 1.0f, 0.0f));    
		
		LineArray tri2Line3=new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
		tri2Line3.setCoordinate(0,new Point3f(x+0.289f, y ,z-0.5f));
		tri2Line3.setCoordinate(1,new Point3f(x-0.289f, y ,z-0.5f));        
		tri2Line3.setColor(0, new Color3f(0.0f, 1.0f, 0.0f));    
		tri2Line3.setColor(1, new Color3f(0.0f, 1.0f, 0.0f));    
		
		Shape3D shape1 = new Shape3D();    
		shape1.setGeometry(tri1Line1);
		coneGroup.addChild(shape1);
		
		Shape3D shape2 = new Shape3D();    
		shape2.setGeometry(tri1Line2);
		coneGroup.addChild(shape2);
		
		Shape3D shape3 = new Shape3D();    
		shape3.setGeometry(tri1Line3);
		coneGroup.addChild(shape3);
		
		Shape3D shape4 = new Shape3D();    
		shape4.setGeometry(tri2Line1);
		coneGroup.addChild(shape4);
		
		Shape3D shape5 = new Shape3D();    
		shape5.setGeometry(tri2Line2);
		coneGroup.addChild(shape5);
		
		Shape3D shape6 = new Shape3D();    
		shape6.setGeometry(tri2Line3);
		coneGroup.addChild(shape6);
		
		objTrans.addChild(coneGroup);
	}
	
	//TODO Arrow is not finished yet
	private void drawArrow(double px,double py, double pz, double fx, double fy, double fz){
		
		// draw main line of the force arrow
		LineArray mainLine =new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
		mainLine.setCoordinate(0,new Point3f((float)px, (float)py ,(float)pz));
		
		// get force volume
		double f = getLength(fx, fy, fz, 0, 0, 0);
		
		mainLine.setCoordinate(1,new Point3f((float)(px-fx*2/f), (float)(py-fy*2/f) ,(float)(pz-fz*2/f)));        
		mainLine.setColor(0, new Color3f(0.0f, 0.0f, 1.0f));    
		mainLine.setColor(1, new Color3f(0.0f, 0.0f, 1.0f));    
		
		Shape3D shape1 = new Shape3D(); 
		shape1.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
		shape1.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
		shape1.setGeometry(mainLine);
		objTrans.addChild(shape1);
		
		// draw other for
		
	}
	
	public void drawTest() {
		// Create a cylinder
		PolygonAttributes attr = new PolygonAttributes();
		attr.setCullFace(PolygonAttributes.CULL_NONE);
		Appearance ap = new Appearance();
	    Material mat = new Material();
	    mat.setEmissiveColor(new Color3f(1.0f,0.0f,0.0f));
	    mat.setLightingEnable(true);
		ap.setMaterial(mat);
		ap.setPolygonAttributes(attr);

		Cylinder CylinderObj = new Cylinder(1.0f, 2.0f, ap);
		objTrans.addChild(CylinderObj);

	}

}
