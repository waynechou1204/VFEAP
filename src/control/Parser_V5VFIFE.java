package control;

import java.util.ArrayList;

import jsdai.SVfife_schema.ANode;
import jsdai.SVfife_schema.CApplied_load_static_force;
import jsdai.SVfife_schema.CBar;
import jsdai.SVfife_schema.CBoundary_condition_logical;
import jsdai.SVfife_schema.CCartesian_point;
import jsdai.SVfife_schema.CLoad_case;
import jsdai.SVfife_schema.CLoad_element_concentrated;
import jsdai.SVfife_schema.CLoad_node;
import jsdai.SVfife_schema.CMaterial;
import jsdai.SVfife_schema.CNode;
import jsdai.SVfife_schema.EBar;
import jsdai.SVfife_schema.ECartesian_point;
import jsdai.SVfife_schema.ELoad_element_concentrated;
import jsdai.SVfife_schema.ELoad_node;
import jsdai.SVfife_schema.EMaterial;
import jsdai.SVfife_schema.ENode;
import jsdai.lang.A_double;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;
import model.VFIFE_AppliedLoadStaticForce;
import model.VFIFE_Bar;
import model.VFIFE_Load;
import model.VFIFE_LoadMemberConcentrated;
import model.VFIFE_LoadNode;
import model.VFIFE_Material;
import model.VFIFE_Model;
import model.VFIFE_Node;

public class Parser_V5VFIFE {
	
	private ArrayList<ENode> m_enodes;
	private ENode getENode(int nodeid) throws SdaiException{
		for(ENode d : m_enodes){
			if(d.getNode_id(null)==nodeid){
				return d;
			}
		}
		return null;
	}
	
	private ArrayList<EMaterial> m_materials;
	private EMaterial getEMaterial(String matname) throws SdaiException{
		for(EMaterial m : m_materials){
			if(m.getName(null).equals(matname)){
				return m;
			}
		}
		return null;
	}
	
	private ArrayList<EBar> m_bars;
	private EBar getEBar(int barid) throws SdaiException{
		for(EBar b : m_bars){
			if(b.getBar_id(null)==barid){
				return b;
			}
		}
		return null;
	}
	
	public Parser_V5VFIFE(){
		this.m_enodes = new ArrayList<ENode>();
		this.m_bars = new ArrayList<EBar>();
		this.m_materials = new ArrayList<EMaterial>();
	}
	
	public void parseNodes(VFIFE_Model v5model, SdaiModel model) throws SdaiException{
	
		for(VFIFE_Node node : v5model.getNodes()){
			
			jsdai.SVfife_schema.ENode enode = (jsdai.SVfife_schema.ENode) model.createEntityInstance(CNode.class);
			
			enode.setNode_id(null, node.getNode_id());
			enode.setNode_name(null, node.getNode_name());

			// coordinates of node
			jsdai.SVfife_schema.ECartesian_point epoint = (ECartesian_point) model.createEntityInstance(CCartesian_point.class);
			epoint.createCoordinates(null);
			
			A_double coords = epoint.getCoordinates(null);
			coords.addByIndex(1, node.getCoord().getCoordinate_x());
			coords.addByIndex(2, node.getCoord().getCoordinate_y());
			coords.addByIndex(3, node.getCoord().getCoordinate_z());

			enode.setNode_coords(null, epoint);

			// boundary condition of node
			if (node.getRestraint() != null) {
				jsdai.SVfife_schema.EBoundary_condition_logical erestrains = (jsdai.SVfife_schema.EBoundary_condition_logical) model
						.createEntityInstance(CBoundary_condition_logical.class);
				
				erestrains.setBoundary_condition_name(null, node.getRestraint().getBoundary_condition_name());
				// erestrains.setBoundary_condition_description(null,
				// node.getRestraint().getBoundary_condition_description());

				erestrains.setBc_x_displacement_free(null, (node.getRestraint().getBc_x_displacement_free()) ? 2 : 1);
				erestrains.setBc_x_rotation_free(null, (node.getRestraint().getBc_x_rotation_free()) ? 2 : 1);
				erestrains.setBc_y_displacement_free(null, (node.getRestraint().getBc_y_displacement_free()) ? 2 : 1);
				erestrains.setBc_y_rotation_free(null, (node.getRestraint().getBc_y_rotation_free()) ? 2 : 1);
				erestrains.setBc_z_displacement_free(null, (node.getRestraint().getBc_z_displacement_free()) ? 2 : 1);
				erestrains.setBc_z_rotation_free(null, (node.getRestraint().getBc_z_rotation_free()) ? 2 : 1);

				enode.setRestraints(null, erestrains);
			}
			
			m_enodes.add(enode);
		}
		
	}
	
	public void parseMaterials(VFIFE_Model v5model, SdaiModel model) throws SdaiException{

		// deal with materiaux of model
		for (VFIFE_Material material : v5model.getMateriaux()) {
			jsdai.SVfife_schema.EMaterial emat = (EMaterial) model.createEntityInstance(CMaterial.class);
			
			emat.setName(null, material.getName());
			
			emat.setDensity(null, material.getDensity());
			
			emat.setYoung_modulus(null, material.getYoung_modulus());
			
			m_materials.add(emat);
		}
	}
	
	public void parseBars(VFIFE_Model v5model, SdaiModel model) throws SdaiException{
		
		// deal with bars
		for (VFIFE_Bar bar : v5model.getBars()) {
			EBar ebar = (EBar) model.createEntityInstance(CBar.class);

			ebar.setBar_id(null, bar.getBar_id());
			ebar.setSection_area(null, bar.getSection_area());

			// deal with nodes of bar
			ebar.createBar_node(null);
			for (VFIFE_Node node : bar.getNodes()) {
				
				ENode enode = getENode(node.getNode_id());
				
				ANode anodes = ebar.getBar_node(null);
				SdaiIterator nodeit = anodes.createIterator();
				nodeit.end();
				anodes.addAfter(nodeit, enode);
			}

			// deal with materail of bar
			if (bar.getMaterial() != null) {
				
				EMaterial emat = getEMaterial(bar.getMaterial().getName());
				
				ebar.setBar_material(null, emat);
			}
			
			m_bars.add(ebar);
		}

	}

	public void parseLoads(VFIFE_Model v5model, SdaiModel model) throws SdaiException{
		
		// deal with force of model
		for (VFIFE_Load force : v5model.getForces()) {
			
			// node load force
			if(force.getClass().toString().contains("VFIFE_LoadNode")){
				
				VFIFE_LoadNode v5nodeforce = (VFIFE_LoadNode) force;
				
				jsdai.SVfife_schema.ELoad_node eloadnode = (ELoad_node) model.createEntityInstance(CLoad_node.class);
				
				// set load name
				eloadnode.setLoad_name(null, force.getForce_name());
				
				// set load duration
				eloadnode.setDuration_time(null, force.getDuration_time());
				
				// load case of force
				jsdai.SVfife_schema.ELoad_case eloadcase = (jsdai.SVfife_schema.ELoad_case) model.createEntityInstance(CLoad_case.class);
				eloadcase.setLoad_case_name(null, force.getParent_load_case().getLoad_case_name());
				eloadcase.setLoad_case_factor(null, force.getParent_load_case().getLoad_case_factor());
				eloadcase.setTime_variation(null, force.getParent_load_case().getTime_variation());

				eloadnode.setParent_load_case(null, eloadcase);

			
				// specific to loadNode
					// supporting node
				ENode enode = getENode(v5nodeforce.getSupporting_node().getNode_id());
				eloadnode.setSupporting_node(null, enode);
				
					// applied load force with value and type
				jsdai.SVfife_schema.EApplied_load_static_force eloadvalue = (jsdai.SVfife_schema.EApplied_load_static_force) model
						.createEntityInstance(CApplied_load_static_force.class);
					//set applied load name
				eloadvalue.setApplied_load_name(null, v5nodeforce.getLoad_values().getAppliedload_name());
				VFIFE_AppliedLoadStaticForce staticforce = (VFIFE_AppliedLoadStaticForce) v5nodeforce.getLoad_values();

				eloadvalue.setApplied_force_fx(null,staticforce.getApplied_force_fx());
				eloadvalue.setApplied_force_fy(null,staticforce.getApplied_force_fy());
				eloadvalue.setApplied_force_fz(null,staticforce.getApplied_force_fz());

				eloadvalue.setApplied_moment_mx(null,staticforce.getApplied_moment_mx());
				eloadvalue.setApplied_moment_my(null,staticforce.getApplied_moment_my());
				eloadvalue.setApplied_moment_mz(null,staticforce.getApplied_moment_mz());

				eloadnode.setLoad_values(null, eloadvalue);
			}
			else if(force.getClass().toString().contains("VFIFE_LoadMemberConcentrated")){
				
				VFIFE_LoadMemberConcentrated v5memforce = (VFIFE_LoadMemberConcentrated) force;
				
				jsdai.SVfife_schema.ELoad_element_concentrated eload = (ELoad_element_concentrated) model.createEntityInstance(CLoad_element_concentrated.class);
				
				// set load name
				eload.setLoad_name(null, force.getForce_name());
				
				// set load duration
				eload.setDuration_time(null, force.getDuration_time());
				
				// load case of force
				jsdai.SVfife_schema.ELoad_case eloadcase = (jsdai.SVfife_schema.ELoad_case) model.createEntityInstance(CLoad_case.class);
				eloadcase.setLoad_case_name(null, force.getParent_load_case().getLoad_case_name());
				eloadcase.setLoad_case_factor(null, force.getParent_load_case().getLoad_case_factor());
				eloadcase.setTime_variation(null, force.getParent_load_case().getTime_variation());

				eload.setParent_load_case(null, eloadcase);

			
				// specific to loadNode
					//supporting bar
				EBar ebar = getEBar(v5memforce.getSupporting_member().getBar_id());
				eload.setSupporting_element(null, ebar);
					// applied load force with value and type
				jsdai.SVfife_schema.EApplied_load_static_force eloadvalue = (jsdai.SVfife_schema.EApplied_load_static_force) model
						.createEntityInstance(CApplied_load_static_force.class);
						
						//set applied load name
				eloadvalue.setApplied_load_name(null, v5memforce.getLoad_value().getAppliedload_name());
				VFIFE_AppliedLoadStaticForce staticforce = (VFIFE_AppliedLoadStaticForce) v5memforce.getLoad_value();
	
				eloadvalue.setApplied_force_fx(null,staticforce.getApplied_force_fx());
				eloadvalue.setApplied_force_fy(null,staticforce.getApplied_force_fy());
				eloadvalue.setApplied_force_fz(null,staticforce.getApplied_force_fz());
	
				eloadvalue.setApplied_moment_mx(null,staticforce.getApplied_moment_mx());
				eloadvalue.setApplied_moment_my(null,staticforce.getApplied_moment_my());
				eloadvalue.setApplied_moment_mz(null,staticforce.getApplied_moment_mz());
	
				eload.setLoad_value(null, eloadvalue);
					
					// set load position
					// coordinates of node
				jsdai.SVfife_schema.ECartesian_point epoint = (ECartesian_point) model.createEntityInstance(CCartesian_point.class);
				epoint.createCoordinates(null);
				A_double coords = epoint.getCoordinates(null);

				coords.addByIndex(1, v5memforce.getLoad_position().getCoordinate_x());
				coords.addByIndex(2, v5memforce.getLoad_position().getCoordinate_y());
				coords.addByIndex(3, v5memforce.getLoad_position().getCoordinate_z());

				eload.setLoad_position(null, epoint);

			}
		}
	}
}
