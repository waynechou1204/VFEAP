package dataStructure.entity;

public class VFIFE_LoadCaseRamp extends VFIFE_LoadCasePermanent {
	private double ramp_duration;

	public VFIFE_LoadCaseRamp(double ramp_duration) {
		super();
		this.ramp_duration = ramp_duration;
	}

	public double getRamp_duration() {
		return ramp_duration;
	}

	public void setRamp_duration(double ramp_duration) {
		this.ramp_duration = ramp_duration;
	}
	
}
