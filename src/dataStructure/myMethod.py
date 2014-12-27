#coding=utf-8
import math
def setFormat(value):
    
    mystr=format(value,'.15e')
    #if(len(mystr)!=22):
    mystr=mystr[:-4]+'E'+mystr[-3:]   #用科学计数法表示输出结果
    mystr=mystr[:-2]+'0'+mystr[-2:]
    
    return mystr

def getLength(value1,value2):
    """
    print('fuc')
    print(value1[0])
    print(value1[1])
    print(value1[2])
    print(value2[0])
    print(value2[1])
    print(value2[2])
    print('endfuc')
    """
    return math.sqrt((value1[0]-value2[0])**2
                     +(value1[1]-value2[1])**2
                     +(value1[2]-value2[2])**2
        )

def ramp_step(startTime,rtime,time):
    if time<startTime:
        return 0
    else:
        if time>rtime:
            return 1.0
        else:
            return time/rtime
# step=0.01
# Times=0
# while step*Times<50:
    # print ramp_step(0,50,step*Times)
    # Times=Times+1

