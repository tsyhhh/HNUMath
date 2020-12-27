package sample.com.hnu.controller;

public class Controller {
    /*
    * 本来view层和controller层应该是分开的，但是由于javafx传递参数比较麻烦，我又是第一次使用javafx，之前也只是学过一点点swing，
    * 所以view层和controller层这次就没分离，这是一个比较大的遗憾，后来想重构的时候也发现传递参数必须要修改construct函数，
    * 并且在.fxml文件中进行相应的配置，保证各容器都在root根目录下，而我将每一个stage都分离在不同容器中，这就导致重构起来非常麻烦，
    * 基本上等同与重写，可能这就是传说中的"垃圾代码"，重构不如重写吧。
    * 总之这次给了我一个教训，先看完视频，确定规范，确定框架，分离层次再开始写代码。
    * 这些本来应该在暑假的时候就已经完成的，只能说我的脑子有点不太好使吧，下次一定。
    * */
    public static void main(String[] args) {
        for (int i=0;i<32;i++){
            System.out.println("dst[temp*dim+i+"+i+"] = src[(i+"+i+")*dim+j];");
        }
    }
}
