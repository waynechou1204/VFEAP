package control;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileNameExtensionFilter;

import jsdai.lang.SdaiException;
import jsdai.lang.SdaiModel;
import model.VFIFE_Bar;
import model.VFIFE_Load;
import model.VFIFE_Material;
import model.VFIFE_Model;
import model.VFIFE_Node;
import model.VFIFE_repository;
import view.JDialogMateriaux;
import view.VFIFE_Modeling_view;

public class Modeling extends JFrame {

    static VFIFE_Model m_v5model = null;
    static VFIFE_Modeling_view view = null;

    public static void main(String[] args) throws SdaiException, FileNotFoundException {

        Modeling mainFrame = new Modeling();

        m_v5model = loadCIS("1memberload.stp");
        
		// view of the model
        //printModel(v5model);
        view = new VFIFE_Modeling_view(m_v5model);
        
        mainFrame.getContentPane().add(view, BorderLayout.CENTER);

        //exportFile(v5model, "out.stp");
        //System.out.println("ok");
    }

    public Modeling() {

        m_v5model = new VFIFE_Model();

        // set frame size
        this.setSize(800, 600);
        // set title
        this.setTitle("VFIFE Modeling Tool");
        // set frame location
        Dimension displaySize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();
        if (frameSize.width > displaySize.width) {
            frameSize.width = displaySize.width;
        }
        if (frameSize.height > displaySize.height) {
            frameSize.height = displaySize.height;
        }
        this.setLocation((displaySize.width - frameSize.width) / 2, (displaySize.height - frameSize.height) / 2);

        // set menubar
        JMenuBar menubar = new JMenuBar();

        JPopupMenu.setDefaultLightWeightPopupEnabled(false);

        JMenu menufile = new JMenu("File");
        JMenuItem itemOpen = new JMenuItem("Open", 'O');
        JMenuItem itemExport = new JMenuItem("Export", 'P');
        itemOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // get file open path
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("CIS2/STP FILE", "stp"));
                int i = fileChooser.showOpenDialog(getContentPane());

                if (i == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String stpFilePath = selectedFile.getAbsolutePath();
                    try {
                        // load file and parse
                        m_v5model = loadCIS(stpFilePath);

                        // show bars
                        //view.drawBars(m_v5model);
                        //view.drawNodes(m_v5model);
						//view.drawLoads(m_v5model);
                    } catch (SdaiException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        menufile.add(itemOpen);

        itemExport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (!m_v5model.isEmpty()) {
                    // get file save path
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setSelectedFile(new File("vifife.stp")); // default file name
                    fileChooser.setFileFilter(new FileNameExtensionFilter("STP & XML FILE", "stp", "xml"));
                    int i = fileChooser.showSaveDialog(getContentPane());
                    if (i == JFileChooser.APPROVE_OPTION) {
                        File getfile = fileChooser.getSelectedFile();
                        try {
                            if (getfile != null) {
                                exportFile(m_v5model, getfile.getAbsolutePath());
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (SdaiException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No model to export", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        menufile.add(itemExport);
        JMenuItem itemExit = new JMenuItem("Exit", 'E');
        itemExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menufile.add(itemExit);
        menubar.add(menufile);

        JMenu menuDefine = new JMenu("Define");
        JMenuItem itemMaterial = new JMenuItem("Material", 'M');
        itemMaterial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (!m_v5model.isEmpty()) {
                    JDialogMateriaux materiaux = new JDialogMateriaux(m_v5model);
                }
            }
        });
        menuDefine.add(itemMaterial);
        menubar.add(menuDefine);

        JMenu menuSpecify = new JMenu("Specify");
        JMenuItem itemLoad = new JMenuItem("Load", 'L');
        menuSpecify.add(itemLoad);
        menubar.add(menuSpecify);

        this.setJMenuBar(menubar);

        // set visibility
        this.setVisible(true);

        // set close action
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static VFIFE_Model loadCIS(String stpFilePath) throws SdaiException {
        // set model as return value
        VFIFE_Model v5model = new VFIFE_Model();

        VFIFE_repository repo_cis = new VFIFE_repository("repositories", "G:\\Repositories");
        SdaiModel model_cis = repo_cis.loadFile("MyCisRepo", stpFilePath);

        // parse
        Parser_CIS2V5 parser = new Parser_CIS2V5();

        v5model.setNodes(parser.parseNodes(model_cis, v5model));
        parser.parseBars(model_cis, v5model); //v5model.setBars();
        v5model.setMateriaux(parser.parseMaterials(model_cis, v5model));
        v5model.getForces().addAll(parser.parseLoadMemberCon(model_cis, v5model));
        v5model.getForces().addAll(parser.parseLoadNode(model_cis, v5model));

        // end
        repo_cis.close();

        return v5model;
    }

    public static void exportFile(VFIFE_Model v5model, String outFilePath) throws SdaiException, FileNotFoundException {
        // set repository
        VFIFE_repository repo_vfife = new VFIFE_repository("repositories",
                "G:\\Repositories");

        // create new model -- bind with vfife schema
        SdaiModel model = repo_vfife.setVfifeOutModel("MyV5Repo", "MyV5Model");

        // parse
        Parser_V5VFIFE parser = new Parser_V5VFIFE();

        parser.parseNodes(v5model, model);
        parser.parseMaterials(v5model, model);
        parser.parseBars(v5model, model);
        parser.parseLoads(v5model, model);

        // out put
        if (outFilePath.endsWith("xml") || outFilePath.endsWith("XML")) {
            FileOutputStream out = new FileOutputStream(outFilePath);
            //outstream to xml (filepath to stp)
            repo_vfife.outputFile(out);
        } else {
            repo_vfife.outputFile(outFilePath);
        }

        // end
        System.out.println("out over");

        repo_vfife.close();
    }

}
