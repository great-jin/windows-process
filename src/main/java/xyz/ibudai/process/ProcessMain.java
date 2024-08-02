package xyz.ibudai.process;

import xyz.ibudai.process.common.FormConst;
import xyz.ibudai.process.common.Header;
import xyz.ibudai.process.model.ProcessDetail;
import xyz.ibudai.process.util.ExceptionUtils;
import xyz.ibudai.process.util.ProcessUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProcessMain {

    private static final JFrame frame = new JFrame(FormConst.Zh.TITLE);


    public static void main(String[] args) {
        try {
            draw();
        } catch (Exception e) {
            String message = ExceptionUtils.buildMsg(e);
            JOptionPane.showMessageDialog(frame, message, FormConst.Zh.MSG_TITLE_UNEXPECT, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 绘制程序窗体页面
     */
    public static void draw() {
        // 创建 JFrame 窗口
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(FormConst.FRAME_WIDTH, FormConst.FRAME_HEIGHT);

        // 端口输入；查询重置按钮
        JPanel inputPanel = new JPanel();
        JLabel portLabel = new JLabel(FormConst.Zh.LB_PORT);
        JTextField portText = new JTextField(20);
        JButton searchBt = new JButton(FormConst.Zh.BT_SEARCH);
        JButton resetBt = new JButton(FormConst.Zh.BT_RESET);
        inputPanel.add(portLabel);
        inputPanel.add(portText);
        inputPanel.add(searchBt);
        inputPanel.add(resetBt);
        // 进程输入；删除按钮
        JLabel pIdLabel = new JLabel(FormConst.Zh.LB_PID);
        JTextField pIdText = new JTextField(20);
        JButton killBt = new JButton(FormConst.Zh.BT_KILL);
        inputPanel.add(pIdLabel);
        inputPanel.add(pIdText);
        inputPanel.add(killBt);

        // Table 对象
        DefaultTableModel tableModel = new DefaultTableModel(Header.zhHeaders(), 0);
        JScrollPane tablePanel = drawTable(tableModel, portText, pIdText);

        // 搜索按钮事件监听
        searchTable(portText, searchBt, tableModel);
        // 重置按钮事件监听
        resetTable(resetBt, portText, pIdText, tableModel);
        // Kill按钮事件监听
        killProcess(portText, pIdText, killBt, resetBt);

        // 内容面板的内边距
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        // 将 panel 添加到内容面板中
        contentPane.add(tablePanel, BorderLayout.CENTER);
        contentPane.add(inputPanel, BorderLayout.NORTH);

        frame.setContentPane(contentPane);
        // 设置面板位置未屏幕中央
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * 绘制 Table
     *
     * @param tableModel table 数据
     * @param portField  PORT 输入框
     * @param pIdField   PID 输入框
     */
    private static JScrollPane drawTable(DefaultTableModel tableModel, JTextField portField, JTextField pIdField) {
        for (ProcessDetail detail : ProcessUtils.getTaskDetail()) {
            tableModel.addRow(ProcessDetail.convert(detail));
        }

        // 创建 JTable
        JTable table = new JTable(tableModel) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setFont(new Font("Serif", Font.PLAIN, 17));
        table.setRowHeight(20);

        // 添加表格选中行监听器
        table.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // PID
                    String pid = (String) tableModel.getValueAt(selectedRow, Header.PID.getIndex());
                    pIdField.setText(pid);
                    // Port
                    String port;
                    String innerHost = (String) tableModel.getValueAt(selectedRow, Header.INNER_HOST.getIndex());
                    if (innerHost.startsWith("[")) {
                        port = innerHost.substring(innerHost.indexOf("]:") + 2);
                    } else {
                        port = innerHost.substring(innerHost.indexOf(":") + 1);
                    }
                    portField.setText(port);
                }
            }
        });

        return new JScrollPane(table);
    }

    /**
     * 根据输入端口号过滤进程
     *
     * @param textField  port input
     * @param searchBt   search button
     * @param tableModel table data
     */
    private static void searchTable(JTextField textField, JButton searchBt, DefaultTableModel tableModel) {
        // 添加按钮点击事件监听器
        searchBt.addActionListener(h -> {
            String input = textField.getText();
            if (Objects.isNull(input) || Objects.equals(FormConst.BLANK, input)) {
                JOptionPane.showMessageDialog(frame, FormConst.Zh.MSG_INPUT_PORT, FormConst.Zh.MSG_TITLE_ERROR, JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<ProcessDetail> detailList = ProcessUtils.getTaskDetail();
            detailList = detailList.stream()
                    .filter(it -> {
                        String port;
                        String innerHost = it.getInnerHost();
                        if (innerHost.startsWith("[")) {
                            port = innerHost.substring(innerHost.indexOf("]:") + 2);
                        } else {
                            port = innerHost.substring(innerHost.indexOf(":") + 1);
                        }
                        return Objects.equals(input.trim(), port.trim());
                    })
                    .collect(Collectors.toList());
            if (detailList.isEmpty()) {
                String msg = String.format("未找到端口 {%s} 进程", input);
                JOptionPane.showMessageDialog(frame, msg, FormConst.Zh.MSG_TITLE_ERROR, JOptionPane.WARNING_MESSAGE);
                return;
            }

            for (ProcessDetail detail : detailList) {
                tableModel.setRowCount(0);
                tableModel.addRow(ProcessDetail.convert(detail));
            }
            textField.setText(FormConst.BLANK);
        });
    }

    /**
     * 重置表格数据
     *
     * @param resetBt    reset button
     * @param portField  port input
     * @param pidField   pid input
     * @param tableModel table data
     */
    private static void resetTable(JButton resetBt, JTextField portField, JTextField pidField, DefaultTableModel tableModel) {
        resetBt.addActionListener(h -> {
            tableModel.setRowCount(0);
            for (ProcessDetail detail : ProcessUtils.getTaskDetail()) {
                tableModel.addRow(ProcessDetail.convert(detail));
            }
            portField.setText(FormConst.BLANK);
            pidField.setText(FormConst.BLANK);
        });
    }

    /**
     * Kill 所选进程
     *
     * @param portField port input
     * @param pidField  pid input
     * @param killBt    kill button
     * @param resetBt   reset button
     */
    private static void killProcess(JTextField portField, JTextField pidField, JButton killBt, JButton resetBt) {
        killBt.addActionListener(h -> {
            String text = pidField.getText();
            if (Objects.isNull(text) || Objects.equals(FormConst.BLANK, text)) {
                JOptionPane.showMessageDialog(frame, FormConst.Zh.MSG_INPUT_PID, FormConst.Zh.MSG_TITLE_ERROR, JOptionPane.ERROR_MESSAGE);
                return;
            }

            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("taskkill", "-PID", text, "-F");
            try {
                processBuilder.start();
                resetBt.doClick();
                portField.setText(FormConst.BLANK);
                pidField.setText(FormConst.BLANK);
                JOptionPane.showMessageDialog(frame, FormConst.Zh.MSG_PROCESS_CLOSE, FormConst.Zh.MSG_TITLE_SUCCESS, JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
