package sample.com.hnu.view;

import com.sun.javafx.application.PlatformImpl;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sample.com.hnu.pojo.User;
import sample.com.hnu.service.ClockService;
import sample.com.hnu.service.UserService;

import java.io.IOException;
import java.util.Random;

public class Paper
{
    private final Stage m_stage = new Stage();
    private User m_user;
    private int m_num;              //总数量
    private Boolean m_Submit = false;
    private String m_degree;
    private String [] m_Problem;
    private Double [] m_Answer;
    private String[] m_CorrectOption;
    private String [] m_RecordOption;
    private int m_cur=0;            //目前是第几题
    private int m_RightOption =0;   //目前对了多少题
    private int m_HasSwitch =0;     //目前写了多少题
    ClockService m_clockService = new ClockService();


    public Paper(User m_user, int m_num, String m_degree, String[] m_Problem,Double [] m_Answer)
    {
        this.m_user = m_user;
        this.m_num = m_num;
        this.m_degree = m_degree;
        this.m_Problem = m_Problem;
        this.m_Answer = m_Answer;
        StartPaper();
    }

    public void SetStage()
    {
        //多线程维护倒计时
        new Thread(new ThreadCountDown()).start();

        //正确的选项
        m_CorrectOption = new String[m_num];
        for(int i=0;i<m_num;i++)
        {
            long seed = System.nanoTime();
            Random random = new Random(seed);
            int rand = random.nextInt(4);
            switch (rand)
            {
                case 0:
                    m_CorrectOption[i] = "A";
                    break;
                case 1:
                    m_CorrectOption[i] = "B";
                    break;
                case 2:
                    m_CorrectOption[i] = "C";
                    break;
                case 3:
                    m_CorrectOption[i] = "D";
                    break;
            }
            //System.out.print(CorrectOption[i]);
        }

        //随机出的选项
        String[][] option = new String[m_num][4];

        for(int i=0;i<m_num;i++)
        {
            switch (m_CorrectOption[i])
            {
                case "A":
                    option[i][0] = String.format("%.3f",m_Answer[i]);
                    option[i][1] = String.format("%.3f",m_Answer[i] + (int) (1 + Math.random() * 100 ));
                    option[i][2] = String.format("%.3f",m_Answer[i] + (int) (1 + Math.random() * 50 ));
                    option[i][3] = String.format("%.3f",m_Answer[i] - (int) (1 + Math.random() * 30 ));
                    break;
                case "B":
                    option[i][0] = String.format("%.3f",m_Answer[i] + (int) (1 + Math.random() * 100 ));
                    option[i][1] = String.format("%.3f",m_Answer[i]);
                    option[i][2] = String.format("%.3f",m_Answer[i] + (int) (1 + Math.random() * 50 ));
                    option[i][3] = String.format("%.3f",m_Answer[i] - (int) (1 + Math.random() * 30));
                    break;
                case "C":
                    option[i][0] = String.format("%.3f",m_Answer[i] - (int) (1 + Math.random() * 100));
                    option[i][1] = String.format("%.3f",m_Answer[i] + (int) (1 + Math.random() * 50 ));
                    option[i][2] = String.format("%.3f",m_Answer[i]);
                    option[i][3] = String.format("%.3f",m_Answer[i] - (int) (1 + Math.random() * 30 ));
                    break;
                case "D":
                    option[i][0] = String.format("%.3f",m_Answer[i] + (int) (1 + Math.random() * 100 ));
                    option[i][1] = String.format("%.3f",m_Answer[i] + (int) (1 + Math.random() * 50));
                    option[i][2] = String.format("%.3f",m_Answer[i] + (int) (1 + Math.random() * 30 ));
                    option[i][3] = String.format("%.3f",m_Answer[i]);
                    break;
            }
        }

        //选择的选项
        m_RecordOption = new String[m_num];
        for(int i=0;i<m_num;i++)
        {
            m_RecordOption[i]="0";
        }

        //布局设置
        Alert alert = new Alert(Alert.AlertType.ERROR);
        TextArea Problem = new TextArea();
        Problem.setFont(Font.font(20));
        Problem.setEditable(false);
        Problem.setPrefSize(400,40);
        Problem.setText((m_cur+1)+"、"+m_Problem[m_cur]+"= ");

        //单选按钮设置
        RadioButton radioButtonA = new RadioButton("A:"+option[m_cur][0]);
        RadioButton radioButtonB = new RadioButton("B:"+option[m_cur][1]);
        RadioButton radioButtonC = new RadioButton("C:"+option[m_cur][2]);
        RadioButton radioButtonD = new RadioButton("D:"+option[m_cur][3]);

        //单选按钮加入group
        final ToggleGroup group = new ToggleGroup();
        radioButtonA.setToggleGroup(group);
        radioButtonB.setToggleGroup(group);
        radioButtonC.setToggleGroup(group);
        radioButtonD.setToggleGroup(group);

        //大小设置
        radioButtonA.setPrefSize(200,20);
        radioButtonB.setPrefSize(200,20);
        radioButtonC.setPrefSize(200,20);
        radioButtonD.setPrefSize(200,20);

        //Label设置
        Label LabelProblem = new Label("已完成考题:"+ m_HasSwitch +"/"+m_num);
        Label LabelName = new Label("  考生:"+m_user.getM_name());
        Label LabelExamRoom = new Label("  考场:"+m_degree);
        Label LabelSubmit = new Label("提交");
        LabelSubmit.setFont(new Font(14));

        //button设置
        Button FrontExam = new Button("上一题");
        Button NextExam = new Button("下一题");
        FrontExam.setPrefSize(100,20);
        NextExam.setPrefSize(100,20);

        //布局设置
        HBox box= new HBox();
        FlowPane flowPane = new FlowPane();
        flowPane.setAlignment(Pos.CENTER);
        flowPane.setStyle("-fx-background-color:#FFF5EE");
        flowPane.getChildren().addAll(LabelProblem,LabelName,LabelExamRoom,m_clockService,Problem,radioButtonA,radioButtonB,radioButtonC,radioButtonD,FrontExam,NextExam,LabelSubmit);
        flowPane.setMargin(LabelSubmit,new Insets(0,0,0,20));

        //布局第二部分的设置
        FlowPane flowPaneSwitch = new FlowPane();
        flowPaneSwitch.setStyle("-fx-background-color:#FFF5EE");
        for (int i=0;i<m_num;i++)
        {
            Button cur = new Button(""+(i+1));
            cur.setStyle("-fx-background-color:#F8F8FF");
            cur.setPrefSize(50,20);
            int finalI = i;
            box.getChildren().add(cur);
            cur.setOnAction(event -> {
                SwitchSerial(m_RecordOption, radioButtonA, radioButtonB, radioButtonC, radioButtonD, Problem, option, finalI);
                m_cur = finalI;
                if(finalI!=m_num-1)
                {
                    NextExam.setText("下一题");
                }
                else
                {
                    NextExam.setText("交卷");
                }
            });
            flowPaneSwitch.getChildren().add(cur);
        }

        box.getChildren().addAll(flowPane,flowPaneSwitch);
        //box

        //其它设置
        flowPane.setHgap(5);
        flowPane.setVgap(17);
        flowPane.setAlignment(Pos.CENTER);
        flowPane.setPrefSize(500,300);
        flowPaneSwitch.setPrefSize(160,300);
        Scene scene = new Scene(box);
        scene.getStylesheets().add(
                getClass().getResource("CSS/MainStyle.css")
                        .toExternalForm());
        m_stage.setScene(scene);
        m_stage.setTitle("考试界面");
        m_stage.setResizable(false);

        //单选按钮的功能设置
        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            RadioButton ans = (RadioButton) newValue;
            ObservableList<Node> children = flowPaneSwitch.getChildren();
            if(ans!=null)
            {
                for(Node node:children)//选完第n题后，第n题的按钮变成绿色
                {
                    Button cur =(Button) node ;
                    int temp = Integer.parseInt(cur.getText());
                    if(temp==m_cur+1)
                    {
                        cur.setStyle("-fx-background-color:#00CED1");
                        break;
                    }
                }

                //选项
                m_RecordOption[m_cur]=ans.getText().substring(0,1);
                m_RightOption =0;
                m_HasSwitch =0;
                for (int i=0;i<m_num;i++)
                {
                    if (m_RecordOption[i].equals(m_CorrectOption[i]))
                    {
                        m_RightOption++;
                    }
                    if(!m_RecordOption[i].equals("0"))
                    {
                        m_HasSwitch++;
                    }
                }
                LabelProblem.setText("已完成考题:"+ m_HasSwitch +"/"+m_num);
            }
        });

        //前一题
        FrontExam.setOnAction(event -> {
            if(m_cur>0)
            {
                m_cur--;
                SwitchSerial(m_RecordOption, radioButtonA, radioButtonB, radioButtonC, radioButtonD, Problem, option,m_cur);
                if(m_cur!=m_num-1)
                {
                    NextExam.setText("下一题");
                }
            }
            else  //提示没有前一题
            {
                alert.setContentText("没有前一题");
                alert.show();
            }
        });

        //下一题
        NextExam.setOnAction(event -> {
            if(m_cur<m_num-1)
            {
                if(m_cur==m_num-2)//做到最后一题了
                {
                    NextExam.setText("交卷");
                }
                m_cur++;
                SwitchSerial(m_RecordOption, radioButtonA, radioButtonB, radioButtonC, radioButtonD, Problem, option,m_cur);
            }
            else if(m_cur==m_num-1)//最后一题写完了交卷
            {
                SubmitAns();
            }
        });

        //提交
        LabelSubmit.setOnMouseClicked(event -> SubmitAns());
    }

    //提交答案
    private void SubmitAns()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("确定提交吗?");
        alert.show();

        //取消提交
        Button cancel = (Button)alert.getDialogPane().lookupButton(ButtonType.CANCEL);
        cancel.setOnAction(event1 -> alert.close());

        //确认提交
        Button ok = (Button)alert.getDialogPane().lookupButton(ButtonType.OK);
        ok.setOnAction(event -> {
            m_RightOption =0;
            for (int i=0;i<m_num;i++)
            {
                if (m_RecordOption[i].equals(m_CorrectOption[i]))
                {
                    m_RightOption++;
                }
                else
                {
                    try
                    {
                        UserService.WrongQuestionSet(m_user,m_Problem[i],m_Answer[i]);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            //System.out.println("你最后的得分为:"+ m_temp /m_num);
            Point point = new Point(m_user, m_RightOption,m_num);
            m_Submit=true;
            m_stage.close();
        });
    }

    //上一题，下一题，直接点击题目序号时记录原来的答案
    private void SwitchSerial(String[] recordOption, RadioButton radioButtonA, RadioButton radioButtonB, RadioButton radioButtonC, RadioButton radioButtonD, TextArea problem, String[][] option, int cur)
    {
        switch (recordOption[cur])
        {
            case "A":
                radioButtonA.setSelected(true);
                break;
            case "B":
                radioButtonB.setSelected(true);
                break;
            case "C":
                radioButtonC.setSelected(true);
                break;
            case "D":
                radioButtonD.setSelected(true);
                break;
            default:
                radioButtonA.setSelected(false);
                radioButtonB.setSelected(false);
                radioButtonC.setSelected(false);
                radioButtonD.setSelected(false);
                break;
        }
        problem.setText((cur+1)+"、"+m_Problem[cur]+"= ");
        radioButtonA.setText("A:"+option[cur][0]);
        radioButtonB.setText("B:"+option[cur][1]);
        radioButtonC.setText("C:"+option[cur][2]);
        radioButtonD.setText("D:"+option[cur][3]);
    }

    //内部类，倒计时结束后自动提交
    public class ThreadSubmit implements Runnable
    {
        public void run()
        {

            if(m_Submit==false)
            {
                m_RightOption =0;
                for (int i=0;i<m_num;i++)
                {
                    if (m_RecordOption[i].equals(m_CorrectOption[i]))
                    {
                        m_RightOption++;
                    }
                    else
                    {
                        try
                        {
                            UserService.WrongQuestionSet(m_user,m_Problem[i],m_Answer[i]);
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
                //System.out.println("你最后的得分为:"+ m_temp /m_num);
                Point point = new Point(m_user, m_RightOption,m_num);
                m_stage.close();
            }
        }
    }

    //内部类，每秒检测一次是否结束
    public class ThreadCountDown implements Runnable
    {
        public void run()
        {
            while (true)
            {
                try
                {
                    Thread.sleep(1000);
                }
                catch(Exception e)
                {
                    System.exit(0);//退出程序
                }
                if(m_clockService.IsG_TimeOut())
                {
                    //必须这样才能在FX application thread线程外更新界面的组件信息
                    PlatformImpl.runLater(new ThreadSubmit());
                    break;
                }
            }
        }
    }

    public void StartPaper()
    {
        SetStage();
        m_stage.show();
    }
}
