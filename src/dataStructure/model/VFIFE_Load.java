package dataStructure.model;

// Force
public class VFIFE_Load {
	private String force_name;						// express load name
	private VFIFE_LoadCase parent_load_case;	// force type
	private double start_time;				// start time of the force
	private double end_time;				// end time of the force
	
	public VFIFE_Load(){
		this.start_time = 0;
		this.end_time = 0;
	}
	public VFIFE_Load(VFIFE_Load load)
	{
		this.force_name=load.force_name;
		this.parent_load_case=load.parent_load_case;
		this.start_time=load.start_time;
		this.end_time=load.end_time;
	}
	
	public String getForce_name() {
		return force_name;
	}
	public void setForce_name(String force_name) {
		this.force_name = force_name;
	}
	
	public double getStart_time() {
		return start_time;
	}

	public void setStart_time(double start_time) {
		this.start_time = start_time;
	}

	public double getEnd_time() {
		return end_time;
	}

	public void setEnd_time(double end_time) {
		this.end_time = end_time;
	}

	public VFIFE_LoadCase getParent_load_case() {
		return parent_load_case;
	}
	public void setParent_load_case(VFIFE_LoadCase parent_load_case) {
		this.parent_load_case = parent_load_case;
	}
	
	
}
