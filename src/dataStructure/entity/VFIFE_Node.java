package dataStructure.entity;


public class VFIFE_Node {
	private int id;
	private double mess;
	private VFIFE_CartesianPoint coordinate;
	private VFIFE_BoundaryConditionLogical restraints;
	private static int max_id=0;
	
	public VFIFE_Node() {
		id = max_id++;
	}
	public  VFIFE_Node(int id) {
		this.id = id;
	}
	public int getNode_id() {
		return id;
	}
//	public void setNode_id(int node_id) {
//		this.id = node_id;
//	}
	
	
	public double getMess() {
		return mess;
	}

	public void setMess(double mess) {
		this.mess = mess;
	}

	public VFIFE_CartesianPoint getCoord() {
		return coordinate;
	}
	public void setCoord(VFIFE_CartesianPoint coord) {
		this.coordinate = coord;
	}
	public VFIFE_BoundaryConditionLogical getRestraint() {
		return restraints;
	}
	public void setRestraint(VFIFE_BoundaryConditionLogical restraint) {
		this.restraints = restraint;
	}
	
}
