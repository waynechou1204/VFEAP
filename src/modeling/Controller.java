package modeling;

import dataStructure.model.VFIFE_LoadNode;
import dataStructure.model.VFIFE_Node;
import dataStructure.VFIFE_repository;
import dataStructure.model.VFIFE_LoadMemberConcentrated;
import dataStructure.model.VFIFE_AppliedLoadStaticForce;
import dataStructure.model.VFIFE_Load;
import dataStructure.model.VFIFE_Bar;
import dataStructure.model.VFIFE_CartesianPoint;
import dataStructure.VFIFE_Model;
import calculation.Parser_ImportV5M;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import jsdai.lang.SdaiException;
import jsdai.lang.SdaiModel;
import view.JFrameMain;

public class Controller {

    public JFrameMain m_frameMain;

    public static void main(String[] args) throws SdaiException {
        //	BigDecimal aa = new BigDecimal(1.1);

        Controller mainClass = new Controller();
    }

    public Controller() throws SdaiException {

        // VFIFE_Model m_v5model = new VFIFE_Model();
        VFIFE_Model m_v5model = loadCIS("model_resources\\5-1.stp");

        // main window of the model
        m_frameMain = new JFrameMain(m_v5model);
    }

    public static VFIFE_Model loadCIS(String stpFilePath) throws SdaiException {
        // set model as return value
        VFIFE_Model v5model = new VFIFE_Model();

        VFIFE_repository repo_cis = new VFIFE_repository("repositories",
                "D:\\Repositories");

        try {
            SdaiModel model_cis = repo_cis.loadFile("MyCisRepo", stpFilePath);

            // parse
            Parser_ImportSTP parser = new Parser_ImportSTP();

            v5model.setNodes(parser.parseNodes(model_cis, v5model));
            parser.parseBars(model_cis, v5model); // v5model.setBars();
            v5model.setMateriaux(parser.parseMaterials(model_cis, v5model));
            v5model.getForces().addAll(
                    parser.parseLoadMemberCon(model_cis, v5model));
            v5model.getForces()
                    .addAll(parser.parseLoadNode(model_cis, v5model));

        } catch (SdaiException e) {
            e.printStackTrace();
        } finally {// end
            repo_cis.close();
        }
        return v5model;
    }

    public static VFIFE_Model loadV5(String stpFilePath) throws SdaiException {
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
            throws SdaiException, FileNotFoundException {
        // set repository
        VFIFE_repository repo_vfife = new VFIFE_repository("repositories",
                "D:\\Repositories");

        try {
            // create new model -- bind with vfife schema
            SdaiModel model = repo_vfife.setVfifeOutModel("MyV5Repo",
                    "MyV5Model");

            // parse
            Parser_ExportV5M parser = new Parser_ExportV5M();

            parser.parseNodes(v5model, model);
            parser.parseMaterials(v5model, model);
            parser.parseBars(v5model, model);
            parser.parseLoads(v5model, model);

            // out put
            if (outFilePath.endsWith("xml") || outFilePath.endsWith("XML")) {
                FileOutputStream out = new FileOutputStream(outFilePath);
                // outstream to xml (filepath to stp)
                repo_vfife.outputFile(out);
            } else {
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
