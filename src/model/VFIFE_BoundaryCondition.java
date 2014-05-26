package model;

public class VFIFE_BoundaryCondition {
	private String boundary_condition_name;
	private String boundary_condition_description;
	private Boolean bc_x_displacement_free;
	private Boolean bc_y_displacement_free;
	private Boolean bc_z_displacement_free;
	private Boolean bc_x_rotation_free;
	private Boolean bc_y_rotation_free;
	private Boolean bc_z_rotation_free;
	public String getBoundary_condition_name() {
		return boundary_condition_name;
	}
	public void setBoundary_condition_name(String boundary_condition_name) {
		this.boundary_condition_name = boundary_condition_name;
	}
	public String getBoundary_condition_description() {
		return boundary_condition_description;
	}
	public void setBoundary_condition_description(
			String boundary_condition_description) {
		this.boundary_condition_description = boundary_condition_description;
	}
	public Boolean getBc_x_displacement_free() {
		return bc_x_displacement_free;
	}
	public void setBc_x_displacement_free(Boolean bc_x_displacement_free) {
		this.bc_x_displacement_free = bc_x_displacement_free;
	}
	public Boolean getBc_y_displacement_free() {
		return bc_y_displacement_free;
	}
	public void setBc_y_displacement_free(Boolean bc_y_displacement_free) {
		this.bc_y_displacement_free = bc_y_displacement_free;
	}
	public Boolean getBc_z_displacement_free() {
		return bc_z_displacement_free;
	}
	public void setBc_z_displacement_free(Boolean bc_z_displacement_free) {
		this.bc_z_displacement_free = bc_z_displacement_free;
	}
	public Boolean getBc_x_rotation_free() {
		return bc_x_rotation_free;
	}
	public void setBc_x_rotation_free(Boolean bc_x_rotation_free) {
		this.bc_x_rotation_free = bc_x_rotation_free;
	}
	public Boolean getBc_y_rotation_free() {
		return bc_y_rotation_free;
	}
	public void setBc_y_rotation_free(Boolean bc_y_rotation_free) {
		this.bc_y_rotation_free = bc_y_rotation_free;
	}
	public Boolean getBc_z_rotation_free() {
		return bc_z_rotation_free;
	}
	public void setBc_z_rotation_free(Boolean bc_z_rotation_free) {
		this.bc_z_rotation_free = bc_z_rotation_free;
	}
	
	
}
