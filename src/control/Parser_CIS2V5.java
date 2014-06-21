package control;

import java.util.ArrayList;

import jsdai.SStructural_frame_schema.AElement_node_connectivity;
import jsdai.SStructural_frame_schema.ALoad_member_concentrated;
import jsdai.SStructural_frame_schema.ALoad_node;
import jsdai.SStructural_frame_schema.AMaterial_isotropic;
import jsdai.SStructural_frame_schema.ARepresentation_item;
import jsdai.SStructural_frame_schema.ASection_profile;
import jsdai.SStructural_frame_schema.CLoad_node;
import jsdai.SStructural_frame_schema.EAnalysis_model;
import jsdai.SStructural_frame_schema.EApplied_load_static_force;
import jsdai.SStructural_frame_schema.EAssembly_design_structural_member_linear;
import jsdai.SStructural_frame_schema.EBoundary_condition_logical;
import jsdai.SStructural_frame_schema.EElement_curve_complex;
import jsdai.SStructural_frame_schema.EElement_node_connectivity;
import jsdai.SStructural_frame_schema.EForce_measure_with_unit;
import jsdai.SStructural_frame_schema.ELoad_case;
import jsdai.SStructural_frame_schema.ELoad_member_concentrated;
import jsdai.SStructural_frame_schema.ELoad_node;
import jsdai.SStructural_frame_schema.EMaterial_elasticity;
import jsdai.SStructural_frame_schema.EMaterial_isotropic;
import jsdai.SStructural_frame_schema.EMaterial_representation;
import jsdai.SStructural_frame_schema.ENode;
import jsdai.SStructural_frame_schema.EPhysical_action;
import jsdai.SStructural_frame_schema.EPhysical_action_accidental;
import jsdai.SStructural_frame_schema.EPhysical_action_permanent;
import jsdai.SStructural_frame_schema.EPhysical_action_variable_long_term;
import jsdai.SStructural_frame_schema.EPhysical_action_variable_short_term;
import jsdai.SStructural_frame_schema.EPhysical_action_variable_transient;
import jsdai.SStructural_frame_schema.EPositive_length_measure_with_unit;
import jsdai.SStructural_frame_schema.ERepresentation_item;
import jsdai.SStructural_frame_schema.ESection_profile_circle;
import jsdai.SStructural_frame_schema.ESi_unit;
import jsdai.lang.A_double;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;
import model.TYPE_action_source_accidential;
import model.TYPE_action_source_permanent;
import model.TYPE_action_source_variable_long_term;
import model.TYPE_action_source_variable_short_term;
import model.TYPE_action_source_variable_transient;
import model.TYPE_direct_or_indirect_action;
import model.TYPE_spatial_variation;
import model.TYPE_static_or_dynamic;
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

public class Parser_CIS2V5 {

	public ArrayList<VFIFE_Node> parseNodes(SdaiModel model_cis, VFIFE_Model v5model) throws SdaiException {

		
		ArrayList<VFIFE_Node> v5nodes = new ArrayList<VFIFE_Node>();

		// deal with nodes in step
		jsdai.SStructural_frame_schema.ANode nodes = (jsdai.SStructural_frame_schema.ANode) model_cis.getInstances(ENode.class);
		SdaiIterator nodeIt = nodes.createIterator();
		while (nodeIt.next()) {
			
			ENode enode =  nodes.getCurrentMember(nodeIt);
			
			VFIFE_Node v5node = new VFIFE_Node();
			
			try {
				/////////////// v5node's id, name////////////////////////////
				
				v5node.setNode_id(Integer.parseInt(enode.getNode_name(null).trim()));
				
				v5node.setNode_name(enode.getNode_name(null).trim());

				//////////////// coords//////////////////////////
				
				jsdai.SStructural_frame_schema.ECartesian_point epoint = (jsdai.SStructural_frame_schema.ECartesian_point) enode
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

				EBoundary_condition_logical eboundary = (EBoundary_condition_logical) enode.getRestraints(enode);
				
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

	public void parseBars(SdaiModel model_cis, VFIFE_Model v5model) throws SdaiException {
		
		// v5-bar
		//ArrayList<VFIFE_Bar> v5bars = new ArrayList<VFIFE_Bar>();
		
		// deal with element-node-connectivity
		AElement_node_connectivity connects = (AElement_node_connectivity) model_cis.getInstances(EElement_node_connectivity.class);
		SdaiIterator connectIter = connects.createIterator();
		while (connectIter.next()) {
			EElement_node_connectivity connect = connects.getCurrentMember(connectIter);

			// get one of the 2 nodes
			ENode node = connect.getConnecting_node(null);
			VFIFE_Node v5node = v5model.getNode(Integer.parseInt(node.getNode_name(null).trim()));
			
			// deal with element
			try {
				EElement_curve_complex element = (EElement_curve_complex) connect.getConnecting_element(null);

				// search bar with this id
				VFIFE_Bar v5bar = v5model.getBar(Integer.parseInt(element.getElement_name(null).trim()));

				// the bar does not exist in model
				if (v5bar == null) {
					
					v5bar = new VFIFE_Bar();
					
					// set bar id
					v5bar.setBar_id(Integer.parseInt(element.getElement_name(null).trim()));
					
					// space dimension
					//EAnalysis_model anamodel = element.getParent_model(null);
					
					// deal with section circle hollow
					ASection_profile sections = element.getCross_sections(null);
					SdaiIterator sectionIter = sections.createIterator();
					while (sectionIter.next()) {
						ESection_profile_circle section = (ESection_profile_circle) sections.getCurrentMember(sectionIter);

						// external radius
						// bar's section area
						EPositive_length_measure_with_unit radius = section.getExternal_radius(null);
						double r = radius.get_double(radius.getAttributeDefinition("value_component"));
						
						// set bar section area
						v5bar.setSection_area(Math.PI * r * r);

						// unit
						//ESi_unit unit = (ESi_unit) radius.getUnit_component(null);
						break;
					}

					// add bar into bars
					//v5bars.add(v5bar);
					v5model.getBars().add(v5bar);
				}
				
				// add node into bar
				v5bar.getNodes().add(v5node);

			} catch (SdaiException e) {

			}

		}
		return;
	}

	public ArrayList<VFIFE_Material> parseMaterials(SdaiModel model_cis, VFIFE_Model v5model) throws SdaiException
	{
		ArrayList<VFIFE_Material> v5materiaux = new ArrayList<VFIFE_Material>();
		
		// deal with material
		// problem, cannot tell which element use which material
		AMaterial_isotropic mats = (AMaterial_isotropic) model_cis.getInstances(EMaterial_isotropic.class);
		SdaiIterator matIter = mats.createIterator();
		while (matIter.next()) {
			EMaterial_isotropic mat = mats.getCurrentMember(matIter);
			
			VFIFE_Material v5material = new VFIFE_Material();
			
			// set name
			v5material.setName(mat.getMaterial_name(null));

			EMaterial_representation matrep = mat.getDefinition(null);
			ARepresentation_item repitems = matrep.getItems(null);
			SdaiIterator itemIter = repitems.createIterator();
			while (itemIter.next()) {
				ERepresentation_item item = repitems.getCurrentMember(itemIter);
				if (item.getInstanceType().getName(null).equals("material_elasticity")) {
					EMaterial_elasticity matela = (EMaterial_elasticity) item;

					// set yound modulus
					v5material.setYoung_modulus(matela.getYoung_modulus(null));

					v5materiaux.add(v5material);
				}
			}
		}
		return v5materiaux;
	}

	// parse load of member concentrated to v5model
	public ArrayList<VFIFE_LoadMemberConcentrated> parseLoadMemberCon(SdaiModel model_cis, VFIFE_Model v5model) throws SdaiException{
		
		ArrayList<VFIFE_LoadMemberConcentrated> v5forces_member = new ArrayList<VFIFE_LoadMemberConcentrated>();
		
		// deal with concentrated forces
		// load_member(or element)_concentrated
		ALoad_member_concentrated concens = (ALoad_member_concentrated) model_cis.getInstances(ELoad_member_concentrated.class);
		SdaiIterator concenIter = concens.createIterator();
		while (concenIter.next()) {
			ELoad_member_concentrated concen = concens.getCurrentMember(concenIter);
			
			//////////////// set force name - identity
			VFIFE_LoadMemberConcentrated v5forcemember = new VFIFE_LoadMemberConcentrated();
			v5forcemember.setForce_name((concen.getLoad_name(null).trim()));

			
			//////////////// start time and end time are empty
			
			
			/////////////// set supporting member - bar // deal with assembly_design_structural_member_linear
			EAssembly_design_structural_member_linear supmember = (EAssembly_design_structural_member_linear) concen.getSupporting_member(null);
			v5forcemember.setSupporting_member(v5model.getBar(Integer.parseInt(supmember.getItem_name(null).trim())));
	
			
			//////////////// deal with load position coords
			jsdai.SStructural_frame_schema.ECartesian_point loadpos = (jsdai.SStructural_frame_schema.ECartesian_point) concen.getLoad_position(null);
			A_double coords = loadpos.getCoordinates(null);
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
			v5forcemember.setLoad_position(v5point);			

			
			///////////////// set load value
			EApplied_load_static_force appforce = (EApplied_load_static_force) concen.getLoad_value(null);
			// v5 applied force name
			VFIFE_AppliedLoadStaticForce v5appliedforce = new VFIFE_AppliedLoadStaticForce();
			v5appliedforce.setAppliedload_name(appforce.getApplied_load_name(null));
			try {
				// directions and force values
				EForce_measure_with_unit forcemes = appforce.getApplied_force_fx(null);
				v5appliedforce.setApplied_force_fx(forcemes.get_double(forcemes.getAttributeDefinition("value_component")));
			} catch (SdaiException e) { }
			try {
				EForce_measure_with_unit forcemes = appforce.getApplied_force_fy(null);
				v5appliedforce.setApplied_force_fy(forcemes.get_double(forcemes.getAttributeDefinition("value_component")));
			} catch (SdaiException e) {	}
			try {
				EForce_measure_with_unit forcemes = appforce.getApplied_force_fz(null);
				v5appliedforce.setApplied_force_fz(forcemes.get_double(forcemes.getAttributeDefinition("value_component")));
				// unit
				//ESi_unit unit = (ESi_unit) forcemes.getUnit_component(null);				
			} catch (SdaiException e) {	}
			v5forcemember.setLoad_value(v5appliedforce);			
			
			
			///////////////// deal with load case - load type
			ELoad_case eloadcase = concen.getParent_load_case(null);
			v5forcemember.setParent_load_case(this.parseLoadCase(eloadcase));

			///////////////// fin
			v5forces_member.add(v5forcemember);
		}
		
		return v5forces_member;
		
	}

	public ArrayList<VFIFE_LoadNode> parseLoadNode(SdaiModel model_cis, VFIFE_Model v5model) throws SdaiException{
		
		ArrayList<VFIFE_LoadNode> v5forces_node = new ArrayList<VFIFE_LoadNode>();
		
		// deal with node forces
		// load_node
		ALoad_node nodeforces = (ALoad_node) model_cis.getInstances(CLoad_node.class);
		SdaiIterator nodeforceIt = nodeforces.createIterator();
		while (nodeforceIt.next()) {
			ELoad_node nodeforce = nodeforces.getCurrentMember(nodeforceIt);
			
			// force id
			VFIFE_LoadNode v5force = new VFIFE_LoadNode();
			v5force.setForce_name((nodeforce.getLoad_name(null).trim()));

			// deal with load type
			ELoad_case loadcase = nodeforce.getParent_load_case(null);
			VFIFE_LoadCase v5loadcase = new VFIFE_LoadCase();
			v5loadcase.setLoad_case_name(loadcase.getLoad_case_name(null));
			// v5loadcase.setLoad_case_factor(loadcase.getLoad_case_factor(null));
			
			//TODO the time variation has been changed
			//v5loadcase.setTime_variation(loadcase.getTime_variation(null).getAction_nature(null));

			// set load case type
			v5force.setParent_load_case(v5loadcase);

			// deal with supporting element
			// EElement_curve_complex supelement = (EElement_curve_complex)
			// concen.getSupporting_element(null);
			// System.out.println("\tElement name: "+supelement.getElement_name(null));

			// deal with supporting node 
			jsdai.SStructural_frame_schema.ENode supnode = (ENode) nodeforce.getSupporting_node(null);
			
			// set supporting node
			v5force.setSupporting_node(v5model.getNode(Integer.parseInt(supnode.getNode_name(null).trim())));

			// load value
			EApplied_load_static_force appforce = (EApplied_load_static_force) nodeforce.getLoad_values(null);

			// v5 applied force name
			VFIFE_AppliedLoadStaticForce v5appliedforce = new VFIFE_AppliedLoadStaticForce();
			v5appliedforce.setAppliedload_name(appforce.getApplied_load_name(null));

			try {
				// directions and force values
				EForce_measure_with_unit forcemes = appforce.getApplied_force_fx(null);
				v5appliedforce.setApplied_force_fx(forcemes.get_double(forcemes.getAttributeDefinition("value_component")));

				// unit
				//ESi_unit unit = (ESi_unit) forcemes.getUnit_component(null);
				//System.out.println("\t\tUnit: "+ ESi_unit_name.toString(unit.getName(null)));
			} catch (SdaiException e) {	}

			try {
				EForce_measure_with_unit forcemes = appforce.getApplied_force_fy(null);
				v5appliedforce.setApplied_force_fy(forcemes.get_double(forcemes.getAttributeDefinition("value_component")));
				
			} catch (SdaiException e) {	}

			try {
				EForce_measure_with_unit forcemes = appforce.getApplied_force_fz(null);
				v5appliedforce.setApplied_force_fz(forcemes.get_double(forcemes.getAttributeDefinition("value_component")));
			
			} catch (SdaiException e) {	}
			
			v5force.setLoad_values(v5appliedforce);
			
			v5forces_node.add(v5force);
		}

		return v5forces_node;
	}

	// deal with load case
	private VFIFE_LoadCase parseLoadCase(ELoad_case eloadcase) throws SdaiException{
		
		VFIFE_LoadCase v5loadcase = new VFIFE_LoadCase();
		
		// set load case name and factor(unknown FIXME)
		v5loadcase.setLoad_case_name(eloadcase.getLoad_case_name(null));
		v5loadcase.setLoad_case_factor(0);
		
		// FIXME not set in eg5-1, maybe useful
		// set load case default governing analysis(analysis method: dynamic or static)
		v5loadcase.setGoverning_analyses(null);
		
		// set load case time variation (physical action nature)
		VFIFE_PhysicalAction v5physicAction = this.parsePhysicalAction(eloadcase.getTime_variation(null));
		v5loadcase.setTime_variation(v5physicAction);
		 
		return v5loadcase;
	}
	
	// deal with physical action
	private VFIFE_PhysicalAction parsePhysicalAction(EPhysical_action ephysicAction) throws SdaiException{
		
		VFIFE_PhysicalAction v5phyAction = null;
		
		// set action source - see follow
		String derivedClass =  ephysicAction.toString();
		if (derivedClass.contains("PHYSICAL_ACTION_PERMANENT")){
			int src = ((EPhysical_action_permanent)ephysicAction).getAction_source(null)-1;
			VFIFE_PhysicalActionPermanent v5phyActionP = new VFIFE_PhysicalActionPermanent();
			v5phyActionP.setAction_source(TYPE_action_source_permanent.values()[src]);
			v5phyAction = v5phyActionP;
		}
		else if(derivedClass.contains("PHYSICAL_ACTION_ACCIDENTAL")){
			int src = ((EPhysical_action_accidental)ephysicAction).getAction_source(null)-1;
			VFIFE_PhysicalActionAccidental v5phyActionA = new VFIFE_PhysicalActionAccidental();
			v5phyActionA.setAction_source(TYPE_action_source_accidential.values()[src]);
			v5phyAction = v5phyActionA;
		}
		else if(derivedClass.contains("PHYSICAL_ACTION_VARIABLE_LONG_TERM")){
			int src = ((EPhysical_action_variable_long_term)ephysicAction).getAction_source(null)-1;
			VFIFE_PhysicalActionVariableLongTerm v5phyActionL = new VFIFE_PhysicalActionVariableLongTerm(); 
			v5phyActionL.setAction_source(TYPE_action_source_variable_long_term.values()[src]);
			v5phyAction = v5phyActionL;
		}
		else if(derivedClass.contains("PHYSICAL_ACTION_VARIABLE_SHORT_TERM")){
			int src = ((EPhysical_action_variable_short_term)ephysicAction).getAction_source(null)-1;
			VFIFE_PhysicalActionVariableShortTerm v5phyActionS = new VFIFE_PhysicalActionVariableShortTerm();
			v5phyActionS.setAction_source(TYPE_action_source_variable_short_term.values()[src]);
			v5phyAction = v5phyActionS;
		}
		else if(derivedClass.contains("PHYSICAL_ACTION_TRANSIENT")){
			int src = ((EPhysical_action_variable_transient)ephysicAction).getAction_source(null)-1;
			VFIFE_PhysicalActionVariableTransient v5phyActionT = new VFIFE_PhysicalActionVariableTransient();
			v5phyActionT.setAction_source(TYPE_action_source_variable_transient.values()[src]);
			v5phyAction = v5phyActionT;
		}
		
		// set action nature - static or dynamic
		v5phyAction.setAction_nature(TYPE_static_or_dynamic.values()[ephysicAction.getAction_nature(null)-1]);
		// set action spatial variation - free or fixed action
		v5phyAction.setAction_spatial_variation(TYPE_spatial_variation.values()[ephysicAction.getAction_spatial_variation(null)-1]);
		// set action type - direct or indirect
		v5phyAction.setAction_type(TYPE_direct_or_indirect_action.values()[ephysicAction.getAction_type(null)-1]);
		
		// FIXME
		// basic_magnitude, derived_magnitude, derivation_factors,  derivation_factor_labels are not useful for now

		return v5phyAction;
	}

}
