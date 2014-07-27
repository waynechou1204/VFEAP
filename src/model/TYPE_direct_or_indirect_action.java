package model;

public enum TYPE_direct_or_indirect_action {
	direct_action, indirect_action;
	
	
	public static TYPE_direct_or_indirect_action valueOf(int value)
	{
		switch(value)
		{
		case 1:
			return direct_action;
		case 2:
			return indirect_action;
		default:
			return null;
		}
	}
}
