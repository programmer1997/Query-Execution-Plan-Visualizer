import json
import os
from tkinter import *
from Node import Node

NODE_WIDTH = 90
NODE_HEIGHT = 65
CANVAS_WIDTH = 1000
CANVAS_HEIGHT = 1000
node_list = []
# Maps GUI elements to logical elements
visual_to_node = {}
instance = None
MAX_DURATION=0

def enter(event,canvas):
    node = visual_to_node[event.widget.find_withtag("current")[0]]
    global instance
    if node.duration == MAX_DURATION:
        instance = canvas.create_text(200, 50, text=node.plan_info,
                                      fill="red", width=350)
    else:
        instance = canvas.create_text(200, 50, text=node.plan_info,
                                      fill="blue", width=350)

def leave(event,canvas):
    canvas.delete(instance)

def help(root):
    help_info = open("help_info.txt").read()
    help_window = Toplevel(root)
    help_window.geometry("800x820")
    help_window.title("Help")
    help_canvas = Canvas(help_window, width=800, height=820)
    help_canvas.pack()
    help_canvas.create_text(400, 420, text=help_info, width=800)

# def clicked(event, canvas):
#     '''
#     Evoked when nodes or text is clicked
#     '''

#     global instance
#     node = visual_to_node[event.widget.find_withtag("current")[0]]

#     if instance != None: canvas.delete(instance)
#     if node.duration==MAX_DURATION:
#         instance = canvas.create_text(200,50, text=node.plan_info,
#                                       fill="red", width=350)
#     else:
#         instance = canvas.create_text(200,50, text=node.plan_info,
#                                       fill="blue", width=350)

def draw_query_plan(data):
    '''
    Main method to be called to draw query plan
    :param data: json object
    '''
    node_list.clear()
    node_list.append(Node(0, CANVAS_WIDTH,10, NODE_HEIGHT))
    build_node_list(data, node_list[0])
    global MAX_DURATION
    MAX_DURATION=max([x.duration for x in node_list])
    # actual drawing
    root = Tk()
    root.geometry("1000x1000")
    root.title("Query execution plan")
    frame=Frame(root,width=1000,height=1000)
    frame.pack()
    canvas = Canvas(frame, width=CANVAS_WIDTH, height=CANVAS_HEIGHT,scrollregion=(0,0,1000,1500))
    vbar = Scrollbar(frame, orient=VERTICAL)
    vbar.pack(side=RIGHT, fill=Y)
    vbar.config(command=canvas.yview)
    canvas.config(yscrollcommand=vbar.set)
    canvas.pack()
    canvas2 = Canvas(frame, width=400, height=100)
    canvas2.pack()
    canvas2.place(x=600,y=0)
    canvas3 = Canvas(frame, width=100, height=50)
    canvas3.pack()
    help_button = Button(canvas3, command=lambda: help(root), text="Help", anchor=W)
    help_button.configure(width=5, activebackground="#33B5E5", relief=FLAT)
    canvas3.create_window(10, 10, anchor=NW, window=help_button)
    canvas3.place(x=0, y=0)
    Misc.lift(canvas2)
    Misc.lift(canvas3)
    Misc.lift(vbar)

    # 3 different for loops are needed for logical binding of rectangles in the node_list
    for element in node_list:
        x = element.center[0]
        y = element.center[1]
        rect = canvas.create_rectangle(x - NODE_WIDTH / 2, y + NODE_HEIGHT / 2, x + NODE_WIDTH / 2, y - NODE_HEIGHT / 2,
                                       fill='grey', tags="hover")
        visual_to_node[rect] = element

    for element in node_list:
        gui_text = canvas.create_text((element.center[0], element.center[1]), text=element.text, tags="clicked")
        visual_to_node[gui_text] = element

    for element in node_list:
        for child in element.children:
            canvas.create_line(child.center[0], child.center[1] - NODE_HEIGHT / 2, element.center[0],
                               element.center[1] + NODE_HEIGHT / 2, arrow=LAST)

    # canvas.tag_bind("clicked", "<Button-1>", lambda event: clicked(event, canvas=canvas2))
    canvas.tag_bind("hover","<Enter>",lambda event:enter(event,canvas=canvas2))
    canvas.tag_bind("hover", "<Leave>", lambda event: leave(event,canvas=canvas2))
    root.mainloop()

def build_node_list(plan, obj):
    '''
    Builds the list of nodes by recursively calling itself
    :param plan: plan dictionary
    :param obj: corresponding node object of plan
    '''
    obj.text = plan["Node Type"]
    info=getInfo(plan)
    obj.plan_info =info[0]
    obj.duration=info[1]
    if "Plans" in plan.keys():
        x1, x2, y1, y2 = obj.x1, obj.x2, obj.y1, obj.y2
        no_children = len(plan['Plans'])
        for i in range(no_children):
            child_obj = Node((i) * ((x2 - x1) / no_children), (i + 1) * ((x2 - x1) / no_children), y2 + NODE_HEIGHT,
                             y2 + 2 * NODE_HEIGHT)
            node_list.append(child_obj)
            obj.children.append(child_obj)
            build_node_list(plan["Plans"][i], child_obj)

def getInfo(plan):
    '''
    Takes a particular plan and checks for node type to get info
    Returns plan info and duration taken
    :param plan: plan dictionary
    '''
    parsed_plan = ""  ## sentence/info to return

    if (plan["Node Type"] == "Aggregate"):
        if plan["Strategy"] == "Sorted":
            if "Group Key" in plan:
                parsed_plan += "The attributes used for grouping the result are "
                for group_key in plan["Group Key"]:
                    parsed_plan += group_key.replace("::text", "") + ", "
                parsed_plan = parsed_plan[:-2]
            if "Filter" in plan:
                parsed_plan += " and it is filtered using the condition " + plan["Filter"].replace("::text", "") + "."

        elif plan["Strategy"] == "Hashed":
            group_keys = ','.join([j.replace("::text", "")
                                   for j in plan["Group Key"]])
            parsed_plan += "All the rows are hashed based on the key {},".format(group_keys)
            parsed_plan = parsed_plan[:-1] + '.'

        elif plan["Strategy"] == "Plain":
            parsed_plan = 'Aggregate'
        duration = float(plan['Actual Total Time']) - float(plan['Actual Startup Time'])
        parsed_plan += "\nDuration taken: " + str(round(duration, 5)) + "ms"

    elif plan["Node Type"] == "Append":

        parsed_plan += "The results of the scan are appended."
        duration = float(plan['Actual Total Time']) - float(plan['Actual Startup Time'])
        parsed_plan += "\nDuration taken: " + str(round(duration, 5)) + "ms"

    elif plan["Node Type"] == "CTE Scan":

        parsed_plan = "The table is scanned using CTE scan "
        parsed_plan += str(plan["CTE Name"]) + " which is stored in memory "

        if "Index Cond" in plan:
            parsed_plan += " with condition(s) " + \
                           plan["Index Cond"].replace('::text', '') + "."

        elif "Filter" in plan:
            parsed_plan += " the filtering condition applied is " + \
                           plan["Filter"].replace('::text', '') + "."

        duration = float(plan['Actual Total Time']) - float(plan['Actual Startup Time'])
        parsed_plan += "\nDuration taken: " + str(round(duration, 5)) + "ms"

    elif plan["Node Type"] == "Unique":
        parsed_plan = "Each row is scanned from the sorted data, and duplicate elements (from the preceeding row) are eliminated."

        duration = float(plan['Actual Total Time']) - float(plan['Actual Startup Time'])
        parsed_plan += "\nDuration taken: " + str(round(duration, 5)) + "ms"

    elif plan["Node Type"] == "Function Scan":
        parsed_plan = "The function {} is executed and the resulting set of tuples is returned.".format(
            plan["Function Name"])

        duration = float(plan['Actual Total Time']) - float(plan['Actual Startup Time'])
        parsed_plan += "\nDuration taken: " + str(round(duration, 5)) + "ms"

    elif plan["Node Type"] == "Group":
        group_keys = ",".join([j.replace("::text", "")
                               for j in plan["Group Key"][:-1]])
        parsed_plan += "The result from the previous operation is grouped together using the key "
        parsed_plan += group_keys + "."

        duration = float(plan['Actual Total Time']) - float(plan['Actual Startup Time'])
        parsed_plan += "\nDuration taken: " + str(round(duration, 5)) + "ms"

    elif plan["Node Type"] == "Hash":
        parsed_plan = "The hash function creates an in-memory hash with the tuples from the source."

        duration = float(plan['Actual Total Time']) - float(plan['Actual Startup Time'])
        parsed_plan += "\nDuration taken: " + str(round(duration, 5)) + "ms"

    elif plan["Node Type"] == "Hash Join":
        parsed_plan += "The result produced by the previous operation is joined using Hash {} Join".format(
            plan["Join Type"])
        if 'Hash Cond' in plan:
            parsed_plan += ' with condition {}.'.format(plan['Hash Cond'].replace("::text", ""))

        duration = float(plan['Actual Total Time']) - float(plan['Actual Startup Time'])
        parsed_plan += "\nDuration taken: " + str(round(duration, 5)) + "ms"

    elif plan["Node Type"] == "Index Scan":
        parsed_plan = "An index scan is performed using " + plan["Index Name"]
        if "Index Cond" in plan:
            parsed_plan += " with conditions {}".format(plan["Index Cond"].replace('::text', ''))
        if "Filter" in plan:
            parsed_plan += ".\nThe result is then filtered by {}.".format(plan["Filter"].replace('::text', ''))

        duration = float(plan['Actual Total Time']) - float(plan['Actual Startup Time'])
        parsed_plan += "\nDuration taken: " + str(round(duration, 5)) + "ms"

    elif plan["Node Type"] == "Index Only Scan":
        parsed_plan = "An index scan is performed using the index on {}".format(plan["Index Name"])
        if "Index Cond" in plan:
            parsed_plan += " with the condition(s) {}.".format(plan["Index Cond"].replace('::text', ''))
        if "Filter" in plan:
            parsed_plan += ".\nThe result is then filtered by {}.".format(plan["Filter"].replace('::text', ''))

        duration = float(plan['Actual Total Time']) - float(plan['Actual Startup Time'])
        parsed_plan += "\nDuration taken: " + str(round(duration, 5)) + "ms"

    elif plan["Node Type"] == "Limit":
        planrows = plan["Plan Rows"]
        parsed_plan += "The table scanning is done, but with a limitation of " + str(planrows) + " items."

        duration = float(plan['Actual Total Time']) - float(plan['Actual Startup Time'])
        parsed_plan += "\nDuration taken: " + str(round(duration, 5)) + "ms"

    elif plan["Node Type"] == "Materialize":
        parsed_plan = "Holding the results in memory enables efficient accessing."

        duration = float(plan['Actual Total Time']) - float(plan['Actual Startup Time'])
        parsed_plan += "\nDuration taken: " + str(round(duration, 5)) + "ms"

    elif plan["Node Type"] == "Merge Join":
        parsed_plan = "Merge join is employed to merge the preceeding result"
        if 'Merge Cond' in plan:
            parsed_plan += " with the following condition: " + plan['Merge Cond'].replace("::text", "") + "."
        if 'Join Type' == 'Semi':
            parsed_plan += " however, the only row returned is that from the left table."
        else:
            parsed_plan += "."

        duration = float(plan['Actual Total Time']) - float(plan['Actual Startup Time'])
        parsed_plan += "\nDuration taken: " + str(round(duration, 5)) + "ms"

    elif plan["Node Type"] == "Nested Loop":
        parsed_plan = "Nested Loop Join."

        duration = float(plan['Actual Total Time']) - float(plan['Actual Startup Time'])
        parsed_plan += "\nDuration taken: " + str(round(duration, 5)) + "ms"

    elif plan["Node Type"] == "Seq Scan":
        parsed_plan = "A sequential scan is performed on the relation "
        if "Relation Name" in plan:
            parsed_plan += plan['Relation Name']

        if "Alias" in plan:
            if plan['Relation Name'] != plan['Alias']:
                parsed_plan += " under the alias name "
                parsed_plan += plan['Alias']
        parsed_plan += "."

        if "Filter" in plan:
            parsed_plan += "\nThis is bounded by the condition, "
            parsed_plan += plan['Filter'].replace("::text", "")[1:-1]
            parsed_plan += "."

        duration = float(plan['Actual Total Time']) - float(plan['Actual Startup Time'])
        parsed_plan += "\nDuration taken: " + str(round(duration, 5)) + "ms"

    elif plan["Node Type"] == "Sort":
        parsed_plan = "The result is sorted on the attribute "
        if "DESC" in plan["Sort Key"]:
            parsed_plan += str(plan["Sort Key"].replace('DESC', '')) + " in desceding order."
        elif "INC" in plan["Sort Key"]:
            parsed_plan += str(plan["Sort Key"].replace('INC', '')) + " in increasing order."
        else:
            parsed_plan += str(plan["Sort Key"]) + "."

        duration = float(plan['Actual Total Time']) - float(plan['Actual Startup Time'])
        parsed_plan += "\nDuration taken: " + str(round(duration, 5)) + "ms"

    elif plan["Node Type"] == "Subquery Scan":
        parsed_plan = "A subquery scan is performed on the result from the previous operation."

        duration = float(plan['Actual Total Time']) - float(plan['Actual Startup Time'])
        parsed_plan += "\nDuration taken: " + str(round(duration, 5)) + "ms"

    elif plan["Node Type"] == "Values Scan":
        parsed_plan += "A scan through the given values from the query is performed."

        duration = float(plan['Actual Total Time']) - float(plan['Actual Startup Time'])
        parsed_plan += "\nDuration taken: " + str(round(duration, 5)) + "ms"

    else:
        parsed_plan = plan["Node Type"]

        duration = float(plan['Actual Total Time']) - float(plan['Actual Startup Time'])
        parsed_plan += "\nDuration taken: " + str(round(duration, 5)) + "ms"
    
    return (parsed_plan,duration)
