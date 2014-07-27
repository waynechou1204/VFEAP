package model;

public enum TYPE_spatial_variation {
	free_action, fixed_action;
	public static TYPE_spatial_variation valueOf(int value)
	{
		switch(value)
		{
		case 1:
			return free_action;
		case 2:
			return fixed_action;
		default:
			return null;
		}
	}
}
