package modeling;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dataStructure.VFIFE_Model;
import dataStructure.entity.VFIFE_Bar;
import dataStructure.entity.VFIFE_BoundaryConditionLogical;
import dataStructure.entity.VFIFE_CartesianPoint;
import dataStructure.entity.VFIFE_Load;
import dataStructure.entity.VFIFE_LoadBar;
import dataStructure.entity.VFIFE_LoadCaseRamp;
import dataStructure.entity.VFIFE_LoadNode;
import dataStructure.entity.VFIFE_Material;
import dataStructure.entity.VFIFE_Node;


public class Parser_ExportV5M {

	private VFIFE_Model m_model;
	private BufferedWriter m_bw;
	
	public Parser_ExportV5M(VFIFE_Model v5model,BufferedWriter bw) {
		super();
		this.m_model = v5model;
		this.m_bw = bw;
	}
   
	public void writeHeader() throws IOException{
		m_bw.write("VFIFE MODEL FILE\n");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//date format
		m_bw.append("Date : "+df.format(new Date())+"\n");
	}
	
	/**
	 * id name youngsModulus density extremeForce
	 * @throws IOException
	 */
	public void writeMaterial() throws IOException{
		m_bw.append("Material\n");
		for(VFIFE_Material mat : m_model.getMateriaux()){
			m_bw.append(mat.getId()+" "+mat.getName()+" "+mat.getYoung_modulus()+" "+mat.getDensity()+" "+mat.getExtreme_force()+"\n");
		}
	}
	
	/**
	 * id mess 
	 * coordinates: x y z
	 * restraints: x_disp x_rotate y_disp y_rotate z_disp z_rotate
	 * @throws IOException
	 */
	public void writeNode() throws IOException{
		m_bw.append("Node\n");
		for(VFIFE_Node nd : m_model.getNodes()){
			m_bw.append(nd.getNode_id()+" "+nd.getMass()+" ");
			
			VFIFE_CartesianPoint cp = nd.getCoord();
			m_bw.append(cp.getCoordinate_x()+" "+cp.getCoordinate_y()+" "+cp.getCoordinate_z()+" ");
		
			VFIFE_BoundaryConditionLogical bc = nd.getRestraint();
			if(bc!=null){
				m_bw.append(bc.getBc_x_displacement_free() ? "T " : "F ");
				m_bw.append(bc.getBc_x_rotation_free() ? "T " : "F ");
				m_bw.append(bc.getBc_y_displacement_free() ? "T " : "F ");
				m_bw.append(bc.getBc_y_rotation_free() ? "T " : "F ");
				m_bw.append(bc.getBc_z_displacement_free() ? "T " : "F ");
				m_bw.append(bc.getBc_z_rotation_free() ? "T " : "F");
			}
			
			m_bw.append("\n");
		}
	}
	
	/**
	 * id sectionArea startNodeID endNodeID barMaterialID
	 * @throws IOException
	 */
	public void writeBar() throws IOException {
		m_bw.append("Bar\n");
		for(VFIFE_Bar ba : m_model.getBars()){
			m_bw.append(ba.getBar_id()+" "+ba.getSection_area()+" "+ba.getStart_node().getNode_id()+" "
					+ba.getEnd_node().getNode_id()+" ");
			if(ba.getMaterial()!=null){
				m_bw.append(ba.getMaterial().getId()+" ");
			}
			
			m_bw.append("\n");
		}
	}
	
	/**
	 * id startTime endTime
	 * className supportNodeID/SupportBarID (loadPosition: x y z)
	 * loadCaseClassName (rampDuration)
	 * appliedLoad: fx fy fz
	 * @throws IOException
	 */
	public void writeLoad() throws IOException {
		m_bw.append("Load\n");
		for(VFIFE_Load ld : m_model.getForces()){
			m_bw.append(ld.getId()+" "+ ld.getStart_time()+" "+ld.getEnd_time()+" ");
			
			String clName = ld.getClass().getSimpleName();
			m_bw.append(clName+" ");
			if(clName.contains("LoadNode")){
				m_bw.append(((VFIFE_LoadNode)ld).getSupporting_node().getNode_id()+" ");
			}else if(clName.contains("LoadBar")){
				m_bw.append(((VFIFE_LoadBar)ld).getSupporting_bar().getBar_id()+" "+
						((VFIFE_LoadBar)ld).getLoad_position().getCoordinate_x()+" "+
						((VFIFE_LoadBar)ld).getLoad_position().getCoordinate_y()+" "+
						((VFIFE_LoadBar)ld).getLoad_position().getCoordinate_z()+" ");
			}
			
			String loadcaseClName = ld.getParent_load_case().getClass().getSimpleName(); 
			m_bw.append(loadcaseClName+" ");
			if(loadcaseClName.contains("LoadCaseRamp")){
				m_bw.append(((VFIFE_LoadCaseRamp)ld.getParent_load_case()).getRamp_duration()+" ");
			}
			
			m_bw.append(ld.getLoad_value().getApplied_force_fx()+" "+ld.getLoad_value().getApplied_force_fy()+" "
					+ld.getLoad_value().getApplied_force_fz()+"\n");
		}
	}
	
	/**
	 * hasGravity calculateDuration timeInterval  
	 * @throws IOException
	 */
	public void writeModel() throws IOException{
		m_bw.append("Model\n");
		m_bw.append(m_model.isHasGravity() ? "T " : "F ");
		m_bw.append(m_model.getCalculate_duration()==0 ? "50 " : (m_model.getCalculate_duration()+" "));
		m_bw.append(m_model.getCalculate_time_interval()==0 ? "1 " : (m_model.getCalculate_time_interval() +"\n"));
	}
}
