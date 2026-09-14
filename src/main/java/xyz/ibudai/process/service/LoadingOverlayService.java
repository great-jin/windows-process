package xyz.ibudai.process.service;

import javax.swing.*;
import java.awt.*;

/**
 * Loading 遮罩服务：在表格上方叠加半透明加载指示器。
 * <p>
 * 职责：
 * - 在 JScrollPane 上方覆盖一个半透明 loading 面板
 * - 绘制旋转圆环加载动画
 * - 管理遮罩层的生命周期
 * <p>
 * 实现原理：
 * 内部维护一个带 {@link OverlayLayout} 的容器，将 JScrollPane 和遮罩面板
 * 共享同一矩形区域，遮罩浮在表格上方。
 */
public class LoadingOverlayService {

    private static final int SPINNER_SIZE = 48;
    private static final int TICK_MS = 16;  // ~60 FPS

    private final JPanel container;
    private JPanel overlayPanel;
    private Timer animationTimer;
    private double angle = 0;

    /**
     * 创建 LoadingOverlayService 并返回包裹了 JScrollPane 的容器。
     * <p>
     * 调用方应将返回的容器放入界面布局，替代原来的 JScrollPane。
     *
     * @param scrollPane 原始的表格滚动面板
     */
    public LoadingOverlayService(JScrollPane scrollPane) {
        this.container = new JPanel();
        this.container.setLayout(new OverlayLayout(container));
        this.container.setOpaque(false);
        this.container.add(scrollPane);
    }

    /**
     * 获取包裹了 JScrollPane 的容器面板。
     * <p>
     * 调用方在布局中应使用此容器替代原来的 JScrollPane。
     *
     * @return OverlayLayout 容器
     */
    public JPanel getContainer() {
        return container;
    }

    /**
     * 显示 loading 遮罩。
     * <p>
     * 在表格上方覆盖一个半透明面板，中心显示旋转加载动画。
     */
    public void showLoading() {
        if (overlayPanel != null) {
            return; // 已经在显示
        }

        overlayPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 半透明背景
                g2.setColor(new Color(255, 255, 255, 180));
                g2.fillRect(0, 0, getWidth(), getHeight());

                // 旋转圆环
                int cx = getWidth() / 2;
                int cy = getHeight() / 2;
                drawSpinner(g2, cx, cy);

                // "Loading ..." 文字
                g2.setColor(UiStyleService.COLOR_TEXT_SECONDARY);
                g2.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
                String text = "Loading ...";
                FontMetrics fm = g2.getFontMetrics();
                int textX = cx - fm.stringWidth(text) / 2;
                int textY = cy + SPINNER_SIZE / 2 + fm.getHeight();
                g2.drawString(text, textX, textY);

                g2.dispose();
            }

            @Override
            public boolean contains(int x, int y) {
                // 拦截鼠标事件，使底层表格不可交互
                return true;
            }
        };
        overlayPanel.setOpaque(true);
        overlayPanel.setBackground(new Color(255, 255, 255, 180));

        // 添加到容器最上层（OverlayLayout 中索引 0 为最上层）
        container.add(overlayPanel, 0);
        container.revalidate();
        container.repaint();

        // 启动旋转动画
        angle = 0;
        animationTimer = new Timer(TICK_MS, e -> {
            angle += 6.0;
            if (angle >= 360.0) {
                angle -= 360.0;
            }
            if (overlayPanel != null) {
                overlayPanel.repaint();
            }
        });
        animationTimer.start();
    }

    /**
     * 隐藏 loading 遮罩。
     */
    public void hideLoading() {
        if (animationTimer != null) {
            animationTimer.stop();
            animationTimer = null;
        }
        if (overlayPanel != null) {
            container.remove(overlayPanel);
            container.revalidate();
            container.repaint();
            overlayPanel = null;
        }
    }

    /**
     * 绘制旋转圆环加载动画。
     *
     * @param g2 图形上下文
     * @param cx 中心 X
     * @param cy 中心 Y
     */
    private void drawSpinner(Graphics2D g2, int cx, int cy) {
        int radius = SPINNER_SIZE / 2;
        int thickness = 4;

        // 背景圆环（浅灰）
        g2.setColor(new Color(220, 223, 230));
        g2.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawOval(cx - radius, cy - radius, radius * 2, radius * 2);

        // 前景弧（主色，旋转）
        g2.setColor(UiStyleService.COLOR_PRIMARY);
        g2.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawArc(cx - radius, cy - radius, radius * 2, radius * 2,
                (int) angle, 90);
    }
}
