package xyz.ibudai.process.service;

import xyz.ibudai.process.common.FormConst;
import xyz.ibudai.process.model.ProcessDetail;
import xyz.ibudai.process.util.ExceptionUtils;
import xyz.ibudai.process.util.ProcessUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ButtonService {

    private final ResourceBundle bundle;
    private final Frame frame;
    private final DefaultTableModel tableModel;
    private final LoadingOverlayService loadingService;
    private final JButton[] allButtons;

    /**
     * @param bundle         国际化资源
     * @param frame          父窗口
     * @param tableModel     表格数据模型
     * @param loadingService loading 遮罩服务
     * @param allButtons     所有操作按钮（loading 时全部禁用）
     */
    public ButtonService(ResourceBundle bundle, Frame frame, DefaultTableModel tableModel,
                         LoadingOverlayService loadingService, JButton... allButtons) {
        this.bundle = bundle;
        this.frame = frame;
        this.tableModel = tableModel;
        this.loadingService = loadingService;
        this.allButtons = allButtons;
    }

    /**
     * 根据输入端口号过滤进程
     *
     * @param textField port input
     * @param searchBt  search button
     */
    public void searchTable(JTextField textField, JButton searchBt) {
        searchBt.addActionListener(h -> {
            String input = textField.getText();
            if (Objects.isNull(input) || Objects.equals(FormConst.BLANK, input)) {
                JOptionPane.showMessageDialog(
                        frame,
                        bundle.getString(FormConst.MSG_INPUT_PORT),
                        bundle.getString(FormConst.MSG_TITLE_ERROR),
                        JOptionPane.WARNING_MESSAGE
                );
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
                String msg = String.format("No found process of {%s}", input);
                JOptionPane.showMessageDialog(frame, msg, bundle.getString(FormConst.MSG_TITLE_ERROR), JOptionPane.WARNING_MESSAGE);
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
     * 重置表格数据。
     * <p>
     * 流程：
     * 1. 显示 loading 遮罩
     * 2. 禁用所有按钮
     * 3. 在后台线程中重新加载进程数据
     * 4. 加载完成后在 EDT 中更新表格并恢复 UI
     *
     * @param portField port input
     * @param pidField  pid input
     * @param resetBt   reset button
     */
    public void resetTable(JTextField portField, JTextField pidField, JButton resetBt) {
        resetBt.addActionListener(h -> {
            // 显示 loading 并禁用所有按钮
            setLoadingState(true);

            // 后台线程加载数据，避免阻塞 EDT
            SwingWorker<List<ProcessDetail>, Void> worker = new SwingWorker<>() {
                @Override
                protected List<ProcessDetail> doInBackground() {
                    // 模拟网络延迟（实际场景中此处为 I/O 耗时操作）
                    try {
                        Thread.sleep(800);
                    } catch (InterruptedException ignored) {
                    }
                    return ProcessUtils.getTaskDetail();
                }

                @Override
                protected void done() {
                    try {
                        List<ProcessDetail> detailList = get();
                        tableModel.setRowCount(0);
                        for (ProcessDetail detail : detailList) {
                            tableModel.addRow(ProcessDetail.convert(detail));
                        }
                        portField.setText(FormConst.BLANK);
                        pidField.setText(FormConst.BLANK);
                        portField.requestFocusInWindow();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(
                                frame,
                                ExceptionUtils.buildMsg(e),
                                bundle.getString(FormConst.MSG_TITLE_ERROR),
                                JOptionPane.ERROR_MESSAGE
                        );
                    } finally {
                        // 隐藏 loading 并恢复所有按钮
                        setLoadingState(false);
                    }
                }
            };
            worker.execute();
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
    public void killProcess(JTextField portField, JTextField pidField, JButton killBt, JButton resetBt) {
        killBt.addActionListener(h -> {
            String text = pidField.getText();
            if (Objects.isNull(text) || Objects.equals(FormConst.BLANK, text)) {
                JOptionPane.showMessageDialog(
                        frame,
                        bundle.getString(FormConst.MSG_INPUT_PID),
                        bundle.getString(FormConst.MSG_TITLE_ERROR),
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // 二次确认弹窗
            String confirmMsg = String.format(bundle.getString(FormConst.MSG_KILL_CONFIRM), text);
            int choice = JOptionPane.showConfirmDialog(
                    frame,
                    confirmMsg,
                    bundle.getString(FormConst.MSG_TITLE_CONFIRM),
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (choice != JOptionPane.YES_OPTION) {
                return;
            }

            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("taskkill", "-PID", text, "-F");
            try {
                processBuilder.start();
                resetBt.doClick();
                portField.setText(FormConst.BLANK);
                pidField.setText(FormConst.BLANK);
                JOptionPane.showMessageDialog(
                        frame,
                        bundle.getString(FormConst.MSG_PROCESS_CLOSE),
                        bundle.getString(FormConst.MSG_TITLE_SUCCESS),
                        JOptionPane.INFORMATION_MESSAGE
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 设置 loading 状态：切换遮罩显示/隐藏，禁用/启用所有按钮。
     *
     * @param loading true 表示进入 loading 状态，false 表示恢复
     */
    private void setLoadingState(boolean loading) {
        if (loading) {
            loadingService.showLoading();
        } else {
            loadingService.hideLoading();
        }

        // 禁用/启用所有按钮（含禁用时的视觉反馈）
        for (JButton button : allButtons) {
            button.setEnabled(!loading);
            if (loading) {
                button.setCursor(Cursor.getDefaultCursor());
            } else {
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        }
    }
}
