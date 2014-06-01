package view;

import model.VFIFE_Bar;
import model.VFIFE_Model;
import model.VFIFE_Node;

import java.applet.Applet;
import java.awt.BorderLayout;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.universe.*;

import javax.media.j3d.*;
import javax.vecmath.*;
import javax.media.j3d.Canvas3D;

import java.awt.GraphicsConfiguration;
import java.util.ArrayList;

import javax.media.j3d.Appearance;
import javax.media.j3d.LineArray;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.Shape3D;
public class lineShape2 extends Shape3D {
	public lineShape2(){
		 float vert[]={
					0.8f,0.4f,0.0f,0.75f,0.35f,0.0f,
					0.8f,0.4f,0.0f,0.85f,0.35f,0.0f,
					0.0f,0.0f,0.0f,-0.1f,0.0f,0.0f,
					-0.1f,0.0f,0.0f,-0.1f,0.1f,0.0f,
					-0.1f,0.1f,0.0f,-0.12f,0.08f,0.0f,
					-0.1f,0.1f,0.0f,-0.08f,0.08f,0.0f,
					0.75f,0.35f,0.0f,0.85f,0.35f,0.0f,
					0.0f,0.0f,0.0f,-0.02f,0.02f,0.0f,
					0.0f,0.0f,0.0f,-0.02f,-0.02f,0.0f,
					0.38f,0.02f,0.0f,0.4f,0.0f,0.0f,
					0.42f,0.02f,0.0f,0.4f,0.0f,0.0f,
					0.88f,0.2f,0.0f,0.86f,0.22f,0.0f,
					0.88f,0.2f,0.0f,0.86f,0.18f,0.0f,
					};
		 float color[]={
				0.0f,0.0f,1.0f, 0.5f,0.0f,1.0f,
				0.0f,0.0f,1.0f, 0.5f,0.0f,1.0f,
				1.0f,0.0f,0.0f, 1.0f,0.0f,0.5f,
				1.0f,0.0f,0.0f, 1.0f,0.0f,0.5f,
				1.0f,0.0f,0.0f, 1.0f,0.0f,0.5f,
				1.0f,0.0f,0.0f, 1.0f,0.0f,0.5f,
				0.0f,0.0f,1.0f, 0.5f,0.0f,1.0f,
				1.0f,0.0f,0.0f, 1.0f,0.0f,0.5f,
				1.0f,0.0f,0.0f, 1.0f,0.0f,0.5f,
				0.0f,0.0f,1.0f, 0.5f,0.0f,1.0f,
				0.0f,0.0f,1.0f, 0.5f,0.0f,1.0f,
				0.0f,0.0f,1.0f, 0.5f,0.0f,1.0f,
				0.0f,0.0f,1.0f, 0.5f,0.0f,1.0f,
				};	
				
		LineArray line1=new LineArray(50,LineArray.COORDINATES|LineArray.COLOR_3);
		line1.setCoordinates(0,vert);
		line1.setColors(0,color);
		LineAttributes la=new LineAttributes();
		la.setLineWidth(5.0f);
		la.setLineAntialiasingEnable(true);
		Appearance ap=new Appearance();
		ap.setLineAttributes(la);
		//Shape3D shape1=new Shape3D();
		this.setGeometry(line1);
		this.setAppearance(ap);
	}
	}