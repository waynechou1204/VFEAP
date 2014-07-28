package dataStructure.model;

public class VFIFE_LoadMemberConcentrated extends VFIFE_LoadMember {
	private VFIFE_CartesianPoint load_position;
	private VFIFE_AppliedLoad load_value;
	
	
	public void setSupporting_member(VFIFE_Bar supporting_member) {
		super.setSupporting_member(supporting_member);
	}
	public VFIFE_LoadMemberConcentrated()
	{
		
	}
	
	public VFIFE_LoadMemberConcentrated(VFIFE_Load load)
	{
		super(load);
	}
	public VFIFE_CartesianPoint getLoad_position() {
		return load_position;
	}
	public void setLoad_position(VFIFE_CartesianPoint load_position) {
		this.load_position = load_position;
	}
	public VFIFE_AppliedLoad getLoad_value() {
		return load_value;
	}
	public void setLoad_value(VFIFE_AppliedLoad load_value) {
		this.load_value = load_value;
	}
	
	
}
