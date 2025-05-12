package xyz.ibudai.process;

import xyz.ibudai.process.common.FormConst;
import xyz.ibudai.process.common.Header;
import xyz.ibudai.process.common.Language;
import xyz.ibudai.process.service.ButtonService;
import xyz.ibudai.process.service.TableService;
import xyz.ibudai.process.util.ExceptionUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class ProcessMain {

    private static final JFrame frame;
    private static final Locale locale;
    private static final ResourceBundle bundle;

    static {
        locale = new Locale(Language.EN.getLanguage());
        bundle = ResourceBundle.getBundle(FormConst.I18N_RESOURCE, locale);
        frame = new JFrame(bundle.getString(FormConst.TITLE));
    }

    public static void main(String[] args) {
        try {
            drawFrame();
        } catch (Exception e) {
            String message = ExceptionUtils.buildMsg(e);
            JOptionPane.showMessageDialog(
                    frame,
                    message,
                    bundle.getString(FormConst.MSG_TITLE_UNEXPECT),
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * 绘制程序窗体页面
     */
    public static void drawFrame() {
        // 创建 JFrame 窗口
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(FormConst.FRAME_WIDTH, FormConst.FRAME_HEIGHT);
        frame.setMinimumSize(new Dimension(800, 300));

        JPanel inputPanel = new JPanel();
        // 输入 [端口]
        JLabel portLabel = new JLabel(bundle.getString(FormConst.LB_PORT));
        inputPanel.add(portLabel);
        JTextField portText = new JTextField(20);
        inputPanel.add(portText);
        // 按钮 [查询、重置]
        JButton searchBt = new JButton(bundle.getString(FormConst.BT_SEARCH));
        inputPanel.add(searchBt);
        JButton resetBt = new JButton(bundle.getString(FormConst.BT_RESET));
        inputPanel.add(resetBt);
        // 输入 [进程]
        JLabel pIdLabel = new JLabel(bundle.getString(FormConst.LB_PID));
        inputPanel.add(pIdLabel);
        JTextField pIdText = new JTextField(20);
        inputPanel.add(pIdText);
        // 按钮 [进程关闭]
        JButton killBt = new JButton(bundle.getString(FormConst.BT_KILL));
        inputPanel.add(killBt);

        // Table 对象
        DefaultTableModel tableModel = new DefaultTableModel(Header.getHeaders(locale), 0);
        JScrollPane tablePanel = TableService.drawTable(tableModel, portText, pIdText);

        // 按钮事件监听
        ButtonService buttonService = new ButtonService(bundle, frame, tableModel);
        buttonService.searchTable(portText, searchBt);
        buttonService.resetTable(portText, pIdText, resetBt);
        buttonService.killProcess(portText, pIdText, killBt, resetBt);

        // 布局调整
        formatStyle(inputPanel, tablePanel);
    }

    private static void formatStyle(JPanel inputPanel, JScrollPane tablePanel) {
        // 内容面板的内边距
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(10, 20, 20, 20));
        // 将 panel 添加到内容面板中
        contentPane.add(inputPanel, BorderLayout.NORTH);
        contentPane.add(tablePanel, BorderLayout.CENTER);

        frame.setContentPane(contentPane);
        // 设置面板位置未屏幕中央
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
