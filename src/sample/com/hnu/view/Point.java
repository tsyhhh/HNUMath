package sample.com.hnu.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import sample.com.hnu.pojo.User;

public class Point
{
    private final Stage m_stage = new Stage();
    private User m_user;
    private int m_RightNum;
    private int m_AllNum;

    public Point(User m_user, int m_RightNum, int m_AllNum)
    {
        this.m_user = m_user;
        this.m_RightNum = m_RightNum;
        this.m_AllNum = m_AllNum;
        StartPoint();
    }

    public void SetStage()
    {
        int point =(int) ((double)m_RightNum/(double)m_AllNum*100);

        //Label设置
        Label LabelPoint = new Label("恭喜您，完成了考题，您的最终成绩为:"+ point);
        Label LabelText = new Label("错题已保存在messages下的错题集中，请自行查阅");
        LabelPoint.setPrefSize(400,20);
        LabelText.setPrefSize(400,20);

        //button设置
        Button Continue = new Button("继续做题");
        Button Exit = new Button("退出");
        Continue.setPrefSize(100,20);
        Exit.setPrefSize(100,20);

        //布局设置
        FlowPane flowPane = new FlowPane();
        flowPane.setAlignment(Pos.CENTER);
        flowPane.setStyle("-fx-background-color:#FFF5EE");

        flowPane.getChildren().addAll(LabelPoint,LabelText,Continue,Exit);

        //其它设置
        flowPane.setHgap(5);
        flowPane.setVgap(17);
        flowPane.setAlignment(Pos.CENTER);
        Scene scene = new Scene(flowPane);
        scene.getStylesheets().add(
                getClass().getResource("CSS/MainStyle.css")
                        .toExternalForm());
        m_stage.setScene(scene);
        m_stage.setTitle("得分界面");
        m_stage.setHeight(300);
        m_stage.setWidth(500);
        m_stage.setResizable(false);

        Continue.setOnAction(event -> {
            m_stage.close();
            Menu menu = new Menu(m_user);
        });

        Exit.setOnAction(event -> {
            m_stage.close();
            System.exit(0);
        });
    }

    public void StartPoint()
    {
        SetStage();
        m_stage.show();
    }
}
