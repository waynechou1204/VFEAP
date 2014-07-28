package dataStructure.model;

public enum TYPE_static_or_dynamic {
	Static, dynamic, quasi_dynamic;
	
	
	public static TYPE_static_or_dynamic valueOf(int value)
	{
		switch(value)
		{
		case 1:
			return Static;
		case 2:
			return dynamic;
		case 3:
			return quasi_dynamic;
		default:
			return null;
		}
	}
}
