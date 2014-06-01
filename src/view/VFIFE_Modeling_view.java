package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.util.ArrayList;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Font3D;
import javax.media.j3d.FontExtrusion;
import javax.media.j3d.LineArray;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.Material;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Text3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import model.VFIFE_Bar;
import model.VFIFE_Model;
import model.VFIFE_Node;

import com.sun.j3d.utils.universe.SimpleUniverse;

public class VFIFE_Modeling_view extends JPanel {

	private SimpleUniverse universe = null;
	private BranchGroup scene = null;
	
	public VFIFE_Modeling_view() {
		this.setLayout(new BorderLayout());

		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas = new Canvas3D(config);
		canvas.setSize(800,600);
		
		universe = new SimpleUniverse(canvas);
		createSceneGraph();

		universe.getViewingPlatform().setNominalViewingTransform();
		
		this.add("Center",canvas);
	}
	
	public void createSceneGraph() {
		scene = new BranchGroup();
		scene.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		scene.setCapability(BranchGroup.ALLOW_DETACH);
	}
	
	public void addBars(VFIFE_Model v5model){
		
		float[] vert;// should put data in this array
		ArrayList<Float> al = new ArrayList<Float>();

		for (VFIFE_Bar bar : v5model.getBars()) {
			ArrayList<VFIFE_Node> arrNodes = bar.getNodes();
			for (VFIFE_Node node : arrNodes) {
				float scale = 12.5f;
				al.add((float) (node.getCoord().getCoordinate_x() / scale));
				al.add((float) (node.getCoord().getCoordinate_z() / scale));
				al.add((float) (node.getCoord().getCoordinate_y() / scale));
			}
		}
		/*
		 * int flag=0; for (VFIFE_Load force : v5model.getForces()) { float
		 * scale1=12.5f; if(flag==0) {
		 * al.add((float)force.getLoad_position().getCoordinate_x()/scale1);
		 * al.add((float)force.getLoad_position().getCoordinate_y()/scale1);
		 * al.add((float)force.getLoad_position().getCoordinate_z()/scale1);
		 * al.add(5.0f/ scale1); al.add(1.0f/ scale1);al.add(0.0f/ scale1); }
		 * if(flag==1) {
		 * al.add(((float)force.getLoad_position().getCoordinate_y(
		 * )/scale1)+0.8f);
		 * al.add((float)force.getLoad_position().getCoordinate_x()/scale1);
		 * al.add((float)force.getLoad_position().getCoordinate_z()/scale1);
		 * al.add(11.0f/ scale1); al.add(2.5f/ scale1);al.add(0.0f/ scale1);
		 * 
		 * } flag=1; }
		 */

		vert = new float[al.size()];
		for (int x = 0; x < al.size(); x++) {
			vert[x] = al.get(x);
		}
		
		// for(float f:vert){System.out.println(f);}
		float color[] = { 0.0f, 0.0f, 1.0f, 0.5f, 0.0f, 1.0f, 
				0.0f, 0.0f, 1.0f, 0.5f, 0.0f, 1.0f, 
				0.0f, 0.0f, 1.0f, 0.5f, 0.0f, 1.0f, 
				0.0f, 0.0f, 1.0f, 0.5f, 0.0f, 1.0f, };
		
		LineArray line = new LineArray(20, LineArray.COORDINATES | LineArray.COLOR_3);
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
		
		scene.detach();
		scene.addChild(sh);
		universe.addBranchGraph(scene);
	}
	
	public void addLights() {
		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
				1000.0);
	
		Color3f light1Color = new Color3f(1.0f, 1.0f, 1.0f);
		Vector3f light1Direction = new Vector3f(4.0f, -7.0f, -12.0f);
		DirectionalLight light1 = new DirectionalLight(light1Color,
				light1Direction);
		light1.setInfluencingBounds(bounds);
		scene.detach();
		scene.addChild(light1);
		universe.addBranchGraph(scene);
		
		// Set up the ambient light 
		Color3f ambientColor = new Color3f(.1f, .1f, .1f); 
		AmbientLight ambientLightNode = new AmbientLight(ambientColor);
		ambientLightNode.setInfluencingBounds(bounds);
		scene.detach();
		scene.addChild(ambientLightNode); 
		universe.addBranchGraph(scene);
	} 
	
	private void addObjects() { 
		Font3D f3d = new Font3D(new Font("TestFont", Font.PLAIN, 2), new FontExtrusion()); 
		Text3D text = new Text3D(f3d, new String("Java3D.org"), new Point3f( -3.5f, -.5f, -4.5f)); 
		text.setString("Java3D.org");
		Color3f white = new Color3f(1.0f, 1.0f,1.0f); 
		Color3f blue = new Color3f(.2f, 0.2f, 0.6f); 
		Appearance a = new Appearance();
		Material m = new Material(blue, blue, blue, white, 80.0f); m.setLightingEnable(true);
		a.setMaterial(m);
		
		Shape3D sh = new Shape3D();
		sh.setGeometry(text);
		sh.setAppearance(a);
		TransformGroup tg = new TransformGroup();
		Transform3D t3d = new Transform3D();
		Transform3D tDown = new Transform3D();
		Transform3D rot = new Transform3D();
		Vector3f v3f = new Vector3f(-1.6f, -1.35f, -6.5f);
		t3d.setTranslation(v3f);
		rot.rotX(Math.PI / 5);
		t3d.mul(rot);
		v3f = new Vector3f(0, -1.4f, 0f);
		tDown.setTranslation(v3f);
		t3d.mul(tDown);
		tg.setTransform(t3d);
		tg.addChild(sh);
		
		scene.detach();
		scene.addChild(tg);
		universe.addBranchGraph(scene);
	}

	
/*
	 * 
	 * public void init(){ setLayout(new BorderLayout()); Canvas3D c = new
	 * Canvas3D(SimpleUniverse.getPreferredConfiguration()); add("Center",c);
	 * 
	 * BranchGroup scene=createSceneGraph();
	 * 
	 * u = new SimpleUniverse(c);
	 * 
	 * u.getViewingPlatform().setNominalViewingTransform();
	 * u.addBranchGraph(scene); }
	 * 
	 * public void destroy() { u.cleanup(); }
	 * 
	 * public static void main(String[] args) throws SdaiException,
	 * FileNotFoundException { VFIFE_Model v5model =
	 * Modeling.loadCIS("eg5-3.stp"); 
	 * lineShape1 line = lineShape1.getInstance(v5model); 
	 * new MainFrame(new VFIFE_Modeling_view(),400,400); 
	 }
	 */
}
