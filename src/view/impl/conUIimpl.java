package view.impl;

import view.baseUI;

public class conUIimpl implements baseUI {
    @Override
    public void showMessage(String message) {
        System.out.println(message);

    }

    @Override
    public void showMessages(String[] messages) {
        for (int i = 0; i < messages.length; i++) showMessage(messages[i]);
    }
}
