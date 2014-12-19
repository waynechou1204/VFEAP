package modeling;

import java.io.BufferedReader;
import java.io.IOException;

import dataStructure.VFIFE_Model;
import dataStructure.entity.VFIFE_AppliedLoad;
import dataStructure.entity.VFIFE_Bar;
import dataStructure.entity.VFIFE_BoundaryConditionLogical;
import dataStructure.entity.VFIFE_CartesianPoint;
import dataStructure.entity.VFIFE_Load;
import dataStructure.entity.VFIFE_LoadBar;
import dataStructure.entity.VFIFE_LoadCaseAccidental;
import dataStructure.entity.VFIFE_LoadCasePermanent;
import dataStructure.entity.VFIFE_LoadCaseRamp;
import dataStructure.entity.VFIFE_LoadCaseVariable;
import dataStructure.entity.VFIFE_LoadNode;
import dataStructure.entity.VFIFE_Material;
import dataStructure.entity.VFIFE_Node;

public class Parser_ImportV5M {
	
	private BufferedReader m_br;
	private VFIFE_Model m_model;
	private String lineTxt;
	
	public Parser_ImportV5M(VFIFE_Model v5model, BufferedReader br){
		m_br = br;
		m_model = v5model;
	}
	
	public void parseHeader() throws IOException{
		lineTxt = m_br.readLine();
		while(!lineTxt.equals("Material")){
            System.out.println(lineTxt);
            lineTxt = m_br.readLine();
        }
	}
	
	/**
	 * id name youngsModulus density extremeForce
	 * @throws IOException
	 */
	public void parseMaterial() throws IOException{
		lineTxt = m_br.readLine();
		while(!lineTxt.equals("Node")){
            String[] attr = lineTxt.split(" ");
            VFIFE_Material mat = new VFIFE_Material(Integer.parseInt(attr[0]));
            mat.setName(attr[1]);
            mat.setYoung_modulus(Double.parseDouble(attr[2]));
            mat.setDensity(Double.parseDouble(attr[3]));
            mat.setExtreme_force(Double.parseDouble(attr[4]));
            m_model.addMaterial(mat);
            lineTxt = m_br.readLine();
        }
	}
	
	
	/**
	 * id mess 
	 * coordinates: x y z
	 * OPTION restraints: x_disp x_rotate y_disp y_rotate z_disp z_rotate
	 * @throws IOException
	 */
	public void parseNode() throws IOException{
		lineTxt = m_br.readLine();
		while(!lineTxt.equals("Bar")){
            String[] attr = lineTxt.split(" ");
            int index = 0;
            VFIFE_Node nd = new VFIFE_Node(Integer.parseInt(attr[index++]));
            nd.setMass(Double.parseDouble(attr[index++]));
            nd.setCoord(new VFIFE_CartesianPoint(Double.parseDouble(attr[index++]), 
            		Double.parseDouble(attr[index++]), Double.parseDouble(attr[index++])));
            if(attr.length==11){ // has restraints
            	nd.setRestraint(new VFIFE_BoundaryConditionLogical(
            			attr[index++].equals("T"), attr[index++].equals("T"), attr[index++].equals("T"), 
            			attr[index++].equals("T"), attr[index++].equals("T"), attr[index++].equals("T")
            			));
            }
            m_model.addNode(nd);
            lineTxt = m_br.readLine();
        }
	}
	
	/**
	 * id sectionArea startNodeID endNodeID barMaterialID
	 * @throws IOException
	 */
	public void parseBar() throws IOException{
		lineTxt = m_br.readLine();
		while(!lineTxt.equals("Load")){
            String[] attr = lineTxt.split(" ");
            VFIFE_Bar ba = new VFIFE_Bar(Integer.parseInt(attr[0]));
            ba.setSection_area(Double.parseDouble(attr[1]));
            ba.setStart_node(m_model.getNode(Integer.parseInt(attr[2])));
            ba.setEnd_node(m_model.getNode(Integer.parseInt(attr[3])));
            if (attr.length==5) {// has material
				ba.setMaterial(m_model.getMaterial(Integer.parseInt(attr[4])));
			}
            m_model.addBar(ba);
            lineTxt = m_br.readLine();
        }
	}
	
	/**
	 * id startTime endTime
	 * className supportNodeID/SupportBarID (loadPosition: x y z)
	 * loadCaseClassName (rampDuration)
	 * appliedLoad: fx fy fz
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public void parseLoad() throws NumberFormatException, IOException{
		lineTxt = m_br.readLine();
		while(!lineTxt.equals("Model")){
		
            String[] attr = lineTxt.split(" ");
            int index=0;
            VFIFE_Load ld = new VFIFE_Load(Integer.parseInt(attr[index++]));
            ld.setStart_time(Double.parseDouble(attr[index++]));
            ld.setEnd_time(Double.parseDouble(attr[index++]));
            
            String clname = attr[index++];
            if(clname.contains("LoadNode")){
            	VFIFE_LoadNode ldnd = new VFIFE_LoadNode(ld.getId());
            	ldnd.setSupporting_node(m_model.getNode(Integer.parseInt(attr[index++])));
            	ld = ldnd;
            }
            else if(clname.contains("LoadBar")){
            	VFIFE_LoadBar ldba = new VFIFE_LoadBar(ld.getId());
            	ldba.setSupporting_bar(m_model.getBar(Integer.parseInt(attr[index++])));
            	ldba.setLoad_position(new VFIFE_CartesianPoint(Double.parseDouble(attr[index++]), 
            			Double.parseDouble(attr[index++]), Double.parseDouble(attr[index++])));
            	ld = ldba;
            }
            
            String loadcasename = attr[index++];
            if(loadcasename.contains("Variable")){
            	ld.setParent_load_case(new VFIFE_LoadCaseVariable());
            } else if(loadcasename.contains("Permanent")){
            	ld.setParent_load_case(new VFIFE_LoadCasePermanent());
            } else if(loadcasename.contains("Accidental")){
            	ld.setParent_load_case(new VFIFE_LoadCaseAccidental());
            } else if(loadcasename.contains("Ramp")){
            	ld.setParent_load_case(new VFIFE_LoadCaseRamp(Double.parseDouble(attr[index++])));
            }
            
            ld.setLoad_value(new VFIFE_AppliedLoad(Double.parseDouble(attr[index++]), 
            		Double.parseDouble(attr[index++]), Double.parseDouble(attr[index++])));
            
            m_model.addForce(ld);
            lineTxt = m_br.readLine();
        }
	}
	
	/**
	 * hasGravity calculateDuration timeInterval  
	 * @throws IOException
	 */
	public void parseModel() throws IOException{
		if((lineTxt=m_br.readLine())!=null){
			String[] attr = lineTxt.split(" ");
            int index=0;
            m_model.setHasGravity(attr[index++].equals("T"));
            m_model.setCalculate_duration(Double.parseDouble(attr[index++]));
            m_model.setCalculate_time_interval(Double.parseDouble(attr[index++]));
		}
	}
}
