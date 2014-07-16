package model;

public class VFIFE_LoadNode extends VFIFE_Load {
	private VFIFE_Node supporting_node;
	private VFIFE_AppliedLoad load_values;
	
	public VFIFE_LoadNode()
	{}
	public VFIFE_LoadNode(VFIFE_Load load)
	{
		super(load);
	}
	public VFIFE_Node getSupporting_node() {
		return supporting_node;
	}
	public void setSupporting_node(VFIFE_Node supporting_node) {
		this.supporting_node = supporting_node;
	}
	public VFIFE_AppliedLoad getLoad_values() {
		return load_values;
	}
	public void setLoad_values(VFIFE_AppliedLoad load_values) {
		this.load_values = load_values;
	}
	
	
}
