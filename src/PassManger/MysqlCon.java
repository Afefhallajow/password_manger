package PassManger;

import java.sql.*;
import java.util.ArrayList;


class MysqlCon {
Connection con;
   MysqlCon() throws ClassNotFoundException, SQLException {
       Class.forName("com.mysql.jdbc.Driver");
        con=DriverManager.getConnection(
               "jdbc:mysql://localhost:3306/passmanger","root","");
//here sonoo is database name, root is username and password


   }


    public String deletepass(String username)
    {
        try{
//here sonoo is database name, root is username and password
            Statement stmt=this.con.createStatement();
            int id=0;
            String afef,pass;
            afef="ssss";
            pass="affff";
            ResultSet rs=stmt.executeQuery("select * from pass where password = "+"'"+username+"';" );
            if(             rs.next()) {
                id = rs.getInt("id");
                System.out.println(rs.getString(2));
                stmt.executeUpdate("DELETE FROM `pass` WHERE `pass`.`id` =" + id);
            }else
                System.out.println("notfound");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "done";
    }


    public String savepass(String Password,String Description,String address,String username)

{ try{
//here sonoo is database name, root is username and password
    Statement stmt=this.con.createStatement();
    int id=0;
    ResultSet rs=stmt.executeQuery("select * from pass ");
    while (rs.next())
        id= rs.getInt("id");
    id++;
    stmt.executeUpdate("INSERT INTO `pass` (`id`,`password`, `description`, `address`, `username`) VALUES (" +
            "('"+id+"'),"
            + "("+"'"+Password+"'"+"),("+"'"+Description+"'"+"),"+"("+"'"+address+"'"+")," +
           "('"+ username+"'));");


} catch (SQLException e) {
    e.printStackTrace();
}

    return "";
}
public ArrayList<Pass> getpassbyusername( String username) throws SQLException {ArrayList<Pass> list=new ArrayList<Pass>();

    Statement stmt=this.con.createStatement();
    ResultSet rs=stmt.executeQuery("select * from pass where username = "+"'"+username+"';" );
    while (             rs.next()) {
    Pass pass=new Pass();
    pass.setId(rs.getInt("id"));
    pass.setPassword(rs.getString("password"));
    pass.setAddress(rs.getString("address"));
pass.setDescription(rs.getString("description"));
    list.add(pass);
    }
        return  list;
}
    public String updatepass(int id,int oldid,String password,String description ,String address) throws SQLException {
        Statement stm=this.con.createStatement();
        stm.executeUpdate("UPDATE `pass` SET `id` ="+
                "'"+id+"',"+
                "`password` = '"+password+"',"+
                "`description` = '"+description+"',"
                +"`address` = '"+address+"'"+

                "  WHERE `pass`.`id` = "+oldid+";");

        return "";
    }
    public Pass getpassbyname(String password,String usename) throws SQLException, ClassNotFoundException {
        Statement stmt=this.con.createStatement();

User user = new User();
String password1 =user.symmetric.encode(password,user.getKey());
        ResultSet rs=stmt.executeQuery("select * from pass where (password = "+"'"+password1+"') && (username ="+"'"+usename+"'"+");" );
        if(             rs.next()) {
            int id = rs.getInt("id");
            Pass pass=new Pass();
            pass.setId(rs.getInt("id"));
            pass.setPassword(user.symmetric.decode(rs.getString("password"),user.getKey()));
            pass.setAddress(user.symmetric.decode( rs.getString("address"),user.getKey()));
            pass.setDescription( user.symmetric.decode(rs.getString("description"),user.getKey()));
            return pass;
        }else
            return null;
    }
    public ArrayList<Pass> findallpass() throws SQLException {
        Statement stmt=this.con.createStatement();
        ArrayList<Pass> passes=new ArrayList<>();

        ResultSet rs=stmt.executeQuery("select * from pass" );
        int i=0;
        while (             rs.next()) {
            int id = rs.getInt("id");
            Pass pass=new Pass();
            pass.setId(id);
            pass.setPassword(rs.getString("password"));
            pass.setAddress(rs.getString("address"));
            pass.setDescription( rs.getString("description"));
            passes.add(i,pass);
            i++;
        }
        return passes;


    }
/////////////////////////////////////////////////puplicKey//////////////////////////
    ////////////////////////////////////////////////////////////////////
public String savekey(String encode,String username)

{ try{
//here sonoo is database name, root is username and password
    Statement stmt=this.con.createStatement();
    int id=0;
    ResultSet rs=stmt.executeQuery("select * from publickey ");
    while (rs.next())
        id= rs.getInt("id");
    id++;
    stmt.executeUpdate("INSERT INTO `publickey` (`id`,`encode`, `username`) VALUES (" +
            "('"+id+"'),"
            + "("+"'"+encode+"'"+")," +
            "('"+ username+"'));");


} catch (SQLException e) {
    e.printStackTrace();
}

    return "";
}
    public String getkeybyname(String username) throws SQLException {
        Statement stmt=this.con.createStatement();


        ResultSet rs=stmt.executeQuery("select * from publickey where username = "+"'"+username+"';" );
        if(             rs.next()) {
            int id = rs.getInt("id");
          String encode=rs.getString("encode");
            // pass=new Pass();
            return encode;
        }else
            return null;
    }

    /////////////////////////////////////////////////////////////////////////////
///////////////////////// USER Dao///////////////////////////////////////////

   public String save(String username,String password)
   {
       try{
//here sonoo is database name, root is username and password
           Statement stmt=this.con.createStatement();
           int id=0;
           String afef,pass;
           afef="ssss";
           pass="affff";
                    ResultSet rs=stmt.executeQuery("select * from user ");
while (rs.next())
  id= rs.getInt("id");
id++;
stmt.executeUpdate("INSERT INTO `user` (`id`, `name`, `password`) VALUES (" +
                   "('"+id+"'),"
                   + "("+"'"+username+"'"+"),("+"'"+password+"'"+"));");


        } catch (SQLException e) {
           e.printStackTrace();
       }
       return "done";
   }

    public String delete(String username)
    {
        try{
//here sonoo is database name, root is username and password
            Statement stmt=this.con.createStatement();
            int id=0;
            String afef,pass;
            afef="ssss";
            pass="affff";
            ResultSet rs=stmt.executeQuery("select * from user where name = "+"'"+username+"';" );
if(             rs.next()) {
    id = rs.getInt("id");
    System.out.println(rs.getString(2));
    stmt.executeUpdate("DELETE FROM `user` WHERE `user`.`id` =" + id);
}else
    System.out.println("notfound");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "done";
    }

public String update(int id,int oldid,String name,String password) throws SQLException {
 Statement stm=this.con.createStatement();
 stm.executeUpdate("UPDATE `user` SET `id` ="+
         "'"+id+"',"+
         "`name` = '"+name+"',"+
         "`password` = '"+password+"'"
         +
       "  WHERE `user`.`id` = "+oldid+";");

       return "";
}

    public ArrayList<User> findall() throws SQLException, ClassNotFoundException {
        Statement stmt=this.con.createStatement();
        ArrayList<User> passes=new ArrayList<>();

        ResultSet rs=stmt.executeQuery("select * from user" );
        int i=0;
        while (             rs.next()) {
            int id = rs.getInt("id");
            User user=new User();
            user.setId(id);
            user.setPassword(rs.getString("password"));
          user.setUsername(rs.getString("name"));

            passes.add(i,user);
            i++;
        }
        return passes;


    }

    public User getbyname(String password) throws SQLException, ClassNotFoundException {
        Statement stmt=this.con.createStatement();


        ResultSet rs=stmt.executeQuery("select * from user where name = "+"'"+password+"';" );
        if(             rs.next()) {
            int id = rs.getInt("id");
            User user=new User();
            user.setUsername(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            user.setId( rs.getInt("id"));
            return user;
        }else
            return null;
    }
//////////////////////////////////////////// //////////////////////////////////////
    //////////////////////////Sharewith//////////////////////////
    public String savesharewith(Pass pass,String Sender,String Reciver) throws SQLException, ClassNotFoundException {
        Statement stmt=this.con.createStatement();
User user=new User();
String Se=user.symmetric.encode(Sender,user.getKey());
        String re=user.symmetric.encode(Reciver,user.getKey());
        String pa=user.symmetric.encode(pass.getPassword(),user.getKey());



        int id=0;
        ResultSet rs=stmt.executeQuery("select * from share ");

        while (rs.next())
               id= rs.getInt("id");
        id++;
        stmt.executeUpdate("INSERT INTO `share` (`id`,`sender`, `receiver` ,`pass_id`,`pass_name`,`accept`) VALUES (" +
                "('"+id+"'),"
                + "("+"'"+Se+"'"+")," +
                 "("+"'"+re+"'"+")," +
                 "("+"'"+pass.getId()+"'"+")," +
                 "("+"'"+pa+"'"+")," +
                "('"+2+"')"
                +");");



    return "";
}
    public ArrayList<sharewith> getsharewithbyname(String username) throws SQLException, ClassNotFoundException {
        Statement stmt=this.con.createStatement();

        User user=new User();
String username1= user.symmetric.encode(username, user.getKey());
        ResultSet rs=stmt.executeQuery("select * from share where (receiver = "+ "'"+username1 +"') && ( accept = '2')" );
        ArrayList<sharewith> list=new ArrayList<>();
        int i=0;
        sharewith sharewith = new sharewith();


        while (rs.next()) {

 int id=rs.getInt(1);
 String sender=rs.getString(2);
 String recever=rs.getString(3);
 String pass_name=rs.getString(5);
int pass_id=rs.getInt(4);
        System.out.println(rs.getInt(1)+""+rs.getString(2)+""+recever);
            String Se=user.symmetric.decode(sender,user.getKey());
            String re=user.symmetric.decode(recever,user.getKey());
            String pa=user.symmetric.decode(pass_name,user.getKey());

           sharewith.id=id;
            sharewith.pass.setId(pass_id);
//System.out.println();
            sharewith.pass.setPassword(pa);
            sharewith.ReciveUsername = re;
            sharewith.SenderUsername = Se;
list.add(i,sharewith);
i++;
        }  // pass=new Pass();
            return list;
    }
    public String updatesharewith(int oldid,int accept) throws SQLException {
        Statement stm=this.con.createStatement();
        stm.executeUpdate("UPDATE `share` SET `accept` ="+
                "'"+accept+"'"+

                "  WHERE `share`.`id` = "+oldid+";");

        return "";
    }
    public ArrayList<sharewith> getsharepassbyname(String username) throws SQLException, ClassNotFoundException {
        Statement stmt=this.con.createStatement();

        User user=new User();
        String username1= user.symmetric.encode(username, user.getKey());

        ResultSet rs=stmt.executeQuery("select * from share where (receiver = "+ "'"+username1 +"') && ( accept = '1')" );
        ArrayList<sharewith> list=new ArrayList<>();
        int i=0;
        sharewith sharewith = new sharewith();

///User user =new User();
        while (rs.next()) {

            int id=rs.getInt(1);
            String sender=rs.getString(2);
            String recever=rs.getString(3);
            String pass_name=rs.getString(5);
            int pass_id=rs.getInt(4);
            System.out.println(rs.getInt(1)+""+rs.getString(2)+""+recever);
            sharewith.id=id;
            //sharewith.pass.setId(pass_id);
//System.out.println();
           // sharewith.pass.setPassword(pass_name);

            String Se=user.symmetric.decode(sender,user.getKey());
            String re=user.symmetric.decode(recever,user.getKey());
            String pa=user.symmetric.decode(pass_name,user.getKey());


            sharewith.setPass(this.getpassbyname( pa,Se));
            sharewith.ReciveUsername = re;
            sharewith.SenderUsername = Se;
            list.add(i,sharewith);
            i++;
        }  // pass=new Pass();
        return list;
    }


    public static void main(String args[]) throws SQLException, ClassNotFoundException {
MysqlCon mysqlCon = new MysqlCon();
     //      mysqlCon.savekey("Afasf;fvl,clx;vlxcvxvxvxc","afef");
//System.out.println(mysqlCon.getkeybyname("afef"));
Pass pass =new Pass();
pass.setPassword("sadasd");
pass.setUsername("afef");
pass.setId(1);
pass=mysqlCon.getpassbyname("ola","afef");
System.out.println(pass.getDescription());
System.out.println( mysqlCon.getsharepassbyname("afef").get(0).pass.getDescription());
       //    mysqlCon.savesharewith(pass,"afef","ahmad");
//System.out.println( mysqlCon.getsharewithbyname("ahmad").toString());
//mysqlCon.getsharewithbyname("ahmad");
mysqlCon.updatesharewith(1,1);
/**     try{
//here sonoo is database name, root is username and password
            Statement stmt=mysqlCon.con.createStatement();
            int na=11;
            String afef,pass;
            afef="ssss";
            pass="affff";
   //         ResultSet rs=stmt.executeQuery("select * from illnesses where symptom_id > "+na);
            stmt.executeUpdate("INSERT INTO `user` (`id`, `name`, `password`) VALUES (('32'),+"+ "("+"'"+afef+"'"+"),("+"'"+pass+"'"+"));");

            //Array name= rs.getArray("name");
//System.out.println(     name.getArray());
          /**  while(rs.next()) {
                System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getInt(5));
                System.out.println(     rs.getString("name"));

            }
          mysqlCon.con.close();

        }catch(Exception e){ System.out.println(e);}
    **/

    }
}
