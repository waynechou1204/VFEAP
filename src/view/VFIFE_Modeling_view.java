package view;

import java.awt.GraphicsConfiguration;
import java.util.*;

import javax.media.j3d.Appearance;
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
import javax.vecmath.AxisAngle4d;
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
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.SimpleUniverse;

import control.VFIFEMouseOverBehavior;
import control.VFIFEMousePickBehavior;
import javax.media.j3d.ColoringAttributes;

public class VFIFE_Modeling_view extends JPanel {

    private static final long serialVersionUID = 344029878504734442L;

    private SimpleUniverse universe = null;
    private BranchGroup scene = null;
    private TransformGroup objTrans = null;
//<<<<<<< HEAD
    private Transform3D ta = new Transform3D();
    private Transform3D arrow = new Transform3D();
//=======
    
//private Transform3D ta = new Transform3D();
//>>>>>>> 948f8b200f6f6a0aedba6a2f0c8d082ef80da03d
    private VFIFEMousePickBehavior mousePickBehavior = null;
    private VFIFEMouseOverBehavior mouseOverBehavior = null;

    private float m_scale=1;
    
    private VFIFE_Model v5model = null;

    public VFIFE_Modeling_view(VFIFE_Model model) {

        this.v5model = model;

        //this.setLayout(new BorderLayout());
        GraphicsConfiguration config = SimpleUniverse
                .getPreferredConfiguration();

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

        float offset_x = 0;
        float offset_y = 0;
        float offset_z = 0;

        if (!v5model.isEmpty()) {
            // scale array for the max distance among nodes
            ArrayList<Float> arr_x = new ArrayList<Float>();
            ArrayList<Float> arr_y = new ArrayList<Float>();
            ArrayList<Float> arr_z = new ArrayList<Float>();

            for (VFIFE_Node node : this.v5model.getNodes()) {
                arr_x.add((float) (node.getCoord().getCoordinate_x()));
                arr_y.add((float) (node.getCoord().getCoordinate_y()));
                arr_z.add((float) (node.getCoord().getCoordinate_z()));
            }

            float span_x = getSpanValueF(arr_x);
            float span_y = getSpanValueF(arr_y);
            float span_z = getSpanValueF(arr_z);

            offset_x = getMidValueF(arr_x);
            offset_y = getMidValueF(arr_y);
            offset_z = getMidValueF(arr_z);

            m_scale = (1 / getMaxOfThreeF(span_x, span_y, span_z));
        }

        BranchGroup objRoot = new BranchGroup();
        objRoot.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        objRoot.setCapability(BranchGroup.ALLOW_DETACH);

        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
                100.0);

        // Create a TG to scale all objects appear in the scene.
        Transform3D t3d = new Transform3D();
        t3d.setScale(m_scale);
        TransformGroup objScale = new TransformGroup(t3d);
        objRoot.addChild(objScale);

        // NO NEED TO Rotate the object to XY orientation
        Transform3D rotate3d = new Transform3D();
         
        // set object to the center of window by offsets
        rotate3d.setTranslation(new Vector3f(-offset_x, -offset_y, -offset_z));

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
        mousePickBehavior = new VFIFEMousePickBehavior(canvas, objRoot, bounds);
        this.mousePickBehavior.setModel(v5model);
        mousePickBehavior.setPanel(this);// be used to pop up dialogue

        mouseOverBehavior = new VFIFEMouseOverBehavior(canvas, objRoot);
        mouseOverBehavior.setSchedulingBounds(bounds);
        objRoot.addChild(mouseOverBehavior);

        // Shine it with colored lights.
        Color3f lColor2 = new Color3f(1.0f, 1.0f, 1.0f);
        Vector3f lDir2 = new Vector3f(0.0f, 0.0f, -1.0f);
        DirectionalLight lgt2 = new DirectionalLight(lColor2, lDir2);
        lgt2.setInfluencingBounds(bounds);
        objScale.addChild(lgt2);

        if (!v5model.isEmpty()) {
            drawNodes();
            drawBars();
            drawLoads();

            objRoot.compile();
        }

        return objRoot;
    }

    // draw node
    public void drawNodes() {
        for (VFIFE_Node node : this.v5model.getNodes()) {
            float pointx = (float) (node.getCoord().getCoordinate_x());
            float pointy = (float) (node.getCoord().getCoordinate_y());
            float pointz = (float) (node.getCoord().getCoordinate_z());

            float color[] = {0.0f, 0.0f, 1.0f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f,
                1.0f, 0.5f, 0.0f, 1.0f,};

            float pvert[] = {pointx, pointy, pointz};
            PointArray point = new PointArray(20, PointArray.COORDINATES
                    | PointArray.COLOR_4);
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
                boolean xdis = node.getRestraint().getBc_x_displacement_free();
                boolean ydis = node.getRestraint().getBc_y_displacement_free();
                boolean zdis = node.getRestraint().getBc_z_displacement_free();
                boolean xrot = node.getRestraint().getBc_x_rotation_free();
                boolean yrot = node.getRestraint().getBc_y_rotation_free();
                boolean zrot = node.getRestraint().getBc_z_rotation_free();
                
                // ball joint /_\
                if (!xdis && !ydis && !zdis && xrot && yrot && zrot) {
                    drawBalljointCone(pointx, pointy, pointz);
                }
                // fixed |__|
                else if (!xdis && !ydis && !zdis && !xrot && !yrot && !zrot) {
                    drawFixedjointCross(pointx, pointy, pointz);
                }
                // fixed z (|)
                else if (xdis && ydis && !zdis && xrot && yrot && zrot) {
                    drawFixZDisjointSphere(pointx, pointy, pointz);
                }
                // free
                else if (xdis && ydis && zdis && xrot && yrot && zrot) { }
                // other boundary conditions : draw X
                else {
                    drawOtherjoints(pointx, pointy, pointz);
                }

            }

        }
    }

    public void drawBars() {

        for (VFIFE_Bar bar : this.v5model.getBars()) {
            ArrayList<Float> arr = new ArrayList<Float>();
            for (VFIFE_Node node : bar.getNodes()) {
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

            float color[] = {1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f};

            TransformGroup lineGroup = new TransformGroup();
            lineGroup.setTransform(new Transform3D());
            lineGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            lineGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
            lineGroup.setCapability(TransformGroup.ENABLE_PICK_REPORTING);

            LineArray line = new LineArray(20, LineArray.COORDINATES
                    | LineArray.COLOR_3);
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
                VFIFE_AppliedLoadStaticForce staticforce = (VFIFE_AppliedLoadStaticForce) v5nodeforce
                        .getLoad_values();
                double fx = staticforce.getApplied_force_fx();
                double fy = staticforce.getApplied_force_fy();
                double fz = staticforce.getApplied_force_fz();

                // draw Load
                drawArrow(px, py, pz, fx, fy, fz,force);
               // drawArrow(5.0, 0.0, 2.5, 0.0,-40.0,0.0,force);
                continue;
            }

            if (force.getClass().toString().contains("VFIFE_LoadMemberConcentrated")) {

                VFIFE_LoadMemberConcentrated v5force = (VFIFE_LoadMemberConcentrated) force;

                // load postion - distance from start end
                VFIFE_CartesianPoint point = v5force.getLoad_position();
                double distance = point.getCoordinate_x();

                VFIFE_Bar supportBar = v5force.getSupporting_member();
                VFIFE_CartesianPoint startPos = supportBar.getNodes().get(0)
                        .getCoord();
                VFIFE_CartesianPoint endPos = supportBar.getNodes().get(1)
                        .getCoord();

                double barLength = this.getLength(startPos.getCoordinate_x(),
                        startPos.getCoordinate_y(), startPos.getCoordinate_z(),
                        endPos.getCoordinate_x(), endPos.getCoordinate_y(),
                        endPos.getCoordinate_z());

                double radio = distance / barLength;

                // position on bar
                double px = (startPos.getCoordinate_x() + endPos
                        .getCoordinate_x()) * radio;
                double py = (startPos.getCoordinate_y() + endPos
                        .getCoordinate_y()) * radio;
                double pz = (startPos.getCoordinate_z() + endPos
                        .getCoordinate_z()) * radio;

                // force vector
                VFIFE_AppliedLoadStaticForce staticforce = (VFIFE_AppliedLoadStaticForce) v5force
                        .getLoad_value();
                double fx = staticforce.getApplied_force_fx();
                double fy = staticforce.getApplied_force_fy();
                double fz = staticforce.getApplied_force_fz();

                // draw Load
//<<<<<<< HEAD

                drawArrow(px, py, pz, fx, fy, fz,force);
              
             
              
//=======
               // drawArrow(px, py, pz, fx, fy, fz);
                
//>>>>>>> 948f8b200f6f6a0aedba6a2f0c8d082ef80da03d
            }
        }
    }


    private void drawBalljointCone(float x, float y, float z) {

        float scale = 0.1f/m_scale;
        
        Transform3D tf3d = new Transform3D();
        tf3d.setTranslation(new Vector3f(x,y-scale/4,z));
        
        PolygonAttributes lineconepolygonAttributes = new PolygonAttributes();
        lineconepolygonAttributes.setPolygonMode(PolygonAttributes.POLYGON_LINE);
        
        Appearance lineconeAppearance = new Appearance();
        lineconeAppearance.setPolygonAttributes(lineconepolygonAttributes);
        lineconeAppearance.setColoringAttributes(new ColoringAttributes(new Color3f(0, 1, 0), ColoringAttributes.FASTEST));
        
        Cone lineCone = new Cone(scale/3.5f, scale/2, Primitive.GENERATE_NORMALS, 4, 1, lineconeAppearance);

        TransformGroup coneGroup = new TransformGroup(tf3d);
        coneGroup.addChild(lineCone);

        coneGroup.setPickable(false);

        objTrans.addChild(coneGroup);
    }
    
    private void drawFixedjointCross(float x, float y, float z) {
        
        float scale = 0.1f/m_scale;
        
        TransformGroup squareGroup = new TransformGroup();
       
        LineArray mainLine = new LineArray(2, LineArray.COORDINATES
                | LineArray.COLOR_3);
        mainLine.setCoordinate(0, new Point3f((float)x,(float)y,(float)z));
        mainLine.setCoordinate(1, new Point3f((float)x,(float)y-0.5f*scale,(float)z));
        mainLine.setColor(0, new Color3f(0.0f, 1.0f, 0.0f));
        mainLine.setColor(1, new Color3f(0.0f, 1.0f, 0.0f));

        Shape3D shape1 = new Shape3D();
        shape1.setGeometry(mainLine);
        squareGroup.addChild(shape1);
        
        LineArray xLine = new LineArray(2, LineArray.COORDINATES
                | LineArray.COLOR_3);
        xLine.setCoordinate(0, new Point3f((float)x-0.5f*scale,(float)y-0.5f*scale,(float)z));
        xLine.setCoordinate(1, new Point3f((float)x+0.5f*scale,(float)y-0.5f*scale,(float)z));
        xLine.setColor(0, new Color3f(0.0f, 1.0f, 0.0f));
        xLine.setColor(1, new Color3f(0.0f, 1.0f, 0.0f));

        Shape3D shape2 = new Shape3D();
        shape2.setGeometry(xLine);
        squareGroup.addChild(shape2);
        
        LineArray yLine = new LineArray(2, LineArray.COORDINATES
                | LineArray.COLOR_3);
        yLine.setCoordinate(0, new Point3f((float)x,(float)y-0.5f*scale,(float)z-0.5f*scale));
        yLine.setCoordinate(1, new Point3f((float)x,(float)y-0.5f*scale,(float)z+0.5f*scale));
        yLine.setColor(0, new Color3f(0.0f, 1.0f, 0.0f));
        yLine.setColor(1, new Color3f(0.0f, 1.0f, 0.0f));

        Shape3D shape3 = new Shape3D();
        shape3.setGeometry(yLine);
        squareGroup.addChild(shape3);
        
        squareGroup.setPickable(false);

        objTrans.addChild(squareGroup);
    }

    private void drawFixZDisjointSphere(float pointx, float pointy, float pointz) {
        
        float radius = 0.125f *0.1f/m_scale;
        
        Transform3D tf3d = new Transform3D();
        tf3d.setTranslation(new Vector3f(pointx,pointy-radius,pointz));
        
        PolygonAttributes polygonAttributes = new PolygonAttributes();
        polygonAttributes.setPolygonMode(PolygonAttributes.POLYGON_FILL);
        
        Appearance ap = new Appearance();
        ap.setPolygonAttributes(polygonAttributes);
        ap.setColoringAttributes(new ColoringAttributes(new Color3f(0, 1, 0), ColoringAttributes.FASTEST));
        
        Sphere ball = new Sphere(radius, Primitive.GENERATE_NORMALS, 64, ap);

        TransformGroup ballGroup = new TransformGroup(tf3d);
        ballGroup.addChild(ball);

        ballGroup.setPickable(false);

        objTrans.addChild(ballGroup);
    }
    
    private void drawOtherjoints(float x, float y, float z){
        
        float scale = 0.1f/m_scale;
        
        TransformGroup xtg = new TransformGroup();
        
        LineArray xLine = new LineArray(2, LineArray.COORDINATES
                | LineArray.COLOR_3);
        xLine.setCoordinate(0, new Point3f((float)x-0.3f*scale,(float)y-0.3f*scale,(float)z));
        xLine.setCoordinate(1, new Point3f((float)x+0.3f*scale,(float)y+0.3f*scale,(float)z));
        xLine.setColor(0, new Color3f(0.0f, 1.0f, 0.0f));
        xLine.setColor(1, new Color3f(0.0f, 1.0f, 0.0f));

        Shape3D shape1 = new Shape3D();
        shape1.setGeometry(xLine);
        xtg.addChild(shape1);
        
        LineArray yLine = new LineArray(2, LineArray.COORDINATES
                | LineArray.COLOR_3);
        yLine.setCoordinate(0, new Point3f((float)x+0.3f*scale,(float)y-0.3f*scale,(float)z));
        yLine.setCoordinate(1, new Point3f((float)x-0.3f*scale,(float)y+0.3f*scale,(float)z));
        yLine.setColor(0, new Color3f(0.0f, 1.0f, 0.0f));
        yLine.setColor(1, new Color3f(0.0f, 1.0f, 0.0f));

        Shape3D shape2 = new Shape3D();
        shape2.setGeometry(yLine);
        xtg.addChild(shape2);
        
        xtg.setPickable(false);

        objTrans.addChild(xtg);
    }
    
    // TODO Arrow is not finished yet
    private void drawArrow(double px, double py, double pz, double fx,
            double fy, double fz,VFIFE_Load force) {

        TransformGroup arrowGroup = new TransformGroup();
        arrowGroup.setTransform(new Transform3D());
        arrowGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        arrowGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        arrowGroup.setCapability(TransformGroup.ENABLE_PICK_REPORTING);

        // draw main line of the force arrow
        LineArray mainLine = new LineArray(2, LineArray.COORDINATES
                | LineArray.COLOR_3);
        
        // get force volume
        double f = getLength(fx, fy, fz, 0, 0, 0);
        mainLine.setCoordinate(0, new Point3f((float)px,(float)py,(float)pz));
        mainLine.setCoordinate(1, new Point3f((float) (px - fx * 2 / f),
                (float) (py - fy * 2 / f), (float) (pz - fz * 2 / f)));
        mainLine.setColor(0, new Color3f(0.0f, 1.0f, 0.0f));
        mainLine.setColor(1, new Color3f(0.0f, 1.0f, 0.0f));


/*

>>>>>>> 948f8b200f6f6a0aedba6a2f0c8d082ef80da03d
        Shape3D shape1 = new Shape3D();
        shape1.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
        shape1.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
        shape1.setGeometry(mainLine);
        objTrans.addChild(shape1);
<<<<<<< HEAD
        */
        //力的方向

        double pow = Math.pow(fx, 2) + Math.pow(fy, 2) + Math.pow(fz, 2);
        double Mforce = Math.sqrt(pow);

        double directx = Math.acos(fx / Mforce);//与坐标轴的夹角，弧度制
        double directy = Math.acos(fy / Mforce);
        double directz = Math.acos(fz / Mforce);
        Appearance ap=new Appearance();
		Material mat=new Material();
	
		mat.setDiffuseColor(new Color3f(0,1.0f,0.0f));
		mat.setShininess(128);
		ap.setMaterial(mat);
		Transform3D t=new Transform3D();
		Transform3D t0=new Transform3D();
		//7种情况 
		if(fz!=0&&fx==0&&fy==0){//只受fz上面的力的作用
			if(fz<0){
			arrow.setTranslation(new Vector3f(0.0f,-0.25f,0.0f));//因为默认的cone在中心，cone长度是0.5，所以尖头在圆心，需要先平移Y0.25个单位
			t.rotX(-directx);
			t.mul(arrow);
			//t.setTranslation(new Vector3f(0.0f, -(float)pz,(float)pz));
			}
			else{
				arrow.setTranslation(new Vector3f(0.0f,-0.25f,0.0f));
				t.rotX(directx);
				t.mul(arrow);
				//t.setTranslation(new Vector3f(0.0f, (float)pz,(float)pz));
				}
			Transform3D tmp=new Transform3D();
			tmp.setTranslation(new Vector3f((float)px,(float)py,(float)pz));
			tmp.mul(t);
			TransformGroup g1=new TransformGroup(tmp);
			Cone cone2=new Cone(0.1f,0.5f,Primitive.GENERATE_NORMALS,ap);
			g1.addChild(cone2);
			objTrans.addChild(g1);
		}
		else if(fz==0&&fx!=0&&fy==0){//只受fx上面的力的作用
			if(fx>0){
			arrow.setTranslation(new Vector3f(0.0f,-0.25f,0.0f));
			t.rotZ(-directy);
			t.mul(arrow);
			//t.setTranslation(new Vector3f((float)px, (float)px,0.0f));
			}
			else{
				arrow.setTranslation(new Vector3f(0.0f,-0.25f,0.0f));
				t.rotZ(directy);
				t.mul(arrow);
				//t.setTranslation(new Vector3f((float)px, -(float)px,0.0f));
			}
			Transform3D tmp=new Transform3D();
			tmp.setTranslation(new Vector3f((float)px,(float)py,(float)pz));
			tmp.mul(t);
			TransformGroup g1=new TransformGroup(tmp);
			Cone cone2=new Cone(0.1f,0.5f,Primitive.GENERATE_NORMALS,ap);
			g1.addChild(cone2);
			objTrans.addChild(g1);
		}
		else if(fz==0&&fx==0&&fy!=0){  //因为cone初始视图是尖头朝着Y轴的，因此Y轴上受正方向上的力无需旋转
			if(fy>0){
				arrow.setTranslation(new Vector3f(0.0f,-0.25f,0.0f));
				t.mul(arrow);
			//t.rotY(-directz);
			//t.setTranslation(new Vector3f((float)py, 0.0f,(float)py));
			}
			else{     //因为cone初始视图是尖头朝着Y轴的，因此Y轴上受负方向上的力需要绕x旋转，并平移2倍pz
				arrow.setTranslation(new Vector3f(0.0f,-0.25f,0.0f));
				t.rotX(directy);
				t.mul(arrow);
				//t.setTranslation(new Vector3f(0.0f, 0.0f,2*((float)pz)));				
			}
			Transform3D tmp=new Transform3D();
			tmp.setTranslation(new Vector3f((float)px,(float)py,(float)pz));
			tmp.mul(t);
			TransformGroup g1=new TransformGroup(tmp);
			Cone cone2=new Cone(0.1f,0.5f,Primitive.GENERATE_NORMALS,ap);
			g1.addChild(cone2);
			objTrans.addChild(g1);
		}
		
		else if(fz!=0&&fx!=0&&fy==0){ //两个方向受力
			arrow.setTranslation(new Vector3f(0.0f,-0.25f,0.0f));
			
			t0.rotX(Math.PI/2);
			t0.mul(arrow);
			if(fx>0){
			t.rotY(directz);
			}
			else{
				t.rotY(-directz);
			}
			t.mul(t0);
			Transform3D tmp=new Transform3D();
			tmp.setTranslation(new Vector3f((float)px,(float)py,(float)pz));
			tmp.mul(t);
			TransformGroup g1=new TransformGroup(tmp);
			Cone cone2=new Cone(0.1f,0.5f,Primitive.GENERATE_NORMALS,ap);
			g1.addChild(cone2);
			objTrans.addChild(g1);
		}
		
		else if(fz!=0&&fx==0&&fy!=0){//两个方向受力
			arrow.setTranslation(new Vector3f(0.0f,-0.25f,0.0f));
			t0.rotX(Math.PI/2-directz);
			t0.mul(arrow);
			if(fy>0){
		
				t0.rotX(Math.PI/2-directz);
			}
			else{
				
				t0.rotX(Math.PI/2+directz);
				}
			
			Transform3D tmp=new Transform3D();
			tmp.setTranslation(new Vector3f((float)px,(float)py,(float)pz));
			tmp.mul(t0);
			TransformGroup g1=new TransformGroup(tmp);
			Cone cone2=new Cone(0.1f,0.5f,Primitive.GENERATE_NORMALS,ap);
			g1.addChild(cone2);
			objTrans.addChild(g1);	
		}
		
		else if(fz==0&&fx!=0&&fy!=0){//两个方向受力
			arrow.setTranslation(new Vector3f(0.0f,-0.25f,0.0f));
			if(fx>0){
				t0.rotZ(-directy);	
				t0.mul(arrow);
			}
			else{
				t0.rotZ(directy);	
				t0.mul(arrow);
			}
			Transform3D tmp=new Transform3D();
			tmp.setTranslation(new Vector3f((float)px,(float)py,(float)pz));
			tmp.mul(t0);
			TransformGroup g1=new TransformGroup(tmp);
			Cone cone2=new Cone(0.1f,0.5f,Primitive.GENERATE_NORMALS,ap);
			g1.addChild(cone2);
			objTrans.addChild(g1);						
			}	
		
		
		else{   //三个方向受力
			double pow1=Math.pow(fx,2)+ Math.pow(fy,2);
			double Mforce1= Math.sqrt(pow1);//fx,fy合成力，在xoy平面上
			double directy1=Math.acos(fy/Mforce1);
			double bili=Math.acos(Mforce1/Mforce);//fx,fy合成力除以fx，fy，fz的合成力，为了求旋转角度
			if(fx>0){
				arrow.setTranslation(new Vector3f(0.0f,-0.25f,0.0f));
				t0.rotZ(-directy1);
				t0.mul(arrow);
				if(fy>0)
				{
					if(fz>0){
						AxisAngle4d angle = new AxisAngle4d(1, -(fx/fy), 0, bili);//绕经过原点，和1,-（fx/fy）,0,旋转bili弧度
						ta.setRotation(angle);
						ta.mul(t0);
					}//quan dayu 0
					else{
						AxisAngle4d angle = new AxisAngle4d(1, -(fx/fy), 0, -bili);
						//Transform3D ta = new Transform3D();
						ta.setRotation(angle);
						ta.mul(t0);
					}//(fz<0)
				}//(fy>0)
				else{
					if(fz>0){
					AxisAngle4d angle = new AxisAngle4d(1, -(fx/fy), 0, -bili);
					ta.setRotation(angle);
					ta.mul(t0);
					}
					else{
						AxisAngle4d angle = new AxisAngle4d(1, -(fx/fy), 0, bili);
						ta.setRotation(angle);
						ta.mul(t0);
					}
				}
			}//(fx>0)
			else{
				arrow.setTranslation(new Vector3f(0.0f,-0.25f,0.0f));
				t0.rotZ(directy1);	
				t0.mul(arrow);
				if(fy>0)
				{
					if(fz>0){
						AxisAngle4d angle = new AxisAngle4d(1, -(fx/fy), 0, bili);
						ta.setRotation(angle);
						ta.mul(t0);
					}//(fz>0)
					else{
						AxisAngle4d angle = new AxisAngle4d(1, -(fx/fy), 0, -bili);
						ta.setRotation(angle);
						ta.mul(t0);
					}//(fy<0)
				}//(fy>0)
				else{
					if(fz>0){
					AxisAngle4d angle = new AxisAngle4d(1, -(fx/fy), 0, -bili);
					ta.setRotation(angle);
					ta.mul(t0);
					}
					else{
						AxisAngle4d angle = new AxisAngle4d(1, -(fx/fy), 0, bili);
						ta.setRotation(angle);
						ta.mul(t0);
					}
				}
			}
			Transform3D tmp=new Transform3D();
			tmp.setTranslation(new Vector3f((float)px,(float)py,(float)pz));
			tmp.mul(ta);
			TransformGroup g1=new TransformGroup(tmp);
			Cone cone2=new Cone(0.1f,0.5f,Primitive.GENERATE_NORMALS,ap);
			g1.addChild(cone2);
			objTrans.addChild(g1);		
		}		
		
         Shape3D shape = new Shape3D();
         shape.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
         shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
         shape.setUserData(force);
         shape.setGeometry(mainLine);
         arrowGroup.addChild(shape);
         arrowGroup.setPickable(true);
         objTrans.addChild(arrowGroup);

        // draw other for
    
    }

    
    public void cone() {
        TransformGroup lineconeGroup = new TransformGroup();
        Transform3D lineconeTransform3D = new Transform3D();
        lineconeTransform3D.setTranslation(new Vector3d(0, 0, 0));
        lineconeGroup.setTransform(lineconeTransform3D);
        Cone lineCone = new Cone(0.5f, 1.5f);

        Appearance lineconeAppearance = new Appearance();


        PolygonAttributes lineconepolygonAttributes = new PolygonAttributes();
        lineconepolygonAttributes.setPolygonMode(PolygonAttributes.CULL_BACK);
        lineconeAppearance.setPolygonAttributes(lineconepolygonAttributes);

        lineCone.setAppearance(lineconeAppearance);

        lineconeGroup.addChild(lineCone);
        objTrans.addChild(lineconeGroup);
    }

    public double getLength(double p1x, double p1y, double p1z, double p2x,
            double p2y, double p2z) {
        double len = 0;
        len += Math.pow(p1x - p2x, 2);
        len += Math.pow(p1y - p2y, 2);
        len += Math.pow(p1z - p2z, 2);
        len = Math.sqrt(len);
        return len;
    }
    
    private float getMidValueF(ArrayList<Float> arr_scale) {
        float min = Collections.min(arr_scale);
        float max = Collections.max(arr_scale);
        return (min + max) / 2;
    }

    private float getSpanValueF(ArrayList<Float> arr_scale) {
        float min = Collections.min(arr_scale);
        float max = Collections.max(arr_scale);
        return (max - min);
    }

    private float getMaxOfThreeF(float a, float b, float c) {
        float max = Math.max(a, b);
        max = Math.max(max, c);
        return max;
    }

}
