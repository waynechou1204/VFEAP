package dataStructure.model;

public class VFIFE_LoadMember extends VFIFE_Load {
	private VFIFE_Bar supporting_member;

	public VFIFE_LoadMember()
	{}
	public VFIFE_LoadMember(VFIFE_Load load)
	{
		super(load);
	}
	public VFIFE_Bar getSupporting_member() {
		return supporting_member;
	}

	public void setSupporting_member(VFIFE_Bar supporting_member) {
		this.supporting_member = supporting_member;
	}
	
	
}
