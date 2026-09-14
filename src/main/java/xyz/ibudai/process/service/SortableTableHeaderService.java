package xyz.ibudai.process.service;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * 表格排序服务：为 JTable 的每一列添加排序功能。
 * <p>
 * 职责：
 * - 配置 TableRowSorter 实现列排序
 * - 绘制带排序箭头的自定义表头渲染器
 * - 管理排序状态（升序、降序、无排序）
 */
public class SortableTableHeaderService {

    /**
     * 排序方向枚举
     */
    public enum ColumnSortState {
        NONE("", "  "),
        ASCENDING("\u25B2", " \u25B2"),
        DESCENDING("\u25BC", " \u25BC");

        private final String arrow;
        private final String displaySuffix;

        ColumnSortState(String arrow, String displaySuffix) {
            this.arrow = arrow;
            this.displaySuffix = displaySuffix;
        }

        public String getArrow() {
            return arrow;
        }

        public String getDisplaySuffix() {
            return displaySuffix;
        }
    }

    /**
     * 为 JTable 配置排序功能。
     * <p>
     * 设置 TableRowSorter 并为每一列安装自定义的带排序箭头的表头渲染器。
     *
     * @param table      要配置排序的表格
     * @param tableModel 表格的数据模型
     */
    public static void configureSorting(JTable table, DefaultTableModel tableModel) {
        // 配置 TableRowSorter
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        // 获取列数，为每一列创建排序状态追踪
        int columnCount = tableModel.getColumnCount();
        ColumnSortState[] columnSortOrders = new ColumnSortState[columnCount];
        for (int i = 0; i < columnCount; i++) {
            columnSortOrders[i] = ColumnSortState.NONE;
        }

        // 获取表头并安装自定义渲染器
        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);

        // 为每一列设置带排序指示器的渲染器
        for (int col = 0; col < columnCount; col++) {
            final int columnIndex = col;
            header.getColumnModel().getColumn(col).setHeaderRenderer(
                    new SortableHeaderRenderer(table.getDefaultRenderer(Object.class), columnSortOrders, columnIndex)
            );
        }

        // 添加表头点击事件来切换排序
        header.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int columnIndex = header.columnAtPoint(e.getPoint());
                if (columnIndex < 0) {
                    return;
                }

                // 循环切换排序状态: NONE -> ASCENDING -> DESCENDING -> NONE
                ColumnSortState currentOrder = columnSortOrders[columnIndex];
                ColumnSortState nextOrder = getNextSortOrder(currentOrder);

                // 重置其他列的排序状态
                for (int i = 0; i < columnCount; i++) {
                    if (i != columnIndex) {
                        columnSortOrders[i] = ColumnSortState.NONE;
                    }
                }
                columnSortOrders[columnIndex] = nextOrder;

                // 应用排序
                if (nextOrder == ColumnSortState.NONE) {
                    sorter.setSortKeys(null);
                } else {
                    javax.swing.SortOrder swingOrder = (nextOrder == ColumnSortState.ASCENDING)
                            ? javax.swing.SortOrder.ASCENDING
                            : javax.swing.SortOrder.DESCENDING;
                    List<RowSorter.SortKey> sortKeys = new ArrayList<>();
                    sortKeys.add(new RowSorter.SortKey(columnIndex, swingOrder));
                    sorter.setSortKeys(sortKeys);
                }

                // 触发表头重绘以更新排序箭头
                header.repaint();
            }
        });
    }

    /**
     * 获取下一个排序状态。
     *
     * @param current 当前排序状态
     * @return 下一个排序状态
     */
    private static ColumnSortState getNextSortOrder(ColumnSortState current) {
        return switch (current) {
            case NONE -> ColumnSortState.ASCENDING;
            case ASCENDING -> ColumnSortState.DESCENDING;
            case DESCENDING -> ColumnSortState.NONE;
        };
    }

    /**
     * 自定义表头渲染器：在列名后绘制排序箭头。
     */
    private static class SortableHeaderRenderer implements TableCellRenderer {

        private final TableCellRenderer defaultRenderer;
        private final ColumnSortState[] sortOrders;
        private final int columnIndex;

        public SortableHeaderRenderer(TableCellRenderer defaultRenderer, ColumnSortState[] sortOrders, int columnIndex) {
            this.defaultRenderer = defaultRenderer;
            this.sortOrders = sortOrders;
            this.columnIndex = columnIndex;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            Component component = defaultRenderer.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            if (component instanceof JLabel label) {
                // 设置表头文本和排序箭头
                String columnName = value != null ? value.toString() : "";
                ColumnSortState order = sortOrders[columnIndex];
                label.setText(columnName + order.getDisplaySuffix());

                // 应用表头样式
                label.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 13));
                label.setForeground(UiStyleService.COLOR_TABLE_HEADER_FG);
                label.setBackground(UiStyleService.COLOR_TABLE_HEADER_BG);
                label.setOpaque(true);

                // 居中对齐
                label.setHorizontalAlignment(SwingConstants.CENTER);

                // 绘制底部边框线
                label.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 2, 0, UiStyleService.COLOR_PRIMARY),
                        BorderFactory.createEmptyBorder(4, 8, 4, 8)
                ));
            }

            return component;
        }
    }
}
