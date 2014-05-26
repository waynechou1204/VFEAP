package model;

public class VFIFE_Force {
	private int force_id;
	private VFIFE_LoadCase parent_load_case;	// force type
	private VFIFE_CartesianPoint load_position;	// force position
	private VFIFE_AppliedLoad load_value;		// force direction
	private double duration_time;
	
	public VFIFE_Force(){
		this.duration_time = 0;
	}
	
	public int getForce_id() {
		return force_id;
	}
	public void setForce_id(int force_id) {
		this.force_id = force_id;
	}
	public VFIFE_CartesianPoint getLoad_position() {
		return load_position;
	}
	public void setLoad_position(VFIFE_CartesianPoint load_position) {
		this.load_position = load_position;
	}
	public VFIFE_AppliedLoad getLoad_value() {
		return load_value;
	}
	public void setLoad_value(VFIFE_AppliedLoad load_value) {
		this.load_value = load_value;
	}
	public double getDuration_time() {
		return duration_time;
	}
	public void setDuration_time(double duration_time) {
		this.duration_time = duration_time;
	}
	public VFIFE_LoadCase getParent_load_case() {
		return parent_load_case;
	}
	public void setParent_load_case(VFIFE_LoadCase parent_load_case) {
		this.parent_load_case = parent_load_case;
	}
	
	
}
