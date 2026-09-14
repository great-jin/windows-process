package xyz.ibudai.process.service;

import xyz.ibudai.process.common.Header;
import xyz.ibudai.process.model.ProcessDetail;
import xyz.ibudai.process.util.ProcessUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * 表格服务：负责绘制和配置数据表格。
 * <p>
 * 职责：
 * - 创建 JTable 并配置数据
 * - 设置表格样式（行高、字体、边框等）
 * - 委托 SortableTableHeaderService 为表头添加排序功能
 * - 处理表格行选中事件
 */
public class TableService {

    /**
     * 绘制 Table
     *
     * @param tableModel table 数据
     * @param portField  PORT 输入框
     * @param pIdField   PID 输入框
     * @return 配置好样式的表格滚动面板
     */
    public static JScrollPane drawTable(DefaultTableModel tableModel, JTextField portField, JTextField pIdField) {
        // 加载数据
        for (ProcessDetail detail : ProcessUtils.getTaskDetail()) {
            tableModel.addRow(ProcessDetail.convert(detail));
        }

        // 创建 JTable
        JTable table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // 配置表格基础样式
        configureTableStyle(table);

        // 配置表头样式
        configureHeaderStyle(table);

        // 配置单元格渲染器
        configureCellRenderer(table);

        // 为每一列添加排序功能
        SortableTableHeaderService.configureSorting(table, tableModel);

        // 添加表格选中行监听器
        addRowSelectionListener(table, tableModel, portField, pIdField);

        // 创建滚动面板并配置样式
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(UiStyleService.COLOR_BORDER, 1, true));
        scrollPane.getViewport().setBackground(UiStyleService.COLOR_BG_CARD);
        scrollPane.setBackground(UiStyleService.COLOR_BG_CARD);

        return scrollPane;
    }

    /**
     * 配置表格基础样式。
     *
     * @param table 表格对象
     */
    private static void configureTableStyle(JTable table) {
        table.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 13));
        table.setRowHeight(36);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setGridColor(UiStyleService.COLOR_TABLE_GRID);
        table.setSelectionBackground(UiStyleService.COLOR_TABLE_ROW_HOVER);
        table.setSelectionForeground(UiStyleService.COLOR_TEXT_PRIMARY);
        table.setBackground(UiStyleService.COLOR_BG_CARD);
        table.setForeground(UiStyleService.COLOR_TEXT_PRIMARY);
        table.setFillsViewportHeight(true);
    }

    /**
     * 配置表头样式。
     *
     * @param table 表格对象
     */
    private static void configureHeaderStyle(JTable table) {
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 13));
        header.setBackground(UiStyleService.COLOR_TABLE_HEADER_BG);
        header.setForeground(UiStyleService.COLOR_TABLE_HEADER_FG);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        header.setReorderingAllowed(false);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, UiStyleService.COLOR_PRIMARY));
    }

    /**
     * 配置单元格居中渲染器。
     *
     * @param table 表格对象
     */
    private static void configureCellRenderer(JTable table) {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component component = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                // 居中对齐
                setHorizontalAlignment(SwingConstants.CENTER);

                // 交替行背景色（斑马纹）
                if (!isSelected) {
                    if (row % 2 == 0) {
                        setBackground(UiStyleService.COLOR_BG_CARD);
                    } else {
                        setBackground(new Color(250, 251, 252));
                    }
                }

                // 设置边距
                setBorder(new EmptyBorder(0, 8, 0, 8));

                return component;
            }
        };

        // 为所有列应用居中渲染器
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    /**
     * 添加表格行选中监听器。
     * <p>
     * 当用户选中表格中的某行时，自动将 PID 和端口填充到对应输入框。
     *
     * @param table      表格对象
     * @param tableModel 表格数据模型
     * @param portField  端口输入框
     * @param pIdField   PID 输入框
     */
    private static void addRowSelectionListener(JTable table, DefaultTableModel tableModel,
                                                 JTextField portField, JTextField pIdField) {
        table.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow == -1) {
                    return;
                }

                // 转换视图行索引为模型行索引（排序后需转换）
                int modelRow = table.convertRowIndexToModel(selectedRow);

                // PID
                String pid = (String) tableModel.getValueAt(modelRow, Header.PID.getIndex());
                pIdField.setText(pid);

                // Port
                String innerHost = (String) tableModel.getValueAt(modelRow, Header.INNER_HOST.getIndex());
                String port;
                if (innerHost.startsWith("[")) {
                    port = innerHost.substring(innerHost.indexOf("]:") + 2);
                } else {
                    port = innerHost.substring(innerHost.indexOf(":") + 1);
                }
                portField.setText(port);
            }
        });
    }
}
