package dataStructure;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import dataStructure.entity.VFIFE_Bar;
import dataStructure.entity.VFIFE_CartesianPoint;
import dataStructure.entity.VFIFE_Load;
import dataStructure.entity.VFIFE_LoadNode;
import dataStructure.entity.VFIFE_Material;
import dataStructure.entity.VFIFE_Node;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ProgramCalculation {
	
	public void LoadData(){
		
	}
	public void caculate(String input){
		try {
			   System.out.println("start");
			   String cmd="python main.py "+input;
			   Process pr = Runtime.getRuntime().exec(cmd);
			   BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			   String line;
			   while ((line = in.readLine()) != null) {
			      System.out.println(line);
			   }
			   in.close();
			   pr.waitFor();
			   System.out.println("end");
			}
		catch (Exception e) {
		       e.printStackTrace();
		}
	}
}
