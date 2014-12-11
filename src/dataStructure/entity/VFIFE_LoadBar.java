package dataStructure.entity;

public class VFIFE_LoadBar extends VFIFE_Load {
	private VFIFE_Bar supporting_bar;
	private VFIFE_CartesianPoint load_position;
	
	public VFIFE_LoadBar()
	{
		
	}
	
	public VFIFE_LoadBar(VFIFE_Load load)
	{
		super(load);
	}
	
	public VFIFE_Bar getSupporting_bar() {
		return supporting_bar;
	}

	public void setSupporting_bar(VFIFE_Bar supporting_bar) {
		this.supporting_bar = supporting_bar;
	}

	public VFIFE_CartesianPoint getLoad_position() {
		return load_position;
	}

	public void setLoad_position(VFIFE_CartesianPoint load_position) {
		this.load_position = load_position;
	}
	
	
}
