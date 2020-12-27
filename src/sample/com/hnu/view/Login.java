package sample.com.hnu.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sample.com.hnu.pojo.User;
import sample.com.hnu.service.UserService;

public class Login
{
    private final Stage m_stage = new Stage();

    public Login()
    {
        SignIn();
    }

    public void SetStage()
    {
        //Label设置
        Label LabelJpg = new Label();
        Label LabelTitle = new Label("欢迎使用数学考试系统!");
        Label LabelName = new Label("账号:");
        Label LabelPassword = new Label("密码:");
        Label LabelForgetWord = new Label("忘记密码？");
        LabelForgetWord.setFont(new Font(12));

        //背景设置
        Image image = new Image(getClass().getResourceAsStream("./CSS/timg.jpg"));
        LabelJpg.setGraphic(new ImageView(image));
        LabelTitle.setFont(new Font(20));

        //输入框设置
        TextField TextName = new TextField();
        PasswordField TextPassword = new PasswordField();

        //按钮设置
        Button login = new Button("登录");
        Button ButtonRegister = new Button("注册");

        //其他设置
        GridPane gridPane = new GridPane();
        gridPane.setStyle("-fx-background-color:#FFF5EE");

        gridPane.add(LabelJpg,0,0);
        gridPane.add(LabelTitle,1,0);

        //用户名
        gridPane.add(LabelName,0,1);
        gridPane.add(TextName,1,1);

        //用户密码
        gridPane.add(LabelPassword,0,2);
        gridPane.add(TextPassword,1,2);
        gridPane.add(LabelForgetWord,2,2);

        //登录和注册按钮
        gridPane.add(login,0,3);
        gridPane.add(ButtonRegister,1,3);

        //界面大小设置
        gridPane.setHgap(5);
        gridPane.setVgap(17);
        GridPane.setMargin(ButtonRegister,new Insets(0,0,0,120));
        GridPane.setMargin(login,new Insets(0,0,0,50));
        GridPane.setMargin(LabelName,new Insets(0,0,0,50));
        GridPane.setMargin(LabelPassword,new Insets(0,0,0,50));
        gridPane.setAlignment(Pos.CENTER);

        Scene scene = new Scene(gridPane);
        scene.getStylesheets().add(
                getClass().getResource("CSS/MainStyle.css")
                        .toExternalForm());
        m_stage.setScene(scene);
        m_stage.setTitle("登录");
        m_stage.setHeight(300);
        m_stage.setWidth(500);
        m_stage.setResizable(false);

        //注册功能
        ButtonRegister.setOnAction(event -> {
            Register register = new Register();
            register.GetRegister();
            m_stage.close();
        });

        //登录功能
        login.setOnAction (event -> {
            String name = TextName.getText();
            String password = TextPassword.getText();
            if(!name.equals("")&&!password.equals(""))    //判断是否输入了密码和用户名
            {
                User user;
                if((user = UserService.IsExist(name,password))!=null)    //检索是否存在
                {
                    Menu menu=new Menu(user);
                    m_stage.close();
                }
                else //不存在
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("请输入正确的用户名和密码!");
                    alert.show();
                }
            }
            else //没有输入了密码和用户名
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("请输入用户名和密码!");
                alert.show();
            }
        });

        //忘记密码，短信服务只能用来注册
        LabelForgetWord.setOnMouseClicked(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("由于短信服务仅能用于注册用户，修改密码业务暂未开通,请见谅!");
            alert.show();
        });
    }


    public void SignIn()
    {
        SetStage();
        m_stage.show();
    }
}
