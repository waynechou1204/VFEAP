package model;

public class VFIFE_LoadCase {
	private String load_case_name;
	private double load_case_factor;
	//private enum physical_action_nature {STATIC, DYNAMIC, QUASI_DYNAMIC};
	private int time_variation;
	
	public VFIFE_LoadCase(){
		load_case_factor=0;
	}
	
	public String getLoad_case_name() {
		return load_case_name;
	}
	public void setLoad_case_name(String load_case_name) {
		this.load_case_name = load_case_name;
	}
	public double getLoad_case_factor() {
		return load_case_factor;
	}
	public void setLoad_case_factor(double load_case_factor) {
		this.load_case_factor = load_case_factor;
	}
	public int getTime_variation() {
		return time_variation;
	}
	public String getTime_variation_string() {
		switch (time_variation){
		case 1:
			return "STATIC";
		case 2:
			return "DYNAMIC";
		case 3:
			return "QUASI_DYNAMIC";
		default:
			break;
		}
		return null;
	}
	public void setTime_variation(int i) {
		this.time_variation = i;
	}
	
	
}
