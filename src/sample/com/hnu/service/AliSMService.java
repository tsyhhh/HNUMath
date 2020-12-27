package sample.com.hnu.service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

//已有模板，修改TODO部分即可
public class AliSMService
{
	/**
	 * 中国移动 134.135.136.137.138.139.150.151.152.157.158.159.187.188 ,147(数据卡)
	 */
	public static String c_mobile = "^1(3[4-9]|47|5[012789]|8[78]|82)\\d{8}$";
	/**
	 * 中国联通 130.131.132.155.156.185.186
	 */
	public static String c_unicom = "^1(3[0-2]|5[56]|8[56]|45)\\d{8}$";
	/**
	 * 中国电信 133.153.180.189
	 */
	public static String c_telecom = "^1([35]3|8[09])\\d{8}$";
	/**
	 * 小灵通
	 */
	public static String c_phs = "^0[1-9]{1}\\d{9,10}$";

	//其他
	private static final String c_MOBILE = "((\\+86|0086)?\\s*)((134[0-8]\\d{7})|(((13([0-3]|[5-9]))|(14[5-9])|15([0-3]|[5-9])|(16(2|[5-7]))|17([0-3]|[5-8])|18[0-9]|19(1|[8-9]))\\d{8})|(14(0|1|4)0\\d{7})|(1740([0-5]|[6-9]|[10-12])\\d{7}))";

	// 产品名称:云通信短信API产品,开发者无需替换
	static final String product = "Dysmsapi";
	// 产品域名,开发者无需替换
	static final String domain = "dysmsapi.aliyuncs.com";

	// TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
	static final String accessKeyId = "LTAI4G6P9g8cB9h4jrK71mMy";           // TODO 改这里
	static final String accessKeySecret = "70OOPbAJDG7vQzZuF6nYXkpmJmhJpc"; // TODO 改这里

	public static SendSmsResponse sendSms(String telephone, String code) throws ClientException
	{

		// 可自助调整超时时间
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");

		// 初始化acsClient,暂不支持region化
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
		DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);   //看不懂算了，反正是调接口
		IAcsClient acsClient = new DefaultAcsClient(profile);

		// 组装请求对象-具体描述见控制台-文档部分内容
		
		SendSmsRequest request = new SendSmsRequest();
		// 必填:待发送手机号
		request.setPhoneNumbers(telephone);
		
		// 必填:短信签名-可在短信控制台中找到
		request.setSignName("数学题自动生成程序"); // TODO 改这里
		
		// 必填:短信模板-可在短信控制台中找到
		request.setTemplateCode("SMS_204115037");  // TODO 改这里
		
		// 可选:模板中的变量替换JSON串,如模板内容为"亲爱的用户,您的验证码为${code}"时,此处的值为
		request.setTemplateParam("{\"code\":\"" + code + "\"}");

		// 选填-上行短信扩展码
		// request.setSmsUpExtendCode("90997");

		// 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
		// request.setOutId("yourOutId");

		// hint 此处可能会抛出异常，注意catch
		SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

		if(sendSmsResponse.getCode()!= null && sendSmsResponse.getCode().equals("OK"))
		{
			//空
		}
		else
		{
			//空
		}
		return sendSmsResponse;
	}

	public static boolean IfPhone(String phone)
	{
		if(phone.matches(c_mobile)||phone.matches(c_unicom)||phone.matches(c_telecom)||phone.matches(c_phs)||phone.matches(c_MOBILE))
		{
			return true;
		}
		else return false;
	}
}