package dataStructure;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import dataStructure.model.VFIFE_AppliedLoadStaticForce;
import dataStructure.model.VFIFE_Bar;
import dataStructure.model.VFIFE_CartesianPoint;
import dataStructure.model.VFIFE_Load;
import dataStructure.model.VFIFE_LoadMemberConcentrated;
import dataStructure.model.VFIFE_LoadNode;
import dataStructure.model.VFIFE_Node;

public class ProgramCalculation {
	
	private String nodeFile;
	private String elementFile;
	
	public ProgramCalculation(String nodefile,String elementfile)
	{
		nodeFile=nodefile;
		elementFile=elementfile;
	}
	
	
    public void exportV5File(VFIFE_Model v5model) throws IOException {
        FileOutputStream nodefile = null;
        FileOutputStream elementfile = null;
        try {
            nodefile = new FileOutputStream(new File(nodeFile));
            elementfile = new FileOutputStream(new File(elementFile));
        } catch (FileNotFoundException e) {

        }
        double Times = 0;
        double forceTime = 50;//Times*step 为当前时间

        double step = 10000; //step为时间步长，即书上公式中的h，程序根据书上公式给出了各点h的算法，然后取最小的h作为整个构件的h值
        double mass = 0;//  #点的质量，是与这个点相连的所有杆件质量的平均值
        double length = 0;//  #杆件长度
        double count = 0;//   #用于后面差分方程迭代公式的计数
        double Young = 0;//   #杨氏模量
        double density = 0;// #密度

        //建一个二维数组表示每个节点的等效力   前三位存力 第四个存node id 第五个mass
        double[][] equivalentForce = new double[v5model.getNodes().size()][];

        for (int i = 0; i < equivalentForce.length; i++) {
            equivalentForce[i] = new double[5];
            equivalentForce[i][0] = 0;
            equivalentForce[i][1] = 0;
            equivalentForce[i][2] = 0;
            equivalentForce[i][3] = v5model.getNodes().get(i).getNode_id();
            equivalentForce[i][4] = -1;
        }

        ArrayList<double[]> exPoList = new ArrayList<double[]>(); //#上一帧所有点的位置
        ArrayList<double[]> curPoList = new ArrayList<double[]>(); //#这一帧所有点的位置
        ArrayList<double[]> nxPoList = new ArrayList<double[]>(); // #下一帧所有点的位置
        double[] exPosition = new double[4];// #上一帧的位置
        double[] nxPosition = new double[4];// #下一帧的位置

        for (VFIFE_Load force : v5model.getForces()) {

            VFIFE_AppliedLoadStaticForce loadvalue;//力的大小
            VFIFE_CartesianPoint loadPosition;//力的作用点
            if (force instanceof VFIFE_LoadMemberConcentrated) {
                double[] xForce = new double[3];//将作用于杆件上的力分解，X为与杆件方向平行的力
                double[] yForce = new double[3];//Y为与杆件方向垂直的力
                loadvalue = (VFIFE_AppliedLoadStaticForce) ((VFIFE_LoadMemberConcentrated) force).getLoad_value();
                loadPosition = ((VFIFE_LoadMemberConcentrated) force).getLoad_position();
                VFIFE_Bar vbar = ((VFIFE_LoadMemberConcentrated) force).getSupporting_member();
                VFIFE_Node vnode1 = vbar.getNodes().get(0); //杆件两端点的对应
                VFIFE_Node vnode2 = vbar.getNodes().get(1);

                //沿着杆件方向的向量
                double Dx = vnode1.getCoord().getCoordinate_x() - vnode2.getCoord().getCoordinate_x();
                double Dy = vnode1.getCoord().getCoordinate_y() - vnode2.getCoord().getCoordinate_y();
                double Dz = vnode1.getCoord().getCoordinate_z() - vnode2.getCoord().getCoordinate_z();

                //集中力的大小
                double magnitude = Math.sqrt(Math.pow(loadvalue.getApplied_force_fx(), 2)
                        + Math.pow(loadvalue.getApplied_force_fy(), 2)
                        + Math.pow(loadvalue.getApplied_force_fz(), 2));
                //杆方向向量的模也就是杆件的长度
                double directMag = Math.sqrt(Dx * Dx + Dy * Dy + Dz * Dz);
                //角度的余弦值，用于分解力时的计算
                double cosang = (loadvalue.getApplied_force_fx() * Dx
                        + loadvalue.getApplied_force_fy() * Dy
                        + loadvalue.getApplied_force_fz() * Dz) / (magnitude * directMag);

//    			将作用于杆件上的力分解为沿杆件方向和垂直杆件方向的力
                xForce[0] = magnitude * cosang * Dx / directMag;
                xForce[1] = magnitude * cosang * Dy / directMag;
                xForce[2] = magnitude * cosang * Dz / directMag;

                yForce[0] = loadvalue.getApplied_force_fx() - magnitude * cosang * Dx / directMag;
                yForce[1] = loadvalue.getApplied_force_fy() - magnitude * cosang * Dy / directMag;
                yForce[2] = loadvalue.getApplied_force_fz() - magnitude * cosang * Dz / directMag;

//    			计算杆件1端点到力作用点的距离，计算力等效到两端节点时需要用到该值
                double lentonode1 = loadPosition.getCoordinate_x();
                double lentonode2 = directMag - loadPosition.getCoordinate_x();
                /*double lentonode1=Math.sqrt(
                 Math.pow(vnode1.getCoord().getCoordinate_x()-loadPosition.getCoordinate_x(),2)
                 +Math.pow(vnode1.getCoord().getCoordinate_y()-loadPosition.getCoordinate_y(),2)
                 +Math.pow(vnode1.getCoord().getCoordinate_z()-loadPosition.getCoordinate_z(),2));
    			
                 //计算杆件2端点到力作用点的距离，计算力等效到两端节点时需要用到该值
                 double lentonode2=Math.sqrt(
                 Math.pow(vnode2.getCoord().getCoordinate_x()-loadPosition.getCoordinate_x(),2)
                 +Math.pow(vnode2.getCoord().getCoordinate_y()-loadPosition.getCoordinate_y(),2)
                 +Math.pow(vnode2.getCoord().getCoordinate_z()-loadPosition.getCoordinate_z(),2));
                 */

                //将力等效到杆件两端
                //vnode1 未被约束
                if (vnode1.getRestraint() == null) {
                    for (int i = 0; i < equivalentForce.length; i++) {
                        if (equivalentForce[i][3] == vnode1.getNode_id()) {
                            equivalentForce[i][3] = vnode1.getNode_id();
                            equivalentForce[i][0] = equivalentForce[i][0]
                                    - (xForce[0] * lentonode2 / directMag)
                                    + yForce[0] * lentonode2 * lentonode2 * (directMag + 2 * lentonode1) / Math.pow(directMag, 3);
                            equivalentForce[i][1] = equivalentForce[i][1]
                                    - (xForce[1] * lentonode2 / directMag)
                                    + yForce[1] * lentonode2 * lentonode2 * (directMag + 2 * lentonode1) / Math.pow(directMag, 3);
                            equivalentForce[i][2] = equivalentForce[i][2]
                                    - (xForce[2] * lentonode2 / directMag)
                                    + yForce[2] * lentonode2 * lentonode2 * (directMag + 2 * lentonode1) / Math.pow(directMag, 3);
                            break;
                        }
                    }
                }

                //vnode2 未被约束
                if (vnode2.getRestraint() == null) {
                    for (int i = 0; i < equivalentForce.length; i++) {
                        if (equivalentForce[i][3] == vnode2.getNode_id()) {
                            equivalentForce[i][3] = vnode2.getNode_id();
                            equivalentForce[i][0] = equivalentForce[i][0]
                                    - (xForce[0] * lentonode2 / directMag)
                                    + yForce[0] * lentonode2 * lentonode2 * (directMag + 2 * lentonode1) / Math.pow(directMag, 3);
                            equivalentForce[i][1] = equivalentForce[i][1]
                                    - (xForce[1] * lentonode2 / directMag)
                                    + yForce[1] * lentonode2 * lentonode2 * (directMag + 2 * lentonode1) / Math.pow(directMag, 3);
                            equivalentForce[i][2] = equivalentForce[i][2]
                                    - (xForce[2] * lentonode2 / directMag)
                                    + yForce[2] * lentonode2 * lentonode2 * (directMag + 2 * lentonode1) / Math.pow(directMag, 3);
                            break;
                        }
                    }
                }

            } else if (force instanceof VFIFE_LoadNode) {
                loadvalue = (VFIFE_AppliedLoadStaticForce) ((VFIFE_LoadNode) force).getLoad_values();
            }

        }
        for (VFIFE_Node node : v5model.getNodes()) {
            if (node.getRestraint() == null) {
                for (VFIFE_Bar bar : v5model.getBars()) {
                    //杆上两端节点的ID
                    int nodeId1 = bar.getNodes().get(0).getNode_id();
                    int nodeId2 = bar.getNodes().get(1).getNode_id();
                    if (node.getNode_id() == nodeId1) {
                        count++;
                        VFIFE_CartesianPoint point1 = v5model.getNode(nodeId1).getCoord();
                        VFIFE_CartesianPoint point2 = v5model.getNode(nodeId2).getCoord();
                        double curlength = Math.sqrt(
                                Math.pow(point1.getCoordinate_x() - point2.getCoordinate_x(), 2)
                                + Math.pow(point1.getCoordinate_y() - point2.getCoordinate_y(), 2)
                                + Math.pow(point1.getCoordinate_z() - point2.getCoordinate_z(), 2)
                        );
                        length += curlength;
                        mass += curlength * bar.getSection_area() * bar.getMaterial().getDensity();
                        Young += bar.getMaterial().getYoung_modulus();
                        density += bar.getMaterial().getDensity();
                    } else if (node.getNode_id() == nodeId2) {
                        count++;
                        VFIFE_CartesianPoint point1 = v5model.getNode(nodeId1).getCoord();
                        VFIFE_CartesianPoint point2 = v5model.getNode(nodeId2).getCoord();
                        double curlength = Math.sqrt(
                                Math.pow(point1.getCoordinate_x() - point2.getCoordinate_x(), 2)
                                + Math.pow(point1.getCoordinate_y() - point2.getCoordinate_y(), 2)
                                + Math.pow(point1.getCoordinate_z() - point2.getCoordinate_z(), 2)
                        );
                        length += curlength;
                        mass += curlength * bar.getSection_area() * bar.getMaterial().getDensity();
                        Young += bar.getMaterial().getYoung_modulus();
                        density += bar.getMaterial().getDensity();
                    }
                }
                length /= count;
                mass = 10;

                for (int i = 0; i < equivalentForce.length; i++) {
                    if (equivalentForce[i][3] == node.getNode_id()) {
                        equivalentForce[i][4] = mass;
                        break;
                    }
                }

                density /= count;
                Young /= count;
                double newstep = 2 * length / (Math.sqrt(Young / density));
                if (newstep < step) {
                    step = newstep;
                }
            }
        }

        step = 0.01;
        double DampingFactor = 0.2;
        double C1 = 1 / (1 + 0.5 * DampingFactor * step); //#中央差分公式中需要用到的常量
        double C2 = C1 * (1 - 0.5 * DampingFactor * step);//#同上

//    	以下属于程式计算部分，也就是书上的中央差分公式
        while (step * Times < 50) {
            for (double[] i : equivalentForce) {
                VFIFE_Node node = v5model.getNode((int) i[3]);
                if (Times == 0) {
                    if (node.getRestraint() == null) {

                        exPosition[0] = node.getCoord().getCoordinate_x() + i[0] * step * Times / 50 * step * step / i[4] / 2;
                        exPosition[1] = node.getCoord().getCoordinate_y() + i[1] * step * Times / 50 * step * step / i[4] / 2;
                        exPosition[2] = node.getCoord().getCoordinate_z() + i[2] * step * Times / 50 * step * step / i[4] / 2;

                        exPoList.add(exPosition);
                        double[] j = {node.getCoord().getCoordinate_x(), node.getCoord().getCoordinate_y(), node.getCoord().getCoordinate_z()};
                        curPoList.add(j);

                        nxPosition[0] = node.getCoord().getCoordinate_x() + i[0] * step * Times / 50 * step * step / i[4] / 2;
                        nxPosition[1] = node.getCoord().getCoordinate_y() + i[1] * step * Times / 50 * step * step / i[4] / 2;
                        nxPosition[2] = node.getCoord().getCoordinate_z() + i[2] * step * Times / 50 * step * step / i[4] / 2;

                        nxPoList.add(nxPosition);

                    } else {
                        double[] j = {node.getCoord().getCoordinate_x(), node.getCoord().getCoordinate_y(), node.getCoord().getCoordinate_z()};
                        exPoList.add(j);
                        curPoList.add(j);
                        nxPoList.add(j);
                    }

                    String writeout = "  ";
                    writeout += SNFormat(step * Times);
                    writeout += "  ";
                    writeout += SNFormat(node.getNode_id());
                    writeout += "  ";
                    writeout += SNFormat(node.getCoord().getCoordinate_x());
                    writeout += "  ";
                    writeout += SNFormat(node.getCoord().getCoordinate_y());
                    writeout += "  ";
                    writeout += SNFormat(node.getCoord().getCoordinate_z());
                    writeout += "\r\n";
                    nodefile.write(writeout.getBytes());
                    nodefile.flush();
                } else if (Times > 0) {
                    double deLength;
                    double mag;
                    if (node.getRestraint() == null) {
//    					找到对应节点在数组中的位置
                        int sign = 0;

                        for (double[] el : equivalentForce) {
                            if (el[3] == i[3]) {
                                break;
                            }
                            sign++;
                        }
                        exPoList.get(sign)[0] = curPoList.get(sign)[0];
                        exPoList.get(sign)[1] = curPoList.get(sign)[1];
                        exPoList.get(sign)[2] = curPoList.get(sign)[2];

                        curPoList.get(sign)[0] = nxPoList.get(sign)[0];
                        curPoList.get(sign)[1] = nxPoList.get(sign)[1];
                        curPoList.get(sign)[2] = nxPoList.get(sign)[2];

                        length = 0;
                        double[] F = null;

                        for (VFIFE_Bar bar : v5model.getBars()) {
                            int nodeId1 = bar.getNodes().get(0).getNode_id();
                            int nodeId2 = bar.getNodes().get(1).getNode_id();
                            int sign1 = 0;
                            int sign2 = 0;
                            for (double[] el : equivalentForce) {
                                if (el[3] == nodeId1) {
                                    break;
                                }
                                sign1++;
                            }
                            for (double[] el : equivalentForce) {
                                if (el[3] == nodeId2) {
                                    break;
                                }
                                sign2++;
                            }
                            if (nodeId1 == (int) i[3]) {
                                length = getLength(curPoList.get(sign1), curPoList.get(sign2));
                                double[] a = {v5model.getNode(nodeId1).getCoord().getCoordinate_x(),
                                    v5model.getNode(nodeId1).getCoord().getCoordinate_y(),
                                    v5model.getNode(nodeId1).getCoord().getCoordinate_z()};
                                double[] b = {v5model.getNode(nodeId2).getCoord().getCoordinate_x(),
                                    v5model.getNode(nodeId2).getCoord().getCoordinate_y(),
                                    v5model.getNode(nodeId2).getCoord().getCoordinate_z()};
                                deLength = getLength(a, b);

                                mag = (length - deLength) * bar.getSection_area() * bar.getMaterial().getYoung_modulus() / length;
                                double x = v5model.getNode(nodeId2).getCoord().getCoordinate_x()
                                        - v5model.getNode(nodeId1).getCoord().getCoordinate_x();
                                double y = v5model.getNode(nodeId2).getCoord().getCoordinate_y()
                                        - v5model.getNode(nodeId1).getCoord().getCoordinate_y();
                                double z = v5model.getNode(nodeId2).getCoord().getCoordinate_z()
                                        - v5model.getNode(nodeId1).getCoord().getCoordinate_z();

                                if (F != null) {
                                    F[0] += x / Math.sqrt(x * x + y * y + z * z) * mag;
                                    F[1] += y / Math.sqrt(x * x + y * y + z * z) * mag;
                                    F[2] += z / Math.sqrt(x * x + y * y + z * z) * mag;
                                } else {
                                    F = new double[3];
                                    F[0] = x / Math.sqrt(x * x + y * y + z * z) * mag;
                                    F[1] = y / Math.sqrt(x * x + y * y + z * z) * mag;
                                    F[2] = z / Math.sqrt(x * x + y * y + z * z) * mag;
                                }
                            } else if (nodeId2 == (int) i[3]) {
                                length = getLength(curPoList.get(sign1), curPoList.get(sign2));
                                double[] a = {v5model.getNode(nodeId1).getCoord().getCoordinate_x(),
                                    v5model.getNode(nodeId1).getCoord().getCoordinate_y(),
                                    v5model.getNode(nodeId1).getCoord().getCoordinate_z()};
                                double[] b = {v5model.getNode(nodeId2).getCoord().getCoordinate_x(),
                                    v5model.getNode(nodeId2).getCoord().getCoordinate_y(),
                                    v5model.getNode(nodeId2).getCoord().getCoordinate_z()};
                                deLength = getLength(a, b);

                                mag = (length - deLength) * bar.getSection_area() * bar.getMaterial().getYoung_modulus() / length;
                                double x = v5model.getNode(nodeId1).getCoord().getCoordinate_x()
                                        - v5model.getNode(nodeId2).getCoord().getCoordinate_x();
                                double y = v5model.getNode(nodeId1).getCoord().getCoordinate_y()
                                        - v5model.getNode(nodeId2).getCoord().getCoordinate_y();
                                double z = v5model.getNode(nodeId1).getCoord().getCoordinate_z()
                                        - v5model.getNode(nodeId2).getCoord().getCoordinate_z();
                                if (F != null) {
                                    F[0] += x / Math.sqrt(x * x + y * y + z * z) * mag;
                                    F[1] += y / Math.sqrt(x * x + y * y + z * z) * mag;
                                    F[2] += z / Math.sqrt(x * x + y * y + z * z) * mag;
                                } else {
                                    F = new double[3];
                                    F[0] = x / Math.sqrt(x * x + y * y + z * z) * mag;
                                    F[1] = y / Math.sqrt(x * x + y * y + z * z) * mag;
                                    F[2] = z / Math.sqrt(x * x + y * y + z * z) * mag;
                                }
                            }
                        }

                        if (step * Times < 50) {
                            //#将作用于点上或等效到点上的力合成，即公式中的F，是由3个力的合成
                            F[0] = F[0] + i[0] * step * (Times - 1) / 50;
                            F[1] = F[1] + i[1] * step * (Times - 1) / 50;
                            F[2] = F[2] + i[2] * step * (Times - 1) / 50;

                        } else {
                            F[0] = F[0] + i[0];
                            F[1] = F[1] + i[1];
                            F[2] = F[2] + i[2];
                        }

                        int sign1 = 0;
                        for (double[] el : equivalentForce) {
                            if (el[3] == i[3]) {
                                break;
                            }
                            sign1++;
                        }
                        nxPoList.get(sign1)[0] = C1 * step * step * F[0] / i[4] + 2 * C1 * curPoList.get(sign1)[0] - C2 * exPoList.get(sign1)[0];
                        nxPoList.get(sign1)[1] = C1 * step * step * F[1] / i[4] + 2 * C1 * curPoList.get(sign1)[1] - C2 * exPoList.get(sign1)[1];
                        nxPoList.get(sign1)[2] = C1 * step * step * F[2] / i[4] + 2 * C1 * curPoList.get(sign1)[2] - C2 * exPoList.get(sign1)[2];

                    }
                    int sign = 0;

                    for (double[] el : equivalentForce) {
                        if (el[3] == i[3]) {
                            break;
                        }
                        sign++;
                    }
                    String writeout = "  ";
                    writeout += SNFormat(step * Times);
                    writeout += "  ";
                    writeout += SNFormat(node.getNode_id());
                    writeout += "  ";
                    writeout += SNFormat(curPoList.get(sign)[0]);
                    writeout += "  ";
                    writeout += SNFormat(curPoList.get(sign)[1]);
                    writeout += "  ";
                    writeout += SNFormat(curPoList.get(sign)[2]);
                    writeout += "\r\n";
                    nodefile.write(writeout.getBytes());
                    nodefile.flush();
                }

            }

            for (VFIFE_Bar bar : v5model.getBars()) {
                String writeout = "  bar";
                writeout += "  ";
                writeout += SNFormat(step * Times);
                writeout += "  ";
                writeout += SNFormat(bar.getBar_id());
                writeout += "  ";
                writeout += SNFormat(bar.getNodes().get(0).getNode_id());
                writeout += "  ";
                writeout += SNFormat(bar.getNodes().get(1).getNode_id());
                writeout += "\r\n";
                elementfile.write(writeout.getBytes());
                elementfile.flush();
            }
            Times += 1;
        }
        nodefile.flush();
        elementfile.flush();
        nodefile.close();
        elementfile.close();

    }
    public static String SNFormat(double i) {

        DecimalFormat a = new DecimalFormat("0.000000000000000E000");
        String aa = a.format(i);
        if (aa.length() == 21) {
            aa = aa.substring(0, 18) + '+' + aa.substring(18);
        }
        return aa;
    }

    public static double getLength(double[] a, double[] b) {
        return Math.sqrt((a[0] - b[0]) * (a[0] - b[0]) + (a[1] - b[1]) * (a[1] - b[1]) + (a[2] - b[2]) * (a[2] - b[2]));
    }
}
