package modeling;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import jsdai.lang.SdaiException;
import jsdai.lang.SdaiModel;
import view.JFrameMain;
import calculation.Parser_ImportV5M;
import dataStructure.VFIFE_Model;
import dataStructure.VFIFE_repository;

public class Controller {

    public JFrameMain m_frameMain;

    public static void main(String[] args) throws SdaiException {
        
        Controller mainClass = new Controller();
    }

    public Controller() throws SdaiException {

    	// FIXME, the second line is for test, to delete
        //VFIFE_Model m_v5model = new VFIFE_Model();
        VFIFE_Model m_v5model = loadCIS("model_resources/building.stp");

        // main window of the model
        m_frameMain = new JFrameMain(m_v5model);
    }

    // FIXME, The repository path should be specified in the project! 
    
    /*
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
            SdaiModel model_cis = repo_cis.loadFile("MyCisRepo", stpFilePath);

            // parse
            Parser_ImportSTP parser = new Parser_ImportSTP();

            v5model.setNodes(parser.parseNodes(model_cis, v5model));
            parser.parseBars(model_cis, v5model); // v5model.setBars();
            v5model.setMateriaux(parser.parseMaterials(model_cis, v5model));
            v5model.getForces().addAll(parser.parseLoadMemberCon(model_cis, v5model));
            v5model.getForces().addAll(parser.parseLoadNode(model_cis, v5model));

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

   
}
