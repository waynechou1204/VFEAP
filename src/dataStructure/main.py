#coding=utf-8
import classMoudle
import myMethod
import math
import os
import sys

def updateForceValue(forceList,nodeList,curTime,curPoList):
        #初始化
        for vnode in nodeList:
                vnode.force=[0,0,0]
        #迭代所有力
        for vforce in forceList:
                forceParraElmts=[]  
                forceVerdicalElmts=[]  
                vbar=barList[int(vforce.barid)]
                vnode1=nodeList[int(vbar.nodeid1)]
                vnode2=nodeList[int(vbar.nodeid2)]
                #获得力所作用的杆上的两个节点的坐标
                x1=curPoList[int(vnode1.id)][0]
                x2=curPoList[int(vnode2.id)][0]
                y1=curPoList[int(vnode1.id)][1]
                y2=curPoList[int(vnode2.id)][1]
                z1=curPoList[int(vnode1.id)][2]
                z2=curPoList[int(vnode2.id)][2]
                Dx=x1-x2
                Dy=y1-y2
                Dz=z1-z2
                #if this force is loaded as ramp
                if vforce.type==1:
                    # print vforce.id
                    # print '-------------'
                    xforce=vforce.direction[0]*vforce.directMag*myMethod.ramp_step(vforce.startTime,vforce.rtime,curTime)
                    yforce=vforce.direction[1]*vforce.directMag*myMethod.ramp_step(vforce.startTime,vforce.rtime,curTime)
                    zforce=vforce.direction[2]*vforce.directMag*myMethod.ramp_step(vforce.startTime,vforce.rtime,curTime)
                    # print vforce.id,vforce.directMag*myMethod.ramp_step(vforce.startTime,vforce.rtime,curTime)
                #如果是恒力
                elif vforce.type==3:
                    # print vforce.id
                    # print 'xxxxxxxxxx'
                    xforce=vforce.direction[0]*vforce.directMag
                    yforce=vforce.direction[1]*vforce.directMag
                    zforce=vforce.direction[2]*vforce.directMag
                elif vforce.type==2:
                    pass#futher study is needed
                magnitude=math.sqrt(xforce**2+yforce**2+zforce**2)
                # if vforce.id==0:
                    
                    # print vforce.rtime,magnitude
                directMagElmt=math.sqrt(Dx**2+Dy**2+Dz**2)
                if magnitude !=0:
                    cosang=((xforce*Dx+yforce*Dy+zforce*Dz)/(magnitude*directMagElmt))
                else:
                    vnode1.force=[0,0,0]
                    vnode2.force=[0,0,0]
                    continue
                forceParraElmts.append(magnitude*cosang*Dx/directMagElmt)
                forceParraElmts.append(magnitude*cosang*Dy/directMagElmt)
                forceParraElmts.append(magnitude*cosang*Dz/directMagElmt)
                
                forceVerdicalElmts.append(xforce-Dx/directMagElmt*magnitude*cosang)
                forceVerdicalElmts.append(yforce-Dy/directMagElmt*magnitude*cosang)
                forceVerdicalElmts.append(zforce-Dz/directMagElmt*magnitude*cosang)
                lentonode1=math.sqrt(
                (x1-vforce.position[0])**2
                +(y1-vforce.position[1])**2
                +(z1-vforce.position[2])**2
                )
                # length from node2 to force
                lentonode2=math.sqrt(
                (x2-vforce.position[0])**2
                +(y2-vforce.position[1])**2
                +(z2-vforce.position[2])**2
                )
        #for node1
                vnode1.force[0]=(vnode1.force[0]-(forceParraElmts[0]*lentonode2/directMagElmt)+(forceVerdicalElmts[0]*(lentonode2**2)*(directMagElmt+2*lentonode1)/(directMagElmt**3)))
                vnode1.force[1]=(vnode1.force[1]-(forceParraElmts[1]*lentonode2/directMagElmt)+(forceVerdicalElmts[1]*(lentonode2**2)*(directMagElmt+2*lentonode1)/(directMagElmt**3)))
                vnode1.force[2]=(vnode1.force[2]-(forceParraElmts[2]*lentonode2/directMagElmt)+(forceVerdicalElmts[2]*(lentonode2**2)*(directMagElmt+2*lentonode1)/(directMagElmt**3)))
        # for node2
                vnode2.force[0]=(vnode2.force[0]-forceParraElmts[0]*lentonode1/directMagElmt+(forceVerdicalElmts[0]*(lentonode1**2)*(directMagElmt+2*lentonode2)/(directMagElmt**3)))
                vnode2.force[1]=(vnode2.force[1]-forceParraElmts[1]*lentonode1/directMagElmt+(forceVerdicalElmts[1]*(lentonode1**2)*(directMagElmt+2*lentonode2)/(directMagElmt**3)))
                vnode2.force[2]=(vnode2.force[2]-forceParraElmts[2]*lentonode1/directMagElmt+(forceVerdicalElmts[2]*(lentonode1**2)*(directMagElmt+2*lentonode2)/(directMagElmt**3)))

def getBarDirection(nodeList,barList,forceList,curPoList):
    xVector=[1,0,0]
    yVector=[0,1,0]
    zVector=[0,0,1]
    for vforce in forceList:
        vbar=barList[int(vforce.barid)]
        vnode1=nodeList[int(vbar.nodeid1)]
        vnode2=nodeList[int(vbar.nodeid2)]
        x1=curPoList[int(vnode1.id)][0]
        x2=curPoList[int(vnode2.id)][0]
        y1=curPoList[int(vnode1.id)][1]
        y2=curPoList[int(vnode2.id)][1]
        z1=curPoList[int(vnode1.id)][2]
        z2=curPoList[int(vnode2.id)][2]
        # xbar=x1-x2
        # ybar=y1-y2
        # zbar=z1-z2
        xbar=x2-x1
        ybar=y2-y1
        zbar=z2-z1
        if vforce.loadway=='rotateVertical':
            # print vforce.id,vforce.nodeidertical
            # print "a node is rotate as vertical "
            if vforce.type==1:
                x = ((zVector[1] * zbar) - (zVector[2] * ybar))
                y = ((zVector[2] * xbar) - (zVector[0] * zbar))
                z = ((zVector[0] * ybar) - (zVector[1] * xbar))
                xCompnt=x/math.sqrt(x**2+y**2+z**2)
                yCompnt=y/math.sqrt(x**2+y**2+z**2)
                zCompnt=z/math.sqrt(x**2+y**2+z**2)
                # print xCompnt,yCompnt,zCompnt
                # print math.sqrt(xCompnt**2+yCompnt**2+zCompnt**2)
                vforce.direction[0]=xCompnt
                vforce.direction[1]=yCompnt
                vforce.direction[2]=zCompnt
    for vforce in forceList:
        if vforce.isNodeForce==True and (vforce.position[0]!=curPoList[vforce.nodeid][0] or vforce.position[1]!=curPoList[vforce.nodeid][1] or vforce.position[2]!=curPoList[vforce.nodeid][2]):
            # print (vforce.id,vforce.nodeid)
            vforce.position[0]=curPoList[vforce.nodeid][0]
            vforce.position[1]=curPoList[vforce.nodeid][1]
            vforce.position[2]=curPoList[vforce.nodeid][2]

def judgeFracture(extForce,bar,nodeList,curtime):#badly organized ,needs to be refactored, guess exPoList,curPoList,nxPoList could be wraped in a single class
    if bar.ExtrForce<extForce:
        # print bar.ExtrForce
        # print extForce
        node1=nodeList[bar.nodeid1]
        node2=nodeList[bar.nodeid2]
        position1=node1.position
        position2=node2.position
        if node1.type!=1:# for hinge and joina connection
            # print 'node '+str(node1.id)+'duanlie le '

            # print node1.id,node1.type
            node1.type=1
            node1.status=True
            return node1.id
        if node2.type!=1:
            # print 'node '+str(node2.id)+'duanlie le '
            # print node2.id,node2.type
            node2.type=1
            node2.status=True
            return node2.id
    return -1

def resetCooord(exPoList,curPoList,nodeList,nodeid,step):
    exPoList[nodeid][0]=curPoList[nodeid][0]+(nodeList[nodeid].force[0]-nodeList[nodeid].inforce[0])/nodeList[nodeid].mass/2*step**2
    exPoList[nodeid][1]=curPoList[nodeid][1]+(nodeList[nodeid].force[1]-nodeList[nodeid].inforce[1])/nodeList[nodeid].mass/2*step**2
    exPoList[nodeid][2]=curPoList[nodeid][2]+(nodeList[nodeid].force[2]-nodeList[nodeid].inforce[2])/nodeList[nodeid].mass/2*step**2
    nodeList[nodeid].status=False

# filename = input('please type file name:\n')
filename = sys.argv[1]
myinput = open(filename)
#myinput = open("newdata2.txt")
aString = myinput.readline()
aString = myinput.readline()
Times=0
initial_velocity=0# it is needed for further development
step=10000
mass=0
length=0
count=0
Young=0
density=0 
terminalMoment=0
loading_Type=0
forceList=[] 
nodeList=[]
barList=[]
# get data from input file
# force id type x_posit y_posit z_posit x_direc y_direc z_direc directMag barid startTime endTime caculation_endTime ramp_time loadway
#           0    1         2           3           4            5         6             7            8             9        10              11                     12                         13              14
while aString[0] !='n' :
    aString=aString.rstrip()
    result=aString.split(' ')
    instForce=classMoudle.force(float(result[0]))
    Loading_Type=float(result[1])
    if Loading_Type == 1:# ramp force
        instForce.settype(Loading_Type,float(result[-2]))
    else:
        instForce.settype(Loading_Type,0)
    instForce.setposition(float(result[2]),float(result[3]),float(result[4]))
    instForce.setdirection(float(result[5]),float(result[6]),float(result[7]))
    instForce.setdirectMag(float(result[8]))
    instForce.setbarid(float(result[9]))
    instForce.setTime(float(result[10]),float(result[11]))
    instForce.setLoadWay(str(result[-1]))
    forceList.append(instForce)
    terminalMoment=float(result[-3])
    aString = myinput.readline()
aString = myinput.readline()
# node #id type posit.x posit.y posit.z mass
#             0     1      2           3         4        5
while aString[0] !='b':
    aString=aString.rstrip()
    result=aString.split(' ')
    instNode=classMoudle.node(float(result[0]))
    instNode.settype(float(result[1]))
    instNode.setposition(float(result[2]),float(result[3]),float(result[4]))
    instNode.setmass(float(result[5]))
    if len(result)>6:
        instNode.setnodeSlideDirect(float(result[6]),float(result[7]),float(result[8]))
    nodeList.append(instNode)
    aString = myinput.readline()
aString = myinput.readline()

while aString:
    aString=aString.rstrip()
    result=aString.split(' ')
    instBar=classMoudle.bar(float(result[0]))
    instBar.setdetial(float(result[1]),float(result[2]))
    instBar.setYoungM(float(result[3]))
    instBar.setArea(float(result[4]))
    instBar.setdensity(float(result[5]))
    if len(result)>6:
        instBar.setExtrForce(float(result[6]))
    barList.append(instBar)
    aString=myinput.readline()   
myinput.close()

for vforce in forceList:
    for vnode in nodeList:
        if vnode.position[0]==vforce.position[0] and vnode.position[1]==vforce.position[1] and vnode.position[2]==vforce.position[2]:
            vforce.setNodeForce(True,vnode.id)
            if int(vforce.type)==3:# needs to be further developed
                nodeList[forceList[int(vforce.id)].nodeid].force=[0,-1*abs(vforce.directMag),0]
            break
        else:
            vforce.setNodeForce(False,None)
# sys.exit()
mynodefile = open('C:\\Users\\Administrator\\Desktop\\v1\\node.txt','w')
myelementfile = open('C:\\Users\\Administrator\\Desktop\\v1\\element.txt','w')
# print '------------------'

#there are three datastructures here,nodeList,forceList,barList!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

step=0.01 
exPoList=[]
curPoList=[]
nxPoList=[]
exPosition=[] 
nxPosition=[] 
DampingFactor=0.2 
C1=1/(1+0.5*DampingFactor*step) 
C2=C1*(1-0.5*DampingFactor*step)
for vanode in nodeList:
    curPoList.append(vanode.position)
# for node in nodeList:
    # print node.status
# sys.exit()
for vanode in nodeList:
    if (vanode.type==1 or vanode.type==4):
        # print vanode.id,vanode.force
        exPosition=[]
        exPosition.append(vanode.position[0]+vanode.force[0]*step**2/vanode.mass/2)
        exPosition.append(vanode.position[1]+vanode.force[1]*step**2/vanode.mass/2)
        exPosition.append(vanode.position[2]+vanode.force[2]*step**2/vanode.mass/2)
        exPoList.append(exPosition)
    else:
        exPoList.append(vanode.position)
length=0
# sys.exit()
inforce=None
while step*Times<2.5:
    nxPoList=[]
    
    for node in nodeList:
        if node.type==1 and node.status is True: 
            # print "node id is ",node.id
            if node.id==3:
                # print step*Times
                resetCooord(exPoList,curPoList,nodeList,node.id,step)
    
    for vanode in nodeList:
            nxPosition=[]
            if (vanode.type==1 or vanode.type==4):
                # print vanode.id
                nxPosition.append(C1*step**2*(vanode.total_force[0])/vanode.mass+2*C1*curPoList[vanode.id][0]-C2*exPoList[vanode.id][0])
                nxPosition.append(C1*step**2*(vanode.total_force[1])/vanode.mass+2*C1*curPoList[vanode.id][1]-C2*exPoList[vanode.id][1])
                nxPosition.append(C1*step**2*(vanode.total_force[2])/vanode.mass+2*C1*curPoList[vanode.id][2]-C2*exPoList[vanode.id][2])
                nxPoList.append(nxPosition)
            else:
                nxPoList.append(vanode.position)
    # sys.exit()
    exPoList=curPoList
    curPoList=nxPoList
    getBarDirection(nodeList,barList,forceList,curPoList)
    # sys.exit()
    updateForceValue(forceList,nodeList,step*(Times+1),curPoList)
    for node in nodeList:
        for vabar in barList:
            if vabar.nodeid1==node.id:
                length=myMethod.getLength(curPoList[vabar.nodeid1],curPoList[vabar.nodeid2])
                deLength=myMethod.getLength(nodeList[vabar.nodeid1].position,nodeList[vabar.nodeid2].position)
                mag=(length-deLength)*vabar.area*vabar.YoungM/length
                # if mag>10:print mag,node.id
                x=curPoList[vabar.nodeid2][0]-curPoList[vabar.nodeid1][0]
                y=curPoList[vabar.nodeid2][1]-curPoList[vabar.nodeid1][1]
                z=curPoList[vabar.nodeid2][2]-curPoList[vabar.nodeid1][2]
                node.inforce[0]+=x/math.sqrt(x**2+y**2+z**2)*mag
                node.inforce[1]+=y/math.sqrt(x**2+y**2+z**2)*mag
                node.inforce[2]+=z/math.sqrt(x**2+y**2+z**2)*mag
                judgeFracture(mag,vabar,nodeList,step*Times)
            elif vabar.nodeid2==node.id:
                length=myMethod.getLength(curPoList[vabar.nodeid1],curPoList[vabar.nodeid2])
                deLength=myMethod.getLength(nodeList[vabar.nodeid1].position,nodeList[vabar.nodeid2].position)
                mag=(length-deLength)*vabar.area*vabar.YoungM/length
                # if mag>10:print mag,node.id
                x=curPoList[vabar.nodeid1][0]-curPoList[vabar.nodeid2][0]
                y=curPoList[vabar.nodeid1][1]-curPoList[vabar.nodeid2][1]
                z=curPoList[vabar.nodeid1][2]-curPoList[vabar.nodeid2][2]
                node.inforce[0]+=x/math.sqrt(x**2+y**2+z**2)*mag
                node.inforce[1]+=y/math.sqrt(x**2+y**2+z**2)*mag
                node.inforce[2]+=z/math.sqrt(x**2+y**2+z**2)*mag
                judgeFracture(mag,vabar,nodeList,step*Times)
        node.total_force[0]=node.force[0]+node.inforce[0]
        node.total_force[1]=node.force[1]+node.inforce[1]
        node.total_force[2]=node.force[2]+node.inforce[2]
        if(node.type==4):
            SlideDirMag=math.sqrt(node.slideDirect[0]**2+node.slideDirect[1]**2+node.slideDirect[2]**2)    
            slideForceMag=(node.total_force[0]*node.slideDirect[0]+node.total_force[1]*node.slideDirect[1]+node.total_force[2]*node.slideDirect[2])/SlideDirMag    
            node.total_force[0]=slideForceMag*node.slideDirect[0]
            node.total_force[1]=slideForceMag*node.slideDirect[1]
            node.total_force[2]=slideForceMag*node.slideDirect[2]
        node.inforce=[0,0,0]
    for vanode in nodeList:
        writeout='  '
        writeout+=str(myMethod.setFormat(step*Times))
        writeout+='  '
        writeout+=str(myMethod.setFormat(vanode.id))
        writeout+='  '
        writeout+=str(myMethod.setFormat(exPoList[vanode.id][0]))
        writeout+='  '
        writeout+=str(myMethod.setFormat(exPoList[vanode.id][1]))
        writeout+='  '
        writeout+=str(myMethod.setFormat(exPoList[vanode.id][2]))
        writeout+='\n'
        mynodefile.write(writeout)
    for vabar in barList:
        writeout='  bar'
        writeout+='  '
        writeout+=str(myMethod.setFormat(step*Times))
        writeout+='  '
        writeout+=str(myMethod.setFormat(vabar.id))
        writeout+='  '
        writeout+=str(myMethod.setFormat(vabar.nodeid1))
        writeout+='  '
        writeout+=str(myMethod.setFormat(vabar.nodeid2))
        writeout+='\n'
        myelementfile.write(writeout)
    Times=Times+1


mynodefile.flush()
myelementfile.flush()
mynodefile.close()
myelementfile.close()
