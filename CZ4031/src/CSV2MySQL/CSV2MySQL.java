import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.sql.*;



public class CSV2MySQL {




    public static void main(String[] args){
        try {
            // connecting to the database
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/CZ4031", "root", "CZ4031admin");


            BufferedReader bufferedReader=new BufferedReader(new FileReader("/Users/Ajinkya/Desktop/CSV/Book.csv"));
            String line;
            String query="insert into book(pubid,title,series,publisher) values(?,?,?,?)";
            PreparedStatement p;
            p=connection.prepareStatement(query);
            int count=0;
          while(( line=bufferedReader.readLine())!=null){

                ++count;
                String[] arr=line.split(",");
                p.setInt(1,Integer.parseInt(arr[0]));
                if(arr.length<2){
                    p.setNull(2,Types.BIGINT);
                }
                else p.setString(2,arr[1]);
                if(arr.length<3){
                    p.setNull(3,Types.BIGINT);
                }
                else p.setString(3,arr[2]);
              if(arr.length<4){
                  p.setNull(4,Types.BIGINT);
              }
              else p.setString(4,arr[3]);
                p.addBatch();
                if (count%10000==0){

                    System.out.println(count);
                    p.executeBatch();
                    p.clearParameters();


                }


            }

            connection.setAutoCommit(false);
            connection.commit();
            p.addBatch();
            connection.commit();
            connection.close();




        }
        catch (SQLException sqlexception){
            sqlexception.printStackTrace();
        }
        catch (ClassNotFoundException cnf){
            cnf.printStackTrace();
        }
        catch (IllegalAccessException iae){
            iae.printStackTrace();
        }
        catch(InstantiationException ie){
            ie.printStackTrace();
        }
        catch(FileNotFoundException fnf){
            fnf.printStackTrace();
        }
        catch (IOException io){
            io.printStackTrace();
        }




    }
}


