package dataStructure.entity;

public class VFIFE_LoadNode extends VFIFE_Load {
	private VFIFE_Node supporting_node;
	
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
	
}
