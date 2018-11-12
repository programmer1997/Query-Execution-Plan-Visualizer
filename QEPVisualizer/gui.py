import json
import psycopg2
from explainQuery import getQuery
import drawing_tools
from tkinter import *

class QueryInteractor(Tk):
    
    def __init__(self, parent, host, port, dbname, user, password):
        
        Tk.__init__(self, parent)
        self.parent = parent
        self.minsize(width=450, height=360)
        self.initializeUI(host, port, dbname, user, password)

    def initializeUI(self, host, port, dbname, user, password):

        ## main UI

        self.label_host = Label(self, text="Host:", width=10, anchor="w")
        self.label_host.grid(column=0, row=0, columnspan=1, sticky='W')
        self.entry_var_host = StringVar()
        self.entry_host = Entry(self, textvariable=self.entry_var_host)
        self.entry_host.grid(column=1, row=0, columnspan=1, sticky='EW')
        self.entry_var_host.set(host)

        self.label_database = Label(self, text="Database:", width=10, anchor="w")
        self.label_database.grid(column=0, row=1, columnspan=1, sticky='W')
        self.entry_var_database = StringVar()
        self.entry_database = Entry(self, textvariable=self.entry_var_database)
        self.entry_database.grid(column=1, row=1, columnspan=1, sticky='EW')
        self.entry_var_database.set(dbname)

        self.label_port = Label(self, text="Port:", width=10, anchor="w")
        self.label_port.grid(column=0, row=2, columnspan=1, sticky='W')
        self.entry_var_port = StringVar()
        self.entry_port = Entry(self, textvariable=self.entry_var_port)
        self.entry_port.grid(column=1, row=2, columnspan=1, sticky='EW')
        self.entry_var_port.set(port)

        self.label_username = Label(self, text="Username:", width=10, anchor="w")
        self.label_username.grid(column=0, row=3, columnspan=1, sticky='W')
        self.entry_var_username = StringVar()
        self.entry_username = Entry(self, textvariable=self.entry_var_username)
        self.entry_username.grid(column=1, row=3, columnspan=1, sticky='EW')
        self.entry_var_username.set(user)

        self.label_password = Label(self, text="Password:", width=10, anchor="w")
        self.label_password.grid(column=0, row=4, columnspan=1, sticky='W')
        self.entry_var_password = StringVar()
        self.entry_password = Entry(self, show='*', textvariable=self.entry_var_password)
        self.entry_password.grid(column=1, row=4, columnspan=1, sticky='EW')
        self.entry_var_password.set(password)

        self.label_query = Label(self, text="Query:", anchor="w")
        self.label_query.grid(column=0, row=5, columnspan=1, sticky='W')
        self.frame_query = Frame(self, borderwidth=1)
        self.frame_query.grid(column=1, row=5, columnspan=3, rowspan=1, sticky='W')
        self.button_query = Button(
            self.frame_query, text="Generate Query Execution Plan",
            width=30, command=self.explain_query)
        self.button_query.pack(side=BOTTOM)
        self.entry_query = Text(self.frame_query, height=10, wrap=WORD)
        self.entry_query.pack(side='left', fill='both', expand=True)
        self.scrollbar_query = Scrollbar(self.frame_query)
        self.entry_query.config(yscrollcommand=self.scrollbar_query.set)
        self.scrollbar_query.config(command=self.entry_query.yview)
        self.grid()
        self.scrollbar_query.pack(side='right', fill='y')

        # Get Query Execution Plan

    def explain_query(self):
        
        self.queryPlan = getQuery(self.entry_var_host.get(), self.entry_var_port.get(), 
        self.entry_var_database.get(), self.entry_var_username.get(), self.entry_var_password.get())
        query = self.entry_query.get("1.0", END)
        drawing_tools.draw_query_plan(self.queryPlan.getQueryPlan(query=query))

def main():
    
    config_path = "config.json"
    with open(config_path, "r") as conf_file:
        conf = json.load(conf_file)

    db_conf = conf["db"]
    app = QueryInteractor(
        None,
        host=db_conf["host"],
        port=db_conf["port"],
        dbname=db_conf["database"],
        user=db_conf["username"],
        password=db_conf["password"]
    )
    app.title('Postgres Query Interactor')
    app.mainloop()

if __name__ == "__main__":
    main()