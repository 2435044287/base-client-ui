package pers.yuews.bcu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.yuews.bcu.core.ApplicationLauncher;
import pers.yuews.bcu.core.common.annotaion.ComponentScan;
import pers.yuews.bcu.core.common.constant.MessageType;
import pers.yuews.bcu.core.common.enums.MessageTypeEnum;
import pers.yuews.bcu.core.ui.BaseWindows;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author yuews
 * @create 2024/5/24 16:41
 * @describe
 */
@ComponentScan(registerPackagePaths = {"pers.yuews.bcu.demo"},fieldValuePackagePaths={"pers.yuews.bcu.demo"})
public class UIDemo extends BaseWindows {

    private static final Logger logger = LoggerFactory.getLogger(UIDemo.class);

    public UIDemo(String title) {
        super(title);
    }

    public UIDemo(){
        super();
    }

    public static void main(String[] args) {
        final long l = System.currentTimeMillis();
        final UIDemo ui = ApplicationLauncher.run(UIDemo.class);
        //com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatGitHubContrastIJTheme.setup();
        logger.info("启动耗时：{}",System.currentTimeMillis()-l);
        ui.getJf().setVisible(true);
        logger.info("启动耗时：{}",System.currentTimeMillis()-l);
        logger.info("启动成功");


        // final UI ui = new UI();
        //ui.getJf().setVisible(true);
    }

    @Override
    public void pushMsg(String msg, MessageTypeEnum msgType) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(date);
        switch (msgType) {
            case ROUTINE:
                getNormalMainInterface().append(dateString + "==>" + msg + "\n");
                getNormalMainInterface().setCaretPosition(getNormalMainInterface().getDocument().getLength());
                break;
            case ERROR:
                getWarnMainInterface().append(dateString + "==>" + msg + "\n");
                getWarnMainInterface().setCaretPosition(getWarnMainInterface().getDocument().getLength());
                break;
            default:
                throw new RuntimeException("未知的消息类型");
        }
    }

    @Override
    public void pushMsg(String s, MessageType messageType) {

    }

}
