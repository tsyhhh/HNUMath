package sample.com.hnu.service;

import java.util.regex.Pattern;

public class MathService
{
    static String[] s_Operators = {"+","-","*","/","²","√","sin","cos","tan"};

    public MathService()
    {
        //空
    }

    static String RandGrade(String grade)
    {
        int number_num = (int)(1+Math.random()*(5));
        int[] numbers = new int[5];
        int[] operators = new int[4];
        for(int i = 0; i < 5; i++)
        {
            numbers[i] = (int)(1+Math.random()*(100));
        }
        StringBuilder problem= new StringBuilder();
        if("小学".equals(grade))
        {
            number_num =  (int)(2+Math.random()*(4));//小学至少2个操作数
            for(int i = 0; i < number_num - 1; i++)
            {
                operators[i] = (int)(Math.random()*(4));
            }
        }

        else if("初中".equals(grade))
        {
            operators[0] = (int)(4+Math.random()*(2));
            for(int i = 1; i < number_num - 1; i++)
            {
                operators[i] = (int)(Math.random()*(6));
            }
        }

        else if("高中".equals(grade))
        {
            operators[0] = (int)(6+Math.random()*(3));
            for(int i = 1; i < number_num - 1; i++)
            {
                operators[i] = (int)(Math.random()*(9));
            }
        }
        if (number_num!=1)
        {
            for(int i = 0; i < number_num - 1; i++)
            {
                if(operators[i]<=4)//后置操作符，操作符放后面
                {
                    problem.append(numbers[i]);
                    problem.append(s_Operators[operators[i]]);

                    if(operators[i]==4)//平方，需要再加一个操作符
                    {
                        problem.append(s_Operators[(int) (Math.random() * (4))]);
                    }
                }

                else//前置操作符，操作符放前面，后面需要补充一个简单操作符
                {
                    problem.append(s_Operators[operators[i]]);
                    problem.append(numbers[i]);
                    problem.append(s_Operators[(int) (Math.random() * (4))]);
                }
            }
            problem.append(numbers[number_num - 1]);
        }

        else
        {
            if(operators[0]<=4)//后置操作符，操作符放后面
            {
                problem.append(numbers[0]);
                problem.append(s_Operators[operators[0]]);
            }

            else//前置操作符，操作符放前面
            {
                problem.append(s_Operators[operators[0]]);
                problem.append(numbers[0]);
            }
        }

        //随机生成括号
        for(int i = 0; i < number_num -2; i++)
        {
            if((int)(Math.random()*(2))==1)
            {
                problem = new StringBuilder(GetBrackets(problem.toString()));
                //break;
            }
        }
        return problem.toString();
    }

    //加括号
    static String GetBrackets(String problem_initial)
    {
        while (true)
        {
            String problem = problem_initial;
            int left_count = 0, right_count = 0 , Median;//对分别可能的位置计数
            int[] left_bracket = new int[6];//左括号最多可能的位置，5个数的前面以及起始位置
            int[] right_bracket = new int[6];//右括号最多可能的位置，5个数的后面以及结束位置

            //首先确定 ( 的位置
            left_bracket[left_count++] = 0;
            for(int i = 1; i < problem.length(); i++)
            {
                if(Character.isDigit(problem.charAt(i)) && ! (Character.isDigit(problem.charAt(i-1))) )
                {
                    left_bracket[left_count++] = i;
                }
            }
            String fs,bs;
            Median = (int)(Math.random()*(left_count-1));   //随机生成一个位置
            fs = problem.substring(0,left_bracket[Median]);
            bs = problem.substring(left_bracket[Median]);   //截取并插入
            problem = fs + "(" +bs;

            //然后确定 ) 的位置
            for(int i = left_bracket[Median] ; i < problem.length() - 1; i++)
            {
                if(Character.isDigit(problem.charAt(i)) && ! (Character.isDigit(problem.charAt(i+1))))
                {
                    right_bracket[right_count++] = i+1;
                }
            }
            right_bracket[right_count++] = problem.length();
            Median = (int)(1+Math.random()*(right_count-1));
            fs = problem.substring(0,right_bracket[Median]);
            bs = problem.substring(right_bracket[Median]);  //截取并插入
            problem = fs + ")" +bs;

            if(IsRight(problem))
            {
                return problem;
            }
            else return problem_initial;//不正确或者不能再加入括号了，直接返回即可
        }
    }

    //括号合理性检测
    static boolean IsRight(String problem)
    {
        String leftDouble="((", rightDouble="))";
        int left = 0,right = 0;
        if(problem.charAt(0) == '(' && problem.charAt(problem.length()-1) == ')')    //全包含
        {
            return false;
        }
        if(problem.contains(leftDouble) && problem.contains(rightDouble))      //两个括号重叠
        {
            return false;
        }
        for(int i = 0; i < problem.length(); i++)
        {
            String temp;
            if(problem.charAt(i) == '(')
            {
                left = i;
            }
            if(problem.charAt(i) == ')')
            {
                right = i;
            }
            if(left<right)
            {
                temp = problem.substring(left+1,right);
                if(IsInteger(temp))     //括号内只有一个数字
                {
                    return false;
                }
            }
        }
        return true;
    }

    //判断字符串是否只包含数字 (空字符也会返回true, 因为一般使用next()读取不到空字符，所以这样设置)
    public static boolean IsInteger(String str)
    {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    public static String[] PaperGenerate(String str,int num)//生成str难度的num道题，返回string[]
    {
        String[] ant = new String[num];
        StringBuffer all = new StringBuffer();
        String content;
        for(int i=0;i<num;)
        {
            content = RandGrade(str);
            if(all.indexOf(content)==-1)
            {
                all.append(content);
                ant[i] = content;
                i++;
            }
        }
        return ant;
    }

    public static String GetRandomNum() //返回6位数的随机数
    {
        return Integer.toString((int) ((Math.random()*900000)+100000));
    }

    public static boolean SatisComplexity(String string) //密码检验
    {
        String s_flag = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])[A-Za-z0-9]{6,10}$";
        return string.matches(s_flag);
    }
}
