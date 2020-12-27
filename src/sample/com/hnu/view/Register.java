package sample.com.hnu.view;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sample.com.hnu.pojo.User;
import sample.com.hnu.service.AliSMService;
import sample.com.hnu.service.MathService;
import sample.com.hnu.service.UserService;

public class Register
{
    private final Stage m_stage = new Stage();
    private String m_code ;
    private User m_user = new User();

    public Register()
    {
        this.m_code = MathService.GetRandomNum();
        GetRegister();
    }

    public void SetStage()
    {
        //Label设置
        Label LabelPhone = new Label("手机号:");
        Label LabelVerification = new Label("验证码:");
        Label LabelReturn = new Label("已有账号,返回登录?");
        LabelReturn.setFont(new Font(14));
        //LabelReturn.setStyle("-fx-color-label-visible: #E6E6FA");

        //Field设置
        TextField TextPhone = new TextField();
        TextField TextVerification = new TextField();

        //Button设置
        Button GetCaptcha = new Button("获取验证码");
        Button ConfirmSubmit = new Button("确认");

        //布局设置
        GridPane gridPane = new GridPane();

        gridPane.setStyle("-fx-background-color:#FFF5EE");

        //Label 位置
        gridPane.add(LabelPhone,0,0);
        gridPane.add(LabelVerification,0,1);

        //TextField 位置
        gridPane.add(TextPhone,1,0);
        gridPane.add(TextVerification,1,1);
        gridPane.add(LabelReturn,1,2);

        //登录和注册按钮
        gridPane.add(GetCaptcha,2,0);
        gridPane.add(ConfirmSubmit,2,1);

        //其它设置
        gridPane.setHgap(5);
        gridPane.setVgap(17);
        GridPane.setMargin(LabelReturn,new Insets(0,0,0,50));
        gridPane.setAlignment(Pos.CENTER);

        Scene scene = new Scene(gridPane);
        m_stage.setScene(scene);
        m_stage.setTitle("注册界面");
        m_stage.setHeight(300);
        m_stage.setWidth(500);
        m_stage.setResizable(false);
        scene.getStylesheets().add(
                getClass().getResource("CSS/MainStyle.css")
                        .toExternalForm());

        //注册功能
        GetCaptcha.setOnAction(event -> {
            String phone = TextPhone.getText();
            if(!phone.equals(""))
            {

                if(UserService.IsExistByPhone(phone))//该手机号已被注册
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("该手机号已经被注册!");
                    alert.show();
                }
                else if(!AliSMService.IfPhone(phone))//不是一个有效的手机号
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("请输入正确的手机号!");
                    alert.show();
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("验证码已经发出，请耐心等待!");
                    alert.show();
                    m_user.setM_phone(phone);
                    try
                    {
                        SendSmsResponse sendSms =AliSMService.sendSms(phone,m_code);
                    }
                    catch (ClientException e1)
                    {
                        e1.printStackTrace();
                    }
                    System.out.println(m_code);
                }
            }
            else//没有输入手机号
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("请输入手机号!");
                alert.show();
            }
        });

        //确认
        ConfirmSubmit.setOnAction(event -> {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            String Verification = TextVerification.getText();
            if(m_code.equals(Verification))//验证码输入正确
            {
                GetPass getPass = new GetPass(m_user);  //下一步
                m_stage.close();
            }
            else
            {
                alert1.setContentText("验证码错误!");
                alert1.show();
            }
        });

        //返回登录界面
        LabelReturn.setOnMouseClicked(event -> {
            Login login = new Login();
            m_stage.close();
        });
    }
    public void GetRegister()
    {
        SetStage();
        m_stage.show();
    }
}
