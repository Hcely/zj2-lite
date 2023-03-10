import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import lombok.SneakyThrows;

/**
 * HttpHandler
 *
 * @author peijie.ye
 * @date 2023/3/5 23:06
 */
public class HttpHandler {
    @SneakyThrows
    public static void main(String[] args) {
        WebClient wc = new WebClient(BrowserVersion.CHROME);
        wc.getOptions().setJavaScriptEnabled(true);
        wc.getOptions().setCssEnabled(true);
        wc.getOptions().setThrowExceptionOnFailingStatusCode(false);
        wc.getOptions().setThrowExceptionOnScriptError(false); //js运行错误时，是否抛出异常
        wc.getOptions().setTimeout(10000); //设置连接超时时间 ，这里是10S。如果为0，则无限期等待
        wc.setAjaxController(new NicelyResynchronizingAjaxController());//设置支持AJAX
        wc.waitForBackgroundJavaScript(30000);
        CookieManager cookieManager = wc.getCookieManager();
        cookieManager.setCookiesEnabled(true);
        cookieManager.addCookie(new Cookie("m.che300.com", "_che300",
                "2pCpEkr6QcIrJdDqNQLyuhH6NZCldrUE5UT89egKMWNvGe2AJud7qIrY3l21VG46G2EHz%2BpAS1cNJop7rI9duW0oJEaSEyzbsPUbllW1kPfRpoLDWgShbs1eu6w6SNIVL7SO1Ataluz94SMF6oGEuHS9SXc84a2aovYLmdfuKNTng5L0rqAkyD%2Bp5EDrvkM0XmAa%2F3yNWss86TD3P7MK2LLJFFr9Ba87srVr5hfzYbXZXlupD2HRHL7RSnyvipS%2B1Ze%2BJv0IpoJFjofo9Umw9iP%2F6SIiozQBc6KasNOhhTrIFQFIp554gkd7%2F9anCQUi9Wx%2FAp2Wbw1C1y0rZ5u%2BrpkTth%2FiAztP%2BUJdtg3WyD2HNm0agxxC%2FSoeTI03o24S3CHCRopg2393e0z4TbXnoh%2B4NYoHJHpP%2F59wcqHwdAT3Nz%2Bcz9HcOrwbavv6RzStS2%2FgqwZBtgZ%2BV5eUcMZV0Wnf4SCEdGbaDNsnOBIggfh1TTJMOHhtmvLq%2BlK5hukPMxes11ut8pvvSvFhfot7afVviwmjd3SMkYdG2zt3xBLxAvzcvXtz0xfyt0cOfBWonrBVCopv1x2h9kSuP8YQNciEFTB1ykQ2WWGVG66%2Fs0jEANrJ%2FIhdYq8vZ%2BkXrDQIf6b19d3ffaf5f7e93784034d9b22f8cea9a2258f"));
        cookieManager.addCookie(new Cookie("m.che300.com", "spidercooskieXX12", "1678027175"));
        cookieManager.addCookie(new Cookie("m.che300.com", "city_name", "%E4%B8%89%E4%BA%9A"));
        cookieManager.addCookie(new Cookie("m.che300.com", "prov_id", "25"));
        wc.getCookieManager().addCookie(new Cookie("m.che300.com", "spidercodeCI12X3", "85314d0baf5a8c81c277d8f72556b1ce"));
        wc.getCookieManager().addCookie(new Cookie("m.che300.com", "PHPSESSID", "c01f6cb81c7a4f4d4aba945e69fac809cd134cee"));
        cookieManager.addCookie(new Cookie("m.che300.com", "prov_name", "%E6%B5%B7%E5%8D%97"));
        cookieManager.addCookie(new Cookie("m.che300.com", "city_id", "54"));
        cookieManager.addCookie(new Cookie(".che300.com", "device_id", "h555d7-2122-069e-66a1-9071"));
        cookieManager.addCookie(
                new Cookie(".che300.com", "zg_did", "%7B%22did%22%3A%20%22186ac06a7ff384-0db826f8d0f1d1-26021f51-144000-186ac06a800109e%22%7D"));
        cookieManager.addCookie(new Cookie(".che300.com", "pcim", "db37d873562e3c17846661ef0732a097b4d3d4db"));
        cookieManager.addCookie(new Cookie(".che300.com", "tel", "18826401135"));
        cookieManager.addCookie(new Cookie(".che300.com", "zg_db630a48aa614ee784df54cc5d0cdabb",
                "%7B%22did%22%3A%20%22186ac06a7ff384-0db826f8d0f1d1-26021f51-144000-186ac06a800109e%22%7D"));
        cookieManager.addCookie(new Cookie(".m.che300.com", "Hm_lpvt_12b6a0c74b9c210899f69b3429653ed6", "1678027177"));
        cookieManager.addCookie(new Cookie(".m.che300.com", "Hm_lvt_12b6a0c74b9c210899f69b3429653ed6", "1677923363"));
        HtmlPage page = wc.getPage("https://m.che300.com/estimate/result/3/3/2931/52541/1830830/2022-1/13/1/null/2023/2022?rt=1");
        if(page == null) {
            System.out.println("采集失败!!!");
        } else {
            Thread.sleep(1000);
            String content = page.asXml();
            System.out.println(content);
        }
    }
}
