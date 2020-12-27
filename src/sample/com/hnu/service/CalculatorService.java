package sample.com.hnu.service;

public class CalculatorService
{
	private static boolean g_IsCal = false;
	public static boolean IsM_IsCal() {
		return g_IsCal;
	}

	public static void SetM_IsCal(boolean m_IsCal) {
		CalculatorService.g_IsCal = m_IsCal;
	}

	//得到答案
	public static double CalcuStr(String numstr)
	{
		double re = GetResult(numstr);
		return re;
	}

	public static double GetResult(String numstr)
	{
		//先把括号中的值给算出来然后替换，找的括号是最小范围的
		//每次处理一个括号
		StringBuffer sb=new StringBuffer(numstr);
		String rs= BracketsString(sb);
		while(rs!=null)
		{
			//计算括号内的值并放回去
			double Value = CalBracketsValue(rs);
			String calBracketString;
			calBracketString = String.valueOf(Value);
			sb.replace(sb.indexOf("@"), sb.indexOf("@")+1, calBracketString);

			//出现--，应该替换成+号
			if(sb.indexOf("--")!=-1)
			{
				sb.replace(sb.indexOf("--"),sb.indexOf("--")+2,"+");
			}
			//出现+-，应该替换成-号
			if(sb.indexOf("+-")!=-1)
			{
				sb.replace(sb.indexOf("+-"),sb.indexOf("+-")+2,"-");
			}

			//再次检测
			rs= BracketsString(sb);
		}
		return CalBracketsValue(sb.toString());
	}

	//每次都找最里面的一对括号
	public static String BracketsString(StringBuffer str)
	{
		int leftBracketsPoint=-1;
		int rightBracketsPoint=-1;//初始化-1
		int length = str.length();//避免对str.length()的重复调用
		String res;
		//一个循环，保证每次都是最里面的一对括号
		for (int i=0;i<length;i++)
		{
			if(str.charAt(i)=='(')
			{
				leftBracketsPoint =i;
			}
			else if(str.charAt(i)==')')
			{
				rightBracketsPoint=i;
			}
			if(rightBracketsPoint>leftBracketsPoint)
			{
				res= str.substring(leftBracketsPoint+1,rightBracketsPoint);
				str.replace(leftBracketsPoint, rightBracketsPoint+1, "@");
				return res;
			}
		}
		return null;
	}


	//递归处理平方，开平方，三角函数组合的式子
	public static double ComplexStr(String st)
	{
		char[] ch = st.toCharArray();
		if(ch[0]=='√')
		{
			return Math.sqrt(ComplexStr(st.substring(1)));
		}
		else if(ch[0]=='s')
		{
			return Math.sin(ComplexStr(st.substring(3)));
		}
		else if(ch[0]=='c')
		{
			return Math.cos(ComplexStr(st.substring(3)));
		}
		else if(ch[0]=='t')
		{
			return Math.tan(ComplexStr(st.substring(3)));
		}
		else
		{
			double num;
			if(ch[ch.length-1]=='²')//平方
			{
				String numstr = st.substring(0,st.length()-1);
				num = Double.parseDouble(numstr);
				num = Math.pow(num, 2);
			}
			else//都不是，就是一个普通的数
			{
				num = Double.parseDouble(st);
			}
			return new Double(String.format("%.3f", num));//三位小数就够了
		}
	}

	//无括号表达式的计算
	public static double CalBracketsValue(String numStr)
	{
		double FinResult = 0;
		int AddSubCount = 0;
		char[] SymAddSub = new char[10];//保存符号
		String[] AddSub = new String[10];//分割+-
		int front =0;//记录位置
		char[] str = numStr.toCharArray();
		for(int i=0;i<str.length;i++)
		{
			if(str[i]=='+')
			{
				SymAddSub[AddSubCount] = '+';
				AddSub[AddSubCount++] = numStr.substring(front,i);
				front = ++i;
			}

			else if(i!=0)//避免索引时越界
			{
				if(str[i]=='-'&&(Character.isDigit(str[i-1])||str[i-1]=='²'))//保证了不会把负数当成减号分割
				{
					SymAddSub[AddSubCount] = '-';
					AddSub[AddSubCount++] = numStr.substring(front,i);
					front = ++i;
				}
			}
		}

		if(front!=0)//出现了分割
		{
			AddSub[AddSubCount++] = numStr.substring(front);
		}
		else  AddSub[AddSubCount++] = numStr;//没有出现分割

		double[] Result = new double[AddSubCount];
		for(int i = 0; i< AddSubCount; i++)	//把分割后的字符串再依次分割
		{
			char[] SymMulDiv = new char[10];		//保留乘除
			char[] temp = AddSub[i].toCharArray();	//暂时用一下
			int count = 0;
			for (char c : temp)
			{
				if (c == '*' || c == '/')
				{
					SymMulDiv[count++] = c;
				}
			}
			String[] MulDiv = AddSub[i].split("[*/]");	//由于不需要考虑负数，所以直接spilt即可
			double[] ResultMulDiv = new double[MulDiv.length];

			//先乘除
			for(int j=0;j<MulDiv.length;j++)
			{
				ResultMulDiv[j] = ComplexStr(MulDiv[j]);	//单个运算数取值
				if(j==0)
				{
					Result[i]= ResultMulDiv[j];
				}
				else
				{
					if(SymMulDiv[j-1]=='*')
					{
						Result[i]=Result[i]*ResultMulDiv[j];
					}

					else if(SymMulDiv[j-1]=='/')
					{
						Result[i]=Result[i]/ResultMulDiv[j];
					}

				}
			}

			//后加减
			if(i==0)
			{
				FinResult = Result[i];
			}
			else
			{
				if(SymAddSub[i-1]=='+')
				{
					FinResult +=Result[i];
				}
				else if(SymAddSub[i-1]=='-')
				{
					FinResult-=Result[i];
				}
			}
		}

		//出现了无穷小或算不出来的数，直接重新出题
		if (new Double(String.format("%.3f", FinResult)) == 0||(Double.isNaN(FinResult)) )
		{
			g_IsCal = false;
		}
		return new Double(String.format("%.3f", FinResult));
	}

	/*以下为测试案例
	public static void main(String[] args) {
		int num=30;
		Double[] answer = new Double[num];
		String []problems = MathService.PaperGenerate("小学",num);
		CalculatorService.SetM_IsCal(false);
		while (!IsM_IsCal())//出现了算不出来的题，重新出题
		{
			SetM_IsCal(true);//进入循环时重新赋正
			for(int i=0;i<num;i++)
			{
				System.out.print(problems[i]+":");
				answer[i]= CalculatorService.Calcu(problems[i]);
				System.out.println( answer[i]);
			}
			problems = MathService.PaperGenerate("高中",num);
		}
	}*/
}