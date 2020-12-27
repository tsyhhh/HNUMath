package sample.com.hnu.service;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class ClockService extends Pane
{

    private Timeline m_animation;
    private int g_tmp = 30;            //总时间
    private int g_hour = g_tmp /60;;    //分
    private int g_second = g_tmp %60;   //秒
    private String g_S ="  剩余时间:"+ g_hour +":"+ g_second;
    private boolean g_TimeOut = false;//是否超时
    Label label = new Label(g_S);


    public ClockService()
    {
        //label.setFont(javafx.scene.text.Font.font(20));
        getChildren().add(label);
        m_animation = new Timeline(new KeyFrame(Duration.millis(1000), e -> TimeLabel()));
        m_animation.setCycleCount(Timeline.INDEFINITE);
        m_animation.play();
    }

    public boolean IsG_TimeOut() {
        return g_TimeOut;
    }

    public void TimeLabel()
    {
        g_tmp--;
        g_hour = g_tmp /60;
        g_second = g_tmp %60;
        g_S = "  剩余时间:"+ g_hour +":"+ g_second;
        label.setText(g_S);
        if(g_tmp ==1)
        {
            g_TimeOut =true;
        }
    }

}