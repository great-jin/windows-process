package xyz.ibudai.process.service;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * UI 样式服务：提供统一的按钮样式、配色方案和组件样式配置。
 * <p>
 * 职责：集中管理所有 UI 组件的视觉样式，确保全局风格一致。
 */
public class UiStyleService {

    // ========== 主色调 ==========
    public static final Color COLOR_PRIMARY = new Color(51, 153, 255);
    public static final Color COLOR_PRIMARY_HOVER = new Color(31, 133, 235);
    public static final Color COLOR_PRIMARY_PRESSED = new Color(21, 113, 215);

    public static final Color COLOR_SUCCESS = new Color(40, 199, 111);
    public static final Color COLOR_SUCCESS_HOVER = new Color(30, 179, 101);
    public static final Color COLOR_SUCCESS_PRESSED = new Color(20, 159, 81);

    public static final Color COLOR_DANGER = new Color(245, 108, 108);
    public static final Color COLOR_DANGER_HOVER = new Color(225, 88, 88);
    public static final Color COLOR_DANGER_PRESSED = new Color(205, 68, 68);

    public static final Color COLOR_SECONDARY = new Color(144, 147, 153);
    public static final Color COLOR_SECONDARY_HOVER = new Color(124, 127, 133);
    public static final Color COLOR_SECONDARY_PRESSED = new Color(104, 107, 113);

    // ========== 文本颜色 ==========
    public static final Color COLOR_TEXT_PRIMARY = new Color(48, 49, 51);
    public static final Color COLOR_TEXT_SECONDARY = new Color(144, 147, 153);
    public static final Color COLOR_TEXT_WHITE = Color.WHITE;

    // ========== 背景与边框 ==========
    public static final Color COLOR_BG_PAGE = new Color(245, 247, 250);
    public static final Color COLOR_BG_CARD = Color.WHITE;
    public static final Color COLOR_BORDER = new Color(220, 223, 230);

    // ========== 表格专用 ==========
    public static final Color COLOR_TABLE_HEADER_BG = new Color(245, 247, 250);
    public static final Color COLOR_TABLE_HEADER_FG = new Color(96, 98, 102);
    public static final Color COLOR_TABLE_ROW_HOVER = new Color(235, 245, 255);
    public static final Color COLOR_TABLE_GRID = new Color(235, 238, 245);

    private UiStyleService() {
    }

    /**
     * 创建主色（蓝色）圆角按钮，用于查询等主操作。
     *
     * @param text 按钮文本
     * @return 配置好样式的按钮
     */
    public static JButton createPrimaryButton(String text) {
        return createStyledButton(text, COLOR_PRIMARY, COLOR_PRIMARY_HOVER, COLOR_PRIMARY_PRESSED, COLOR_TEXT_WHITE);
    }

    /**
     * 创建绿色圆角按钮，用于成功/确认操作。
     *
     * @param text 按钮文本
     * @return 配置好样式的按钮
     */
    public static JButton createSuccessButton(String text) {
        return createStyledButton(text, COLOR_SUCCESS, COLOR_SUCCESS_HOVER, COLOR_SUCCESS_PRESSED, COLOR_TEXT_WHITE);
    }

    /**
     * 创建红色圆角按钮，用于危险操作（如结束进程）。
     *
     * @param text 按钮文本
     * @return 配置好样式的按钮
     */
    public static JButton createDangerButton(String text) {
        return createStyledButton(text, COLOR_DANGER, COLOR_DANGER_HOVER, COLOR_DANGER_PRESSED, COLOR_TEXT_WHITE);
    }

    /**
     * 创建灰色圆角按钮，用于次要操作（如重置）。
     *
     * @param text 按钮文本
     * @return 配置好样式的按钮
     */
    public static JButton createSecondaryButton(String text) {
        return createStyledButton(text, COLOR_SECONDARY, COLOR_SECONDARY_HOVER, COLOR_SECONDARY_PRESSED, COLOR_TEXT_WHITE);
    }

    /**
     * 创建带样式的基础圆角按钮。
     *
     * @param text          按钮文本
     * @param bg            正常背景色
     * @param hoverBg       悬浮背景色
     * @param pressedBg     按下背景色
     * @param foreground    文本颜色
     * @return 配置好样式的按钮
     */
    private static JButton createStyledButton(String text, Color bg, Color hoverBg, Color pressedBg, Color foreground) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 绘制圆角背景
                Color fillColor;
                if (!isEnabled()) {
                    // 禁用状态：使用半透明灰色
                    fillColor = new Color(200, 200, 200);
                } else if (getModel().isPressed()) {
                    fillColor = pressedBg;
                } else if (getModel().isRollover()) {
                    fillColor = hoverBg;
                } else {
                    fillColor = bg;
                }
                g2.setColor(fillColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());

                g2.dispose();
                super.paintComponent(g);
            }
        };

        button.setForeground(foreground);
        button.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(8, 20, 8, 20));
        button.setPreferredSize(new Dimension(button.getPreferredSize().width + 10, 36));

        return button;
    }

    /**
     * 配置输入框样式。
     *
     * @param textField 要配置的文本框
     * @param columns   列宽
     */
    public static JTextField createStyledTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 13));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(6, 12, 6, 12)
        ));
        textField.setPreferredSize(new Dimension(textField.getPreferredSize().width, 36));
        return textField;
    }

    /**
     * 创建输入面板的统一内边距容器。
     * <p>
     * 仅设置背景、边框和内边距，布局由调用方决定。
     *
     * @return 带边距和样式的面板
     */
    public static JPanel createInputPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(COLOR_BG_CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER),
                new EmptyBorder(10, 10, 10, 10)
        ));
        return panel;
    }
}
