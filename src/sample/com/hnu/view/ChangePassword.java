package sample.com.hnu.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import sample.com.hnu.pojo.User;
import sample.com.hnu.service.MathService;
import sample.com.hnu.service.UserService;

public class ChangePassword
{
    private final Stage m_stage = new Stage();
    private User m_user;

    public ChangePassword(User m_user)
    {
        this.m_user = m_user;
        SetMenu();
    }

    public void SetStage()
    {

        //Label设置
        Label LabelOld = new Label("旧密码:");
        Label LabelPassword = new Label("新密码:");
        Label LabelConfirm = new Label("再次确认:");

        //field设置
        PasswordField TextOld = new PasswordField();
        PasswordField TextPassword = new PasswordField();
        PasswordField TextConfirm = new PasswordField();

        //Button设置
        Button SubmitChange = new Button("确定");
        Button cancelChange = new Button("取消");

        //pane设置
        GridPane gridPane = new GridPane();
        gridPane.setStyle("-fx-background-color:#FFF5EE");

        //Label
        gridPane.add(LabelOld,0,0);
        gridPane.add(LabelPassword,0,1);
        gridPane.add(LabelConfirm,0,2);

        //题目
        gridPane.add(TextOld,1,0);
        gridPane.add(TextPassword,1,1);
        gridPane.add(TextConfirm,1,2);

        //登录和注册按钮
        gridPane.add(cancelChange,0,3);
        gridPane.add(SubmitChange,1,3);


        //其它设置
        gridPane.setHgap(5);
        gridPane.setVgap(17);
        GridPane.setMargin(SubmitChange,new Insets(0,0,0,120));
        gridPane.setAlignment(Pos.CENTER);

        Scene scene = new Scene(gridPane);
        scene.getStylesheets().add(
                getClass().getResource("CSS/MainStyle.css")
                        .toExternalForm());

        m_stage.setScene(scene);
        m_stage.setTitle("修改密码");
        m_stage.setHeight(300);
        m_stage.setWidth(400);
        m_stage.setResizable(false);


        SubmitChange.setOnAction(event -> {
            System.out.println(m_user);
            String OldPassword=TextOld.getText();
            String NewPassword=TextPassword.getText();
            String ConfirmPassword=TextConfirm.getText();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            if(OldPassword.equals(m_user.getM_password()))
            {
                if(!MathService.SatisComplexity(NewPassword))   //密码不符合要求
                {
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setContentText("请确保密码6到10位且包含大小写字母和数字!");
                    alert.show();
                }
                else if(!ConfirmPassword.equals(NewPassword))   //两次输入的密码不一致
                {
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setContentText("请确保两次密码输入相同!");
                    alert.show();
                }
                else//符合要求
                {
                    m_user.setM_password(NewPassword);
                    alert.setContentText("密码修改成功!即将返回开始界面");
                    UserService.ModifyUser(m_user);
                    alert.show();
                    Button ok = (Button)alert.getDialogPane().lookupButton(ButtonType.OK);
                    alert.getDialogPane().getButtonTypes().removeAll(ButtonType.CANCEL);
                    ok.setOnAction(event1 -> {
                        alert.close();
                        m_stage.close();
                        Menu menu=new Menu(m_user);
                    });
                }
            }
            else   //旧密码输入错误
            {
                alert.setContentText("旧密码输入错误!");
                alert.show();
            }
        });

        //取消修改密码
        cancelChange.setOnAction (event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("确定取消修改密码？");
            alert.show();

            //取消->取消修改密码
            Button cancel = (Button)alert.getDialogPane().lookupButton(ButtonType.CANCEL);
            cancel.setOnAction(event1 -> alert.close());

            //确定->取消修改密码
            Button ok = (Button)alert.getDialogPane().lookupButton(ButtonType.OK);
            ok.setOnAction(event2 -> {
                alert.close();
                m_stage.close();
                Menu menu=new Menu(m_user);
            });
        });
    }


    public void SetMenu()
    {
        SetStage();
        m_stage.show();
    }
}
