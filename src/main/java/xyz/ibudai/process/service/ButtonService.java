package xyz.ibudai.process.service;

import xyz.ibudai.process.common.FormConst;
import xyz.ibudai.process.model.ProcessDetail;
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

    public ButtonService(ResourceBundle bundle, Frame frame, DefaultTableModel tableModel) {
        this.bundle = bundle;
        this.frame = frame;
        this.tableModel = tableModel;
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
                        JOptionPane.ERROR_MESSAGE
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
     * 重置表格数据
     *
     * @param portField port input
     * @param pidField  pid input
     * @param resetBt   reset button
     */
    public void resetTable(JTextField portField, JTextField pidField, JButton resetBt) {
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
    public void killProcess(JTextField portField, JTextField pidField, JButton killBt, JButton resetBt) {
        killBt.addActionListener(h -> {
            String text = pidField.getText();
            if (Objects.isNull(text) || Objects.equals(FormConst.BLANK, text)) {
                JOptionPane.showMessageDialog(
                        frame,
                        bundle.getString(FormConst.MSG_INPUT_PID),
                        bundle.getString(FormConst.MSG_TITLE_ERROR),
                        JOptionPane.ERROR_MESSAGE
                );
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
}
