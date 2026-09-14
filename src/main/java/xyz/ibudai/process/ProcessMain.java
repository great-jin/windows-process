package xyz.ibudai.process;

import xyz.ibudai.process.common.FormConst;
import xyz.ibudai.process.common.Header;
import xyz.ibudai.process.common.Language;
import xyz.ibudai.process.service.ButtonService;
import xyz.ibudai.process.service.LoadingOverlayService;
import xyz.ibudai.process.service.TableService;
import xyz.ibudai.process.service.ThemeService;
import xyz.ibudai.process.service.UiStyleService;
import xyz.ibudai.process.util.ExceptionUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class ProcessMain {

    private static final JFrame frame;
    private static final Locale locale;
    private static final ResourceBundle bundle;

    static {
        // 必须在创建任何 Swing 组件之前初始化 FlatLaf 主题
        ThemeService.initTheme();

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
        frame.setMinimumSize(new Dimension(940, 300));

        // 创建输入框
        JTextField portText = UiStyleService.createStyledTextField(16);
        JTextField pIdText = UiStyleService.createStyledTextField(16);

        // 创建按钮
        JButton searchBt = UiStyleService.createPrimaryButton(bundle.getString(FormConst.BT_SEARCH));
        JButton resetBt = UiStyleService.createSecondaryButton(bundle.getString(FormConst.BT_RESET));
        JButton killBt = UiStyleService.createDangerButton(bundle.getString(FormConst.BT_KILL));

        // 使用样式服务创建输入面板（GridBagLayout 居中 + 自适应）
        JPanel inputPanel = createCenteredInputPanel(portText, pIdText, searchBt, resetBt, killBt);

        // Table 对象
        DefaultTableModel tableModel = new DefaultTableModel(Header.getHeaders(locale), 0);
        JScrollPane tablePanel = TableService.drawTable(tableModel, portText, pIdText);

        // Loading 遮罩服务 — 用 OverlayLayout 容器包裹 tablePanel
        LoadingOverlayService loadingService = new LoadingOverlayService(tablePanel);
        JPanel tableContainer = loadingService.getContainer();

        // 按钮事件监听（传入 loading 服务和所有按钮）
        ButtonService buttonService = new ButtonService(
                bundle, frame, tableModel, loadingService, searchBt, resetBt, killBt
        );
        buttonService.searchTable(portText, searchBt);
        buttonService.resetTable(portText, pIdText, resetBt);
        buttonService.killProcess(portText, pIdText, killBt, resetBt);

        // 布局调整（使用 tableContainer 替代原来的 tablePanel）
        formatStyle(inputPanel, tableContainer);
    }

    /**
     * 创建居中且自适应的输入面板。
     * <p>
     * 使用 GridBagLayout 实现：
     * - 输入区域（标签 + 输入框 + 按钮）水平居中
     * - 输入框在窗口缩放时自适应宽度
     *
     * @param portText 端口输入框
     * @param pIdText  PID 输入框
     * @param searchBt 查询按钮
     * @param resetBt  重置按钮
     * @param killBt   结束进程按钮
     * @return 配置好的输入面板
     */
    private static JPanel createCenteredInputPanel(JTextField portText, JTextField pIdText,
                                                    JButton searchBt, JButton resetBt, JButton killBt) {
        // 外层面板
        JPanel inputPanel = UiStyleService.createInputPanel();
        inputPanel.setLayout(new GridBagLayout());

        // 内部表单面板：一行放置所有组件
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.anchor = GridBagConstraints.CENTER;

        int col = 0;

        // [Port 标签]
        JLabel portLabel = new JLabel(bundle.getString(FormConst.LB_PORT));
        portLabel.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 13));
        portLabel.setForeground(UiStyleService.COLOR_TEXT_PRIMARY);
        gbc.gridx = col++;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(portLabel, gbc);

        // [Port 输入框] — 可水平伸缩
        gbc.gridx = col++;
        gbc.weightx = 0.3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(portText, gbc);

        // [查询按钮]
        gbc.gridx = col++;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(searchBt, gbc);

        // [重置按钮]
        gbc.gridx = col++;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(resetBt, gbc);

        // [PID 标签]
        JLabel pIdLabel = new JLabel(bundle.getString(FormConst.LB_PID));
        pIdLabel.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 13));
        pIdLabel.setForeground(UiStyleService.COLOR_TEXT_PRIMARY);
        gbc.gridx = col++;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(pIdLabel, gbc);

        // [PID 输入框] — 可水平伸缩
        gbc.gridx = col++;
        gbc.weightx = 0.3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(pIdText, gbc);

        // [结束进程按钮]
        gbc.gridx = col++;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(killBt, gbc);

        // 将 formPanel 居中放入外层面板
        GridBagConstraints outerGbc = new GridBagConstraints();
        outerGbc.gridx = 0;
        outerGbc.gridy = 0;
        outerGbc.anchor = GridBagConstraints.CENTER;
        outerGbc.weightx = 1.0;
        outerGbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(formPanel, outerGbc);

        return inputPanel;
    }

    /**
     * 组装内容面板并显示窗口。
     */
    private static void formatStyle(JPanel inputPanel, JPanel tableContainer) {
        // 内容面板使用页面背景色
        JPanel contentPane = new JPanel(new BorderLayout(0, 10));
        contentPane.setBackground(UiStyleService.COLOR_BG_PAGE);
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 将 panel 添加到内容面板中
        contentPane.add(inputPanel, BorderLayout.NORTH);
        contentPane.add(tableContainer, BorderLayout.CENTER);

        frame.setContentPane(contentPane);
        // 设置面板位置为屏幕中央
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
