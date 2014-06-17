package com.coinbase.api.entity;

public class ButtonResponse extends Response {
    private Button _button;
    
    public Button getButton() {
        return _button;
    }

    public void setButton(Button button) {
        _button = button;
    }
}
