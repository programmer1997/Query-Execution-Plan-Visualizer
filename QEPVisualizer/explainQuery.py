import psycopg2
import json

class getQuery():

    def __init__(self, host, port, dbname, user, password):

        self.conn = psycopg2.connect(host=host,database=dbname, user=user, password=password)
        self.cur = self.conn.cursor()

    def getQueryPlan(self, query=None):
        
        self.query_plan = ""
        if query:
            self.query = query
            try:
                self.cur.execute("EXPLAIN (ANALYZE, FORMAT JSON) " + self.query)
                plan = self.cur.fetchall()
                self.query_plan = plan[0][0][0]["Plan"]
            except Exception as e:
                print ("\nError: %s" %str(e))
                self.conn.rollback()
        else:
            self.query_plan = "Failed to generate query plan!"

        return self.query_plan



        