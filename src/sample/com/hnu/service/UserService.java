package sample.com.hnu.service;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import sample.com.hnu.pojo.User;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UserService
{
    //用户列表
    private static ArrayList<User> g_UserList = GetUp();

    //判断是否存在且正确
    public static User IsExist(String name,String password)
    {
        for (User user : g_UserList)
        {
            if(name.equals(user.getM_name())&&password.equals(user.getM_password()))
            {
                return user;
            }
        }
        return null;
    }

    //判断是否手机号是否已经存在
    public static boolean IsExistByPhone(String phone)
    {
        for (User user : g_UserList)
        {
            if(phone.equals(user.getM_phone()))
            {
                return true;
            }
        }
        return false;
    }

    //判断是否用户名是否已经存在
    public static boolean IsExistByName(String name)
    {
        for (User user : g_UserList)
        {
            if(name.equals(user.getM_name()))
            {
                return true;
            }
        }
        return false;
    }

    //添加用户
    public static void AddUser(User user)
    {
        g_UserList.add(user);
        UpdateJson();
    }

    //修改用户
    public static void ModifyUser(User user)
    {
        for (User user1 : g_UserList)
        {
            if(user1.getM_phone().equals(user.getM_phone()))
            {
               user1.setM_password(user.getM_password());
            }
        }
        UpdateJson();
    }

    //得到用户列表
    public static ArrayList GetUp()
    {
        FileReader fr;
        StringBuilder jsonString = new StringBuilder();
        try
        {
            String route = "./"+"messages/message.json"; //检索目录
            File file = new File(route);
            fr = new FileReader(file);
            char[] a = new char[1024];
            int num;
            while ((num=fr.read(a)) !=-1)
            {
                jsonString.append(new String(a, 0, num));
            }
            fr.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        ArrayList<User> UserList = new ArrayList<>();
        try
        {
            JsonToList(jsonString, UserList);
            //ArrayList upList = new ArrayList<>();
            return UserList;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    //json转list
    public static void JsonToList(StringBuilder jsonString, ArrayList<User> userList)
    {
        JSONArray jsonArray = new JSONArray();
        jsonArray = JSONArray.fromObject(jsonString.toString());
        for (Object o : jsonArray)
        {
            JSONObject jsonObject = (JSONObject) o;
            userList.add(new User(jsonObject.optString("m_phone"), jsonObject.optString("m_name"), jsonObject.optString("m_password")));
        }
    }

    //更新json
    public static void UpdateJson()
    {
        JSONArray ja = new JSONArray();
        ja = ja.fromObject(g_UserList);
        FileWriter fw;
        try
        {
            fw = new FileWriter("messages/message.json");
            ja.write(fw);
            fw.close();
        }
        catch (IOException e1)
        {
            // TODO 自动生成的 catch 块
            e1.printStackTrace();
        }
    }

    //错题集
    public static void WrongQuestionSet(User user,String problem,Double ans) throws IOException
    {
        Date date=new Date();
        SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String time=sim.format(date);
        String route = "messages/错题集/"+user.getM_name(); //在当前目录下自动生成文件夹
        File file = new File(route);
        if(!file.exists())
        {
            file.mkdir();
        }
        //先创建文件夹再创建.txt文件。
        route += "/"+time+".txt";
        file  = new File(route);
        if(!file.exists())
        {
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file,true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("原题:"+problem+"=?   参考答案为:"+ans);
        bw.write("\r\n");
        bw.write("\r\n");
        bw.close();
        fw.close();
    }
}