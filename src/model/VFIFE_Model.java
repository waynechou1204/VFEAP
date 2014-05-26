package model;

import java.util.ArrayList;

public class VFIFE_Model {
	private ArrayList<VFIFE_Bar> bars;
	private ArrayList<VFIFE_Force> forces;
	private ArrayList<VFIFE_Material> materiaux;
	
	public VFIFE_Model() {
		this.bars = new ArrayList<VFIFE_Bar>();
		this.forces = new ArrayList<VFIFE_Force>();
		this.materiaux = new ArrayList<VFIFE_Material>();
	}
	
	public ArrayList<VFIFE_Material> getMateriaux() {
		return materiaux;
	}

	public void setMateriaux(ArrayList<VFIFE_Material> materiaux) {
		this.materiaux = materiaux;
	}
	
	public VFIFE_Bar getBar(int barid){
		for(VFIFE_Bar bartemp : bars){
			if(bartemp.getBar_id()==barid){
				return bartemp;
			}
		}
		return null;
	}
	public void addBar(VFIFE_Bar barin){
		VFIFE_Bar bartemp = getBar(barin.getBar_id());
		if(bartemp==null){
			bars.add(barin);
		}
	}
	
	
	public ArrayList<VFIFE_Bar> getBars() {
		return bars;
	}
	public void setBars(ArrayList<VFIFE_Bar> bars) {
		this.bars = bars;
	}
	public ArrayList<VFIFE_Force> getForces() {
		return forces;
	}
	public void setForces(ArrayList<VFIFE_Force> forces) {
		this.forces = forces;
	}
	
	
}
