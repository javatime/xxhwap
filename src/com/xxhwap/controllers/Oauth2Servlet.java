package com.xxhwap.controllers;
import com.xxhwap.contrants.MobilePageContants;
import com.xxhwap.services.ICoreService;
import com.xxhwap.utils.Config;
import com.xxhwap.utils.HttpUtil;
import com.xxhwap.utils.weixin.UrlContants;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * Created by Administrator on 2015/7/10.
 */
@Controller
public class Oauth2Servlet {
    private static final long serialVersionUID = -644518508267758016L;
    @Autowired
    protected ICoreService coreService;
    @RequestMapping(value = "/oauth/do.html", method = RequestMethod.GET)
    public ModelAndView oauth(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        String get_access_token_url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=APPID" +
                "&secret=SECRET&" +
                "code=CODE&grant_type=authorization_code";
        String get_userinfo = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
        ModelAndView mv = new ModelAndView();
        try {
            // 将请求、响应的编码均设置为UTF-8（防止中文乱码）
            // xml请求解析
            // 将请求、响应的编码均设置为UTF-8（防止中文乱码）
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            String code = request.getParameter("code");
            //判断页面跳转
            String key = request.getParameter("state");
            Config config = new Config();
            String appid = config.getString("appid");
            String appsecret = config.getString("appsecret");
            String domain = config.getString("domain");
            get_access_token_url = get_access_token_url.replace("APPID", appid);
            get_access_token_url = get_access_token_url.replace("SECRET", appsecret);
            get_access_token_url = get_access_token_url.replace("CODE", code);
            String json = HttpUtil.getUrl(get_access_token_url);
            String openid = "";
            //获取当前登录的用户id

            JSONObject jsonObject = JSONObject.fromObject(json);
            if (jsonObject.has("openid")) {
                openid = jsonObject.getString("openid");
            }
            ServletContext application =request.getSession().getServletContext();
            //保存用户的openid到全局缓存中
           // application.setAttribute(MobilePageContants.CURRENT_USER_OPENID,openid);
            application.setAttribute(MobilePageContants.CURRENT_USER_KEY,key);
            request.getSession().setAttribute(MobilePageContants.CURRENT_USER_OPENID,openid);
            String page="";
            if(UrlContants.MENU_KEY_1.equals(key)){
                page=domain+"book/buy.html";
            }
            if(UrlContants.MENU_KEY_2.equals(key)){
                page=domain+"book/sell.html";
            }
            if(UrlContants.MENU_KEY_3.equals(key)){
                page=domain+"book/mybuy.html";
            }
            if(UrlContants.MENU_KEY_5.equals(key)){
                page=domain+"book/mysell.html";
            }
            if(StringUtils.isEmpty(openid)){
                page="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appid+"&redirect_uri="+domain+"/oauth/do.html&response_type=code&scope=snsapi_base&state="+key+"#wechat_redirect";
            }
            mv.setViewName("redirect:"+page);
            /*String access_token = jsonObject.getString("access_token");
            WeiXinUserBo bo = WeixinMain.getWechatUserInfo(appid, access_token);
            mv.addObject("bo", bo);
            if (!StringUtils.isEmpty(bo)) {
                System.out.println("昵称=====================================" + bo.getNickname());
            }*/
            response.setContentType("text/html; charset=utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mv;
    }
}
