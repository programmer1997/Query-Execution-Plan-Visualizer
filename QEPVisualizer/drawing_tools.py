import json
from tkinter import *
from Node import Node


NODE_WIDTH=50
NODE_HEIGHT=50
CANVAS_WIDTH=1000
CANVAS_HEIGHT=1000
node_list=[]


def clicked(*args):
    '''
    Evoked when nodes or text is clicked
    '''
    #TODO: Add a pop-up about information when clicked
    print("clicked")


def draw_query_plan(path):
    '''
    Main method to be called to draw query plan
    :param path:path to json file

    '''
    data = open(path).read()
    data = json.loads(data)[0]["Plan"]
    node_list.append(Node(0, CANVAS_WIDTH, 0, NODE_HEIGHT))
    build_node_list(data, node_list[0])
    # actual drawing
    root = Tk()
    root.geometry("1000x1000")
    canvas = Canvas(root, width=1000, height=1000)
    canvas.pack()
    for element in node_list:
        x = element.center[0]
        y = element.center[1]
        canvas.create_rectangle(x - NODE_WIDTH / 2, y + NODE_HEIGHT / 2, x + NODE_WIDTH / 2, y - NODE_HEIGHT / 2,
                                fill='grey', tags="click_event")
        canvas.create_text((x, y), text=element.text, tags="click_event")
        for child in element.children:
            canvas.create_line(child.center[0], child.center[1] - NODE_HEIGHT / 2, x, y + NODE_HEIGHT / 2, arrow=LAST)
    canvas.tag_bind("click_event", "<Button-1>", clicked)
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











