package dataStructure;

import dataStructure.entity.VFIFE_Bar;
import dataStructure.entity.VFIFE_Material;
import dataStructure.entity.VFIFE_Node;
import dataStructure.entity.VFIFE_Load;
import java.util.ArrayList;

public class VFIFE_Model {

	private boolean hasGravity;
	private double calculate_duration;
	private double calculate_time_interval;
	
    private ArrayList<VFIFE_Bar> bars;
    private ArrayList<VFIFE_Load> forces;
    private ArrayList<VFIFE_Material> materiaux;
    private ArrayList<VFIFE_Node> nodes;

    public VFIFE_Model() {
    	this.hasGravity = false;
    	this.calculate_duration = 0;
    	this.calculate_time_interval = 0;
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

    public void addMaterial(VFIFE_Material mat) {
    	VFIFE_Material mattemp = getMaterial(mat.getId());
        if (mattemp == null) {
            materiaux.add(mat);
        }
    }

    public VFIFE_Material getMaterial(int id){
    	for (VFIFE_Material mat : materiaux) {
            if (mat.getId()==id) {
                return mat;
            }
        }
        return null;
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
    	this.hasGravity = false;
    	this.calculate_duration = 0;
    	this.calculate_time_interval = 0;
        this.bars.clear();
        this.forces.clear();
        this.materiaux.clear();
        this.nodes.clear();
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

    public void addNode(VFIFE_Node nd){
    	VFIFE_Node ndtemp = getNode(nd.getNode_id());
    	if(ndtemp==null){
    		nodes.add(nd);
    	}
    }
    
    public void addForce(VFIFE_Load fc) {
        VFIFE_Load ldtemp = getForce(fc.getId());
        if (ldtemp == null) {
            forces.add(fc);
        }
    }
    public VFIFE_Load getForce(int forceid) {
        for (VFIFE_Load temp : forces) {
            if (temp.getId() == forceid) {
                return temp;
            }
        }
        return null;
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

	public boolean isHasGravity() {
		return hasGravity;
	}

	public void setHasGravity(boolean hasGravity) {
		this.hasGravity = hasGravity;
	}

	public double getCalculate_duration() {
		return calculate_duration;
	}

	public void setCalculate_duration(double calculate_duration) {
		this.calculate_duration = calculate_duration;
	}

	public double getCalculate_time_interval() {
		return calculate_time_interval;
	}

	public void setCalculate_time_interval(double calculate_time_interval) {
		this.calculate_time_interval = calculate_time_interval;
	}

}
