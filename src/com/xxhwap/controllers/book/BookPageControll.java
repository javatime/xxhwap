package com.xxhwap.controllers.book;

import com.xxhwap.book.RetrieveDocumentByURL;
import com.xxhwap.book.TudouBookInfo;
import com.xxhwap.contrants.MobilePageContants;
import com.xxhwap.services.ICoreService;
import com.xxhwap.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * 手机端主页调整控制
 * @author zhengyunfei
 * @date 2015-04-22
 *
 */

@Controller
public class BookPageControll {
	public  final static String TU_DOU_BOOK_URI="http://api.douban.com/book/subject/isbn/ISBN";
	public  final static String DOWNLOAD_WEIXIN_IMAGE="http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
	/**
	 * 首页
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/book/index.html", method = RequestMethod.GET)
	public ModelAndView indexPage(HttpServletRequest request,
								  HttpServletResponse response, ModelMap model) {
		ModelAndView mv=new ModelAndView();
		FmUtils.FmData(request,model);
		sweepParam(request, mv);//获取扫一扫参数
		mv.setViewName(MobilePageContants.BUG_BOOK_PAGE);
		return mv;
	}

	private void sweepParam(HttpServletRequest request, ModelAndView mv) {
		String url=request.getRequestURL().toString();
		Map<String, String> res= Sign.getConfigMessageForWater(url);
		Config config=new Config();
		mv.addObject("appid",config.getString("appid"));
		mv.addObject("timestamp",res.get("timestamp"));
		mv.addObject("nonceStr",res.get("nonceStr"));
		mv.addObject("signature",res.get("signature"));
	}

	/**
	 * 根据扫一扫获取的条形码后再根据条形码获取图书信息
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/book/searchByIsbn.html", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> searchByIsbn(HttpServletRequest request,
								  HttpServletResponse response, ModelMap model,String isbn) {
		Map<String,Object> returnMap=new HashMap<String, Object>();
		TudouBookInfo bookInfo=null;
		try {
			 bookInfo=RetrieveDocumentByURL.getTuDouBookInfo(TU_DOU_BOOK_URI.replace("ISBN",isbn));
			returnMap.put("book",bookInfo);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return returnMap;
	}

	/**
	 * my send book
	 * @param request
	 * @param response
	 * @param model
     * @return
     */
	@RequestMapping(value = "/book/sendList.html", method = RequestMethod.GET)
	public ModelAndView sendList(HttpServletRequest request,
								  HttpServletResponse response, ModelMap model) {
		ModelAndView mv=new ModelAndView();
		FmUtils.FmData(request,model);
		mv.setViewName(MobilePageContants.MY_SEND_LIST_BOOK);
		return mv;
	}
	/**
	 * my sell book index
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/book/sellbook.html", method = RequestMethod.GET)
	public ModelAndView sellbook(HttpServletRequest request,
								 HttpServletResponse response, ModelMap model) {
		ModelAndView mv=new ModelAndView();
		FmUtils.FmData(request,model);
		System.out.println(System.getProperty("user.dir").replace("bin", "upload"));
		sweepParam(request, mv);//获取扫一扫参数
		mv.setViewName(MobilePageContants.MY_SELL_BOOK);
		return mv;
	}

	/**
	 * download image to local server computer
	 * @param request
	 * @param response
	 * @param model
     * @return
     */
	@RequestMapping(value = "/book/downimage.html", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> downloadImageToLocalServer(HttpServletRequest request,
								 HttpServletResponse response, ModelMap model,String media_id) {
		Map<String,Object> map=new HashMap<String, Object>();
		System.out.println("方法进来了嘛》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》media_id==="+media_id);
		try{
			//String localPath=System.getProperty("user.dir").replace("bin", "webapps/upload/");
			//通过media_id获取网络图片url
			Config config=new Config();
			String appId = config.getString("appid");
			String appSecret = config.getString("appsecret");
			String token = WeChatApiUtil.getToken(appId, appSecret);
			System.out.println("token==========================="+token);
			String requestUrl =DOWNLOAD_WEIXIN_IMAGE;
			requestUrl = requestUrl.replace("ACCESS_TOKEN", token).replace("MEDIA_ID",media_id);
			GetImage.downImageForNetUrl(requestUrl,media_id);
			map.put("success",true);
		}catch (Exception e){
			e.printStackTrace();
		}

		return map;
	}
	@Autowired
	protected ICoreService coreService;

}