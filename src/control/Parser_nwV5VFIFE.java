package control;

import java.util.ArrayList;

import jsdai.SGeometry_schema.EPyramid_volume;
import jsdai.SStructural_frame_schema.AElement_node_connectivity;
import jsdai.SStructural_frame_schema.AMaterial_isotropic;
import jsdai.SStructural_frame_schema.ARepresentation_item;
import jsdai.SStructural_frame_schema.ASection_profile;
import jsdai.SStructural_frame_schema.EElement_curve_complex;
import jsdai.SStructural_frame_schema.EElement_node_connectivity;
import jsdai.SStructural_frame_schema.EMaterial_elasticity;
import jsdai.SStructural_frame_schema.EMaterial_isotropic;
import jsdai.SStructural_frame_schema.EMaterial_representation;
import jsdai.SStructural_frame_schema.EPositive_length_measure_with_unit;
import jsdai.SStructural_frame_schema.ERepresentation_item;
import jsdai.SStructural_frame_schema.ESection_profile_circle;
import jsdai.SVfife_schema.AAnalysis_method;
import jsdai.SVfife_schema.ANode;
import jsdai.SVfife_schema.CApplied_load_static_force;
import jsdai.SVfife_schema.CBar;
import jsdai.SVfife_schema.CBoundary_condition_logical;
import jsdai.SVfife_schema.CCartesian_point;
import jsdai.SVfife_schema.CLoad_case;
import jsdai.SVfife_schema.CLoad_member_concentrated;
import jsdai.SVfife_schema.CLoad_node;
import jsdai.SVfife_schema.CMaterial;
import jsdai.SVfife_schema.CNode;
import jsdai.SVfife_schema.EAnalysis_method;
import jsdai.SVfife_schema.EApplied_load;
import jsdai.SVfife_schema.EApplied_load_static_force;
import jsdai.SVfife_schema.EBar;
import jsdai.SVfife_schema.EBoundary_condition_logical;
import jsdai.SVfife_schema.ECartesian_point;
import jsdai.SVfife_schema.ELoad;
import jsdai.SVfife_schema.ELoad_case;
import jsdai.SVfife_schema.ELoad_member_concentrated;
import jsdai.SVfife_schema.ELoad_node;
import jsdai.SVfife_schema.EMaterial;
import jsdai.SVfife_schema.ENode;
import jsdai.SVfife_schema.EPhysical_action;
import jsdai.SVfife_schema.EPhysical_action_accidental;
import jsdai.SVfife_schema.EPhysical_action_permanent;
import jsdai.SVfife_schema.EPhysical_action_seismic;
import jsdai.SVfife_schema.EPhysical_action_variable_long_term;
import jsdai.SVfife_schema.EPhysical_action_variable_short_term;
import jsdai.SVfife_schema.EPhysical_action_variable_transient;
import jsdai.lang.A_double;
import jsdai.lang.A_string;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;
import model.TYPE_direct_or_indirect_action;
import model.TYPE_spatial_variation;
import model.TYPE_static_or_dynamic;
import model.VFIFE_AppliedLoad;
import model.VFIFE_AppliedLoadStaticForce;
import model.VFIFE_Bar;
import model.VFIFE_Load;
import model.VFIFE_LoadCase;
import model.VFIFE_LoadMemberConcentrated;
import model.VFIFE_LoadNode;
import model.VFIFE_Material;
import model.VFIFE_Model;
import model.VFIFE_Node;
import model.VFIFE_PhysicalActionAccidental;
import model.VFIFE_PhysicalActionPermanent;
import model.VFIFE_PhysicalActionVariableLongTerm;
import model.VFIFE_PhysicalActionVariableShortTerm;
import model.VFIFE_PhysicalActionVariableTransient;
import model.VFIFE_AnalysisMethod;
import model.VFIFE_AppliedLoadStaticForce;
import model.VFIFE_Bar;
import model.VFIFE_BoundaryCondition;
import model.VFIFE_CartesianPoint;
import model.VFIFE_LoadCase;
import model.VFIFE_LoadMemberConcentrated;
import model.VFIFE_LoadNode;
import model.VFIFE_Material;
import model.VFIFE_Model;
import model.VFIFE_Node;
import model.VFIFE_PhysicalAction;
import model.VFIFE_PhysicalActionAccidental;
import model.VFIFE_PhysicalActionPermanent;
import model.VFIFE_PhysicalActionVariableLongTerm;
import model.VFIFE_PhysicalActionVariableShortTerm;
import model.VFIFE_PhysicalActionVariableTransient;

public class Parser_nwV5VFIFE {
	
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
	
	public Parser_nwV5VFIFE(){
		this.m_enodes = new ArrayList<ENode>();
		this.m_bars = new ArrayList<EBar>();
		this.m_materials = new ArrayList<EMaterial>();
	}
	
	
	
	//hahahahhahahaha
	public ArrayList<VFIFE_Node> parseNodes(SdaiModel model_v5,VFIFE_Model v5model) throws SdaiException{
	
		ArrayList<VFIFE_Node> v5nodes = new ArrayList<VFIFE_Node>();
		

		// deal with nodes in step
		jsdai.SVfife_schema.ANode nodes = (jsdai.SVfife_schema.ANode) model_v5.getInstances(ENode.class);
		SdaiIterator nodeIt = nodes.createIterator();
		while (nodeIt.next()) {
			
			ENode enode =  nodes.getCurrentMember(nodeIt);
			
			VFIFE_Node v5node = new VFIFE_Node();
			
			try {
				/////////////// v5node's id, name////////////////////////////
				
				v5node.setNode_id(Integer.parseInt(enode.getNode_name(null).trim()));
				
				v5node.setNode_name(enode.getNode_name(null).trim());

				//////////////// coords//////////////////////////	
				jsdai.SVfife_schema.ECartesian_point epoint = (jsdai.SVfife_schema.ECartesian_point) enode
						.getNode_coords(null);
				A_double coords = epoint.getCoordinates(null);
				SdaiIterator coordsIt = coords.createIterator();
				
				VFIFE_CartesianPoint v5point = new VFIFE_CartesianPoint();
				
				if (coordsIt.next()) {
					v5point.setCoordinate_x(coords.getCurrentMember(coordsIt));
				}
				if (coordsIt.next()) {
					v5point.setCoordinate_y(coords.getCurrentMember(coordsIt));
				}
				if (coordsIt.next()) {
					v5point.setCoordinate_z(coords.getCurrentMember(coordsIt));
				}
				
				v5node.setCoord(v5point);

				////////////////////// constraints///////////////////////
				
				VFIFE_BoundaryCondition v5boundary = new VFIFE_BoundaryCondition();

				jsdai.SVfife_schema.EBoundary_condition_logical eboundary = (jsdai.SVfife_schema.EBoundary_condition_logical) enode.getRestraints(enode);
				
				if (eboundary != null) {
					// 1==false, 2==true, 3==unknow
					v5boundary.setBc_x_displacement_free(eboundary.getBc_x_displacement_free(null) == 2);
					v5boundary.setBc_x_rotation_free(eboundary.getBc_x_rotation_free(null) == 2);
					v5boundary.setBc_y_displacement_free(eboundary.getBc_y_displacement_free(null) == 2);
					v5boundary.setBc_y_rotation_free(eboundary.getBc_y_rotation_free(null) == 2);
					v5boundary.setBc_z_displacement_free(eboundary.getBc_z_displacement_free(null) == 2);
					v5boundary.setBc_z_rotation_free(eboundary.getBc_z_rotation_free(null) == 2);

					v5boundary.setBoundary_condition_name(eboundary.getBoundary_condition_name(null));
					// v5boundary.setBoundary_condition_description(boundary.getBoundary_condition_description(null));

					v5node.setRestraint(v5boundary);
				}
				
			} catch (SdaiException e) {
				
			} finally{
				v5nodes.add(v5node);
			}
		}

		return v5nodes;
		
	}
	
	
	
	//hahahahhahahaha
	public ArrayList<VFIFE_Material> parseMaterials(VFIFE_Model v5model, SdaiModel model) throws SdaiException{

		/*
		// deal with materiaux of model
		for (VFIFE_Material material : v5model.getMateriaux()) {
			jsdai.SVfife_schema.EMaterial emat = (EMaterial) model.createEntityInstance(CMaterial.class);
			
			emat.setName(null, material.getName());
			
			emat.setDensity(null, material.getDensity());
			
			emat.setYoung_modulus(null, material.getYoung_modulus());
			
			m_materials.add(emat);
			
		}*/
		ArrayList<VFIFE_Material> v5materiaux = new ArrayList<VFIFE_Material>();
		
		// deal with material
		// problem, cannot tell which element use which material
		jsdai.SVfife_schema.AMaterial mats = (jsdai.SVfife_schema.AMaterial)model.getInstances(jsdai.SVfife_schema.EMaterial.class);
		SdaiIterator matIter = mats.createIterator();
		while (matIter.next()) {
			EMaterial mat = mats.getCurrentMember(matIter);
			
			VFIFE_Material v5material = new VFIFE_Material();
			
			// set name
			v5material.setName(mat.getName(null));
			
			//set density
			v5material.setDensity(mat.getDensity(null));
			
			//set
			v5material.setYoung_modulus(mat.getYoung_modulus(null));
			
			v5materiaux.add(v5material);
		}
		return v5materiaux;
	}
	
	
	
	
	//hahahahhahahaha
	public ArrayList<VFIFE_Load> parseLoads_r(VFIFE_Model v5model,SdaiModel model)throws SdaiException
	{
		//jsdai.SVfife_schema.ALoad_node load_nodes = (jsdai.SVfife_schema.ALoad_node)model.getInstances(jsdai.SVfife_schema.ELoad_node.class);
		//jsdai.SVfife_schema.ALoad_member_concentrated load_member_concentrateds = (jsdai.SVfife_schema.ALoad_member_concentrated)model.getInstances(jsdai.SVfife_schema.ELoad_member_concentrated.class);
		//jsdai.SVfife_schema.AApplied_load_static_force load2s = (jsdai.SVfife_schema.AApplied_load_static_force)model.getInstances(jsdai.SVfife_schema.EApplied_load_static_force.class);
		ArrayList<VFIFE_Load> v5loads = new ArrayList<VFIFE_Load>();
		jsdai.SVfife_schema.ALoad loads=(jsdai.SVfife_schema.ALoad)model.getInstances(jsdai.SVfife_schema.ELoad.class);
		SdaiIterator loadIter = loads.createIterator();
		while(loadIter.next())
		{

			jsdai.SVfife_schema.ELoad load = loads.getCurrentMember(loadIter); 
			
			VFIFE_Load v5load = new VFIFE_Load();
			
			//load name
			try{
			
				v5load.setForce_name(load.getLoad_name(null));
			
			}
			catch (SdaiException e)
			{
				if(e.getErrorId()==e.VA_NSET)
				{}
				else
				{
					throw e;
				}
					
			}
			//load case
			try
			{
				VFIFE_LoadCase v5loadcase = new VFIFE_LoadCase();
				jsdai.SVfife_schema.ELoad_case loadcase = load.getParent_load_case(null);
				v5loadcase.setLoad_case_name(loadcase.getLoad_case_name(null));
				v5loadcase.setLoad_case_factor(loadcase.getLoad_case_factor(null));
				//---------PhysicalAction
				try
				{
				jsdai.SVfife_schema.EPhysical_action physical_action = loadcase.getTime_variation(null);
				VFIFE_PhysicalAction v5physicalAction = new VFIFE_PhysicalAction();
				v5physicalAction.setAction_nature(TYPE_static_or_dynamic.valueOf(physical_action.getAction_nature(null)));
				v5physicalAction.setAction_spatial_variation(TYPE_spatial_variation.valueOf(physical_action.getAction_spatial_variation(null)));
				v5physicalAction.setAction_type(TYPE_direct_or_indirect_action.valueOf(physical_action.getAction_type(null)));
				//-----------------------derivation_factors
				try
				{
						A_double Derivation_factors =physical_action.getDerivation_factors(null);
						SdaiIterator DfactorIt = Derivation_factors.createIterator();
						ArrayList<Double> v5Derivation_factors = new ArrayList<Double>();
						while(DfactorIt.next())
						{
							v5Derivation_factors.add(Derivation_factors.getCurrentMember(DfactorIt));
						}
						v5physicalAction.setDerivation_factors(v5Derivation_factors);
				}
				catch (SdaiException e)
				{
					if(e.getErrorId()==e.VA_NSET)
					{}
					else
					{
					throw e;
					}
				}
			
				//-----------------------derivation_factor_labels
				try
				{
						A_string Derivation_factor_labels =physical_action.getDerivation_factor_labels(null);
						SdaiIterator DFLabelIt = Derivation_factor_labels.createIterator();
						ArrayList<String> v5Derivation_factor_labels = new ArrayList<String>();
						while(DFLabelIt.next())
						{
							v5Derivation_factor_labels.add(Derivation_factor_labels.getCurrentMember(DFLabelIt));
						}
						v5physicalAction.setDerivation_factor_labels(v5Derivation_factor_labels);
			
						v5loadcase.setTime_variation(v5physicalAction);
				}
				catch (SdaiException e)
				{
					if(e.getErrorId()==e.VA_NSET)
					{}
					else
					{
					throw e;
					}
				}
				}
				catch (SdaiException e)
				{
					if(e.getErrorId()==e.VA_NSET)
					{}
					else
					{
					throw e;
					}
				}
				//---------analysis method
				try
				{
						ArrayList<VFIFE_AnalysisMethod> v5analysisMethods = new ArrayList<VFIFE_AnalysisMethod>();
						AAnalysis_method analysisMethods=loadcase.getGoverning_analyses(null);
						SdaiIterator analysisMethodIt = analysisMethods.createIterator();
						while(analysisMethodIt.next())
						{
							VFIFE_AnalysisMethod v5analysisMethod= new VFIFE_AnalysisMethod();
							EAnalysis_method analysisMethod = analysisMethods.getCurrentMember(analysisMethodIt);
							v5analysisMethod.setAnalysis_assumptions(analysisMethod.getAnalysis_assumptions(null));
							v5analysisMethod.setAnalysis_name(analysisMethod.getAnalysis_name(null));
							v5analysisMethods.add(v5analysisMethod);
						}
						v5loadcase.setGoverning_analyses(v5analysisMethods);	
						v5load.setParent_load_case(v5loadcase);
				}
				catch (SdaiException e)
				{
					if(e.getErrorId()==e.VA_NSET)
					{}
					else
					{
					throw e;
					}
				}
			
			}
			catch (SdaiException e)
			{
				if(e.getErrorId()==e.VA_NSET)
				{}
				else
				{
					throw e;
				}
			}
			
			
			//start time
			try
			{
			v5load.setStart_time(load.getStart_time(null));
			}
			catch (SdaiException e)
			{
				if(e.getErrorId()==e.VA_NSET)
				{}
				else
				{
				throw e;
				}
			}
			//end time
			try
			{
			v5load.setEnd_time(load.getEnd_time(null));
			}
			catch (SdaiException e)
			{
				if(e.getErrorId()==e.VA_NSET)
				{}
				else
				{
				throw e;
				}
			}
			if(jsdai.SVfife_schema.CLoad_member_concentrated.class== load.getClass())
			{
				VFIFE_LoadMemberConcentrated v5loadin = new VFIFE_LoadMemberConcentrated(v5load);
				//VFIFE_LoadMemberConcentrated v5loadin = (VFIFE_LoadMemberConcentrated)v5load;
				
				jsdai.SVfife_schema.ELoad_member_concentrated loadin = (jsdai.SVfife_schema.ELoad_member_concentrated)load;
				
				//supporting member
				v5loadin.setSupporting_member(v5model.getBar(loadin.getSupporting_member(null).getBar_id(null)));
				
				
				
				//applied load
				VFIFE_AppliedLoadStaticForce v5applied_load = new VFIFE_AppliedLoadStaticForce();
				EApplied_load applied_load = loadin.getLoad_value(null);
				EApplied_load_static_force eloadvalue = (EApplied_load_static_force)applied_load;
			
				v5applied_load.setAppliedload_name(eloadvalue.getApplied_load_name(null));
				v5applied_load.setApplied_force_fx(eloadvalue.getApplied_force_fx(null));
				v5applied_load.setApplied_force_fy(eloadvalue.getApplied_force_fy(null));
				v5applied_load.setApplied_force_fz(eloadvalue.getApplied_force_fz(null));
				v5applied_load.setApplied_moment_mx(eloadvalue.getApplied_moment_mx(null));
				v5applied_load.setApplied_moment_my(eloadvalue.getApplied_moment_my(null));
				v5applied_load.setApplied_moment_mz(eloadvalue.getApplied_moment_mz(null));
				v5loadin.setLoad_value(v5applied_load);
				
				//load position
				ECartesian_point cartesian_point =  loadin.getLoad_position(null);
				VFIFE_CartesianPoint cartesian_pointv5 = new VFIFE_CartesianPoint();
				A_double coord = cartesian_point.getCoordinates(null);
				SdaiIterator coIter = coord.createIterator();
				coIter.next();
				cartesian_pointv5.setCoordinate_x(coord.getCurrentMember(coIter));
				coIter.next();
				cartesian_pointv5.setCoordinate_y(coord.getCurrentMember(coIter));
				coIter.next();
				cartesian_pointv5.setCoordinate_z(coord.getCurrentMember(coIter));
				
				v5loadin.setLoad_position(cartesian_pointv5);
				
				v5loads.add(v5loadin);
			}
			else if(jsdai.SVfife_schema.CLoad_node.class==load.getClass())
			{
				VFIFE_LoadNode v5loadin = new VFIFE_LoadNode(v5load);
				jsdai.SVfife_schema.ELoad_node loadin=(jsdai.SVfife_schema.ELoad_node)load;
				//applied load
				VFIFE_AppliedLoadStaticForce v5applied_load = new VFIFE_AppliedLoadStaticForce();
				
				//supporting member
				
				v5loadin.setSupporting_node(v5model.getNode(loadin.getSupporting_node(null).getNode_id(null)));
				
				
				//loadin.getLoad_values(null);
				EApplied_load applied_load = loadin.getLoad_values(null);
				EApplied_load_static_force eloadvalue = (EApplied_load_static_force)applied_load;
			
				v5applied_load.setAppliedload_name(eloadvalue.getApplied_load_name(null));
				v5applied_load.setApplied_force_fx(eloadvalue.getApplied_force_fx(null));
				v5applied_load.setApplied_force_fy(eloadvalue.getApplied_force_fy(null));
				v5applied_load.setApplied_force_fz(eloadvalue.getApplied_force_fz(null));
				v5applied_load.setApplied_moment_mx(eloadvalue.getApplied_moment_mx(null));
				v5applied_load.setApplied_moment_my(eloadvalue.getApplied_moment_my(null));
				v5applied_load.setApplied_moment_mz(eloadvalue.getApplied_moment_mz(null));
				v5loadin.setLoad_values(v5applied_load);
				
				
				
				v5loads.add(v5loadin);
				
			}
			else
			{
				v5loads.add(v5load);
			}
			

			
		}
		
		return v5loads;
		
	}
	

	
	
	//hahahahhahahaha
	public void parseBars(SdaiModel model,VFIFE_Model v5model) throws SdaiException{
		// v5-bar
		//ArrayList<VFIFE_Bar> v5bars = new ArrayList<VFIFE_Bar>();
		
		// deal with element-node-connectivity
		
		jsdai.SVfife_schema.ABar bars = (jsdai.SVfife_schema.ABar) model.getInstances(EBar.class);
		SdaiIterator barIt = bars.createIterator();
		ArrayList<VFIFE_Bar> v5bars = new ArrayList<VFIFE_Bar>();
		while (barIt.next()) {
			EBar bar =  bars.getCurrentMember(barIt);
			
			VFIFE_Bar v5bar = new VFIFE_Bar();
			try{
				
				//bar_id
				v5bar.setBar_id(bar.getBar_id(null));
				
				//material
				VFIFE_Material v5material = new VFIFE_Material();
				
				try
				{
				EMaterial material=bar.getBar_material(null);
				v5material.setName(material.getName(null));
				v5material.setDensity(material.getDensity(null));
				v5material.setYoung_modulus(material.getYoung_modulus(null));
				v5bar.setMaterial(v5material);
				}
				catch(SdaiException e)
				{
					v5bar.setMaterial(null);	
				}
				
				//section_area
				v5bar.setSection_area(bar.getSection_area(null));
				
				//nodes
				jsdai.SVfife_schema.ANode nodes=bar.getBar_node(null);
				ArrayList<VFIFE_Node> nodes_bar=new ArrayList<VFIFE_Node>();
				SdaiIterator nodeIt = nodes.createIterator();
				while(nodeIt.next())
				{
					ENode node =nodes.getCurrentMember(nodeIt);
					nodes_bar.add(v5model.getNode(node.getNode_id(null)));
				}
				v5bar.setNodes(nodes_bar);
			}
			catch(SdaiException e){
				int i=0;
				i++;
				
				
			}
			v5bars.add(v5bar);
		}
		
		
		v5model.setBars(v5bars);
		return;
	}


	

	
	

}
