package com.github.desperado2.data.open.api.user.manage.service.impl;

import com.github.desperado2.data.open.api.common.manage.exception.DataOpenPlatformRuntimeException;
import com.github.desperado2.data.open.api.user.manage.model.CaptchaModel;
import com.github.desperado2.data.open.api.user.manage.service.ICaptchaService;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 验证码
 * @author tu nan
 * @date 2021/3/31
 **/
@Service
public class CaptchaServiceImpl implements ICaptchaService {


    /**
     * 宽度
     */
    private static final int WIDTH = 90;

    /**
     * 高度
     */
    private static final int HEIGHT = 30;

    /**
     * 行数
     */
    private static final int LINENUM = 2;

    /** 字体重 */
    //    private static final float YAWP     = 0.01f;

    /**
     * 颜色
     */
    private static final Color COLOR = new Color(253, 251, 255);

    /**
     * 字符
     */
    private static char[] CH_ARRAY = "ABCDEFGHJKMNPQRSTUVWXYZ23456789".toCharArray();

    /**
     * 验证码长度
     */
    private static final int CODECNT = 4;

    @Override
    public CaptchaModel generateSimple() {

        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();

        graphics.setColor(COLOR);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);

        // 干扰线
        drawPoint(image);
        drawLine(image);

        // 画验证码
        char[] simpleCode = generateSimpleCode(CODECNT);
        drawCode(graphics, simpleCode);

        CaptchaModel result = new CaptchaModel();
        result.setCodeArray(simpleCode);
        result.setResult(String.valueOf(simpleCode));
        result.setBufferedImage(image);
        return result;
    }

    /**
     * 把验证码画到画板上
     *
     * @param graphics
     */
    private void drawCode(Graphics graphics, char[] simpleCode) {
        Random random = new Random();
        // 画笔设置字体
        Font font = getFont(16);
        graphics.setFont(font);
        if (simpleCode == null || simpleCode.length == 0) {
            throw new DataOpenPlatformRuntimeException("字符数组不能为空");
        }
        for (int i = 0; i < simpleCode.length; i++) {
            String code = String.valueOf(simpleCode[i]);
            // 将随机产生的颜色将验证码绘制到图像中
            graphics.setColor(getRandColor(1, 255));
            graphics.drawString(code, (i * WIDTH / 5) + 5, HEIGHT / 2 + random.nextInt(HEIGHT / 4));
        }
    }

    /**
     * 生成简单验证码字符数组
     *
     * @param len
     * @return
     */
    private char[] generateSimpleCode(int len) {
        Random random = new Random();
        char[] codeArray = new char[len];
        for (int i = 0; i < len; i++) {
            char c = CH_ARRAY[random.nextInt(CH_ARRAY.length)];
            codeArray[i] = c;
        }
        return codeArray;
    }

    /**
     * 画干扰线
     *
     * @param image
     */
    private void drawLine(BufferedImage image) {
        Random random = new Random();
        Graphics graphics = image.getGraphics();
        for (int i = 0; i < LINENUM; i++) {
            int xs = random.nextInt(WIDTH);
            int ys = random.nextInt(HEIGHT);
            int xe = xs + random.nextInt(WIDTH);
            int ye = ys + random.nextInt(HEIGHT);
            graphics.setColor(getRandColor(1, 255));
            graphics.drawLine(xs, ys, xe, ye);
        }
    }

    /**
     * 画噪点
     *
     * @param image
     */
    private void drawPoint(BufferedImage image) {
        Random random = new Random();
        float yawpRate = 0f;
        int area = (int) (yawpRate * WIDTH * HEIGHT);
        for (int i = 0; i < area; i++) {
            int x = random.nextInt(WIDTH);
            int y = random.nextInt(HEIGHT);
            image.setRGB(x, y, random.nextInt(255));
        }
    }

    /**
     * 随机颜色
     *
     * @param fc
     * @param bc
     * @return
     */
    private Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    private Font getFont(int size) {
        Font[] font = new Font[5];
        font[0] = new Font("Ravie", Font.BOLD, size);
        font[1] = new Font("Antique Olive Compact", Font.BOLD, size);
        font[2] = new Font("Fixedsys", Font.BOLD, size);
        font[3] = new Font("Wide Latin", Font.BOLD, size);
        font[4] = new Font("Gill Sans Ultra Bold", Font.BOLD, size);
        return font[1];
    }

}
