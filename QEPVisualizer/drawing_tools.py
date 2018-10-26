import json
from tkinter import *
from Node import Node


NODE_WIDTH=50
NODE_HEIGHT=50
CANVAS_WIDTH=1000
CANVAS_HEIGHT=1000
node_list=[]
instance_dict={}

def clicked(event,canvas):
    '''
    Evoked when nodes or text is clicked
    '''
    #TODO: Add a pop-up about information when clicked


    node=node_list[event.widget.find_withtag("current")[0]-1]
    if node in instance_dict.keys():
        canvas.delete(instance_dict[node])
        del instance_dict[node]
    else:
        instance=canvas.create_text((node.center[0]-(NODE_WIDTH/2),node.center[1]-(NODE_HEIGHT/2)),text="How are you")
        instance_dict[node]=instance





def draw_query_plan(path):
    '''
    Main method to be called to draw query plan
    :param path:path to json file

    '''
    data = open(path).read()
    data = json.loads(data)[0]["Plan"]
    node_list.append(Node(0, CANVAS_WIDTH,10, NODE_HEIGHT))
    build_node_list(data, node_list[0])
    # actual drawing
    root = Tk()
    root.geometry("1000x1000")
    canvas = Canvas(root, width=1000, height=1000)
    canvas.pack()
    # 3 different for loops are needed for logical binding of rectangles in the node_list
    for element in node_list:
        x = element.center[0]
        y = element.center[1]
        rect=canvas.create_rectangle(x - NODE_WIDTH / 2, y + NODE_HEIGHT / 2, x + NODE_WIDTH / 2, y - NODE_HEIGHT / 2,
                                fill='grey',tags="clicked")

    for element in node_list:
        canvas.create_text((element.center[0], element.center[1]), text=element.text,tags="clicked")

    for element in node_list:
        for child in element.children:
            canvas.create_line(child.center[0], child.center[1] - NODE_HEIGHT / 2, element.center[0], element.center[1]+ NODE_HEIGHT / 2, arrow=LAST)

    canvas.tag_bind("clicked","<Button-1>", lambda event:clicked(event,canvas=canvas))

    root.mainloop()




def build_node_list(plan, obj):
    '''
    BUilds the list of nodes by recursively calling itself
    :param plan: plan dictionary
    :param obj: corresponding node object of plan
    '''
    obj.text=plan["Node Type"]
    if "Plans" in plan.keys():
        x1, x2, y1, y2 = obj.x1, obj.x2, obj.y1, obj.y2
        no_children=len(plan['Plans'])
        for i in range(no_children):
            child_obj=Node((i)*((x2-x1)/no_children),(i+1)*((x2-x1)/no_children),y2+NODE_HEIGHT,y2+2*NODE_HEIGHT)
            node_list.append(child_obj)
            obj.children.append(child_obj)
            build_node_list(plan["Plans"][i], child_obj)
