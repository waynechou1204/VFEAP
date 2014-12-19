package modeling;

import java.util.ArrayList;

import jsdai.SStructural_frame_schema.AElement_node_connectivity;
import jsdai.SStructural_frame_schema.ALoad_member_concentrated;
import jsdai.SStructural_frame_schema.ALoad_node;
import jsdai.SStructural_frame_schema.AMaterial_isotropic;
import jsdai.SStructural_frame_schema.ARepresentation_item;
import jsdai.SStructural_frame_schema.ASection_profile;
import jsdai.SStructural_frame_schema.CLoad_node;
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
import jsdai.SStructural_frame_schema.EMaterial_strength;
import jsdai.SStructural_frame_schema.ENode;
import jsdai.SStructural_frame_schema.EPhysical_action;
import jsdai.SStructural_frame_schema.EPositive_length_measure_with_unit;
import jsdai.SStructural_frame_schema.ERepresentation_item;
import jsdai.SStructural_frame_schema.ESection_profile;
import jsdai.SStructural_frame_schema.ESection_profile_angle;
import jsdai.SStructural_frame_schema.ESection_profile_circle;
import jsdai.SStructural_frame_schema.ESection_profile_i_type;
import jsdai.lang.A_double;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;
import dataStructure.VFIFE_Model;
import dataStructure.entity.VFIFE_AppliedLoad;
import dataStructure.entity.VFIFE_Bar;
import dataStructure.entity.VFIFE_BoundaryConditionLogical;
import dataStructure.entity.VFIFE_CartesianPoint;
import dataStructure.entity.VFIFE_LoadBar;
import dataStructure.entity.VFIFE_LoadCase;
import dataStructure.entity.VFIFE_LoadCaseAccidental;
import dataStructure.entity.VFIFE_LoadCasePermanent;
import dataStructure.entity.VFIFE_LoadCaseRamp;
import dataStructure.entity.VFIFE_LoadCaseVariable;
import dataStructure.entity.VFIFE_LoadNode;
import dataStructure.entity.VFIFE_Material;
import dataStructure.entity.VFIFE_Node;

public class Parser_ImportSTP {

    public ArrayList<VFIFE_Node> parseNodes(SdaiModel model_cis, VFIFE_Model v5model) throws SdaiException {

        ArrayList<VFIFE_Node> v5nodes = new ArrayList<VFIFE_Node>();

        // deal with nodes of STEP
        jsdai.SStructural_frame_schema.ANode nodes = (jsdai.SStructural_frame_schema.ANode) model_cis.getInstances(ENode.class);
        SdaiIterator nodeIt = nodes.createIterator();
        while (nodeIt.next()) {

            ENode enode = nodes.getCurrentMember(nodeIt);

            VFIFE_Node v5node = null;

            try {
				/////////////// v5node's id, name////////////////////////////
                v5node = new VFIFE_Node(Integer.parseInt(enode.getNode_name(null).trim()));

				//////////////// coordinates//////////////////////////
                jsdai.SStructural_frame_schema.ECartesian_point epoint = (jsdai.SStructural_frame_schema.ECartesian_point) enode
                        .getNode_coords(null);
                A_double coords = epoint.getCoordinates(null);
                SdaiIterator coordsIt = coords.createIterator();

                VFIFE_CartesianPoint v5point = new VFIFE_CartesianPoint(0,0,0);

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
                VFIFE_BoundaryConditionLogical v5boundary = new VFIFE_BoundaryConditionLogical(true,true,true,true,true,true);

                EBoundary_condition_logical eboundary = (EBoundary_condition_logical) enode.getRestraints(enode);

                if (eboundary != null) {
                    // 1==false, 2==true, 3==unknow
                    v5boundary.setBc_x_displacement_free(eboundary.getBc_x_displacement_free(null) == 2);
                    v5boundary.setBc_x_rotation_free(eboundary.getBc_x_rotation_free(null) == 2);
                    v5boundary.setBc_y_displacement_free(eboundary.getBc_y_displacement_free(null) == 2);
                    v5boundary.setBc_y_rotation_free(eboundary.getBc_y_rotation_free(null) == 2);
                    v5boundary.setBc_z_displacement_free(eboundary.getBc_z_displacement_free(null) == 2);
                    v5boundary.setBc_z_rotation_free(eboundary.getBc_z_rotation_free(null) == 2);

                    v5node.setRestraint(v5boundary);
                }
            } catch (SdaiException e) {

            } finally {
                v5nodes.add(v5node);
            }
        }

        return v5nodes;
    }

    public void parseBars(SdaiModel model_cis, VFIFE_Model v5model) throws SdaiException {
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

                    // set bar id
                    v5bar = new VFIFE_Bar(Integer.parseInt(element.getElement_name(null).trim()));

                    // deal with bar area
                    ASection_profile sections = element.getCross_sections(null);
                    SdaiIterator sectionIter = sections.createIterator();
                    while (sectionIter.next()) {
                        ESection_profile section = (ESection_profile) sections.getCurrentMember(sectionIter);
                        
                        // external radius -> bar's section area
                        double area=0;
                        
                        // section-circle-hollow bar
                        if(section.getClass().toString().contains("CSection_profile_circle")){
                        	EPositive_length_measure_with_unit radius = ((ESection_profile_circle) section).getExternal_radius(null);
                        	double r = radius.get_double(radius.getAttributeDefinition("value_component"));
                        	area = Math.PI * r * r;
                        }
                        // other section type, like 'I' type
                        else if(section.getClass().toString().contains("CSection_profile_i_type")){
                        	EPositive_length_measure_with_unit temp = ((ESection_profile_i_type) section).getOverall_depth(null);
                        	double dep = temp.get_double(temp.getAttributeDefinition("value_component"));
                        	
                        	temp = ((ESection_profile_i_type) section).getOverall_width(null);
                        	double width = temp.get_double(temp.getAttributeDefinition("value_component"));
                        	
                        	temp = ((ESection_profile_i_type) section).getWeb_thickness(null);
                        	double web_thick = temp.get_double(temp.getAttributeDefinition("value_component"));
                        	
                        	temp = ((ESection_profile_i_type) section).getFlange_thickness(null);
                        	double flange_thick = temp.get_double(temp.getAttributeDefinition("value_component"));
                        	
                        	area = 2*dep*web_thick + width*flange_thick;
                        }
						// 'L' type
                        else if(section.getClass().toString().contains("CSection_profile_angle")){
                        	EPositive_length_measure_with_unit temp = ((ESection_profile_angle) section).getDepth(null);
                        	double dep = temp.get_double(temp.getAttributeDefinition("value_component"));
                        	
                        	temp =((ESection_profile_angle) section).getWidth(null);
                        	double wid = temp.get_double(temp.getAttributeDefinition("value_component"));
                        	
                        	temp =((ESection_profile_angle) section).getThickness(null);
                        	double thick = temp.get_double(temp.getAttributeDefinition("value_component"));
                        	area = (dep+wid)*thick;
                        }
                        
                        // set bar section area
                        v5bar.setSection_area(area);

						// unit
                        //ESi_unit unit = (ESi_unit) radius.getUnit_component(null);
                        break;
                    }

					// add bar into bars
                    //v5bars.add(v5bar);
                    v5model.getBars().add(v5bar);
                }

                // add node into bar
                if(v5bar.getStart_node()==null){
                	v5bar.setStart_node(v5node);
                }else{
                	v5bar.setEnd_node(v5node);
                }

            } catch (SdaiException e) {

            }

        }
        return;
    }

    public ArrayList<VFIFE_Material> parseMaterials(SdaiModel model_cis, VFIFE_Model v5model) throws SdaiException {
        ArrayList<VFIFE_Material> v5materiaux = new ArrayList<VFIFE_Material>();

		// deal with material
        // problem, cannot tell which element use which material
        AMaterial_isotropic mats = (AMaterial_isotropic) model_cis.getInstances(EMaterial_isotropic.class);
        SdaiIterator matIter = mats.createIterator();
        while (matIter.next()) {
            EMaterial_isotropic mat = mats.getCurrentMember(matIter);

            //set id
            VFIFE_Material v5material = new VFIFE_Material(mat.getItem_number(null));
            
            // set name
            v5material.setName(mat.getMaterial_name(null));

            // set default density, data lost from sap2000 
            v5material.setDensity(7849);
            
            EMaterial_representation matrep = mat.getDefinition(null);
            ARepresentation_item repitems = matrep.getItems(null);
            SdaiIterator itemIter = repitems.createIterator();
            while (itemIter.next()) {
                ERepresentation_item item = repitems.getCurrentMember(itemIter);
                if (item.getInstanceType().getName(null).equals("material_elasticity")) {
                    EMaterial_elasticity matela = (EMaterial_elasticity) item;
                    // set young modulus
                    v5material.setYoung_modulus(matela.getYoung_modulus(null));
                }
                else if (item.getInstanceType().getName(null).equals("material_strength") 
                		&& item.getName(null).equals("yield strength")) {
                    EMaterial_strength matstr = (EMaterial_strength) item;
                    double yield_strength = matstr.getMaterial_strength_value(null);
                    // set yield strength = extreme force
                    v5material.setExtreme_force(yield_strength==0 ? Double.MAX_VALUE : yield_strength);                                        
                }
            }
            v5materiaux.add(v5material);
        }
        return v5materiaux;
    }

    // parse load of member concentrated to v5model
    public ArrayList<VFIFE_LoadBar> parseLoadMemberCon(SdaiModel model_cis, VFIFE_Model v5model) throws SdaiException {

        ArrayList<VFIFE_LoadBar> v5forces_member = new ArrayList<VFIFE_LoadBar>();

		// deal with concentrated forces
        // load_member(or element)_concentrated
        ALoad_member_concentrated concens = (ALoad_member_concentrated) model_cis.getInstances(ELoad_member_concentrated.class);
        SdaiIterator concenIter = concens.createIterator();
        while (concenIter.next()) {
            ELoad_member_concentrated concen = concens.getCurrentMember(concenIter);

            //////////////// set force name - identity
            VFIFE_LoadBar v5forcemember = new VFIFE_LoadBar(Integer.parseInt(concen.getLoad_name(null).trim()));
            
			//////////////// start time and end time are empty, set default value
            v5forcemember.setStart_time(0);
            v5forcemember.setEnd_time(50);
            
            /////////////// set supporting member - bar // deal with assembly_design_structural_member_linear
            EAssembly_design_structural_member_linear supmember = (EAssembly_design_structural_member_linear) concen.getSupporting_member(null);
            v5forcemember.setSupporting_bar(v5model.getBar(Integer.parseInt(supmember.getItem_name(null).trim())));

            //////////////// deal with load position coordinates
            jsdai.SStructural_frame_schema.ECartesian_point loadpos = (jsdai.SStructural_frame_schema.ECartesian_point) concen.getLoad_position(null);
            A_double coords = loadpos.getCoordinates(null);
            SdaiIterator coordsIt = coords.createIterator();
            VFIFE_CartesianPoint v5point = new VFIFE_CartesianPoint(0,0,0);
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
            VFIFE_AppliedLoad v5appliedforce = new VFIFE_AppliedLoad(0,0,0);
            try {
                // directions and force values
                EForce_measure_with_unit forcemes = appforce.getApplied_force_fx(null);
                v5appliedforce.setApplied_force_fx(forcemes.get_double(forcemes.getAttributeDefinition("value_component")));
            } catch (SdaiException e) {
            }
            try {
                EForce_measure_with_unit forcemes = appforce.getApplied_force_fy(null);
                v5appliedforce.setApplied_force_fy(forcemes.get_double(forcemes.getAttributeDefinition("value_component")));
            } catch (SdaiException e) {
            }
            try {
                EForce_measure_with_unit forcemes = appforce.getApplied_force_fz(null);
                v5appliedforce.setApplied_force_fz(forcemes.get_double(forcemes.getAttributeDefinition("value_component")));
				// unit
                //ESi_unit unit = (ESi_unit) forcemes.getUnit_component(null);				
            } catch (SdaiException e) {
            }
            v5forcemember.setLoad_value(v5appliedforce);

            ///////////////// deal with load case - load type
            ELoad_case eloadcase = concen.getParent_load_case(null);
            v5forcemember.setParent_load_case(this.parseLoadCase(eloadcase));

            ///////////////// fin
            v5forces_member.add(v5forcemember);
        }

        return v5forces_member;

    }

    public ArrayList<VFIFE_LoadNode> parseLoadNode(SdaiModel model_cis, VFIFE_Model v5model) throws SdaiException {

        ArrayList<VFIFE_LoadNode> v5forces_node = new ArrayList<VFIFE_LoadNode>();

		// deal with node forces
        // load_node
        ALoad_node nodeforces = (ALoad_node) model_cis.getInstances(CLoad_node.class);
        SdaiIterator nodeforceIt = nodeforces.createIterator();
        while (nodeforceIt.next()) {
            ELoad_node nodeforce = nodeforces.getCurrentMember(nodeforceIt);

            // force id
            VFIFE_LoadNode v5force = new VFIFE_LoadNode(Integer.parseInt(nodeforce.getLoad_name(null).trim()));
            //v5force.setForce_name((nodeforce.getLoad_name(null).trim()));
            
            ///////////////// deal with load case - load type
            ELoad_case eloadcase = nodeforce.getParent_load_case(null);
            v5force.setParent_load_case(this.parseLoadCase(eloadcase));

            // set default start end time
            v5force.setStart_time(0);
            v5force.setEnd_time(50);
            
            ////////////////// deal with supporting node 
            jsdai.SStructural_frame_schema.ENode supnode = (ENode) nodeforce.getSupporting_node(null);
            // set supporting node
            v5force.setSupporting_node(v5model.getNode(Integer.parseInt(supnode.getNode_name(null).trim())));

            ///////////////// load value
            EApplied_load_static_force appforce = (EApplied_load_static_force) nodeforce.getLoad_values(null);

            // v5 applied force name
            VFIFE_AppliedLoad v5appliedforce = new VFIFE_AppliedLoad(0,0,0);
            
            try {
                // directions and force values
                EForce_measure_with_unit forcemes = appforce.getApplied_force_fx(null);
                v5appliedforce.setApplied_force_fx(forcemes.get_double(forcemes.getAttributeDefinition("value_component")));
            } catch (SdaiException e) {
            }
            try {
                EForce_measure_with_unit forcemes = appforce.getApplied_force_fy(null);
                v5appliedforce.setApplied_force_fy(forcemes.get_double(forcemes.getAttributeDefinition("value_component")));
            } catch (SdaiException e) {
            }
            try {
                EForce_measure_with_unit forcemes = appforce.getApplied_force_fz(null);
                v5appliedforce.setApplied_force_fz(forcemes.get_double(forcemes.getAttributeDefinition("value_component")));
            } catch (SdaiException e) {
            }

            v5force.setLoad_value(v5appliedforce);

            v5forces_node.add(v5force);
        }

        return v5forces_node;
    }

    // deal with load case
    private VFIFE_LoadCase parseLoadCase(ELoad_case eloadcase) throws SdaiException {

        VFIFE_LoadCase v5loadcase = null;

        // set load case time variation (physical action nature)
        EPhysical_action ephysicAction = eloadcase.getTime_variation(null);
        
        // set action source - see follow
        String derivedClass = ephysicAction.toString();
        if (derivedClass.contains("PHYSICAL_ACTION_PERMANENT")) {
            v5loadcase = new VFIFE_LoadCasePermanent();
        } else if (derivedClass.contains("PHYSICAL_ACTION_ACCIDENTAL")) {
            v5loadcase = new VFIFE_LoadCaseAccidental();
        } else if (derivedClass.contains("PHYSICAL_ACTION_VARIABLE")) {
        	v5loadcase = new VFIFE_LoadCaseVariable();
        } else {
        	v5loadcase = new VFIFE_LoadCaseRamp(0);
        }

        return v5loadcase;
    }


}
