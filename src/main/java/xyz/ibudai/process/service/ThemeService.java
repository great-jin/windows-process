package xyz.ibudai.process.service;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

/**
 * 主题服务：负责初始化 FlatLaf 现代化外观。
 * <p>
 * 职责：配置全局 Look & Feel，确保应用程序使用统一的现代化主题。
 */
public class ThemeService {

    private ThemeService() {
    }

    /**
     * 初始化 FlatLaf 浅色主题及全局 UI 默认值。
     * <p>
     * 必须在任何 Swing 组件创建之前调用。
     */
    public static void initTheme() {
        try {
            // 先注册 FlatLaf 主题
            FlatLightLaf.setup();

            // 在主题基础上覆盖自定义属性
            UIManager.put("Component.focusWidth", 1);
            UIManager.put("Component.innerFocusWidth", 0);
            UIManager.put("Button.arc", 999);
            UIManager.put("TextComponent.arc", 999);
            UIManager.put("ScrollBar.thumbArc", 999);
            UIManager.put("ScrollBar.thumbInsets", new java.awt.Insets(2, 2, 2, 2));
            UIManager.put("Table.showHorizontalLines", true);
            UIManager.put("Table.showVerticalLines", false);
            UIManager.put("Table.intercellSpacing", new java.awt.Dimension(0, 1));
            UIManager.put("Table.selectionBackground", new java.awt.Color(51, 153, 255));
            UIManager.put("Table.selectionForeground", java.awt.Color.WHITE);
            UIManager.put("TableHeader.height", 32);
            UIManager.put("TableHeader.font", new java.awt.Font("Microsoft YaHei UI", java.awt.Font.BOLD, 13));
        } catch (Exception e) {
            System.err.println("Failed to initialize FlatLaf theme: " + e.getMessage());
        }
    }
}
