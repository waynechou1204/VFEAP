package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.util.ArrayList;

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
import javax.vecmath.Vector3f;

import model.BarInfo;
import model.NodeInfo;
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

import control.VFIFEMousePickBehavior;

public class VFIFE_Modeling_view extends JPanel {

	private static final long serialVersionUID = 344029878504734442L;

	private SimpleUniverse universe = null;
	private TransformGroup objTrans = null;
	private TransformGroup objScale = null;
	private VFIFEMousePickBehavior behavior = null;
	private BranchGroup scene = null;
	
	private VFIFE_Model v5model = null;

	private float x1 = 0;
	private float y1 = 0;
	private float z1 = 0;
	private float x2 = 0;
	private float y2 = 0;
	private float z2 = 0;

	public VFIFE_Modeling_view() {

	}

	public VFIFE_Modeling_view(VFIFE_Model model) {

		this.v5model = model;

		this.setLayout(new BorderLayout());

		GraphicsConfiguration config = SimpleUniverse
				.getPreferredConfiguration();

		Canvas3D canvas = new Canvas3D(config);
		canvas.setSize(800, 600);
		this.add("Center", canvas);
		
		//nom
		universe = new SimpleUniverse(canvas);
		universe.getViewingPlatform().setNominalViewingTransform();
		scene = createSceneGraph(canvas);
		universe.addBranchGraph(scene);
		
		//设置鼠标拾取构造函数的参数bouds
		//BoundingSphere bouds=new BoundingSphere(new Point3d(0.0,0.0,0.0),100);
//		Background bg=new Background(new Color3f(Color.white));
//		bg.setApplicationBounds(bouds); 
//		scene.addChild(bg);
		
		
	}

	public void destroy() {
		universe.cleanup();
	}

	public BranchGroup createSceneGraph(Canvas3D canvas) {

		BranchGroup objRoot = new BranchGroup();

		objRoot.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		objRoot.setCapability(BranchGroup.ALLOW_DETACH);

		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
				1.0);

		// Create a Transformgroup to scale all objects so they appear in the
		// scene.
		objScale = new TransformGroup();
		Transform3D t3d = new Transform3D();
		t3d.setScale(0.1);
		objScale.setTransform(t3d);
		objRoot.addChild(objScale);

		// This Transformgroup is used by the mouse manipulators to move the
		// CYlinder.
		objTrans = new TransformGroup();
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
		
		//鼠标拾取
		behavior = new VFIFEMousePickBehavior(canvas,objRoot,bounds);
		this.behavior.setModel(v5model);
		behavior.setPanel(this);//用于弹出对话框

		// Shine it with colored lights.
		Color3f lColor2 = new Color3f(1.0f, 1.0f, 1.0f);
		Vector3f lDir2 = new Vector3f(0.0f, 0.0f, -1.0f);
		DirectionalLight lgt2 = new DirectionalLight(lColor2, lDir2);
		lgt2.setInfluencingBounds(bounds);
		objScale.addChild(lgt2);

		// draw elements here
		// this.drawTest();
		if (this.v5model != null) {
			this.drawNodes();
			this.drawBars();
			//this.drawTest();
			this.drawLoads();
		}

		objRoot.compile();

		return objRoot;
	}

	// draw node
	public void drawNodes(){
//		for (VFIFE_Node node : this.v5model.getNodes()) {
//			if (node.getRestraint() != null) {
//				float pointx = (float) (node.getCoord().getCoordinate_x());
//				float pointy = (float) (node.getCoord().getCoordinate_y());
//				float pointz = (float) (node.getCoord().getCoordinate_z());
//
//				// TODO USE DIFFERENT SHAPE TO ILLUSTRATE Restraints
//				drawConeSimple( pointx, pointy, pointz);
//				
//			}
//		}
		float color[] = { 0.0f, 0.0f, 1.0f, 0.5f, 0.0f, 1.0f, 
				 0.0f, 0.0f, 1.0f, 0.5f, 0.0f, 1.0f, };
		
	    
	    for(int point_num=0;point_num<v5model.getNodes().size();point_num=point_num+1){  
	    	PointArray point = new PointArray(20, PointArray.COORDINATES | PointArray.COLOR_4);
	    	float pvert[] = {(float)( v5model.getNodes().get(point_num).getCoord().getCoordinate_x()),
	    			(float) (v5model.getNodes().get(point_num).getCoord().getCoordinate_y()),
	    			(float) (v5model.getNodes().get(point_num).getCoord().getCoordinate_z())};
	    	
	    	point.setCoordinates(0, pvert);
	    	point.setColors(0, color);

			
			PointAttributes pa = new PointAttributes();
			pa.setPointSize(20.0f);
			pa.setPointAntialiasingEnable(true);
			
			Appearance ap = new Appearance();
			ap.setPointAttributes(pa);
			
			TransformGroup pointGroup = new TransformGroup();  
		    pointGroup.setTransform(new Transform3D());
		    pointGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);  
		    pointGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ); 
		    pointGroup.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
			
			
			Shape3D sh = new Shape3D();
			//sh.setName("barid:"+v5model.getBars().get(i).getBar_id());
			NodeInfo nodeinfo = new NodeInfo();
			nodeinfo.setNodeid(v5model.getNodes().get(point_num).getNode_id());
			nodeinfo.setNodename(v5model.getNodes().get(point_num).getNode_name());
			sh.setUserData(nodeinfo);
			sh.setGeometry(point);
			sh.setAppearance(ap);
			pointGroup.addChild(sh);
			
			objTrans.addChild(pointGroup);
	    }
	    
	}
	
	public void drawConeSimple(float x,float y,float z){	
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
		objTrans.addChild(shape1);
		
		Shape3D shape2 = new Shape3D();    
		shape2.setGeometry(tri1Line2);
		objTrans.addChild(shape2);
		
		Shape3D shape3 = new Shape3D();    
		shape3.setGeometry(tri1Line3);
		objTrans.addChild(shape3);
		
		Shape3D shape4 = new Shape3D();    
		shape4.setGeometry(tri2Line1);
		objTrans.addChild(shape4);
		
		Shape3D shape5 = new Shape3D();    
		shape5.setGeometry(tri2Line2);
		objTrans.addChild(shape5);
		
		Shape3D shape6 = new Shape3D();    
		shape6.setGeometry(tri2Line3);
		objTrans.addChild(shape6);
	}
	
	/*public void drawNodes() {
		for (VFIFE_Node node : this.v5model.getNodes()) {
			if (node.getRestraint() != null) {
				float scale = 12.5f;
				// addLoad(v5model);
				float pointx = (float) (node.getCoord().getCoordinate_x() / scale);
				float pointy = (float) (node.getCoord().getCoordinate_y() / scale);
				float pointz = (float) (node.getCoord().getCoordinate_z() / scale);

				// System.out.println("fgy"+pointx+"0.0"+pointy+"name"+pointz);
				int sCount[] = new int[1];
				int[] index = { 0, 1, 2 };
				int iCount = 4;
				int vCount = 3;
				if (x2 < 0.2 && y2 < 0.2 && z2 < 0.2) {
					float vert[] = { pointx, pointy, pointz, (pointx - 0.003f),
							(pointy - 0.003f), pointz, (pointx + 0.003f),
							(pointy - 0.003f), (pointz + 0.0f) };
					float color[] = { 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
							1.0f, 0.0f };
					sCount[0] = 4;
					IndexedLineStripArray line = new IndexedLineStripArray(
							vCount, IndexedLineStripArray.COORDINATES
									| IndexedLineStripArray.COLOR_3, iCount,
							sCount);
					line.setCoordinates(0, vert);
					line.setColors(0, color);
					line.setCoordinateIndices(0, index);
					line.setColorIndices(0, index);
					LineAttributes la = new LineAttributes();
					la.setLineWidth(2.0f);
					la.setLineAntialiasingEnable(true);
					la.setLinePattern(LineAttributes.PATTERN_SOLID);
					Appearance ap = new Appearance();
					ap.setLineAttributes(la);
					Shape3D sh = new Shape3D();
					sh.setGeometry(line);
					sh.setAppearance(ap);
					if (x2 < 0.2 && y2 < 0.2 && z2 < 0.2) {
						TransformGroup g6 = new TransformGroup();
						Transform3D t2 = new Transform3D();
						t2.setScale(5.0);
						g6.setTransform(t2);
						g6.addChild(sh);
						objTrans.addChild(g6);
					} else {
						TransformGroup g6 = new TransformGroup();
						Transform3D t2 = new Transform3D();
						t2.setScale(0.5);
						g6.setTransform(t2);
						g6.addChild(sh);
						objTrans.addChild(g6);
					}

				} else {
					float vert[] = { pointx, pointy, pointz, pointx - 0.03f,
							pointy - 0.03f, pointz, pointx + 0.03f,
							pointy - 0.03f, pointz + 0.0f };

					float color[] = { 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
							1.0f, 0.0f };
					sCount[0] = 4;
					IndexedLineStripArray line = new IndexedLineStripArray(
							vCount, IndexedLineStripArray.COORDINATES
									| IndexedLineStripArray.COLOR_3, iCount,
							sCount);
					line.setCoordinates(0, vert);
					line.setColors(0, color);
					line.setCoordinateIndices(0, index);
					line.setColorIndices(0, index);
					LineAttributes la = new LineAttributes();
					la.setLineWidth(2.0f);
					la.setLineAntialiasingEnable(true);
					la.setLinePattern(LineAttributes.PATTERN_SOLID);
					Appearance ap = new Appearance();
					ap.setLineAttributes(la);
					Shape3D sh = new Shape3D();
					sh.setGeometry(line);
					sh.setAppearance(ap);
					if (x2 < 0.2 && y2 < 0.2 && z2 < 0.2) {
						TransformGroup g6 = new TransformGroup();
						Transform3D t2 = new Transform3D();
						t2.setScale(5.0);
						g6.setTransform(t2);
						g6.addChild(sh);
						objTrans.addChild(g6);
					} else {
						TransformGroup g6 = new TransformGroup();
						Transform3D t2 = new Transform3D();
						t2.setScale(0.5);
						g6.setTransform(t2);
						g6.addChild(sh);
						objTrans.addChild(g6);
					}

				}
			}
		}

	}*/

	public void drawBars(){
		for(VFIFE_Bar bar : this.v5model.getBars()){
			ArrayList<Float> arr = new ArrayList<Float>();
			for (VFIFE_Node node : bar.getNodes()){
				arr.add((float) (node.getCoord().getCoordinate_x()));
				arr.add((float) (node.getCoord().getCoordinate_y()));
				arr.add((float) (node.getCoord().getCoordinate_z()));
			}
			
			// copy nodes coords into vertex[]
//			int size = arr.size();
//			float[] vertex = new float[size];
//			for (int x = 0; x < size; x++) {
//				vertex[x] = arr.get(x);
//			}
			float[] vert = new float[6];
			
			for (int x = 0; x < 6; x++) {
				//for(int y = 0;y < 6;y++)
				vert[x] = arr.get(x);
			}
			
			float color[] = { 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f };
			
//			LineArray line = new LineArray(6, LineArray.COORDINATES | LineArray.COLOR_3);
//			
//			line.setCoordinates(0, vertex);
//			line.setColors(0, color);
//			
//			//LineAttributes la = new LineAttributes();
//			//la.setLineWidth(1.0f);
//			//la.setLineAntialiasingEnable(true);
//			
//		    //Appearance ap = new Appearance();
//			//ap.setLineAttributes(la);
//			
//			Shape3D sh = new Shape3D();
//			sh.setGeometry(line);
//			//sh.setAppearance(ap);
//			objTrans.addChild(sh);
			
		    
		   
		    
			TransformGroup lineGroup = new TransformGroup();  
		    lineGroup.setTransform(new Transform3D());
		    lineGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);  
		    lineGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ); 
		    lineGroup.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
		    
		    
		    LineArray line = new LineArray(20, LineArray.COORDINATES | LineArray.COLOR_3);
			line.setCoordinates(0, vert);
			line.setColors(0, color);

				
			LineAttributes la = new LineAttributes();
			la.setLineWidth(2.0f);
			la.setLineAntialiasingEnable(true);
				
			Appearance ap = new Appearance();
			ap.setLineAttributes(la);
				
			Shape3D sh = new Shape3D();
			BarInfo barinfo = new BarInfo();
			barinfo.setBarid(bar.getBar_id());
			sh.setUserData(barinfo);
			sh.setGeometry(line);
			sh.setAppearance(ap);
			lineGroup.addChild(sh);
		    
			
			
			objTrans.addChild(lineGroup);
			
			//scene.detach();
			
			//trans.addChild(pointGroup);
			
			//scene.compile();
			//universe.addBranchGraph(scene);
			
		}
		
	}
	
	/*public void drawBars() {
		float[] vert;// should put data in this array
		ArrayList<Float> al = new ArrayList<Float>();
		for (VFIFE_Bar bar : v5model.getBars()) {
			ArrayList<VFIFE_Node> arrNodes = bar.getNodes();
			int flag1 = 0;
			for (VFIFE_Node node : arrNodes) {
				float scale = 12.5f;

				al.add((float) (node.getCoord().getCoordinate_x() / scale));
				al.add((float) (node.getCoord().getCoordinate_y() / scale));
				al.add((float) (node.getCoord().getCoordinate_z() / scale));

				flag1++;// �˴���flag��Ϊ��ȡ�����еĵڶ�����ݣ�
				if (flag1 == 1) {
					x1 = (float) (node.getCoord().getCoordinate_x() / scale);
					y1 = (float) (node.getCoord().getCoordinate_y() / scale);
					z1 = (float) (node.getCoord().getCoordinate_z() / scale);
				}
				x2 = Math
						.abs((float) (node.getCoord().getCoordinate_x() / scale));
				y2 = Math
						.abs((float) (node.getCoord().getCoordinate_y() / scale));
				z2 = Math
						.abs((float) (node.getCoord().getCoordinate_z() / scale));

			}

		}

		vert = new float[al.size()];
		for (int x = 0; x < al.size(); x++) {
			vert[x] = al.get(x);
		}
		// for(float f:vert){System.out.println(f);}
		// Color3f red = new Color3f(1.0f, 0.0f, 0.0f);
		// for(float f:vert){System.out.println(f);}
		float color[] = { 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
				0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
				1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
				0.0f, };

		// Color3f white = new Color3f(1.0f, 1.0f,1.0f);
		LineArray line = new LineArray(50, LineArray.COORDINATES
				| LineArray.COLOR_3);
		line.setCoordinates(0, vert);
		line.setColors(0, color);
		LineAttributes la = new LineAttributes();
		la.setLineWidth(2.0f);
		la.setLineAntialiasingEnable(true);
		Appearance ap = new Appearance();
		ap.setLineAttributes(la);
		Shape3D sh = new Shape3D();
		sh.setGeometry(line);
		sh.setAppearance(ap);

		if (x2 < 0.2 && y2 < 0.2 && z2 < 0.2) { // judge the data,then decide to
												// choose how to make a scale.
			TransformGroup g6 = new TransformGroup();
			Transform3D t2 = new Transform3D();
			t2.setScale(5.0);
			g6.setTransform(t2);
			g6.addChild(sh);
			objTrans.addChild(g6);
		} else if (x2 < 1 && y2 < 1 && z2 < 1) {
			TransformGroup g6 = new TransformGroup();
			Transform3D t2 = new Transform3D();
			t2.setScale(0.5);
			g6.setTransform(t2);
			g6.addChild(sh);
			objTrans.addChild(g6);
		} else {
			TransformGroup g6 = new TransformGroup();
			Transform3D t2 = new Transform3D();
			t2.setScale(0.05);
			g6.setTransform(t2);
			g6.addChild(sh);
			objTrans.addChild(g6);
		}

	}*/

	/*public void drawLoads() {
		// int flag=0;
		float[] vert;// should put data in this array
		ArrayList<Float> al = new ArrayList<Float>();
		float scale = 1.0f;

		for (VFIFE_Load force : v5model.getForces()) {
			if (force.getClass().toString().contains("VFIFE_LoadNode")) {
				VFIFE_LoadNode v5nodeforce = (VFIFE_LoadNode) force;
				VFIFE_AppliedLoadStaticForce staticforce = (VFIFE_AppliedLoadStaticForce) v5nodeforce.getLoad_values();
				double fx0 = staticforce.getApplied_force_fx();
				// double fy0=staticforce.getApplied_force_fy();
				double fz0 = staticforce.getApplied_force_fz();

				if (fx0 == 0 && fz0 != 0) {
					if (fz0 < 0) {
						al.add((float) v5nodeforce.getSupporting_node()
								.getCoord().getCoordinate_x()
								/ scale);
						float ax = (float) v5nodeforce.getSupporting_node()
								.getCoord().getCoordinate_x()
								/ scale;

						// al.add((float)v5nodeforce.getSupporting_node().getCoord().getCoordinate_z()/scale);
						// float
						// cz=(float)v5nodeforce.getSupporting_node().getCoord().getCoordinate_z()/scale;

						al.add((float) v5nodeforce.getSupporting_node()
								.getCoord().getCoordinate_y()
								/ scale);
						float by = (float) v5nodeforce.getSupporting_node()
								.getCoord().getCoordinate_y()
								/ scale;

						// al.add((float)v5nodeforce.getSupporting_node().getCoord().getCoordinate_y()/scale);
						// float
						// by=(float)v5nodeforce.getSupporting_node().getCoord().getCoordinate_y()/scale;

						al.add((float) v5nodeforce.getSupporting_node()
								.getCoord().getCoordinate_z()
								/ scale);
						float cz = (float) v5nodeforce.getSupporting_node()
								.getCoord().getCoordinate_z()
								/ scale;
						al.add(ax);
						al.add(10 / scale + cz);
						al.add(by);
						al.add(ax);
						al.add(cz);
						al.add(by);
						al.add(ax - 0.2f);
						al.add(cz + 0.2f);
						al.add(by);
						al.add(ax);
						al.add(cz);
						al.add(by);
						al.add(ax + 0.2f);
						al.add(cz + 0.2f);
						al.add(by);

					} else if (fz0 > 0) {
					}// now it does't need ,u can use it when you need
				}
				continue;
			}
		
			if (force.getClass().toString()
					.contains("VFIFE_LoadMemberConcentrated")) {
				// flag=2;
				// System.out.println("shaoen");
				VFIFE_LoadMemberConcentrated v5nodeforce = (VFIFE_LoadMemberConcentrated) force;
				VFIFE_Bar fBar = v5nodeforce.getSupporting_member(); // get the
																		// bar
																		// related
																		// with
																		// the
																		// force
				VFIFE_AppliedLoadStaticForce staticforce = (VFIFE_AppliedLoadStaticForce) v5nodeforce
						.getLoad_value();

				// get the nodes of the bar
				ArrayList<VFIFE_Node> bnodes = fBar.getNodes();

				// get the CartesianPoint of the nodes
				VFIFE_CartesianPoint fpoint1 = bnodes.get(0).getCoord();
				float fx1 = (float) fpoint1.getCoordinate_x() / scale;
				float fy1 = (float) fpoint1.getCoordinate_y() / scale;
				float fz1 = (float) fpoint1.getCoordinate_z() / scale;

				// get the CartesianPoint of the nodes
				VFIFE_CartesianPoint fpoint2 = bnodes.get(1).getCoord();
				float fx2 = (float) fpoint2.getCoordinate_x() / scale;
				float fy2 = (float) fpoint2.getCoordinate_y() / scale;
				float fz2 = (float) fpoint2.getCoordinate_z() / scale;

				// direction of the bar ---- node2 decrease the node1
				float bDirectX = fx2 - fx1;
				float bDirectY = fy2 - fy1;
				float bDirectZ = fz2 - fz1;

				// the force position according to the bar
				float length = (float) v5nodeforce.getLoad_position()
						.getCoordinate_x() / scale;
				// float
				// by=(float)v5nodeforce.getLoad_position().getCoordinate_y()/scale;
				// float
				// cz=(float)v5nodeforce.getLoad_position().getCoordinate_z()/scale;

				float ax = fx1
						+ length
						* (bDirectX / (float) Math.sqrt(bDirectX * bDirectX
								+ bDirectY * bDirectY + bDirectZ * bDirectZ));
				float by = fy1
						+ length
						* (bDirectY / (float) Math.sqrt(bDirectX * bDirectX
								+ bDirectY * bDirectY + bDirectZ * bDirectZ));
				float cz = fz1
						+ length
						* (bDirectZ / (float) Math.sqrt(bDirectX * bDirectX
								+ bDirectY * bDirectY + bDirectZ * bDirectZ));

				al.add(ax);
				al.add(by);
				al.add(cz);

				float fx = (float) staticforce.getApplied_force_fx();
				float fy = (float) staticforce.getApplied_force_fy();
				float fz = (float) staticforce.getApplied_force_fz();

				float ex = ax
						- ((1 / scale) * (fx / (float) Math.sqrt(fx * fx + fy
								* fy + fz * fz)));
				float ey = by
						- ((1 / scale) * (fy / (float) Math.sqrt(fx * fx + fy
								* fy + fz * fz)));
				float ez = cz
						- ((1 / scale) * (fz / (float) Math.sqrt(fx * fx + fy
								* fy + fz * fz)));

				al.add(ex);
				al.add(ey);
				al.add(ez);
			}
		}
		
		vert = new float[al.size()];
		for (int x = 0; x < al.size(); x++) {
			vert[x] = al.get(x);
		}
		float color[] = { 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
				0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
				1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
				0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, };
		Shape3D sh2 = new Shape3D();
		LineArray line = new LineArray(50, LineArray.COORDINATES
				| LineArray.COLOR_3);
		line.setCoordinates(0, vert);
		line.setColors(0, color);
		Appearance ap = new Appearance();
		LineAttributes la = new LineAttributes();
		la.setLineWidth(2.0f);
		la.setLineAntialiasingEnable(true);
		Appearance ap1 = new Appearance();
		ap1.setLineAttributes(la);
		sh2.setGeometry(line);
		sh2.setAppearance(ap);
		// return objRoot;
		if (x2 < 1 && y2 < 1 && z2 < 1) {
			TransformGroup g6 = new TransformGroup();
			Transform3D t2 = new Transform3D();
			t2.setScale(0.5);
			g6.setTransform(t2);
			g6.addChild(sh2);
			objTrans.addChild(g6);
		} else {
			TransformGroup g6 = new TransformGroup();
			Transform3D t2 = new Transform3D();
			t2.setScale(0.05);
			// System.out.println("dayin wo le ma fgy");
			g6.setTransform(t2);
			g6.addChild(sh2);
			objTrans.addChild(g6);
		}

	}*/
	
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
	
	private void drawArrow(double px,double py, double pz, double fx, double fy, double fz){
		
		// draw main line of the force arrow
		LineArray mainLine =new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
		mainLine.setCoordinate(0,new Point3f((float)px, (float)py ,(float)pz));
		// get force volum
		double f = getLength(fx, fy, fz, 0, 0, 0);
		
		mainLine.setCoordinate(1,new Point3f((float)(px-fx*2/f), (float)(py-fy*2/f) ,(float)(pz-fz*2/f)));        
		mainLine.setColor(0, new Color3f(0.0f, 0.0f, 1.0f));    
		mainLine.setColor(1, new Color3f(0.0f, 0.0f, 1.0f));    
		
		Shape3D shape1 = new Shape3D();    
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
