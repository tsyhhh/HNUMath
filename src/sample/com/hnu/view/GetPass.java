package sample.com.hnu.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.com.hnu.pojo.User;
import sample.com.hnu.service.MathService;
import sample.com.hnu.service.UserService;

public class GetPass
{
    private final Stage m_stage = new Stage();
    private User m_user;
    //private String m_code = MathService.GetRandomNum();

    public GetPass(User m_user)
    {
        this.m_user = m_user;
        GetPass();
    }

    public void SetStage()
    {
        //名称设置
        Label LabelName = new Label("名称:");
        LabelName.setPrefSize(70,40);
        TextField TextName = new TextField();
        TextName.setMinWidth(300);

        //密码设置
        Label LabelPassword = new Label("密码:");
        LabelPassword.setPrefSize(70,40);
        PasswordField TextPassword = new PasswordField();
        TextPassword.setMinWidth(300);

        //确认密码设置
        Label LabelPasswordII = new Label("再次确认:");
        LabelPasswordII.setPrefSize(70,40);
        PasswordField TextPasswordII = new PasswordField();
        TextPasswordII.setMinWidth(300);

        //提示语设置
        Label LabelMessage = new Label("");
        LabelMessage.setPrefSize(450,10);
        LabelMessage.setAlignment(Pos.CENTER);
        LabelMessage.setTextFill(Color.web("red"));

        //提交
        Button ConfirmSubmit = new Button("确认");

        //把上述组件放入盒中以方便布局
        HBox hBox = new HBox();
        HBox hBoxII = new HBox();
        HBox hBoxIII = new HBox();
        HBox hBoxIV = new HBox();

        hBoxIV.setPrefSize(500,30);
        hBoxIV.setPadding(new Insets(5));
        hBoxIV.setSpacing(10);

        hBox.setPrefSize(500,30);
        hBox.setPadding(new Insets(5));
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER);

        hBoxII.setPrefSize(500,30);
        hBoxII.setPadding(new Insets(5));
        hBoxII.setSpacing(10);
        hBoxII.setAlignment(Pos.CENTER);

        hBoxIII.setPrefSize(500,30);
        hBoxIII.setPadding(new Insets(5));
        hBoxIII.setSpacing(10);
        hBoxIII.setAlignment(Pos.CENTER);

        hBox.getChildren().addAll(LabelName,TextName);
        hBoxII.getChildren().addAll(LabelPassword,TextPassword);
        hBoxIII.getChildren().addAll(LabelPasswordII,TextPasswordII);

        //布局设置
        FlowPane flowPane = new FlowPane();
        flowPane.setAlignment(Pos.CENTER);
        flowPane.setStyle("-fx-background-color:#FFF5EE");
        flowPane.getChildren().addAll(hBoxIV,hBox,hBoxII,hBoxIII,LabelMessage,ConfirmSubmit);

        //其它设置
        flowPane.setHgap(0);
        flowPane.setVgap(17);
        Scene scene = new Scene(flowPane);
        scene.getStylesheets().add(
                getClass().getResource("CSS/MainStyle.css")
                        .toExternalForm());
        m_stage.setScene(scene);
        m_stage.setTitle("注册");
        m_stage.setHeight(350);
        m_stage.setWidth(500);
        m_stage.setResizable(false);


        //提示输入的密码规范
        TextPassword.setPromptText("6到10位且包含大小写字母和数字");
        TextPasswordII.setPromptText("请确保两次输入的密码相同");
        TextPassword.setTooltip(new Tooltip("密码6到10位且包含大小写字母和数字"));
        TextPasswordII.setTooltip(new Tooltip("请确保两次输入的密码相同"));

        //提交
        ConfirmSubmit.setOnAction(event -> {
            String name = TextName.getText();
            String password = TextPassword.getText();
            String passwordII = TextPasswordII.getText();
            if(name.equals("")) //用户名为空
            {
                LabelMessage.setText("请输入用户名!");
            }
            else if(UserService.IsExistByName(name))//用户名已被注册
            {
                LabelMessage.setText("抱歉，该用户名已被注册!");
            }
            else if(!MathService.SatisComplexity(password))//密码不符合规范
            {
                LabelMessage.setText("请确保密码6到10位且包含大小写字母和数字!");
            }
            else if(!password.equals(passwordII))//两次密码不相同
            {
                LabelMessage.setText("请确保两次输入的密码相同!");
            }
            else//符合规范了
            {
                m_user.setM_name(name);
                m_user.setM_password(password);
                UserService.AddUser(m_user);
                Menu menu= new Menu(m_user);
                m_stage.close();
            }
        });
    }
    public void GetPass()
    {
        SetStage();
        m_stage.show();
    }
}
