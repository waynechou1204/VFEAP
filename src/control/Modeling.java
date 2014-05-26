package control;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import view.VFIFE_Modeling_view;
import jsdai.SStructural_frame_schema.AElement_node_connectivity;
import jsdai.SStructural_frame_schema.ALoad_member_concentrated;
import jsdai.SStructural_frame_schema.AMaterial_isotropic;
import jsdai.SStructural_frame_schema.ARepresentation_item;
import jsdai.SStructural_frame_schema.ASection_profile;
import jsdai.SStructural_frame_schema.EAnalysis_model;
import jsdai.SStructural_frame_schema.EApplied_load_static_force;
import jsdai.SStructural_frame_schema.EAssembly_design_structural_member_linear;
import jsdai.SStructural_frame_schema.EBoundary_condition_logical;
import jsdai.SStructural_frame_schema.EElement_curve_complex;
import jsdai.SStructural_frame_schema.EElement_node_connectivity;
import jsdai.SStructural_frame_schema.EForce_measure_with_unit;
import jsdai.SStructural_frame_schema.ELoad_case;
import jsdai.SStructural_frame_schema.ELoad_member_concentrated;
import jsdai.SStructural_frame_schema.EMaterial_elasticity;
import jsdai.SStructural_frame_schema.EMaterial_isotropic;
import jsdai.SStructural_frame_schema.EMaterial_representation;
import jsdai.SStructural_frame_schema.ENode;
import jsdai.SStructural_frame_schema.EPositive_length_measure_with_unit;
import jsdai.SStructural_frame_schema.ERepresentation_item;
import jsdai.SStructural_frame_schema.ESection_profile_circle;
import jsdai.SStructural_frame_schema.ESi_unit;
import jsdai.SStructural_frame_schema.ESi_unit_name;
import jsdai.SVfife_schema.ANode;
import jsdai.SVfife_schema.CApplied_load_static_force;
import jsdai.SVfife_schema.CBar;
import jsdai.SVfife_schema.CBoundary_condition_logical;
import jsdai.SVfife_schema.CCartesian_point;
import jsdai.SVfife_schema.CForce;
import jsdai.SVfife_schema.CLoad_case;
import jsdai.SVfife_schema.CMaterial;
import jsdai.SVfife_schema.CNode;
import jsdai.SVfife_schema.EBar;
import jsdai.SVfife_schema.ECartesian_point;
import jsdai.SVfife_schema.EForce;
import jsdai.SVfife_schema.EMaterial;
import jsdai.SVfife_schema.SVfife_schema;
import jsdai.lang.A_double;
import jsdai.lang.SdaiException;
import jsdai.lang.SdaiIterator;
import jsdai.lang.SdaiModel;
import model.VFIFE_AppliedLoadStaticForce;
import model.VFIFE_Bar;
import model.VFIFE_BoundaryCondition;
import model.VFIFE_CartesianPoint;
import model.VFIFE_Force;
import model.VFIFE_LoadCase;
import model.VFIFE_Material;
import model.VFIFE_Model;
import model.VFIFE_Node;
import model.VFIFE_repository;

public class Modeling {

	public static void main(String[] args) throws SdaiException,
			FileNotFoundException {
		VFIFE_Model v5model = loadCIS();

		// view of the model
		printModel(v5model);
		//VFIFE_Modeling_view view = new VFIFE_Modeling_view();
		//view.showModel(v5model);
		
		//exportFile(v5model);
	}

	public static VFIFE_Model loadCIS() throws SdaiException {
		// set model as return value
		VFIFE_Model v5model = new VFIFE_Model();

		// set repository
		VFIFE_repository repo_cis = new VFIFE_repository("repositories",
				"G:\\Repositories");

		// load cis/2 file
		SdaiModel model_cis = repo_cis.loadFile("MyCisRepo", "eg5-4.stp");

		try {
			// deal with element-node-connectivity
			// v5-bar
			AElement_node_connectivity connects = (AElement_node_connectivity) model_cis
					.getInstances(EElement_node_connectivity.class);
			SdaiIterator connectIter = connects.createIterator();
			while (connectIter.next()) {
				EElement_node_connectivity connect = connects
						.getCurrentMember(connectIter);
				System.out.println("\n" + connect.getConnectivity_name(null)
						+ " of connectivity");

				// deal with node
				VFIFE_Node v5node = new VFIFE_Node();
				VFIFE_BoundaryCondition v5boundary = new VFIFE_BoundaryCondition();

				try {
					ENode node = connect.getConnecting_node(null);
					System.out.println("\tNode name: "
							+ node.getNode_name(null));

					v5node.setNode_id(Integer.parseInt(node.getNode_name(null)
							.trim()));
					v5node.setNode_name(node.getNode_name(null));

					// coords
					jsdai.SStructural_frame_schema.ECartesian_point point = (jsdai.SStructural_frame_schema.ECartesian_point) node
							.getNode_coords(null);
					A_double coords = point.getCoordinates(null);
					SdaiIterator coordsIt = coords.createIterator();
					VFIFE_CartesianPoint v5point = new VFIFE_CartesianPoint();
					if (coordsIt.next()) {
						v5point.setCoordinate_x(coords
								.getCurrentMember(coordsIt));
					}
					if (coordsIt.next()) {
						v5point.setCoordinate_y(coords
								.getCurrentMember(coordsIt));
					}
					if (coordsIt.next()) {
						v5point.setCoordinate_z(coords
								.getCurrentMember(coordsIt));
					}
					v5node.setCoord(v5point);

					// constraints
					EBoundary_condition_logical boundary = (EBoundary_condition_logical) node
							.getRestraints(node);

					if (boundary != null) {
						// 1==false, 2==true, 3==unknow
						v5boundary.setBc_x_displacement_free(boundary
								.getBc_x_displacement_free(null) == 2);
						v5boundary.setBc_x_rotation_free(boundary
								.getBc_x_rotation_free(null) == 2);
						v5boundary.setBc_y_displacement_free(boundary
								.getBc_y_displacement_free(null) == 2);
						v5boundary.setBc_y_rotation_free(boundary
								.getBc_y_rotation_free(null) == 2);
						v5boundary.setBc_z_displacement_free(boundary
								.getBc_z_displacement_free(null) == 2);
						v5boundary.setBc_z_rotation_free(boundary
								.getBc_z_rotation_free(null) == 2);

						v5boundary.setBoundary_condition_name(boundary
								.getBoundary_condition_name(null));
						// v5boundary.setBoundary_condition_description(boundary.getBoundary_condition_description(null));

						v5node.setRestraint(v5boundary);

						System.out
								.println("\t\tConstraints X displacement free: "
										+ (boundary
												.getBc_x_displacement_free(null) == 2));
						System.out.println("\t\tConstraints X rotation free: "
								+ (boundary.getBc_x_rotation_free(null) == 2));
						System.out
								.println("\t\tConstraints Y displacement free: "
										+ (boundary
												.getBc_y_displacement_free(null) == 2));
						System.out.println("\t\tConstraints Y rotation free: "
								+ (boundary.getBc_y_rotation_free(null) == 2));
						System.out
								.println("\t\tConstraints Z displacement free: "
										+ (boundary
												.getBc_z_displacement_free(null) == 2));
						System.out.println("\t\tConstraints Z rotation free: "
								+ (boundary.getBc_z_rotation_free(null) == 2));
					}
				} catch (SdaiException e) {
					System.out.println("\t\tNO Constraints");
				}

				// deal with element
				try {
					EElement_curve_complex element = (EElement_curve_complex) connect
							.getConnecting_element(null);

					System.out.println("\tElement name: "
							+ element.getElement_name(null));
					VFIFE_Bar v5bar = v5model.getBar(Integer.parseInt(element
							.getElement_name(null).trim()));

					// the bar does not exist in model
					if (v5bar == null) {
						v5bar = new VFIFE_Bar();
						v5bar.setBar_id(Integer.parseInt(element
								.getElement_name(null).trim()));
						// space dimension
						EAnalysis_model anamodel = element
								.getParent_model(null);
						System.out.println("\t\tSpace Dimension: "
								+ anamodel.getCoordinate_space_dimension(null));

						// deal with section circle hollow
						ASection_profile sections = element
								.getCross_sections(null);
						SdaiIterator sectionIter = sections.createIterator();
						while (sectionIter.next()) {
							ESection_profile_circle section = (ESection_profile_circle) sections
									.getCurrentMember(sectionIter);

							// external radius
							// bar's section area
							EPositive_length_measure_with_unit radius = section
									.getExternal_radius(null);
							System.out
									.println("\t\tRadius: "
											+ radius.get_double(radius
													.getAttributeDefinition("value_component")));
							double r = radius.get_double(radius
									.getAttributeDefinition("value_component"));
							v5bar.setSection_area(Math.PI * r * r);

							// unit
							ESi_unit unit = (ESi_unit) radius
									.getUnit_component(null);
							System.out
									.println("\t\tUnit: "
											+ ESi_unit_name.toString(unit
													.getName(null)));
							break;
						}

						// add node into bar
						v5bar.getNodes().add(v5node);

						// add bar into model
						v5model.addBar(v5bar);
					} else {
						// add node into bar
						v5bar.getNodes().add(v5node);
					}

				} catch (SdaiException e) {
					
				}

			}

			// deal with material
			// problem, cannot tell which element use which material
			AMaterial_isotropic mats = (AMaterial_isotropic) model_cis
					.getInstances(EMaterial_isotropic.class);
			SdaiIterator matIter = mats.createIterator();
			while (matIter.next()) {
				EMaterial_isotropic mat = mats.getCurrentMember(matIter);
				System.out.println("\nMaterial name: "
						+ mat.getMaterial_name(null));

				VFIFE_Material v5material = new VFIFE_Material();
				v5material.setName(mat.getMaterial_name(null));

				EMaterial_representation matrep = mat.getDefinition(null);
				ARepresentation_item repitems = matrep.getItems(null);
				SdaiIterator itemIter = repitems.createIterator();
				while (itemIter.next()) {
					ERepresentation_item item = repitems
							.getCurrentMember(itemIter);
					if (item.getInstanceType().getName(null)
							.equals("material_elasticity")) {
						EMaterial_elasticity matela = (EMaterial_elasticity) item;
						System.out.println("\tYoung Modulus: "
								+ matela.getYoung_modulus(null));
						v5material.setYoung_modulus(matela
								.getYoung_modulus(null));

						v5model.getMateriaux().add(v5material);
					}
				}
			}

			// deal with concentrated forces
			// load_member(or element)_concentrated
			ALoad_member_concentrated concens = (ALoad_member_concentrated) model_cis
					.getInstances(ELoad_member_concentrated.class);
			SdaiIterator concenIter = concens.createIterator();
			while (concenIter.next()) {
				ELoad_member_concentrated concen = concens
						.getCurrentMember(concenIter);
				System.out.println("\nLoad name: " + concen.getLoad_name(null));

				// force id
				VFIFE_Force v5force = new VFIFE_Force();
				v5force.setForce_id(Integer.parseInt(concen.getLoad_name(null)
						.trim()));

				// deal with load type
				ELoad_case loadcase = concen.getParent_load_case(null);
				System.out.println("\tLoad case name: "
						+ loadcase.getLoad_case_name(null));

				VFIFE_LoadCase v5loadcase = new VFIFE_LoadCase();
				v5loadcase.setLoad_case_name(loadcase.getLoad_case_name(null));
				// v5loadcase.setLoad_case_factor(loadcase.getLoad_case_factor(null));
				v5loadcase.setTime_variation(loadcase.getTime_variation(null)
						.getAction_nature(null));

				v5force.setParent_load_case(v5loadcase);

				// deal with supporting element
				// EElement_curve_complex supelement = (EElement_curve_complex)
				// concen.getSupporting_element(null);
				// System.out.println("\tElement name: "+supelement.getElement_name(null));

				// deal with assembly_design_structural_member_linear
				EAssembly_design_structural_member_linear supmember = (EAssembly_design_structural_member_linear) concen
						.getSupporting_member(null);
				System.out.println("\tSupporting member name: "
						+ supmember.getItem_name(null));

				// deal with load position
				jsdai.SStructural_frame_schema.ECartesian_point loadpos = (jsdai.SStructural_frame_schema.ECartesian_point) concen
						.getLoad_position(null);
				System.out.println("\tLoad on position"
						+ loadpos.getCoordinates(null));

				// coords
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
				v5force.setLoad_position(v5point);

				// load value
				EApplied_load_static_force appforce = (EApplied_load_static_force) concen
						.getLoad_value(null);

				// v5 applied force name
				VFIFE_AppliedLoadStaticForce v5appliedforce = new VFIFE_AppliedLoadStaticForce();
				v5appliedforce.setAppliedload_name(appforce
						.getApplied_load_name(null));

				try {
					// directions and force values
					EForce_measure_with_unit forcemes = appforce.getApplied_force_fx(null);
					System.out.println("\t\tForce value of x: " + forcemes.get_double(forcemes.getAttributeDefinition("value_component")));

					v5appliedforce.setApplied_force_fx(forcemes.get_double(forcemes.getAttributeDefinition("value_component")));

					// unit
					ESi_unit unit = (ESi_unit) forcemes.getUnit_component(null);
					System.out.println("\t\tUnit: "+ ESi_unit_name.toString(unit.getName(null)));
				} catch (SdaiException e) {
					
				}

				try {
					EForce_measure_with_unit forcemes = appforce.getApplied_force_fy(null);
					System.out.println("\t\tForce value of y: "+ forcemes.get_double(forcemes.getAttributeDefinition("value_component")));

					v5appliedforce.setApplied_force_fy(forcemes.get_double(forcemes.getAttributeDefinition("value_component")));

					// unit
					ESi_unit unit = (ESi_unit) forcemes.getUnit_component(null);
					System.out.println("\t\tUnit: "+ ESi_unit_name.toString(unit.getName(null)));
				} catch (SdaiException e) {
				}

				try {
					EForce_measure_with_unit forcemes = appforce
							.getApplied_force_fz(null);
					System.out
							.println("\t\tForce value of z: "+ forcemes.get_double(forcemes.getAttributeDefinition("value_component")));

					v5appliedforce.setApplied_force_fz(forcemes.get_double(forcemes.getAttributeDefinition("value_component")));

					// unit
					ESi_unit unit = (ESi_unit) forcemes.getUnit_component(null);
					System.out.println("\t\tUnit: "+ ESi_unit_name.toString(unit.getName(null)));
				} catch (SdaiException e) {
				}
				v5force.setLoad_value(v5appliedforce);
				v5model.getForces().add(v5force);
			}
			
			repo_cis.close();
		} catch (Exception e) {
			
		} 

		
		return v5model;
	}

	public static void exportFile(VFIFE_Model v5model) throws SdaiException,
			FileNotFoundException {
		// set repository
		VFIFE_repository repo_vfife = new VFIFE_repository("repositories",
				"G:\\Repositories");

		// create new model -- bind with vfife schema
		SdaiModel model = repo_vfife.setVfifeOutModel("MyV5Repo", "MyV5Model");
		
		// deal with bars
		for (VFIFE_Bar bar : v5model.getBars()) {
			EBar ebar = (EBar) model.createEntityInstance(CBar.class);
			
			ebar.setBar_id(null, bar.getBar_id());
			ebar.setSection_area(null, bar.getSection_area());

			// deal with nodes of bar
			ebar.createBar_node(null);
			for (VFIFE_Node node : bar.getNodes()) {
				jsdai.SVfife_schema.ENode enode = (jsdai.SVfife_schema.ENode) model
						.createEntityInstance(CNode.class);

				enode.setNode_id(null, node.getNode_id());
				enode.setNode_name(null, node.getNode_name());

				// coordinates of node
				jsdai.SVfife_schema.ECartesian_point epoint = (ECartesian_point) model
						.createEntityInstance(CCartesian_point.class);
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
					erestrains.setBoundary_condition_name(null, node
							.getRestraint().getBoundary_condition_name());
					// erestrains.setBoundary_condition_description(null,
					// node.getRestraint().getBoundary_condition_description());

					erestrains.setBc_x_displacement_free(null, (node
							.getRestraint().getBc_x_displacement_free()) ? 2
							: 1);
					erestrains.setBc_x_rotation_free(null, (node.getRestraint()
							.getBc_x_rotation_free()) ? 2 : 1);
					erestrains.setBc_y_displacement_free(null, (node
							.getRestraint().getBc_y_displacement_free()) ? 2
							: 1);
					erestrains.setBc_y_rotation_free(null, (node.getRestraint()
							.getBc_y_rotation_free()) ? 2 : 1);
					erestrains.setBc_z_displacement_free(null, (node
							.getRestraint().getBc_z_displacement_free()) ? 2
							: 1);
					erestrains.setBc_z_rotation_free(null, (node.getRestraint()
							.getBc_z_rotation_free()) ? 2 : 1);

					enode.setRestraints(null, erestrains);
				}
				
				ANode anodes = ebar.getBar_node(null);
				SdaiIterator nodeit = anodes.createIterator();
				nodeit.end();
				anodes.addAfter(nodeit, enode);
			}
			
			
			// deal with materail of bar
			if (bar.getMaterial() != null) {
				jsdai.SVfife_schema.EMaterial emat = (EMaterial) model
						.createEntityInstance(CMaterial.class);
				emat.setName(null, bar.getMaterial().getName());
				emat.setDensity(null, bar.getMaterial().getDensity());
				emat.setYoung_modulus(null, bar.getMaterial()
						.getYoung_modulus());

				ebar.setBar_material(null, emat);
			}
		}

		// deal with materiaux of model
		for (VFIFE_Material material : v5model.getMateriaux()) {
			jsdai.SVfife_schema.EMaterial emat = (EMaterial) model
					.createEntityInstance(CMaterial.class);
			emat.setName(null, material.getName());
			emat.setDensity(null, material.getDensity());
			emat.setYoung_modulus(null, material.getYoung_modulus());
		}

		// deal with force of model
		for (VFIFE_Force force : v5model.getForces()) {
			jsdai.SVfife_schema.EForce eforce = (EForce) model
					.createEntityInstance(CForce.class);

			eforce.setForce_id(null, force.getForce_id());
			eforce.setDuration_time(null, force.getDuration_time());

			// load case of force
			jsdai.SVfife_schema.ELoad_case eloadcase = (jsdai.SVfife_schema.ELoad_case) model
					.createEntityInstance(CLoad_case.class);

			eloadcase.setLoad_case_name(null, force.getParent_load_case()
					.getLoad_case_name());
			eloadcase.setLoad_case_factor(null, force.getParent_load_case()
					.getLoad_case_factor());
			eloadcase.setTime_variation(null, force.getParent_load_case()
					.getTime_variation());

			eforce.setParent_load_case(null, eloadcase);

			// load position
			// coordinates of node
			jsdai.SVfife_schema.ECartesian_point epoint = (ECartesian_point) model
					.createEntityInstance(CCartesian_point.class);
			epoint.createCoordinates(null);
			A_double coords = epoint.getCoordinates(null);
			
			coords.addByIndex(1, force.getLoad_position().getCoordinate_x());
			coords.addByIndex(2, force.getLoad_position().getCoordinate_y());
			coords.addByIndex(3, force.getLoad_position().getCoordinate_z());
			
			eforce.setLoad_position(null, epoint);

			// applied load force with value and type
			jsdai.SVfife_schema.EApplied_load_static_force eloadvalue = (jsdai.SVfife_schema.EApplied_load_static_force) model
					.createEntityInstance(CApplied_load_static_force.class);
			eloadvalue.setApplied_load_name(null, force.getLoad_value()
					.getAppliedload_name());
			VFIFE_AppliedLoadStaticForce staticforce = (VFIFE_AppliedLoadStaticForce) force
					.getLoad_value();

			eloadvalue.setApplied_force_fx(null,
					staticforce.getApplied_force_fx());
			eloadvalue.setApplied_force_fy(null,
					staticforce.getApplied_force_fy());
			eloadvalue.setApplied_force_fz(null,
					staticforce.getApplied_force_fz());

			eloadvalue.setApplied_moment_mx(null,
					staticforce.getApplied_moment_mx());
			eloadvalue.setApplied_moment_my(null,
					staticforce.getApplied_moment_my());
			eloadvalue.setApplied_moment_mz(null,
					staticforce.getApplied_moment_mz());

			eforce.setLoad_value(null, eloadvalue);
		}

		String filename = "out_modeling.xml";
		FileOutputStream out = new FileOutputStream(filename);

		repo_vfife.outputFile(out); 
	
		System.out.println("out over");
		
		repo_vfife.close();
	}

	public static void printModel(VFIFE_Model v5model) throws SdaiException,
			FileNotFoundException {
		// deal with bars
		for (VFIFE_Bar bar : v5model.getBars()) {
			System.out.println("\n\n---------------------\n" + "bar id: "
					+ bar.getBar_id());
			System.out.println("bar section: " + bar.getSection_area());

			// deal with nodes of bar
			for (VFIFE_Node node : bar.getNodes()) {
				System.out.println("node id: " + node.getNode_id());
				System.out.println("node name: " + node.getNode_name());

				// coordinates of node
				System.out.println("node coord x: "
						+ node.getCoord().getCoordinate_x());
				System.out.println("node coord y: "
						+ node.getCoord().getCoordinate_y());
				System.out.println("node coord z: "
						+ node.getCoord().getCoordinate_z());

				// boundary condition of node
				if (node.getRestraint() != null) {
					System.out.println("node restraint name: "
							+ node.getRestraint().getBoundary_condition_name());
					// System.out.println("node restraint desc: "+node.getRestraint().getBoundary_condition_description());

					System.out
							.println("node restraint x disp: "
									+ ((node.getRestraint()
											.getBc_x_displacement_free())));
					System.out
							.println("node restraint x rotate: "
									+ ((node.getRestraint()
											.getBc_x_rotation_free())));
					System.out
							.println("node restraint y disp: "
									+ ((node.getRestraint()
											.getBc_y_displacement_free())));
					System.out
							.println("node restraint y rotate: "
									+ ((node.getRestraint()
											.getBc_y_rotation_free())));
					System.out
							.println("node restraint z disp: "
									+ ((node.getRestraint()
											.getBc_z_displacement_free())));
					System.out
							.println("node restraint z rotate: "
									+ ((node.getRestraint()
											.getBc_z_rotation_free())));
				} else {
					System.out.println("no restraints");
				}
			}

			// deal with materail of bar
			if (bar.getMaterial() != null) {
				System.out.println("bar material name: "
						+ bar.getMaterial().getName());
				System.out.println("bar material density: "
						+ bar.getMaterial().getDensity());
				System.out.println("bar material modulus: "
						+ bar.getMaterial().getYoung_modulus());
			} else {
				System.out.println("no material");
			}
		}

		// deal with materiaux of model
		for (VFIFE_Material material : v5model.getMateriaux()) {
			System.out.println("\nbar material name: " + material.getName());
			System.out
					.println("bar material density: " + material.getDensity());
			System.out.println("bar material modulus: "
					+ material.getYoung_modulus());

		}

		// deal with force of model
		for (VFIFE_Force force : v5model.getForces()) {
			System.out.println("force id: " + force.getForce_id());
			System.out.println("force duree: " + force.getDuration_time());

			// load case of force
			System.out.println("force load case name: "
					+ force.getParent_load_case().getLoad_case_name());
			System.out.println("force load case factor: "
					+ force.getParent_load_case().getLoad_case_factor());
			System.out.println("force load case time var: "
					+ force.getParent_load_case().getTime_variation_string());

			// load position
			// coordinates of node
			System.out.println("force load position x: "
					+ force.getLoad_position().getCoordinate_x());
			System.out.println("force load position y: "
					+ force.getLoad_position().getCoordinate_y());
			System.out.println("force load position z: "
					+ force.getLoad_position().getCoordinate_z());

			// applied load force with value and type
			System.out.println("force applied name: "
					+ force.getLoad_value().getAppliedload_name());

			VFIFE_AppliedLoadStaticForce staticforce = (VFIFE_AppliedLoadStaticForce) force
					.getLoad_value();

			System.out
					.println("force fx :" + staticforce.getApplied_force_fx());
			System.out
					.println("force fy :" + staticforce.getApplied_force_fy());
			System.out
					.println("force fz :" + staticforce.getApplied_force_fz());

			System.out.println("force mx :"
					+ staticforce.getApplied_moment_mx());
			System.out.println("force my :"
					+ staticforce.getApplied_moment_my());
			System.out.println("force mz :"
					+ staticforce.getApplied_moment_mz());

		}
	}
}
