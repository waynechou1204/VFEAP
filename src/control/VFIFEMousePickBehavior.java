package control;

import java.awt.Color;

import javax.media.j3d.Appearance;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.LineArray;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.PointArray;
import javax.media.j3d.Shape3D;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.vecmath.Color3f;

import view.BarInfoDialog;
import view.NodeInfoDialog;

import model.VFIFE_Bar;
import model.VFIFE_Model;
import model.VFIFE_Node;

import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.PickTool;
import com.sun.j3d.utils.picking.behaviors.PickMouseBehavior;

public class VFIFEMousePickBehavior extends PickMouseBehavior{
	
	Appearance appear=null;  
    Material mater=null;
    VFIFE_Model v5model = null;
    JPanel panel = null;
    
	public VFIFEMousePickBehavior(Canvas3D canvas, BranchGroup bg, Bounds bounds) {
		super(canvas, bg, bounds);

		this.setSchedulingBounds(bounds);
		bg.addChild(this);
		pickCanvas.setMode(PickTool.GEOMETRY);
		appear = new Appearance();
		mater = new Material();
		mater.setDiffuseColor(new Color3f(Color.red));
		appear.setMaterial(mater);
	}

	@Override
	public void updateScene(int xpos, int ypos) {
		
		PickResult pickResult = null;  
        Shape3D shape = null;  
        pickCanvas.setShapeLocation(xpos, ypos);  
        pickCanvas.setTolerance(5.0f);
        pickResult = pickCanvas.pickClosest();  
        if (pickResult != null) {  
        	
            shape = (Shape3D) pickResult.getNode(PickResult.SHAPE3D);
            if(shape.getUserData()!=null)
            if(shape.getUserData().getClass().equals(VFIFE_Node.class))
            {
            	float [] d = new float[3];
            	((PointArray)shape.getGeometry()).getCoordinate(0, d);
            	System.out.println(d[0]+","+d[1]+","+d[2]);

            	NodeInfoDialog nodeinfodlg = new NodeInfoDialog((VFIFE_Node)shape.getUserData());
            	nodeinfodlg.show();
            }
            else if(shape.getUserData().getClass().equals(VFIFE_Bar.class))
            {
            	BarInfoDialog barinfodlg = new BarInfoDialog((VFIFE_Bar)shape.getUserData());
            	barinfodlg.show();
            }
        }
	}
	
	public void setModel(VFIFE_Model v5model)
	{
		this.v5model = v5model;
	}
	
	public void setPanel(JPanel panel)
	{
		this.panel = panel;
	}

}
