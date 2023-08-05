package com.github.desperado2.data.open.api.user.manage.model;

import java.awt.image.BufferedImage;

/**
 * 验证码对象
 * @author tu nan
 * @date 2021/3/17
 **/

public class CaptchaModel {

    /** 结果 */
    private String result;

    /** 验证码数组 */
    private char[] codeArray;

    /** 图像 */
    private BufferedImage bufferedImage;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public char[] getCodeArray() {
        return codeArray;
    }

    public void setCodeArray(char[] codeArray) {
        this.codeArray = codeArray;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }
}
