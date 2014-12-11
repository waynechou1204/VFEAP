package dataStructure.entity;

import java.util.ArrayList;

public class VFIFE_Bar {
	private int id;
	private double section_area;
	private VFIFE_Node start_node;
	private VFIFE_Node end_node;
	private VFIFE_Material bar_material;
	
	public VFIFE_Bar(){
		
	}
	public int getBar_id() {
		return id;
	}
	public void setBar_id(int bar_id) {
		this.id = bar_id;
	}
	
	public VFIFE_Node getStart_node() {
		return start_node;
	}
	public void setStart_node(VFIFE_Node start_node) {
		this.start_node = start_node;
	}
	public VFIFE_Node getEnd_node() {
		return end_node;
	}
	public void setEnd_node(VFIFE_Node end_node) {
		this.end_node = end_node;
	}
	public VFIFE_Material getMaterial() {
		return bar_material;
	}
	public void setMaterial(VFIFE_Material material) {
		this.bar_material = material;
	}
	public double getSection_area() {
		return section_area;
	}
	public void setSection_area(double section_area) {
		this.section_area = section_area;
	}
}
