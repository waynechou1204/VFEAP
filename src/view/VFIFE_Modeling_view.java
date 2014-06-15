package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.util.ArrayList;

import jsdai.lang.SdaiException;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Font3D;
import javax.media.j3d.FontExtrusion;
import javax.media.j3d.IndexedLineStripArray;
import javax.media.j3d.LineArray;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.LineStripArray;
import javax.media.j3d.Material;
import javax.media.j3d.PointArray;
import javax.media.j3d.PointAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Text3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import model.VFIFE_AppliedLoad;
import model.VFIFE_AppliedLoadStaticForce;
import model.VFIFE_Bar;
import model.VFIFE_Load;
import model.VFIFE_LoadMemberConcentrated;
import model.VFIFE_LoadNode;
import model.VFIFE_Model;
import model.VFIFE_Node;

import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class VFIFE_Modeling_view extends JPanel {

	private SimpleUniverse universe = null;
	private BranchGroup scene = null;

	private float x1 = 0;
	private float y1 = 0;
	private float z1 = 0;
	private float x2 = 0;
	private float y2 = 0;
	private float z2 = 0;

	public VFIFE_Modeling_view() {
		this.setLayout(new BorderLayout());
		GraphicsConfiguration config = SimpleUniverse
				.getPreferredConfiguration();
		Canvas3D canvas = new Canvas3D(config);
		canvas.setSize(800, 600);
		universe = new SimpleUniverse(canvas);
		createSceneGraph();
		universe.getViewingPlatform().setNominalViewingTransform();
		this.add("Center", canvas);

	}

	public void createSceneGraph() {
		scene = new BranchGroup();
		scene.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		scene.setCapability(BranchGroup.ALLOW_DETACH);
	}

	
	// draw rigid connect node -- gang jie
	private void drawRigidNode(VFIFE_Node node){
		if (node.getRestraint() != null) {
			float x = (float) node.getCoord().getCoordinate_x();
			float y = (float) node.getCoord().getCoordinate_y();
			float z = (float) node.getCoord().getCoordinate_z();
			
			// there are 6 vertex 
			float vert[] = { 
					x, y, z, 
					(x+0.1f), y, z-0.1f,
					(x1-0.1f), y, z-0.1f,
					x, y, z,
					x, y+0.1f,z-0.1f,
					x, y-0.1f,z-0.1f
					};
			float color[] = { 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,0.0f, 1.0f, 0.0f, 0.0f, 
					1.0f, 0.0f,0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f };
			
			
			LineArray lines = new LineArray(12, LineArray.COORDINATES | LineArray.COLOR_3);
			lines.setCoordinates(0, vert);
			lines.setColors(0, color);
			LineAttributes la = new LineAttributes();
			la.setLineWidth(2.0f);
			la.setLineAntialiasingEnable(true);
			la.setLinePattern(LineAttributes.PATTERN_SOLID);
			Appearance ap = new Appearance();
			ap.setLineAttributes(la);
			Shape3D sh = new Shape3D();
			sh.setGeometry(lines);
			sh.setAppearance(ap);
			scene.detach();
			scene.addChild(sh);
			universe.addBranchGraph(scene);
		}
	}
	
	// draw hinge joint node -- jiao jie
	private void drawHingeNode(VFIFE_Node node){
		
	}
	
	// draw constraints of nodes
	public void drawNodes(VFIFE_Model v5model) {
		for(VFIFE_Node node : v5model.getNodes()){
			drawRigidNode(node);
			/*if (node.getRestraint() != null) {
				
				float scale = 10.0f;
				
				float pointx = (float) (node.getCoord().getCoordinate_x() / scale);
				float pointy = (float) (node.getCoord().getCoordinate_z() / scale);
				float pointz = (float) (node.getCoord().getCoordinate_y() / scale);
				
				int sCount[] = new int[1];
				int[] index = { 0, 1, 2 };
				int iCount = 4;
				int vCount = 3;
				if (x2 < 0.2 && y2 < 0.2 && z2 < 0.2) {
					float vert[] = { pointx, pointy, pointz,
							(pointx - 0.003f), (pointy - 0.003f), pointz,
							(pointx + 0.003f), (pointy - 0.003f),
							(pointz + 0.0f) };
					float color[] = { 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
							0.0f, 1.0f, 0.0f };
					sCount[0] = 4;
					IndexedLineStripArray line = new IndexedLineStripArray(
							vCount, IndexedLineStripArray.COORDINATES
									| IndexedLineStripArray.COLOR_3,
							iCount, sCount);
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
						scene.detach();
						scene.addChild(g6);
					} else {
						TransformGroup g6 = new TransformGroup();
						Transform3D t2 = new Transform3D();
						t2.setScale(0.5);
						g6.setTransform(t2);
						g6.addChild(sh);
						scene.detach();
						scene.addChild(g6);
					}

					universe.addBranchGraph(scene);
				} else {
					float vert[] = { pointx, pointy, pointz,
							pointx - 0.03f, pointy - 0.03f, pointz,
							pointx + 0.03f, pointy - 0.03f, pointz + 0.0f };

					float color[] = { 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
							0.0f, 1.0f, 0.0f };
					sCount[0] = 4;
					IndexedLineStripArray line = new IndexedLineStripArray(
							vCount, IndexedLineStripArray.COORDINATES
									| IndexedLineStripArray.COLOR_3,
							iCount, sCount);
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
						scene.detach();
						scene.addChild(g6);
					} else {
						TransformGroup g6 = new TransformGroup();
						Transform3D t2 = new Transform3D();
						t2.setScale(0.5);
						g6.setTransform(t2);
						g6.addChild(sh);
						scene.detach();
						scene.addChild(g6);
					}

					universe.addBranchGraph(scene);
				}
			}*/
		}
	}

	// order of x y z !!!
	public void addBars(VFIFE_Model v5model) {
		float[] vert;// should put data in this array
		ArrayList<Float> al = new ArrayList<Float>();

		for (VFIFE_Bar bar : v5model.getBars()) {
			ArrayList<VFIFE_Node> arrNodes = bar.getNodes();
			int flag1 = 0;
			for (VFIFE_Node node : arrNodes) {
				float scale = 12.5f;
				al.add((float) (node.getCoord().getCoordinate_x() / scale));
				al.add((float) (node.getCoord().getCoordinate_z() / scale));
				al.add((float) (node.getCoord().getCoordinate_y() / scale));
				flag1++;// 此处加flag是为了取数组中的第二个数据；
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

		float color[] = { 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
				0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
				1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
				0.0f, };

		LineArray line = new LineArray(50, LineArray.COORDINATES
				| LineArray.COLOR_3); // vertex num problem
		line.setCoordinates(0, vert);
		line.setColors(0, color); // color problem
		LineAttributes la = new LineAttributes();
		la.setLineWidth(1.0f);
		la.setLineAntialiasingEnable(true);
		Appearance ap = new Appearance();
		ap.setLineAttributes(la);
		Shape3D sh = new Shape3D();
		sh.setGeometry(line);
		sh.setAppearance(ap);
		scene.detach();
		if (x2 < 0.2 && y2 < 0.2 && z2 < 0.2) { // judge the data,then decide to
												// choose how to make a scale.
			TransformGroup g6 = new TransformGroup();
			Transform3D t2 = new Transform3D();
			t2.setScale(5.0);
			g6.setTransform(t2);
			g6.addChild(sh);
			scene.addChild(g6);
		}

		else if (x2 < 1 && y2 < 1 && z2 < 1) {
			TransformGroup g6 = new TransformGroup();
			Transform3D t2 = new Transform3D();
			t2.setScale(0.5);
			g6.setTransform(t2);
			g6.addChild(sh);
			scene.addChild(g6);
		} else {

			TransformGroup g6 = new TransformGroup();
			Transform3D t2 = new Transform3D();
			t2.setScale(0.05);
			g6.setTransform(t2);
			g6.addChild(sh);
			scene.addChild(g6);
		}

		universe.addBranchGraph(scene);
	}

	public void addLoads(VFIFE_Model v5model) {
		// int flag=0;
		float[] vert;// should put data in this array
		ArrayList<Float> al = new ArrayList<Float>();
		float scale = 12.5f;

		for (VFIFE_Load force : v5model.getForces()) {
			if (force.getClass().toString().contains("VFIFE_LoadNode")) {
				// flag=1;
				// System.out.println("fgy ");
				VFIFE_LoadNode v5nodeforce = (VFIFE_LoadNode) force;
				VFIFE_AppliedLoadStaticForce staticforce = (VFIFE_AppliedLoadStaticForce) v5nodeforce
						.getLoad_values();
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
						al.add((float) v5nodeforce.getSupporting_node()
								.getCoord().getCoordinate_z()
								/ scale);
						float cz = (float) v5nodeforce.getSupporting_node()
								.getCoord().getCoordinate_z()
								/ scale;
						al.add((float) v5nodeforce.getSupporting_node()
								.getCoord().getCoordinate_y()
								/ scale);
						float by = (float) v5nodeforce.getSupporting_node()
								.getCoord().getCoordinate_y()
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
			}
		}

		for (VFIFE_Load force1 : v5model.getForces()) {
			if (force1.getClass().toString()
					.contains("VFIFE_LoadMemberConcentrated")) {
				// flag=2;
				// System.out.println("shaoen");
				VFIFE_LoadMemberConcentrated v5nodeforce = (VFIFE_LoadMemberConcentrated) force1;

				VFIFE_AppliedLoadStaticForce staticforce = (VFIFE_AppliedLoadStaticForce) v5nodeforce
						.getLoad_value();
				double fx = staticforce.getApplied_force_fx();
				double fz = staticforce.getApplied_force_fz();
				if (fx == 0 && fz != 0) {
					if (fz < 0) {
						al.add((float) v5nodeforce.getLoad_position()
								.getCoordinate_x() / scale);
						float ax = (float) v5nodeforce.getLoad_position()
								.getCoordinate_x() / scale;
						al.add((float) v5nodeforce.getLoad_position()
								.getCoordinate_y() / scale);
						float by = (float) v5nodeforce.getLoad_position()
								.getCoordinate_y() / scale;
						al.add((float) v5nodeforce.getLoad_position()
								.getCoordinate_z() / scale);
						float cz = (float) v5nodeforce.getLoad_position()
								.getCoordinate_z() / scale;
						al.add(ax);
						al.add(1 / scale);
						al.add(cz);
						al.add(ax);
						al.add(by);
						al.add(cz);
						al.add(ax - 0.02f);
						al.add(by + 0.02f);
						al.add(cz);
						al.add(ax);
						al.add(by);
						al.add(cz);
						al.add(ax + 0.02f);
						al.add(by + 0.02f);
						al.add(cz);
					} else if (fz > 0) {
					}// now it does't need ,u can use it when you need
				}
				if (fx != 0 && fz == 0) {
					if (fx > 0) {
						float instance = 0;
						float x = Math.abs((float) v5nodeforce
								.getLoad_position().getCoordinate_x() / scale);
						float y = Math.abs((float) v5nodeforce
								.getLoad_position().getCoordinate_y() / scale);
						float z = Math.abs((float) v5nodeforce
								.getLoad_position().getCoordinate_z() / scale);
						instance = (float) (Math.sqrt(x * x + y * y + z * z));
						al.add(x1);
						al.add(y1 + instance);
						al.add(z1);
						al.add(x1 + 1 / scale);
						al.add(instance);
						al.add(z1);
						al.add(x1 + 1 / scale);
						al.add(instance);
						al.add(z1);
						al.add(x1 + 1 / scale - 0.02f);
						al.add(instance + 0.02f);
						al.add(z1);
						al.add(x1 + 1 / scale);
						al.add(instance);
						al.add(z1);
						al.add(x1 + 1 / scale - 0.02f);
						al.add(instance - 0.02f);
						al.add(z1);

					} else if (fz < 0) {
					}// now it does't need ,u can use it when you need
				}

			}

		}
		vert = new float[al.size()];
		for (int x = 0; x < al.size(); x++) {
			vert[x] = al.get(x);
		}
		float color[] = { 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
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
			scene.detach();
			scene.addChild(g6);
		} else {
			TransformGroup g6 = new TransformGroup();
			Transform3D t2 = new Transform3D();
			t2.setScale(0.05);
			// System.out.println("dayin wo le ma fgy");
			g6.setTransform(t2);
			g6.addChild(sh2);
			scene.detach();
			scene.addChild(g6);
		}

		universe.addBranchGraph(scene);
	}

	public void addCres() {
		float vert[] = { 0.0f, -0.04f, 0.0f, 0.0f, 0.1f, 0.0f, -0.04f, 0.0f,
				0.0f, 0.1f, 0.0f, 0.0f, };
		float color[] = { 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
				1.0f, 0.0f, 0.0f, };
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
		Font3D f3d = new Font3D(new Font("TestFont", Font.PLAIN, 2),
				new FontExtrusion());
		Text3D text = new Text3D(f3d, new String("Java3D.org"), new Point3f(
				-3.5f, -.5f, -4.5f));
		text.setString("Java3D.org");
		Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
		Color3f blue = new Color3f(.2f, 0.2f, 0.6f);
		Appearance a = new Appearance();
		Material m = new Material(blue, blue, blue, white, 80.0f);
		m.setLightingEnable(true);
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

}
