package xyz.ibudai.process.service;

import xyz.ibudai.process.common.FormConst;
import xyz.ibudai.process.common.Language;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class I18nService {

    private Frame frame;
    private Locale locale;
    private ResourceBundle bundle;

    public I18nService(Frame frame, Locale locale, ResourceBundle bundle) {
        this.frame = frame;
        this.locale = locale;
        this.bundle = bundle;
    }

    /**
     * Switch language
     */
    public void switchLanguage(JButton button) {
        button.addActionListener(h -> {
            if (Objects.equals(locale.getLanguage(), Language.EN.getLanguage())) {
                locale = new Locale(Language.ZH.getLanguage());
            } else {
                locale = new Locale(Language.EN.getLanguage());
            }
            bundle = ResourceBundle.getBundle(FormConst.I18N_RESOURCE, locale);
        });
        // TODO: 2024/8/3 switch element language
    }
}
