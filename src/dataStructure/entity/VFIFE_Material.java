package dataStructure.entity;

public class VFIFE_Material {
	private int id;
	private String material_name;
	private double young_modulus;
	private double density;
	private double extreme_force;
	private static int max_id=0;
	
	public VFIFE_Material(){
		this.id = max_id++;
		this.material_name="";
		this.young_modulus = 0;
		this.density = 0;
		this.extreme_force = Double.MAX_VALUE;
	}
	
	public VFIFE_Material(int id){
		this.id = id;
		this.material_name="";
		this.young_modulus = 0;
		this.density = 0;
		this.extreme_force = Double.MAX_VALUE;
	}
	
	public int getId() {
		return id;
	}

//	public void setId(int id) {
//		this.id = id;
//	}

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
