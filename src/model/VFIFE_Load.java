package model;

// Force
public class VFIFE_Load {
	private String force_name;						// express load name
	private VFIFE_LoadCase parent_load_case;	// force type
	private double duration_time;				// duration of the force
	
	public VFIFE_Load(){
		this.duration_time = 0;
	}
	
	public String getForce_name() {
		return force_name;
	}
	public void setForce_name(String force_name) {
		this.force_name = force_name;
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
