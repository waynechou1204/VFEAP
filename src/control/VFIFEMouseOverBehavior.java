package control;

import java.awt.AWTEvent;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.media.j3d.*;
import javax.vecmath.Color3f;

import com.sun.j3d.utils.picking.*;

public class VFIFEMouseOverBehavior extends Behavior {

    private PickCanvas pickCanvas;
    private PickResult pickResult;
    private Appearance pickedShapeOldApp, cyanApp;
    private Node pickedNode;
    private boolean isObjectSelectedBefore = false;
    private Shape3D pickedShape;
    private Shape3D oldPickedNode = new Shape3D();
    private BranchGroup dataBranchGroup;
    private Cursor savedCursor;
    private Canvas3D canvas;
    private boolean overAnObject = false;

    private BranchGroup shapeLabelBG = new BranchGroup();

    public VFIFEMouseOverBehavior(Canvas3D canvas, BranchGroup dataBranchGroup) {
        this.canvas = canvas;
        this.dataBranchGroup = dataBranchGroup;

        pickCanvas = new PickCanvas(canvas, dataBranchGroup);
        pickCanvas.setTolerance(5.0f);
        pickCanvas.setMode(PickCanvas.GEOMETRY_INTERSECT_INFO);

        Color3f objColor = new Color3f(0.8f, 0.8f, 0.0f);
        Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
        Color3f white = new Color3f(1.0f, 1.0f, 1.0f);

        cyanApp = new Appearance();
        objColor = new Color3f(0.0f, 0.8f, 0.8f);
        cyanApp.setMaterial(new Material(objColor, black, objColor, white, 10.0f));
        //cyanApp.setTransparencyAttributes (new TransparencyAttributes (TransparencyAttributes.NICEST, 0.3f));

    }

    @Override
    public void initialize() {
        // TODO Auto-generated method stub
        wakeupOn(new WakeupOnAWTEvent(MouseEvent.MOUSE_MOVED));
    }

    @Override
    public void processStimulus(Enumeration criteria) {
        // TODO Auto-generated method stub
        WakeupCriterion wakeup;
        AWTEvent[] event;
        int eventId;

        while (criteria.hasMoreElements()) {
            wakeup = (WakeupCriterion) criteria.nextElement();

            if (wakeup instanceof WakeupOnAWTEvent) {
                event = ((WakeupOnAWTEvent) wakeup).getAWTEvent();

                for (int ii = 0; ii < event.length; ii++) {
                    eventId = event[ii].getID();

                    if (eventId == MouseEvent.MOUSE_MOVED) {
                        int x = ((MouseEvent) event[ii]).getX();
                        int y = ((MouseEvent) event[ii]).getY();

                        pickCanvas.setShapeLocation(x, y);

                        try {
                            pickResult = pickCanvas.pickClosest();

                            if (pickResult != null) {
                                pickedNode = ((Shape3D) pickResult.getNode(PickResult.SHAPE3D));
                                pickedShape = ((Shape3D) pickedNode);

                                if (isObjectSelectedBefore) {
                                    if (oldPickedNode == null) {
                                        oldPickedNode = pickedShape;
                                        pickedShapeOldApp = pickedShape.getAppearance();
                                        canvas.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                                        System.out.println("Mouse Over - object is selected before");

                                    } else {
                                        ((Shape3D) oldPickedNode).setAppearance(pickedShapeOldApp);
                                        oldPickedNode = pickedShape;
                                        pickedShapeOldApp = ((Shape3D) pickedNode).getAppearance();
                                        ((Shape3D) pickedNode).setAppearance(cyanApp);
                                        canvas.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                                        overAnObject = true;
                                        System.out.println("Mouse Over - object is not selected before");

                                    }
                                }
                                isObjectSelectedBefore = true;
                            } else if (overAnObject) {
                                canvas.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                                oldPickedNode.setAppearance(pickedShapeOldApp);
                                System.out.println("pickResult is null");
                                overAnObject = false;
                            }
                        } catch (CapabilityNotSetException e) {
                            e.printStackTrace();

                        }
                    }
                }
            }
        }
        wakeupOn(new WakeupOnAWTEvent(MouseEvent.MOUSE_MOVED));
    }

}
