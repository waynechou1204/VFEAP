package dataStructure.entity;

// Force
public class VFIFE_Load {
	private int id;
	private double load_start_time;				// start time of the force
	private double load_end_time;				// end time of the force
	private VFIFE_LoadCase parent_load_case;	// force type
	private VFIFE_AppliedLoad load_value;		// load value
	private static int max_id=0;
	
	public VFIFE_Load(){
		this.id = max_id++;
		this.load_start_time = 0;
		this.load_end_time = 0;
	}
	public VFIFE_Load(int id){
		this.id = id;
	}
	
	public VFIFE_Load(VFIFE_Load load)
	{
		this.id=load.id;
		this.parent_load_case=load.parent_load_case;
		this.load_start_time=load.load_start_time;
		this.load_end_time=load.load_end_time;
	}
	
	
	public int getId() {
		return id;
	}
//	public void setId(int id) {
//		this.id = id;
//	}
	public VFIFE_AppliedLoad getLoad_value() {
		return load_value;
	}
	public void setLoad_value(VFIFE_AppliedLoad load_value) {
		this.load_value = load_value;
	}
	public double getStart_time() {
		return load_start_time;
	}

	public void setStart_time(double start_time) {
		this.load_start_time = start_time;
	}

	public double getEnd_time() {
		return load_end_time;
	}

	public void setEnd_time(double end_time) {
		this.load_end_time = end_time;
	}

	public VFIFE_LoadCase getParent_load_case() {
		return parent_load_case;
	}
	public void setParent_load_case(VFIFE_LoadCase parent_load_case) {
		this.parent_load_case = parent_load_case;
	}
	
	
}
