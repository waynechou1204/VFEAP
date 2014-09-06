package dataStructure.entity;

import java.util.ArrayList;

public class VFIFE_Bar {
	private int bar_id;
	private ArrayList<VFIFE_Node> nodes;
	private VFIFE_Material material;
	private double section_area;
	
	public VFIFE_Bar(){
		nodes = new ArrayList<VFIFE_Node>();
	}
	public int getBar_id() {
		return bar_id;
	}
	public void setBar_id(int bar_id) {
		this.bar_id = bar_id;
	}
	public ArrayList<VFIFE_Node> getNodes() {
		return nodes;
	}
	public void setNodes(ArrayList<VFIFE_Node> nodes) {
		this.nodes = nodes;
	}
	public VFIFE_Material getMaterial() {
		return material;
	}
	public void setMaterial(VFIFE_Material material) {
		this.material = material;
	}
	public double getSection_area() {
		return section_area;
	}
	public void setSection_area(double section_area) {
		this.section_area = section_area;
	}
}
