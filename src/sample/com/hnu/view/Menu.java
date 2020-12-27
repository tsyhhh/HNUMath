package sample.com.hnu.view;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import sample.com.hnu.pojo.User;
import sample.com.hnu.service.CalculatorService;
import sample.com.hnu.service.MathService;

public class Menu
{
    private final Stage m_stage = new Stage();
    private User m_user;

    public Menu(User m_user)
    {
        this.m_user = m_user;
        System.out.println(m_user);
        SetMenu();
    }

    public void SetStage()
    {

        //Label设置
        Label LabelDegree = new Label("难度:");
        Label LabelNum = new Label("题量:");

        //ChoiceBox设置
        ChoiceBox<String> CB = new ChoiceBox<>(
                FXCollections.observableArrayList("小学", "初中", "高中")
        );
        CB.setPrefSize(250,30);

        //输入num设置
        TextField TextNum = new TextField();

        Button ChangePassword = new Button("修改密码");
        Button StartExam = new Button("开始做题");

        GridPane gridPane = new GridPane();
        gridPane.setStyle("-fx-background-color:#FFF5EE");

        //难度
        gridPane.add(LabelDegree,0,0);
        gridPane.add(CB,1,0);

        //题目数量
        gridPane.add(LabelNum,0,1);
        gridPane.add(TextNum,1,1);

        //选项
        gridPane.add(ChangePassword,0,2);
        gridPane.add(StartExam,1,2);

        //其它设置
        gridPane.setHgap(5);
        gridPane.setVgap(17);
        GridPane.setMargin(StartExam,new Insets(0,0,0,120));
        gridPane.setAlignment(Pos.CENTER);
        Scene scene = new Scene(gridPane);
        scene.getStylesheets().add(
                getClass().getResource("CSS/MainStyle.css")
                        .toExternalForm());

        m_stage.setScene(scene);
        m_stage.setTitle("菜单");
        m_stage.setHeight(300);
        m_stage.setWidth(500);
        m_stage.setResizable(false);

        //修改密码功能
        ChangePassword.setOnAction(event -> {
           ChangePassword changePassword = new ChangePassword(m_user);
            m_stage.close();
        });

        //开始考试
        StartExam.setOnAction (event -> {
            String degree = CB.getValue();
            String cnt = TextNum.getText();
            if(degree!=null&&!cnt.equals(""))    //难度和数量不能为空
            {
                if(MathService.IsInteger(cnt))  //输入的需要是一个整数
                {
                    int num = Integer.parseInt(cnt);
                    if(num>=10&&num<=30)    //输入的只能在10到30之间
                    {
                        Double[] answer = new Double[num];
                        String []problems = MathService.PaperGenerate(degree,num);
                        CalculatorService.SetM_IsCal(false);
                        while (!CalculatorService.IsM_IsCal())//出现了算不出来的题，重新出题
                        {
                            //System.out.println("-------------------");
                            problems = MathService.PaperGenerate(degree,num);
                            CalculatorService.SetM_IsCal(true);//进入循环时重新赋正
                            for(int i=0;i<num;i++)
                            {
                                //System.out.print((i+1)+"、"+problems[i]+":");
                                answer[i]= CalculatorService.CalcuStr(problems[i]);
                                //System.out.println( answer[i]);
                            }
                        }
                        Paper paper=new Paper(m_user,num,degree,problems,answer);
                        m_stage.close();
                    }
                    else//输入的不在10到30之间
                    {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("出题量在10到30之间!");
                        alert.show();
                    }

                }
                else//输入不是一个整数
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("请输入一个整数作为出题量!");
                    alert.show();
                }
            }
            else    //难度和数量有一个为空
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("请选择难度且输入出题量!");
                alert.show();
            }
        });
    }


    public void SetMenu()
    {
        SetStage();
        m_stage.show();
    }

}
