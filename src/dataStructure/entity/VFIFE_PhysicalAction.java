package dataStructure.entity;

import java.util.ArrayList;

public class VFIFE_PhysicalAction {
	private TYPE_static_or_dynamic action_nature;
	private TYPE_spatial_variation action_spatial_variation;
	private TYPE_direct_or_indirect_action action_type;
	private ArrayList<Double> derivation_factors;
	private ArrayList<String> derivation_factor_labels;
	
	public TYPE_static_or_dynamic getAction_nature() {
		return action_nature;
	}
	public void setAction_nature(TYPE_static_or_dynamic action_nature) {
		this.action_nature = action_nature;
	}
	public TYPE_spatial_variation getAction_spatial_variation() {
		return action_spatial_variation;
	}
	public void setAction_spatial_variation(
			TYPE_spatial_variation action_spatial_variation) {
		this.action_spatial_variation = action_spatial_variation;
	}
	public TYPE_direct_or_indirect_action getAction_type() {
		return action_type;
	}
	public void setAction_type(TYPE_direct_or_indirect_action action_type) {
		this.action_type = action_type;
	}
	public ArrayList<Double> getDerivation_factors() {
		return derivation_factors;
	}
	public void setDerivation_factors(ArrayList<Double> derivation_factors) {
		this.derivation_factors = derivation_factors;
	}
	public ArrayList<String> getDerivation_factor_labels() {
		return derivation_factor_labels;
	}
	public void setDerivation_factor_labels(
			ArrayList<String> derivation_factor_labels) {
		this.derivation_factor_labels = derivation_factor_labels;
	}
	
	
}
