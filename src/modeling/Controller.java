package modeling;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import jsdai.lang.SdaiException;
import jsdai.lang.SdaiModel;
import view.JFrameMain;
import calculation.Parser_ImportV5M;
import dataStructure.VFIFE_Model;
import dataStructure.VFIFE_repository;
import dataStructure.entity.VFIFE_AppliedLoadStaticForce;
import dataStructure.entity.VFIFE_Bar;
import dataStructure.entity.VFIFE_Load;
import dataStructure.entity.VFIFE_LoadMemberConcentrated;
import dataStructure.entity.VFIFE_LoadNode;
import dataStructure.entity.VFIFE_Node;

public class Controller {

    public JFrameMain m_frameMain;

    public static void main(String[] args) throws SdaiException {
        
        Controller mainClass = new Controller();
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
            System.out.println("After "+(loadendtime-loadstarttime)/1000+" seconds");
            
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
            System.out.println("After "+(parseendtime-loadendtime)/1000+" seconds");
            
        } catch (SdaiException e) {
            e.printStackTrace();
        } finally {// end
            repo_cis.close();
        }
        return v5model;
    }

    public static VFIFE_Model loadV5M(String stpFilePath) throws SdaiException 
    {
        // set model as return value
        VFIFE_Model v5model = new VFIFE_Model();

        VFIFE_repository repo_v5 = new VFIFE_repository("repositories", "D:\\Repositories");
        SdaiModel model_v5 = repo_v5.loadFile("MyCisRepo", stpFilePath);

        // parse
        Parser_ImportV5M parser = new Parser_ImportV5M();

        v5model.setNodes(parser.parseNodes(model_v5, v5model));
        parser.parseBars(model_v5, v5model); //v5model.setBars();
        v5model.setMateriaux(parser.parseMaterials(v5model, model_v5));
        v5model.setForces(parser.parseLoads_r(v5model, model_v5));

        //     v5model.getForces().addAll(parser.parseLoadMemberCon(model_v5, v5model));
        //     v5model.getForces().addAll(parser.parseLoadNode(model_v5, v5model));
        // end
        repo_v5.close();

        return v5model;
    }
  
    public static void exportFile(VFIFE_Model v5model, String outFilePath)
            throws SdaiException, FileNotFoundException 
    {
        // set repository
        VFIFE_repository repo_vfife = new VFIFE_repository("repositories",
                "D:\\Repositories");

        try {
            // create new model -- bind with vfife schema
            SdaiModel model = repo_vfife.setVfifeOutModel("MyV5Repo", "MyV5Model");

            // parse
            Parser_ExportV5M parser = new Parser_ExportV5M();

            parser.parseNodes(v5model, model);
            parser.parseMaterials(v5model, model);
            parser.parseBars(v5model, model);
            parser.parseLoads(v5model, model);

            // output to v5m file (xml or txt file)
            if (outFilePath.endsWith("xml") || outFilePath.endsWith("XML")) {
                FileOutputStream out = new FileOutputStream(outFilePath); // out stream to xml
                repo_vfife.outputFile(out);
            } 
            else {
                repo_vfife.outputFile(outFilePath);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SdaiException e) {
            e.printStackTrace();
        } finally {
            // end
            System.out.println("out over");

            repo_vfife.close();
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
			   double fx=((VFIFE_AppliedLoadStaticForce)fnd.getLoad_values()).getApplied_force_fx();
			   double fy=((VFIFE_AppliedLoadStaticForce)fnd.getLoad_values()).getApplied_force_fy();
			   double fz=((VFIFE_AppliedLoadStaticForce)fnd.getLoad_values()).getApplied_force_fz();
			   bw.append(
					   fnd.getForce_name()+" "
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
			   VFIFE_LoadMemberConcentrated fmem = (VFIFE_LoadMemberConcentrated) force;
			   double fx=((VFIFE_AppliedLoadStaticForce)fmem.getLoad_value()).getApplied_force_fx();
			   double fy=((VFIFE_AppliedLoadStaticForce)fmem.getLoad_value()).getApplied_force_fy();
			   double fz=((VFIFE_AppliedLoadStaticForce)fmem.getLoad_value()).getApplied_force_fz();
			   bw.append(
					   fmem.getForce_name()+" "
					   +"1 "
					   +fmem.getLoad_position().getCoordinate_x()+" "
					   +fmem.getLoad_position().getCoordinate_y()+" "
					   +fmem.getLoad_position().getCoordinate_z()+" "
					   +fx/(Math.sqrt(fx*fx+fy*fy+fz*fz))+" "
					   +fy/(Math.sqrt(fx*fx+fy*fy+fz*fz))+" "
					   +fz/(Math.sqrt(fx*fx+fy*fy+fz*fz))+" "
					   +Math.sqrt(fx*fx+fy*fy+fz*fz)+" "
					   +fmem.getSupporting_member().getBar_id()+" "
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
		   bw.append(nd.getNode_name()+" ");
		   if(nd.getRestraint()!=null)
			   bw.append(nd.getRestraint().getBoundary_condition_name()+" ");
		   else
			   bw.append("1 "); //suppose no restrain?
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
				   +ba.getNodes().get(0).getNode_name()+" "
				   +ba.getNodes().get(1).getNode_name()+" "
				   +ba.getMaterial().getYoung_modulus()+" "
				   +ba.getSection_area()+" "
				   +ba.getMaterial().getDensity()+" "
				   +"\n");
	   }
	   
	   
	   bw.close();
  
	   System.out.println("out over");       
    }
}
