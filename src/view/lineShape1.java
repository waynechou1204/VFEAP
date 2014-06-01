package view;

import java.util.ArrayList;

import javax.media.j3d.Appearance;
import javax.media.j3d.LineArray;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.Shape3D;

import model.VFIFE_Bar;
import model.VFIFE_Load;
import model.VFIFE_Model;
import model.VFIFE_Node;
//import java.applet.Applet;
//import java.awt.BorderLayout;

public class lineShape1 extends Shape3D {

	private static lineShape1 uniqueInstance = null;
	static float[] vert;// should put data in this array
	private ArrayList<Float> al = new ArrayList<Float>();

	public static lineShape1 getInstance(VFIFE_Model v5model) {
		if (uniqueInstance == null) {
			uniqueInstance = new lineShape1(v5model);
		}
		return uniqueInstance;
	}

	public static lineShape1 getInstance() {
		return uniqueInstance;

	}

	private lineShape1(VFIFE_Model v5model) {
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
		
		this.setGeometry(line);
		this.setAppearance(ap);
	}
}