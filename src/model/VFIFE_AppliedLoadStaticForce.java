package model;

public class VFIFE_AppliedLoadStaticForce extends VFIFE_AppliedLoadStatic {
	private double applied_force_fx;
	private double applied_force_fy;
	private double applied_force_fz;
	
	private double applied_moment_mx;
	private double applied_moment_my;
	private double applied_moment_mz;
	
	public VFIFE_AppliedLoadStaticForce(){
		this.applied_force_fx=0;
		this.applied_force_fy=0;
		this.applied_force_fz=0;
		this.applied_moment_mx=0;
		this.applied_moment_my=0;
		this.applied_moment_mz=0;
	}
	
			
	public double getApplied_force_fx() {
		return applied_force_fx;
	}
	public void setApplied_force_fx(double applied_force_fx) {
		this.applied_force_fx = applied_force_fx;
	}
	public double getApplied_force_fy() {
		return applied_force_fy;
	}
	public void setApplied_force_fy(double applied_force_fy) {
		this.applied_force_fy = applied_force_fy;
	}
	public double getApplied_force_fz() {
		return applied_force_fz;
	}
	public void setApplied_force_fz(double applied_force_fz) {
		this.applied_force_fz = applied_force_fz;
	}
	public double getApplied_moment_mx() {
		return applied_moment_mx;
	}
	public void setApplied_moment_mx(double applied_moment_mx) {
		this.applied_moment_mx = applied_moment_mx;
	}
	public double getApplied_moment_my() {
		return applied_moment_my;
	}
	public void setApplied_moment_my(double applied_moment_my) {
		this.applied_moment_my = applied_moment_my;
	}
	public double getApplied_moment_mz() {
		return applied_moment_mz;
	}
	public void setApplied_moment_mz(double applied_moment_mz) {
		this.applied_moment_mz = applied_moment_mz;
	}
	
	
}
