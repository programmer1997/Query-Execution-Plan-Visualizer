class Node:
    def __init__(self,x1,x2,y1,y2,text=""):
        self.x1=x1
        self.x2=x2
        self.y1=y1
        self.y2=y2
        self.text=text
        self.center=((x1+x2)/2,(y1+y2)/2)
        self.children=[]

    def add_child(self,child):
        self.children.append(child)
