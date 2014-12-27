#coding=utf-8
class force:
    def __init__(self,value):
        self.id=value   #对力进行编号
        self.time=50    #按照书上例5-1，斜坡加载的加载时间为50s
        
    def settype(self,value1,value2):
        self.type=value1 #1 for ramped loading,2 for sudden loading，3 for gravity 外力的两种加载手段：斜坡加载和突然加载
        self.rtime=value2
        
    def setposition(self,x,y,z):
        self.position=[x,y,z] #集中力的初始作用点
        
    def setdirection(self,x,y,z):
        self.direction=[x,y,z]  #集中力初始方向
        
    def setbarid(self,value):
        self.barid=value  #力所作用的杆件的id
        
    def setTime(self,value1,value2):
        self.startTime=value1
        self.endTime=value2
        self.duaration=value2-value1
    def setLoadWay(self,value):
        self.loadway=value
    def setdirectMag(self,value):
        self.directMag=value
    def setNodeForce(self,boolvalue,nodeid):
        self.isNodeForce=boolvalue
        self.nodeid=nodeid
class node:
    def __init__(self,value):
        self.id=int(value)  #对节点进行编号
        self.force=[0,0,0]  #直接作用于点的集中力大小，这里默认为0，可用程序后面作用于杆件的力处于杆件边缘的情况代替此力
        self.total_force=[0,0,0]
        self.inforce=[0,0,0]
        self.status=False
       # self.slideDirect=[0,0,0]
    def settype(self,value):
        self.type=value #1 for unfixed node, 2 for hinge joint,3 for  rigid joint，4 for slideDirect
    def setposition(self,x,y,z):
        self.position=[x,y,z] #点的位置
    def setforce(self,value):#value must be the instance of force 等效力
        self.force=value  #value是个三元数组
    def setmass(self,value):
        self.mass=value  #点的质量
    def setRestrain(self,value):
        self.restrain=[0,0,0]
        self.restrain=value
        if self.type == 1:
            pass # needs futher discussion
        if self.type == 2:
            pass # 
        if self.type == 3:
            pass #
    def setnodeForce(self,value):
        self.nodeforce=value
    def setnodeSlideDirect(self,x,y,z):
        self.slideDirect=[x,y,z]
    def setStatus(self,status):
        self.status=status
class bar:
    def __init__(self,value):
        self.id=value  #对杆件进行编号
    def setdetial(self,type1,type2):
        self.nodeid1=int(type1)  #杆件编号与杆件两端点编号对应
        self.nodeid2=int(type2)
    def setYoungM(self,value):
        self.YoungM=value  #杨氏模量，用于计算的常数，公式中的E
    def setArea(self,value):
        self.area=value   #截面积，用于计算的常数，公式中的A
    def setdensity(self,value):
        self.density=value   #杆件密度，用于计算的常数，公式中的ρ
    def setExtrForce(self,force):
        self.ExtrForce=force
