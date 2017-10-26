import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


public class test {
	
	private final String https_url = "http://182.150.61.146/lywx";
	private final String transType = "103";//交易代码
	private final String tradingPartner = "xxxx";//业务合作伙伴代码，可使用太保之前给分配的商户代码进行测试
	private final String documentProtocol = "CPIC_ECOM";//业务协议，固定值
	

	public void testHttp() throws Exception {
		postMsg();
	}
	
	//使用https方式调用
    private void postMsg() throws Exception {
    	long startTime = System.currentTimeMillis();
        HttpClient httpclient = new DefaultHttpClient();
        registerSSLSocketFactory(httpclient);
        HttpPost httppost = new HttpPost(https_url);
        //加载请求报文
        URL url = Thread.currentThread().getContextClassLoader().getResource(transType + "_Request.xml");
        String requestMessage = IOUtils.toString(url.openStream());
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("requestMessage", requestMessage));
        params.add(new BasicNameValuePair("transType", transType));
        params.add(new BasicNameValuePair("tradingPartner", tradingPartner));
        params.add(new BasicNameValuePair("documentProtocol", documentProtocol));
        HttpEntity request = new UrlEncodedFormEntity(params, "utf-8");
        httppost.setEntity(request);
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity);
            System.out.println(result);
        }
        long processTime =  System.currentTimeMillis() - startTime;
        System.out.println("process time :" + processTime);
        if (processTime > 60000)
        	System.out.println("timeout");
    }
    
    //加载证书，此方法是本次切换重点
    private void registerSSLSocketFactory(HttpClient httpclient) throws Exception {
		KeyStore trustStore  = KeyStore.getInstance(KeyStore.getDefaultType());   
		// 注意：请将数字证书的路径（D:\\cpic_jttp.keystore）换成您保存本文件的具体路径
        FileInputStream instream = new FileInputStream(new File("D:\\cpic_jttp.keystore")); 
        try {
            trustStore.load(instream, "cpicJttp".toCharArray());//生成keystore文件时使用的密码
        } finally {
            instream.close();
        }
        SSLSocketFactory socketFactory = new SSLSocketFactory(trustStore);
        Scheme sch = new Scheme("https", socketFactory, 443);
        httpclient.getConnectionManager().getSchemeRegistry().register(sch);
	}

}
