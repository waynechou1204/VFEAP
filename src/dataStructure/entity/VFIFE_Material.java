package dataStructure.entity;

public class VFIFE_Material {
	private String material_name;
	private double young_modulus;
	private double density;
	private double extreme_force;
	
	public VFIFE_Material(){
		this.material_name="";
		this.young_modulus = 0;
		this.density = 0;
		this.extreme_force = 0;
	}
	
	public String getName() {
		return material_name;
	}

	public void setName(String name) {
		this.material_name = name;
	}

	public double getYoung_modulus() {
		return young_modulus;
	}
	public void setYoung_modulus(double young_modulus) {
		this.young_modulus = young_modulus;
	}
	
	public double getDensity() {
		return density;
	}
	public void setDensity(double density) {
		this.density = density;
	}

	public double getExtreme_force() {
		return extreme_force;
	}

	public void setExtreme_force(double extreme_force) {
		this.extreme_force = extreme_force;
	}
	
}
