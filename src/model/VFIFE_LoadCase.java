package model;

import java.util.ArrayList;

public class VFIFE_LoadCase {
	private String load_case_name;
	private double load_case_factor;
	private ArrayList<VFIFE_AnalysisMethod> governing_analyses;
 	private VFIFE_PhysicalAction time_variation;
	
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
	public VFIFE_PhysicalAction getTime_variation() {
		return time_variation;
	}
	public void setTime_variation(VFIFE_PhysicalAction time_variation) {
		this.time_variation = time_variation;
	}

	public ArrayList<VFIFE_AnalysisMethod> getGoverning_analyses() {
		return governing_analyses;
	}

	public void setGoverning_analyses(
			ArrayList<VFIFE_AnalysisMethod> governing_analyses) {
		this.governing_analyses = governing_analyses;
	}
	
	
}
