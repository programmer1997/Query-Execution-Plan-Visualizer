import psycopg2
import json

class getQuery():

    def __init__(self, host, port, dbname, user, password):

        self.conn = psycopg2.connect(host=host,database=dbname, user=user, password=password)
        self.cur = self.conn.cursor()

    def getQueryPlan(self, query=None):

        if query:
            self.query = query
            try:
                self.cur.execute("EXPLAIN (FORMAT JSON) " + self.query)
                plan = self.cur.fetchall()
                print(plan)
                self.query_plan = plan[0][0][0]["Plan"]
                return self.query_plan
                # with open('query.json', 'w') as outfile:
                #     json.dump(self.query_plan, outfile, indent = 4)
            except Exception as e:
                print ("\nError: %s" %str(e))
                self.conn.rollback()
        else:
            self.query_plan = "Failed to generate query plan!"




        