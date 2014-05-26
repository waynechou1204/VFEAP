package model;


public class VFIFE_Node {
	private int node_id;
	private String node_name;
	private VFIFE_CartesianPoint coord;
	private VFIFE_BoundaryCondition restraint;
	
	
	
	public VFIFE_Node() {
		this.node_name=" ";
	}
	
	public int getNode_id() {
		return node_id;
	}
	public void setNode_id(int node_id) {
		this.node_id = node_id;
	}
	public String getNode_name() {
		return node_name;
	}
	public void setNode_name(String node_name) {
		this.node_name = node_name;
	}
	
	public VFIFE_CartesianPoint getCoord() {
		return coord;
	}
	public void setCoord(VFIFE_CartesianPoint coord) {
		this.coord = coord;
	}
	public VFIFE_BoundaryCondition getRestraint() {
		return restraint;
	}
	public void setRestraint(VFIFE_BoundaryCondition restraint) {
		this.restraint = restraint;
	}
	
}
