package pers.yuews.bcu.core.ui;

import pers.yuews.bcu.core.common.constant.MessageType;
import pers.yuews.bcu.core.common.enums.MessageTypeEnum;

import javax.swing.*;
import java.awt.*;

/**
 * 将共用的代码提取复用 <br/>
 * 继承本类 重写方法 实现执行器 即可<br/>
 * 若添加文本框等 在navPanel 实现即可<br/>
 * mainPanel 主界面  navPanel 导航栏<br/>
 * 详情见bcu-demo<br/>
 * @author yuews
 * @create 2024/5/24 10:01
 * @describe BaseUI
 *
 */
public abstract class BaseWindows extends JFrame {

    /**
     * 入口
     * 启动界面
     */
    public BaseWindows(String title) {
        buildUIBefore();
        jf = new JFrame(title);
        buildBaseWindows();
        buildUIAfter();
    }

    /**
     * 入口
     * 启动界面
     */
    public BaseWindows() {
        this(null);
    }

    /**
     * 构建UI之前需要做的事
     */
    protected void buildUIBefore() {
    }

    /**
     * 构建UI之后需要做的事
     */
    protected void buildUIAfter() {
    }


    /**
     * 构建UI
     */
    protected void buildBaseWindows() {
        createWindows();
        createMainPanel();
        createNavPanel();
        addPanel();
    }

    /**
     * 创建JFrame对象
     */
    protected void createWindows() {
        jf.setBounds(WINDOW_X_LOCATION, WINDOW_Y_LOCATION, WINDOW_WIDTH, WINDOW_HEIGHT);
        jf.setDefaultCloseOperation(EXIT_ON_CLOSE);
        jf.setLayout(new BorderLayout());
    }

    /**
     * 添加组件
     */
    protected void addPanel() {
        jf.add(navPanel, BorderLayout.NORTH);
        jf.add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * 初始化主界面
     */
    protected void createMainPanel() {
        // 多行文本框初始化及其配置
        normalMainInterface = createTextArea(72, NORMAL_FONT, Color.white);
        warnMainInterface = createTextArea(81, WARN_FONT, Color.red);
        JScrollPane mainAreaP1 = new JScrollPane(normalMainInterface);
        JScrollPane mainAreaP2 = new JScrollPane(warnMainInterface);
        // 主页面使用BorderLayout以方便内部布局调整
        mainPanel = new JPanel(new FlowLayout());
        mainPanel.add(mainAreaP1, BorderLayout.NORTH);
        mainPanel.add(mainAreaP2, BorderLayout.CENTER);
    }

    /**
     * 初始化导航栏
     */
    protected void createNavPanel() {
        navPanel = new JPanel();
        navPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 10));
        extendNavPanel();
    }

    /**
     * 导航栏扩展实现
     * <blockquote><pre>
     * JButton test = new JButton("测试按钮");
     * test.addActionListener(starterActionListener());
     * this.getNavPanel().add(test);
     * </pre></blockquote>
     */
    protected void extendNavPanel() {

    }


    /**
     * 创建文本框
     *
     * @param columns    列数
     * @param font       字体
     * @param foreground 字体颜色
     * @return 文本框
     */
    protected JTextArea createTextArea(int columns, Font font, Color foreground) {
        JTextArea textArea = new JTextArea(8, columns);
        textArea.setLineWrap(true);
        textArea.setForeground(foreground);
        textArea.setBackground(Color.gray);
        textArea.setFont(font);
        return textArea;
    }

    /**
     * 推送消息 由子类实现具体逻辑
     *
     * @param msg     消息内容
     * @param msgType 消息类型
     */
    public abstract void pushMsg(String msg, MessageTypeEnum msgType);

    /**
     * 枚举类型扩展性差，新增消息接口类型代替
     * 推送消息 由子类实现具体逻辑
     * @param msg     消息内容
     * @param msgType 消息类型
     */
    public abstract void pushMsg(String msg, MessageType msgType);

    /**
     * 主窗口
     */
    private JFrame jf;

    /**
     * 导航栏面板
     **/
    private JPanel navPanel;

    /**
     * 主页面面板
     **/
    private JPanel mainPanel;

    /**
     * 主文本框1,用于平常过站提示语显示，楷体，加粗，15号，白色
     */
    private JTextArea normalMainInterface;

    /**
     * 主文本框2，用于警示错误提示语显示，黑体，斜体，15号，红色
     */
    private JTextArea warnMainInterface;

    // 窗口大小和位置常量
    protected static int WINDOW_WIDTH = 690;
    protected static int WINDOW_HEIGHT = 430;
    protected static int WINDOW_X_LOCATION = 650;
    protected static int WINDOW_Y_LOCATION = 40;

    /**
     * 正常字体
     */
    private static final Font NORMAL_FONT = new Font("楷体", Font.BOLD, 15);

    /**
     * 警告字体
     */
    private static final Font WARN_FONT = new Font("黑体", Font.ITALIC, 15);

    public JPanel getNavPanel() {
        return navPanel;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JTextArea getNormalMainInterface() {
        return normalMainInterface;
    }

    public JTextArea getWarnMainInterface() {
        return warnMainInterface;
    }

    public JFrame getJf() {
        return jf;
    }
}

