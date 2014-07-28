package dataStructure.model;

public class VFIFE_Material {
	private double young_modulus;
	private double density;
	private String name;
	
	public VFIFE_Material(){
		this.young_modulus = 0;
		this.density = 0;
		this.name="";
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	
}
