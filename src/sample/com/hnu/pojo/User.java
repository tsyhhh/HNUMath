package sample.com.hnu.pojo;

public class User
{
    private String m_name;
    private String m_password;
    private String m_phone;

    public User(String m_phone)
    {
        this.m_phone = m_phone;
    }

    public User()
    {
        //空
    }

    public User(String m_name, String m_password)
    {
        this.m_name = m_name;
        this.m_password = m_password;
    }

    public User(String m_phone, String m_name, String m_password)
    {
        this.m_name = m_name;
        this.m_password = m_password;
        this.m_phone = m_phone;
    }


    /**
     * 由于需要使用json库将用户信息转换成json保存在文件夹下,而java函数命名的一般规范首字母是不大写的，
     * 而json库中的接口刚好需要检测类的get,set方法,所以如果将get,set的首字母大写接口将无法识别，
     * 所以这些方法只能用小写开头，经过老师同意符合规范，辛苦助教查阅检查了!
     */
    public String getM_name()
    {
        return m_name;
    }

    public void setM_name(String m_name)
    {
        this.m_name = m_name;
    }

    public String getM_password()
    {
        return m_password;
    }

    public void setM_password(String m_password)
    {
        this.m_password = m_password;
    }

    public String getM_phone()
    {
        return m_phone;
    }

    public void setM_phone(String m_phone)
    {
        this.m_phone = m_phone;
    }

    @Override
    public String toString()
    {
        return "User{" +
                "m_name='" + m_name + '\'' +
                ", m_password='" + m_password + '\'' +
                ", m_phone='" + m_phone + '\'' +
                '}';
    }
}

