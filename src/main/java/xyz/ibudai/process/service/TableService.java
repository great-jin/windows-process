package xyz.ibudai.process.service;

import xyz.ibudai.process.common.Header;
import xyz.ibudai.process.model.ProcessDetail;
import xyz.ibudai.process.util.ProcessUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TableService {

    /**
     * 绘制 Table
     *
     * @param tableModel table 数据
     * @param portField  PORT 输入框
     * @param pIdField   PID 输入框
     */
    public static JScrollPane drawTable(DefaultTableModel tableModel, JTextField portField, JTextField pIdField) {
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
}
