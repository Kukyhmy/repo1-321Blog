package com.kuky.blog.common;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.kuky.blog.browser.filter.ImageCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import static com.kuky.blog.basics.config.Constants.EXPIRE_SECOND;
import static com.kuky.blog.basics.config.Constants.SESSION_KEY;

/**
 * 产生验证码
 */
@Controller
public class CommonController {

    @Autowired
    private DefaultKaptcha captchaProducer;

    @Autowired
    private SessionStrategy sessionStrategy;


    @GetMapping("/common/kaptcha")
    public void defaultKaptcha(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        byte[] captchaOutputStream = null;
        ByteArrayOutputStream imgOutputStream = new ByteArrayOutputStream();
        try {
            //生产验证码字符串并保存到session中
            String verifyCode = captchaProducer.createText();


            BufferedImage challenge = captchaProducer.createImage(verifyCode);
            ImageIO.write(challenge, "jpg", imgOutputStream);
            ImageCode ic = new ImageCode(challenge,verifyCode,EXPIRE_SECOND);
            sessionStrategy.setAttribute(new ServletWebRequest(req), SESSION_KEY, ic);
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        captchaOutputStream = imgOutputStream.toByteArray();
        resp.setHeader("Cache-Control", "no-store");
        resp.setHeader("Pragma", "no-cache");
        resp.setDateHeader("Expires", 0);
        resp.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = resp.getOutputStream();
        responseOutputStream.write(captchaOutputStream);
        responseOutputStream.flush();
        responseOutputStream.close();
    }
}
