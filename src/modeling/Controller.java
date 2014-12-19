package modeling;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import jsdai.lang.SdaiException;
import jsdai.lang.SdaiModel;
import view.JFrameMain;
import dataStructure.VFIFE_Model;
import dataStructure.VFIFE_repository;
import dataStructure.entity.VFIFE_Bar;
import dataStructure.entity.VFIFE_Load;
import dataStructure.entity.VFIFE_LoadBar;
import dataStructure.entity.VFIFE_LoadNode;
import dataStructure.entity.VFIFE_Node;

public class Controller {

    public JFrameMain m_frameMain;

    public static void main(String[] args) throws SdaiException {
    	
        new Controller(); //Controller mainClass
        
    }

    public Controller() throws SdaiException {

    	// FIXME, the second line is for test, to delete
        //VFIFE_Model m_v5model = new VFIFE_Model();
        VFIFE_Model m_v5model = loadCIS("resources/5-1.stp");

        // main window of the model
        m_frameMain = new JFrameMain(m_v5model);
    }

    // FIXME, The repository path should be specified in the project! 
    
    /**
     * Load stp file with cis/2 standard (exported by SAP2000)
     * parses the file components to v5model  
     * 
     * CIMSteel Integration Standards (CIS/2) is a product model 
     * and electronic data exchange file format for structural steel project information.
     */
    public static VFIFE_Model loadCIS(String stpFilePath) throws SdaiException 
    {
    	
    	// set model as return value
        VFIFE_Model v5model = new VFIFE_Model();

        VFIFE_repository repo_cis = new VFIFE_repository("repositories", "D:\\Repositories");

        try {
            System.out.println("Loading cis file: "+stpFilePath);
            System.out.println("Waiting...");
            long loadstarttime = System.currentTimeMillis();
            
            SdaiModel model_cis = repo_cis.loadFile("MyCisRepo", stpFilePath);
           
            long loadendtime = System.currentTimeMillis();
            System.out.println("After "+(loadendtime-loadstarttime)+" milliseconds");
            
            // parse
            System.out.println("Parsing file");
            System.out.println("Waiting...");
            Parser_ImportSTP parser = new Parser_ImportSTP();

            v5model.setNodes(parser.parseNodes(model_cis, v5model));
            parser.parseBars(model_cis, v5model); // v5model.setBars();
            v5model.setMateriaux(parser.parseMaterials(model_cis, v5model));
            v5model.getForces().addAll(parser.parseLoadMemberCon(model_cis, v5model));
            v5model.getForces().addAll(parser.parseLoadNode(model_cis, v5model));

            long parseendtime = System.currentTimeMillis();
            System.out.println("After "+(parseendtime-loadendtime)+" milliseconds");
            
        } catch (SdaiException e) {
            e.printStackTrace();
        } finally {// end
            repo_cis.close();
        }
        return v5model;
    }

    public static VFIFE_Model loadV5M(File v5mFile) throws IOException 
    {
        // set model as return value
        VFIFE_Model v5model = new VFIFE_Model();
        
        System.out.println("Loading v5m file: "+v5mFile.getAbsolutePath());
        System.out.println("Waiting...");
        long loadstarttime = System.currentTimeMillis();
        
        if(v5mFile.isFile() && v5mFile.exists()){ //判断文件是否存在
            InputStreamReader read = new InputStreamReader(new FileInputStream(v5mFile));//考虑到编码格式
            BufferedReader br = new BufferedReader(read);
             
            // parse
            Parser_ImportV5M parser = new Parser_ImportV5M(v5model, br);
            // order is important
     	   	parser.parseHeader();
            parser.parseMaterial();
            parser.parseNode();
            parser.parseBar();
            parser.parseLoad();
            parser.parseModel();
            read.close();
		}else{
		    System.out.println("找不到指定的文件");
		}
       
        long loadendtime = System.currentTimeMillis();
        System.out.println("After "+(loadendtime-loadstarttime)+" milliseconds");
        
        return v5model;
      }

    
    public static boolean exportV5M(VFIFE_Model v5model, File file) throws IOException
    {
    	if (!file.exists()) {
    	    file.createNewFile();
	   }

	   FileWriter fw = new FileWriter(file.getAbsoluteFile());
	   BufferedWriter bw = new BufferedWriter(fw);
	   
	   transformMass(v5model);
	   
	   Parser_ExportV5M parser = new Parser_ExportV5M(v5model, bw);
	   // order is important
	   parser.writeHeader();
	   parser.writeMaterial();
	   parser.writeNode();
	   parser.writeBar();
	   parser.writeLoad();
	   parser.writeModel();
	   bw.close();
  
	   System.out.println("out over"); 
	   return true;
    }
    
    public static void transformMass(VFIFE_Model v5model){
    	for(VFIFE_Bar bar : v5model.getBars()){
    		if(bar.getMaterial()!=null){
	    		VFIFE_Node nd1 = bar.getStart_node();
	    		VFIFE_Node nd2 = bar.getEnd_node();
	    		double l = Util.getLength(nd1.getCoord().getCoordinate_x(), nd1.getCoord().getCoordinate_y(),
	    				nd1.getCoord().getCoordinate_z(), nd2.getCoord().getCoordinate_x(), 
	    				nd2.getCoord().getCoordinate_y(), nd2.getCoord().getCoordinate_z());
	    		double v = l*bar.getSection_area();
	    		double ro = bar.getMaterial().getDensity();
	    		double mass = v*ro;
	    		nd1.setMass(nd1.getMass()+mass/2);
	    		nd2.setMass(nd2.getMass()+mass/2);
    		}
    	}
    }

    public static void exportFileAsInput(VFIFE_Model v5model, File file) throws IOException 
    {
       if (!file.exists()) {
	    file.createNewFile();
	   }

	   FileWriter fw = new FileWriter(file.getAbsoluteFile());
	   BufferedWriter bw = new BufferedWriter(fw);
	   bw.write("force "
	   		+ "#id "
	   		+ "type "
	   		+ "x_posit "
	   		+ "y_posit "
	   		+ "z_posit "
	   		+ "x_direc "
	   		+ "y_direc "
	   		+ "z_direc "
	   		+ "directMag "
	   		+ "barid "
	   		+ "startTime "
	   		+ "endTime "
	   		+ "caculation_endTime "
	   		+ "ramp_time "
	   		+ "loadway\n");
	   
	   for(VFIFE_Load force : v5model.getForces()){
		   if(force.getClass().toString().contains("Node")){
			   VFIFE_LoadNode fnd = (VFIFE_LoadNode) force;
			   double fx=(fnd.getLoad_value()).getApplied_force_fx();
			   double fy=(fnd.getLoad_value()).getApplied_force_fy();
			   double fz=(fnd.getLoad_value()).getApplied_force_fz();
			   bw.append(
					   fnd.getId()+" "
					   +"1 "
					   +fnd.getSupporting_node().getCoord().getCoordinate_x()+" "
					   +fnd.getSupporting_node().getCoord().getCoordinate_y()+" "
					   +fnd.getSupporting_node().getCoord().getCoordinate_z()+" "
					   +fx/(Math.sqrt(fx*fx+fy*fy+fz*fz))+" "
					   +fy/(Math.sqrt(fx*fx+fy*fy+fz*fz))+" "
					   +fz/(Math.sqrt(fx*fx+fy*fy+fz*fz))+" "
					   +Math.sqrt(fx*fx+fy*fy+fz*fz)+" "
					   +"barid??? "
					   +"0 "  //start time
					   +"50 " //end time
					   +"50 " //calculation duration
					   +"50 " //ramp time
					   +"Vertical\n" // loadway
					   );
		   }
		   else if(force.getClass().toString().contains("Member")){
			   VFIFE_LoadBar fmem = (VFIFE_LoadBar) force;
			   double fx=(fmem.getLoad_value()).getApplied_force_fx();
			   double fy=(fmem.getLoad_value()).getApplied_force_fy();
			   double fz=(fmem.getLoad_value()).getApplied_force_fz();
			   bw.append(
					   fmem.getId()+" "
					   +"1 "
					   +fmem.getLoad_position().getCoordinate_x()+" "
					   +fmem.getLoad_position().getCoordinate_y()+" "
					   +fmem.getLoad_position().getCoordinate_z()+" "
					   +fx/(Math.sqrt(fx*fx+fy*fy+fz*fz))+" "
					   +fy/(Math.sqrt(fx*fx+fy*fy+fz*fz))+" "
					   +fz/(Math.sqrt(fx*fx+fy*fy+fz*fz))+" "
					   +Math.sqrt(fx*fx+fy*fy+fz*fz)+" "
					   +fmem.getSupporting_bar().getBar_id()+" "
					   +"0 "  //start time
					   +"50 " //end time
					   +"50 " //calculation duration
					   +"50 " //ramp time
					   +"Vertical\n" // load way
					   );
		   }
	   }
	   
	   bw.append("node "
		   		+ "#id "
		   		+ "type "
		   		+ "posit.x "
		   		+ "posit.y "
		   		+ "posit.z "
		   		+ "mass\n");
	   for(VFIFE_Node nd : v5model.getNodes()){
		   bw.append(nd.getNode_id()+" ");
		   if(nd.getRestraint()!=null){
			//   bw.append(nd.getRestraint().getBoundary_condition_name()+" ");
		   }
		   else
		   {
			   bw.append("1 "); //suppose no restrain?
		   }
		   bw.append(nd.getCoord().getCoordinate_x()+" "
				   +nd.getCoord().getCoordinate_y()+" "
				   +nd.getCoord().getCoordinate_z()+" "
				   +"10mass\n" //mass
				   );
	   }
	   
	   bw.append("bar "
	   		+ "#id "
	   		+ "node1id "
	   		+ "node2id "
	   		+ "Young_modulus "
	   		+ "area "
	   		+ "density\n");
	   for(VFIFE_Bar ba : v5model.getBars()){
		   bw.append(Integer.toString(ba.getBar_id())+" "
				   +ba.getStart_node().getNode_id()+" "
				   +ba.getEnd_node().getNode_id()+" "
				   +ba.getMaterial().getYoung_modulus()+" "
				   +ba.getSection_area()+" "
				   +ba.getMaterial().getDensity()+" "
				   +"\n");
	   }
	   
	   
	   bw.close();
  
	   System.out.println("out over");       
    }
}
