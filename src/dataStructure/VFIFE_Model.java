package dataStructure;

import java.util.ArrayList;
import dataStructure.model.*;

public class VFIFE_Model {

    private ArrayList<VFIFE_Bar> bars;
    private ArrayList<VFIFE_Load> forces;
    private ArrayList<VFIFE_Material> materiaux;
    private ArrayList<VFIFE_Node> nodes;

    public VFIFE_Model() {
        this.bars = new ArrayList<VFIFE_Bar>();
        this.forces = new ArrayList<VFIFE_Load>();
        this.materiaux = new ArrayList<VFIFE_Material>();
        this.nodes = new ArrayList<VFIFE_Node>();
    }

    public void removeMaterial(String s) {
        for (VFIFE_Material mat : materiaux) {
            if (mat.getName().equals(s)) {
                materiaux.remove(mat);
                return;
            }
        }
    }

    public void addMaterial(String name, double density, double young_modulus) {
        VFIFE_Material mat = new VFIFE_Material();
        mat.setName(name);
        mat.setDensity(density);
        mat.setYoung_modulus(young_modulus);
        materiaux.add(mat);
    }

    public VFIFE_Material getMaterial(String s) {
        for (VFIFE_Material mat : materiaux) {
            if (mat.getName().equals(s)) {
                return mat;
            }
        }
        return null;
    }

    public void clear() {
        bars.clear();
        forces.clear();
        materiaux.clear();
        nodes.clear();
    }

    public boolean isEmpty() {
        if (bars.isEmpty() && forces.isEmpty() && materiaux.isEmpty() && nodes.isEmpty()) {
            return true;
        }
        return false;
    }

    public VFIFE_Bar getBar(int barid) {
        for (VFIFE_Bar bartemp : bars) {
            if (bartemp.getBar_id() == barid) {
                return bartemp;
            }
        }
        return null;
    }

    public void addBar(VFIFE_Bar barin) {
        VFIFE_Bar bartemp = getBar(barin.getBar_id());
        if (bartemp == null) {
            bars.add(barin);
        }
    }

    public VFIFE_Node getNode(int nodeid) {
        for (VFIFE_Node nodetemp : nodes) {
            if (nodetemp.getNode_id() == nodeid) {
                return nodetemp;
            }
        }
        return null;
    }

    public ArrayList<VFIFE_Material> getMateriaux() {
        return materiaux;
    }

    public void setMateriaux(ArrayList<VFIFE_Material> materiaux) {
        this.materiaux = materiaux;
    }

    public ArrayList<VFIFE_Bar> getBars() {
        return bars;
    }

    public void setBars(ArrayList<VFIFE_Bar> bars) {
        this.bars = bars;
    }

    public ArrayList<VFIFE_Load> getForces() {
        return forces;
    }

    public void setForces(ArrayList<VFIFE_Load> forces) {
        this.forces = forces;
    }

    public ArrayList<VFIFE_Node> getNodes() {
        return nodes;
    }

    public void setNodes(ArrayList<VFIFE_Node> nodes) {
        this.nodes = nodes;
    }

}
