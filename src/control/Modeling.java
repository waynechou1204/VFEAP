package control;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import jsdai.lang.SdaiException;
import jsdai.lang.SdaiModel;
import model.VFIFE_Model;
import model.VFIFE_repository;
import view.JFrameMain;
import view.VFIFE_Modeling_view;

public class Modeling {

    public static void main(String[] args) throws SdaiException {
        Modeling mainFrame = new Modeling();
    }

    public Modeling() throws SdaiException {

        VFIFE_Model m_v5model = new VFIFE_Model();
        //VFIFE_Model m_v5model = loadCIS("eg5-2zxw.stp");

        // view of the model
        JFrameMain frameMain = new JFrameMain(m_v5model);
    }

    public static VFIFE_Model loadCIS(String stpFilePath) throws SdaiException {
        // set model as return value
        VFIFE_Model v5model = new VFIFE_Model();

        VFIFE_repository repo_cis = new VFIFE_repository("repositories",
                "G:\\Repositories");

        try {
            SdaiModel model_cis = repo_cis.loadFile("MyCisRepo", stpFilePath);

            // parse
            Parser_CIS2V5 parser = new Parser_CIS2V5();

            v5model.setNodes(parser.parseNodes(model_cis, v5model));
            parser.parseBars(model_cis, v5model); // v5model.setBars();
            v5model.setMateriaux(parser.parseMaterials(model_cis, v5model));
            v5model.getForces().addAll(
                    parser.parseLoadMemberCon(model_cis, v5model));
            v5model.getForces()
                    .addAll(parser.parseLoadNode(model_cis, v5model));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {// end
            repo_cis.close();
        }
        return v5model;
    }

    public static void exportFile(VFIFE_Model v5model, String outFilePath)
            throws SdaiException, FileNotFoundException {
        // set repository
        VFIFE_repository repo_vfife = new VFIFE_repository("repositories",
                "G:\\Repositories");

        try {
            // create new model -- bind with vfife schema
            SdaiModel model = repo_vfife.setVfifeOutModel("MyV5Repo",
                    "MyV5Model");

            // parse
            Parser_V5VFIFE parser = new Parser_V5VFIFE();

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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // end
            System.out.println("out over");

            repo_vfife.close();
        }
    }

}
