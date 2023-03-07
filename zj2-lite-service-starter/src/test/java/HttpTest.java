import com.alibaba.fastjson.util.IOUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * HttpTest
 *
 * @author peijie.ye
 * @date 2023/3/5 22:42
 */
public class HttpTest {
    @SneakyThrows
    public static void main(String[] args) {
        RestTemplate template = new RestTemplate();
        ClientHttpRequest request = template.getRequestFactory().createRequest(
                new URI("https://m.che300.com/estimate/result/3/3/2931/52541/1830830/2022-1/13/1/null/2023/2022?rt=1"),
                HttpMethod.GET);
        request.getHeaders().add("authority", "m.che300.com");
        request.getHeaders().add("accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        request.getHeaders().add("accept-language", "zh-CN,zh;q=0.9");
        request.getHeaders().add("cookie",
                "Hm_lvt_12b6a0c74b9c210899f69b3429653ed6=1677923363; device_id=h555d7-2122-069e-66a1-9071; tel=18826401135; pcim=db37d873562e3c17846661ef0732a097b4d3d4db; PHPSESSID=c01f6cb81c7a4f4d4aba945e69fac809cd134cee; city_id=54; prov_id=25; city_name=%E4%B8%89%E4%BA%9A; prov_name=%E6%B5%B7%E5%8D%97; zg_did=%7B%22did%22%3A%20%22186ac06a7ff384-0db826f8d0f1d1-26021f51-144000-186ac06a800109e%22%7D; zg_db630a48aa614ee784df54cc5d0cdabb=%7B%22sid%22%3A%201678025394202%2C%22updated%22%3A%201678026304824%2C%22info%22%3A%201677923362819%2C%22superProperty%22%3A%20%22%7B%7D%22%2C%22platform%22%3A%20%22%7B%7D%22%2C%22utm%22%3A%20%22%7B%7D%22%2C%22referrerDomain%22%3A%20%22m.che300.com%22%2C%22zs%22%3A%200%2C%22sc%22%3A%200%2C%22firstScreen%22%3A%201678025394202%7D; _che300=TREGuv2cqG7IneEcNJoI%2FMMX%2FnuBvNyQBbdeto81hmRu1cxUKoG%2FhQw31FtXzK7AGncrqKQDaz8dnGTZRbly9pX%2By1NBG3JGwf9mrMjt6IG9N%2BKeYZ6gJlm8CXQM8uWSS8ki%2FE5W%2BRUWsTMmAkOLvb2ObippZJwe6we%2FcmNBLP9CRwyNsP6juJBXvmyJ1BY08Cq6kqXhH1Wf4XyqK3mlYyhRFdntTFq6J7xLkxT2U%2FQqk9hBfG6FAQ8tDbtOUZ90SFnW53%2FSR1AtnK8Mi%2BPmG7cOUBIEsQXRxon4KVs1RQHJJs%2FrwdFHGl5Ch1PPxzzZNUSWFhoDTsvKOpNCE8bgV8qOjpPCo3nmBr69V1aZ7NkXUgzgnm0DZyUwpRZllHfKwxzJf%2B1Z2udBgJXXZr1ATpVW3l0HKB6RRKGQJNdAMkMNFcqpRWm9QsvhzPO0VfhQ4pfhQ62RDFDNxteF5YEOxtwRSYCdx0OapPSohVuDGwISfvu%2BzQX%2FYdUZxFc0rSil442D5jml6CCNK1f7DIscDNLrAG5D9V89%2B%2FwUzxjmYrk6z5n6oNK0j3Hkz6K7Bk3ZchD%2FXstCzfg6S3k31adir3rKrWaUrV6bqNSkJKKh02KFCr3Bvmp%2FD6F1kqFsOuVTe5094bd878f2e71d6b6b00bc596c882fefe6380d; Hm_lpvt_12b6a0c74b9c210899f69b3429653ed6=1678026327; spidercooskieXX12=1678026613; spidercodeCI12X3=3eb59adeb97e73f9ec4b7c7f1eb18c6e");
        request.getHeaders().add("referer",
                "https://m.che300.com/estimate/result/3/3/2931/52541/1830830/2022-1/13/1/null/2023/2022?rt=1");
        request.getHeaders()
                .add("sec-ch-ua", "\"Chromium\";v=\"106\", \"Google Chrome\";v=\"106\", \"Not;A=Brand\";v=\"99\"");
        request.getHeaders().add("sec-ch-ua-mobile", "?0");
        request.getHeaders().add("sec-ch-ua-platform", "Windows");
        request.getHeaders().add("sec-ch-ua-dest", "document");
        request.getHeaders().add("sec-ch-ua-mode", "navigate ");
        request.getHeaders().add("sec-ch-ua-site", "same-origin");
        request.getHeaders().add("upgrade-insecure-requests", "1");
        request.getHeaders().add("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36");
        ClientHttpResponse response = request.execute();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = response.getBody();
        byte[] b = new byte[256];
        int i;
        while ((i = input.read(b)) != -1) {
            output.write(b, 0, i);
        }
        System.out.println(output.toString(StandardCharsets.UTF_8));

    }
}
