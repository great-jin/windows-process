package xyz.ibudai.process;

import xyz.ibudai.process.model.ProcessDetail;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProcessMain {

    public static void main(String[] args) {
        // 创建 JFrame 窗口
        JFrame frame = new JFrame("Windows 进程管理");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);

        // 端口查询重置
        JPanel inputPanel = new JPanel();
        JLabel portLabel = new JLabel("Port:");
        JTextField portText = new JTextField(20);
        JButton searchBt = new JButton("Search");
        JButton resetBt = new JButton("Reset");
        inputPanel.add(portLabel);
        inputPanel.add(portText);
        inputPanel.add(searchBt);
        inputPanel.add(resetBt);
        // 进程查询删除
        JLabel pIdLabel = new JLabel("PID:");
        JTextField pIdText = new JTextField(20);
        JButton killBt = new JButton("Kill");
        inputPanel.add(pIdLabel);
        inputPanel.add(pIdText);
        inputPanel.add(killBt);

        // Table 对象
        String[] heads = new String[]{"PID", "Protocol", "Inner Host", "Outer Host", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(heads, 0);
        JScrollPane tablePanel = drawTable(tableModel, portText, pIdText);

        // 端口进程搜索
        searchTable(frame, portText, searchBt, tableModel);
        // 表格数据重置
        resetTable(resetBt, portText, pIdText, tableModel);
        // 删除进程
        killProcess(frame, portText, pIdText, killBt, resetBt);

        // 设置内容面板的内边距
        JPanel contentPane = new JPanel(new BorderLayout());
        // 上、左、下、右各 20 像素
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        // 将 panel 添加到内容面板中
        contentPane.add(tablePanel, BorderLayout.CENTER);
        contentPane.add(inputPanel, BorderLayout.NORTH);

        // 将内容面板设置为 JFrame 的内容面板
        frame.setContentPane(contentPane);
        // 设置面板位置未屏幕中央
        frame.setLocationRelativeTo(null);
        // 设置 JFrame 可见
        frame.setVisible(true);
    }

    /**
     * 绘制 Table
     *
     * @param tableModel table 数据
     * @param portField  PORT 输入框
     * @param pIdField   PID 输入框
     * @return
     */
    private static JScrollPane drawTable(DefaultTableModel tableModel, JTextField portField, JTextField pIdField) {
        for (ProcessDetail detail : buildTableData()) {
            tableModel.addRow(
                    new Object[]{
                            detail.getPid(),
                            detail.getProtocol(),
                            detail.getInnerHost(),
                            detail.getOuterHost(),
                            detail.getStatus()
                    }
            );
        }

        // 创建 JTable
        JTable table = new JTable(tableModel) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setFont(new Font("Serif", Font.PLAIN, 17)); // 设置表格字体大小为 16px
        table.setRowHeight(20); // 设置行高，使得字体更适合

        // 添加表格选中行监听器
        table.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // PID
                    String pid = (String) tableModel.getValueAt(selectedRow, 0);
                    pIdField.setText(pid);
                    // Port
                    String innerHost = (String) tableModel.getValueAt(selectedRow, 2);
                    String port = innerHost.substring(innerHost.indexOf(":") + 1);
                    portField.setText(port);
                }
            }
        });

        return new JScrollPane(table);
    }

    /**
     * 查询系统中当前进程
     *
     * @return
     */
    public static List<ProcessDetail> buildTableData() {
        List<ProcessDetail> detailList = new ArrayList<>();

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("netstat", "-ano");
        try {
            Process process = processBuilder.start();
            try (
                    InputStreamReader in = new InputStreamReader(process.getInputStream(), "GBK");
                    BufferedReader reader = new BufferedReader(in)
            ) {
                String line;
                while ((line = reader.readLine()) != null) {
                    List<String> list = Arrays.stream(line.split(" "))
                            .filter(it -> !Objects.equals("", it))
                            .collect(Collectors.toList());
                    if (list.size() != 5) {
                        continue;
                    }

                    detailList.add(
                            new ProcessDetail(
                                    list.get(0),
                                    list.get(1),
                                    list.get(2),
                                    list.get(3),
                                    list.get(4)
                            )
                    );
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return detailList.stream().skip(1).collect(Collectors.toList());
    }

    /**
     * 根据输入端口号过滤进程
     *
     * @param frame
     * @param textField
     * @param searchBt
     * @param tableModel
     */
    public static void searchTable(JFrame frame, JTextField textField, JButton searchBt, DefaultTableModel tableModel) {
        // 添加按钮点击事件监听器
        searchBt.addActionListener(h -> {
            // 获取输入框的值
            String input = textField.getText();
            // 在此处添加将输入框的值处理并更新表格逻辑
            if (Objects.isNull(input) || Objects.equals("", input)) {
                JOptionPane.showMessageDialog(frame, "请输入端口号", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<ProcessDetail> detailList = buildTableData();
            detailList = detailList.stream()
                    .filter(it -> {
                        String innerHost = it.getInnerHost();
                        String host = innerHost.substring(innerHost.indexOf(":") + 1);
                        return Objects.equals(input.trim(), host.trim());
                    })
                    .collect(Collectors.toList());
            if (detailList.isEmpty()) {
                String msg = String.format("未找到端口 {%s} 进程", input);
                JOptionPane.showMessageDialog(frame, msg, "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            for (ProcessDetail detail : detailList) {
                tableModel.setRowCount(0); // 清空表格
                tableModel.addRow(
                        new Object[]{
                                detail.getPid(),
                                detail.getProtocol(),
                                detail.getInnerHost(),
                                detail.getOuterHost(),
                                detail.getStatus()
                        }
                );
            }
            textField.setText(""); // 清空输入框
        });
    }

    /**
     * 重置表格数据
     *
     * @param resetBt
     * @param tableModel
     */
    public static void resetTable(JButton resetBt, JTextField portField, JTextField pidField, DefaultTableModel tableModel) {
        resetBt.addActionListener(h -> {
            tableModel.setRowCount(0);
            for (ProcessDetail detail : buildTableData()) {
                tableModel.addRow(
                        new Object[]{
                                detail.getPid(),
                                detail.getProtocol(),
                                detail.getInnerHost(),
                                detail.getOuterHost(),
                                detail.getStatus()
                        }
                );
            }
            portField.setText("");
            pidField.setText("");
        });
    }

    /**
     * Kill 所选进程
     *
     * @param frame
     * @param pidField
     * @param killBt
     * @param resetBt
     */
    public static void killProcess(JFrame frame, JTextField portField, JTextField pidField, JButton killBt, JButton resetBt) {
        killBt.addActionListener(h -> {
            String text = pidField.getText();
            if (Objects.isNull(text) || Objects.equals("", text)) {
                JOptionPane.showMessageDialog(frame, "请输入 PID", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("taskkill", "-PID", text, "-F");
            try {
                processBuilder.start();
                resetBt.doClick();
                portField.setText("");
                pidField.setText("");
                JOptionPane.showMessageDialog(frame, "进程关闭成功", "Successfully", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
