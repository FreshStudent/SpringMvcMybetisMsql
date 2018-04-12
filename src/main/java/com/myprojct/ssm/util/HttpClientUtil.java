package com.myprojct.ssm.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.axis.encoding.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 * http 请求
 * 
 * @author Administrator
 *
 */
public class HttpClientUtil {
	
	private static Logger log = Logger.getLogger(HttpClientUtil.class);
	
	private static String DEFAULT_CHARSET = "GBK";
	
	/**
	 * 连接超时时间，由bean factory设置，缺省为8秒钟 
	 **/
	private static int                        defaultConnectionTimeout            = 60000;

	/** 回应超时时间, 由bean factory设置，缺省为30秒钟 */
	private static int                        defaultSoTimeout                    = 60000;

	/** 闲置连接超时时间, 由bean factory设置，缺省为60秒钟 */
    private static HttpConnectionManager connectionManager = ConnectionManagerPool.getInstance().connectionManager;
    
    private static HttpClientUtil httpProtocolHandler = new HttpClientUtil();
    
    private static SSLConnectionSocketFactory sslSocketFactory;
    
    static{
		try{
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, new TrustManager[] { new HttpsX509TrustManager() },new java.security.SecureRandom());
			sslSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
		}catch (Exception e) {
			log.error("init poolingConnectionManager failed.", e);
		}
    }

    /**
     * 工厂方法
     * 
     * @return
     */
    public static HttpClientUtil getInstance() {
        return httpProtocolHandler;
    }
   
    private HttpClientUtil(){
   }

	/*
	 * @reqUrl 请求的url @dateType 数据类型 json,xml
	 */
	public String getResponseMess(String reqUrl, String dateType) {
	   long startTime = System.currentTimeMillis();
		String resMess = "";
		HttpClient httpClient = null;
		GetMethod getMethod = null;
		try {
			httpClient = new HttpClient(connectionManager);
			
			getMethod = new GetMethod(reqUrl);
			getMethod.getParams().setParameter(HttpMethodParams.USER_AGENT, "FZSservice 1.0");

			log.info("request:" + getMethod.getStatusLine());
			 getMethod.getParams().setContentCharset(DEFAULT_CHARSET);
//			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(defaultConnectionTimeout);
//			httpClient.getHttpConnectionManager().getParams().setSoTimeout(defaultSoTimeout);
			 httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(defaultConnectionTimeout);
			httpClient.getHttpConnectionManager().getParams().setSoTimeout(defaultSoTimeout);
			if (dateType != null && "json".equals(dateType)) {
				getMethod.setRequestHeader("Accept", "application/json");
			}
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode == HttpStatus.SC_OK) {
				// 读取内容
				resMess = getMethod.getResponseBodyAsString();
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error(" httpClient error:" + e.getMessage());

		} finally {
			log.info("http连接 耗费时间："+(System.currentTimeMillis()-startTime)+"ms");
			if (getMethod != null) {
				getMethod.releaseConnection();
			}
		
		}

		return resMess;
	}
	/**
	 * param以流的方式写入到请求中
	 * @param requestUrl
	 * @param method
	 * @param param
	 * @return
	 */
	public String httpRequest(String requestUrl, String method, String param) {
		StringBuffer temp;
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

			urlConnection.setRequestMethod(method);
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.setUseCaches(false);
			OutputStream outputStream = urlConnection.getOutputStream();
			outputStream.write(param.getBytes("utf-8"));
			outputStream.flush();
			InputStream in = urlConnection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "utf-8"));
			temp = new StringBuffer();
			String line = bufferedReader.readLine();
			while (line != null) {
				temp.append(line).append("\r\n");
				line = bufferedReader.readLine();
			}
			bufferedReader.close();
			int code = urlConnection.getResponseCode();
			if (code != 200) {
				throw new RuntimeException("服务器错误：" + code);
			}
		} catch (Exception e) {
			throw new RuntimeException("服务器错误：" + e.getMessage());
		}
		return temp.toString();
	}
	
	/**
	 * 
	 * @param url
	 * @return
	 */
	public static String doGet(String url) {
		return doGet(url, null);
	}
	
	/**
	 * 
	 * @param url
	 * @param headers
	 * @return
	 */
	public static String doGet(String url, Map <String, String> headers) {
		
		long startTime = System.currentTimeMillis();

		CloseableHttpClient client = null;
		HttpGet httpGet = null;
		CloseableHttpResponse response = null;
		
		try {
			
			String charset = getCharset(headers, "utf-8");
			
			client = createHttpClient();
			httpGet = createHttpMethod(new HttpGet(url), headers);
			
			return execute(client, httpGet, headers, url, charset);

		} catch (Exception e) {
			log.warn(url + " request error: " + e.getMessage(), e);
		} finally {
			log.info( "execute " + url + " used time: " + (System.currentTimeMillis() - startTime) + "ms");
			if(response != null){
	            EntityUtils.consumeQuietly(response.getEntity());
			}
			if (httpGet != null) {
				httpGet.releaseConnection();
			}
		}

		return null;
		
	}

	private static CloseableHttpClient createHttpClient() {
		PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager(RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.INSTANCE).register("https", sslSocketFactory).build());
		return HttpClients.custom().setConnectionManager(poolingConnectionManager).build();
	}
	
	/**
	 * 
	 * @param url
	 * @param param
	 * @return
	 */
	public static String doPost(String url, String param) {
		return doPost(url, param, (Map<String, String>) null);
	}
	
	/**
	 * 
	 * @param url
	 * @param params
	 * @param contentType
	 * @return
	 */
	public static String doPost(String url, String params, String contentType) {
		return doPost(url, params, contentType, null) ;
	}
	
	/**
	 * 
	 * @param url
	 * @param params
	 * @param requestProperties
	 * @return
	 */
	public static String doPost(String url, String params, Map <String, String> requestProperties) {
		return doPost(url, params, "application/x-www-form-urlencoded", requestProperties) ;
	}
	
	/**
	 * 
	 * @param url
	 * @param params
	 * @param requestProperties
	 * @return
	 */
	public static String doPostByStream(String url, String params, Map <String, String> requestProperties) {
		return doPost(url, params, "*/*", requestProperties) ;
	}
	
	/**
	 * 
	 * @param url
	 * @param params
	 * @param headers
	 * @return
	 */
	public static String doPost(String url, String params, String contentType, Map <String, String> headers) {
		
		long startTime = System.currentTimeMillis();
		
		CloseableHttpClient client = null;
		HttpPost httpPost = null;
		
		try {
			
			String charset = getCharset(headers, "utf-8");
				
			client = createHttpClient();
			httpPost = createHttpMethod(new HttpPost(url), headers);
			
			if(StringUtils.isNotEmpty(params)){
				httpPost.setEntity(new StringEntity(params, ContentType.create(contentType, charset)));
			}
			
			return execute(client, httpPost, headers, url, charset);
			
		} catch (Exception e) {
			log.warn(url + " request error: " + e.getMessage(), e);
		} finally {
			log.info( "execute " + url + " used time: " + (System.currentTimeMillis() - startTime) + "ms");
			if (httpPost != null) {
				httpPost.releaseConnection();
			}
		}
		
		return null;
	}

	private static String execute(CloseableHttpClient client, HttpRequestBase httpMethod, Map <String, String> headers, String url, String charset) throws IOException, ClientProtocolException {
		
		CloseableHttpResponse response = null;
		
		boolean returnHttpStatus = getParameter(headers, "Http-Status", false);
		boolean acceptErrorResult = getParameter(headers, "Read-Error", false);
	
		response = client.execute(httpMethod);
		
		int statusCode = response.getStatusLine().getStatusCode();
		String responseText = EntityUtils.toString(response.getEntity(), charset);
		
		if (statusCode == HttpStatus.SC_OK || acceptErrorResult) {
			return responseText;
		}else{
			log.warn(url + " request failed: " + responseText);
			if(returnHttpStatus){
				if(StringUtils.isNotEmpty(responseText) && responseText.length() > 256){
					responseText = responseText.substring(0, 256);
				}
				return "httpStatus=" + statusCode + "&httpMessage=" + StringUtils.defaultString(responseText);
			}
		}
			
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private static <T> T getParameter(Map<String, String> headers, String propertyName, T defaultValue) {
		
		if(headers == null || headers.size() == 0){
			return (T) defaultValue;
		}
		
		String value = headers.get(propertyName);
		
		if(StringUtils.isNotBlank(value)){
			Class<T> clazz = (Class<T>) defaultValue.getClass();
			if(clazz == Integer.class){
				return (T) Integer.valueOf(value);
			}
			if(clazz == Boolean.class){
				return (T) Boolean.valueOf("true".equals(value));
			}
			return (T) value;
		}
		
		return defaultValue;
		
	}

	private static String getCharset(Map<String, String> headers, String defaultValue) {
		if(headers == null || headers.size() == 0){
			return defaultValue;
		}
		String ct = headers.get("Content-Type");
		if(StringUtils.isNotEmpty(ct)){
			int a = ct.lastIndexOf("charset");
			if(a > -1){
				return ct.substring(a+8);
			}
		}
		return defaultValue;
	}
	
	/**
	 * 
	 * @param requestUrl
	 * @param params
	 * @param headers
	 * @return
	 */
	public static String doPostByMimePart(String requestUrl, Map <String, String> params, Map <String, String> headers) {
		
		long startTime = System.currentTimeMillis();
		
		HttpPost httppost = null;
		CloseableHttpResponse response = null;
		
		try {
			
			CloseableHttpClient httpclient = HttpClients.createDefault();
			
			String partContentType = getParameter(headers, "Part-Content-Type", "application/xml");
			int connectTimeout = getParameter(headers, "Connection-Timeout", defaultConnectionTimeout);
			String charset = getCharset(headers, "UTF-8");
			
			httppost = new HttpPost(requestUrl);
			httppost.setConfig(RequestConfig.custom().setConnectionRequestTimeout(connectTimeout).setConnectTimeout(connectTimeout).setSocketTimeout(connectTimeout).build());
			
			if(headers != null && headers.size() > 0){
				for(Map.Entry <String, String> entry : headers.entrySet()){
					httppost.addHeader(entry.getKey(), entry.getValue());
				}
			}
			
			MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
			
			if(params != null && params.size() > 0){
				for(Map.Entry <String, String> entry : params.entrySet()){
					entityBuilder.addPart(entry.getKey(), new StringBody(entry.getValue(), ContentType.create(partContentType, charset)));
				}
			}
			
			httppost.setEntity(entityBuilder.build());
			
			response = httpclient.execute(httppost);
			
			int statusCode = response.getStatusLine().getStatusCode();
			
			if (statusCode == HttpStatus.SC_OK) {
				return EntityUtils.toString(response.getEntity(), charset);
			}else{
				log.warn(requestUrl + " request failed: " + EntityUtils.toString(response.getEntity(), charset));
			}
			
		} catch (Exception e) {
			log.warn(requestUrl + " request failed: " + e.getMessage(), e);
		} finally {
			log.info( "execute " + requestUrl + " used time: " + (System.currentTimeMillis() - startTime) + "ms");
			if(httppost != null){
				httppost.releaseConnection();
			}
			if(response != null){
	            EntityUtils.consumeQuietly(response.getEntity());
			}
		}
		
		return null;
	}

	/**
	 * @reqUrl 请求的url @dateType 数据类型 json,xml
	 */
	public String postResponseMess(String reqUrl, String dateType, Map<String, String> postParam) {
		String resMess = "";
		 long startTime = System.currentTimeMillis();
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		try {
			httpClient = new HttpClient(connectionManager);
			postMethod = new PostMethod(reqUrl);
			postMethod.getParams().setParameter(HttpMethodParams.USER_AGENT, "FZSservice 1.0");
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(defaultConnectionTimeout);
			httpClient.getHttpConnectionManager().getParams().setSoTimeout(defaultSoTimeout);
			if (dateType != null && "json".equals(dateType)) {
				postMethod.setRequestHeader("Accept", "application/json");

			}

			if (postParam != null && !postParam.isEmpty()) {
				Set<String> keySet = postParam.keySet();
				for (String keyName : keySet) {
					String keyValue = postParam.get(keyName);
					postMethod.addParameter(keyName, keyValue);
				}
			}
			// 设置请求参数为utf-8字符集
			postMethod.getParams().setContentCharset("UTF-8");
			System.out.println(Arrays.toString(postMethod.getParameters()));
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				// 读取内容
				resMess = postMethod.getResponseBodyAsString();
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			log.info("http连接 耗费时间："+(System.currentTimeMillis()-startTime)+"ms");
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}

		return resMess;
	}

	public void post(String reqUrl, String dateType, Map<String, String> postParam) {
		String resMess = "";
		 long startTime = System.currentTimeMillis();
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		try {
			httpClient = new HttpClient(connectionManager);
			postMethod = new PostMethod(reqUrl);
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(defaultConnectionTimeout);
			httpClient.getHttpConnectionManager().getParams().setSoTimeout(defaultSoTimeout);
			postMethod.getParams().setParameter(HttpMethodParams.USER_AGENT, "FZSservice 1.0");
			
			if (dateType != null && "json".equals(dateType)) {
				postMethod.setRequestHeader("Accept", "application/json");

			}
			if (postParam != null && !postParam.isEmpty()) {
				Set<String> keySet = postParam.keySet();
				for (String keyName : keySet) {
					String keyValue = postParam.get(keyName);
					postMethod.addParameter(keyName, keyValue);
				}
			}
			// 设置请求参数为utf-8字符集
			postMethod.getParams().setContentCharset("UTF-8");
			System.out.println(Arrays.toString(postMethod.getParameters()));
			System.out.println("uri is:" + postMethod.getURI());
			int statusCode = httpClient.executeMethod(postMethod);

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			log.info("http连接 耗费时间："+(System.currentTimeMillis()-startTime)+"ms");
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}

	}

	/*
	 * @reqUrl 请求的url @dateType 数据类型 json,xml @postParam post参数 @charset 字符集
	 */
	public String postResponseMess(String reqUrl, String dateType, Map<String, String> postParam, String charset) {
		String resMess = "";
		 long startTime = System.currentTimeMillis();
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		try {
			httpClient = new HttpClient(connectionManager);
			postMethod = new PostMethod(reqUrl);
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(defaultConnectionTimeout);
			httpClient.getHttpConnectionManager().getParams().setSoTimeout(defaultSoTimeout);
			postMethod.getParams().setParameter(HttpMethodParams.USER_AGENT, "FZSservice 1.0");
			if (dateType != null && "json".equals(dateType)) {
				postMethod.setRequestHeader("Accept", "application/json");

			}

			if (postParam != null && !postParam.isEmpty()) {
				Set<String> keySet = postParam.keySet();
				for (String keyName : keySet) {
					String keyValue = postParam.get(keyName);
					postMethod.addParameter(keyName, keyValue);
				}
			}
			if (charset != null && !"".equals(charset)) {
				postMethod.getParams().setContentCharset(charset);
			}

			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				// 读取内容
				resMess = postMethod.getResponseBodyAsString();
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			log.info("http连接 耗费时间："+(System.currentTimeMillis()-startTime)+"ms");
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}

		return resMess;
	}
	
	
	

	public String postXML(String reqUrl, String data,Map<String,String> requestHeader) {
		String resMess = "";
		 long startTime = System.currentTimeMillis();
		HttpClient httpClient = null;
		PostMethod postMethod = getPostMethod(requestHeader,reqUrl);
		try {
			httpClient = new HttpClient(connectionManager);
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(defaultConnectionTimeout);
			httpClient.getHttpConnectionManager().getParams().setSoTimeout(defaultSoTimeout);
			postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			postMethod.getParams().setParameter(HttpMethodParams.USER_AGENT, "FZSservice 1.0");
			String encodeStr=data;
			if(Util.isNull(requestHeader) || requestHeader.size()<=0){
				if(Util.isNotEmpty(data)){
					encodeStr=URLEncoder.encode(data,"ISO-8859-1");
					postMethod.setRequestHeader("Content-type", "text/xml; charset=utf-8");
				}
			}else{
				if(Util.isNotEmpty(data))
				encodeStr=URLEncoder.encode(data,"utf-8");
			}
			postMethod.setRequestBody(data);
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				// 读取内容
				resMess = postMethod.getResponseBodyAsString();
			}else{
				System.out.println(postMethod.getResponseBodyAsString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			log.info("http连接 耗费时间："+(System.currentTimeMillis()-startTime)+"ms");
			if (postMethod != null) {
				
				postMethod.releaseConnection();
			}
		}
		return resMess;
	}
	
	public String postStatus(String reqUrl, String data,Map<String,String> requestHeader) {
		String resMess = "falied";
		 long startTime = System.currentTimeMillis();
		HttpClient httpClient = null;
		PostMethod postMethod = getPostMethod(requestHeader,reqUrl);
		try {
			httpClient = new HttpClient(connectionManager);
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(defaultConnectionTimeout);
			httpClient.getHttpConnectionManager().getParams().setSoTimeout(defaultSoTimeout);
			postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
			postMethod.getParams().setParameter(HttpMethodParams.USER_AGENT, "FZSservice 1.0");
			String encodeStr=data;
			if(Util.isNull(requestHeader) || requestHeader.size()<=0){
				if(Util.isNotEmpty(data)){
					encodeStr=URLEncoder.encode(data,"ISO-8859-1");
					postMethod.setRequestHeader("Content-type", "text/xml; charset=utf-8");
				}
			}else{
				if(Util.isNotEmpty(data))
				encodeStr=URLEncoder.encode(data,"utf-8");
			}
			postMethod.setRequestBody(data);
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				// 读取内容
				resMess = "success";
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			log.info("http连接 耗费时间："+(System.currentTimeMillis()-startTime)+"ms");
			if (postMethod != null) {
				
				postMethod.releaseConnection();
			}
		}
		return resMess;
	}
	

	public static void main(String[] args) throws Exception {

		HttpClientUtil http = HttpClientUtil.getInstance();
		String txnDate="20151009102012";
		String sid="8a48b5515018a0f401504bbf16d95c2d";
		String token="5d69abfc6c754624ad3abb4c6fd9a84c";
		String sign=DigestUtils.md5Hex(sid+token+txnDate);
		String reqUrl="https://112.124.97.198:8883/2013-12-26/Accounts/8a48b5515018a0f401504bbf16d95c2d/flowPackage/flowPackage?sig="+sign.toUpperCase();
		String data="{\"phoneNum\":\"15015589858\"}";
//		data="{\"appId\":\"aaf98f8950189e9b01504bc0e06b2037\",\"phoneNum\":\"13609611464\",\"sn\":\"100001\",\"packet\":\"10\",\"reason\":\"test\",\"customId\":\"695136f5028d11e5a1610050568e55bd\",\"callbackUrl\":\"http://183.237.4.11:8097/fzsFlow/access/callback/synCard/ejoinedcallback\"}";
		System.out.println(data);
		String authorization=sid+":"+txnDate;
		System.out.println(http.postTestQueryXML(http,reqUrl, data,authorization));
//		System.out.println(http.getResponseMess("http://www.baidu.com/",null));
		
//		String s=sid+"\"\""+txnDate;
//		System.out.println(s);
//		Base64 base64 = new Base64();
//		byte[] textByte = s.getBytes("UTF-8");
//		String encodedText = base64.encode(textByte);
//		System.out.println(encodedText);
		// http://www.phone580.com:8082/fbsauth/api/auth/login?userName=gdlt&password=12345678
		// http://10.20.1.47:8082/fbs/api/user/login?userName=heyi&password=heyi0419
//
//		String json = h.getResponseMess(
//				"http://www.phone580.com:8082/fbsauth/api/auth/login?userName=gdlt&password=12345678", "json");
//		System.out.println(json);
//		Map map = JsonUtil.getMapFromJson(json);
//		Boolean succes = (Boolean) map.get("success");
//		if (succes) {
//			System.out.println("为真");
//		}

	}
	public String postTestQueryXML(HttpClientUtil client,String reqUrl, String data,String authorization) {
		String resMess = "";
		 long startTime = System.currentTimeMillis();
		HttpClient httpClient = client.getHttpClient();
		PostMethod postMethod = null;
		Base64 base64 = new Base64();
		try {
			byte[] textByte = authorization.getBytes("UTF-8");
			String encodedText = base64.encode(textByte);
			postMethod = new PostMethod(reqUrl);
			InputStream in=new ByteArrayInputStream(data.getBytes("utf-8"));
			BufferedInputStream bf=new BufferedInputStream(in);
			postMethod.setRequestBody(bf);
			postMethod.setRequestHeader("Content-type", "application/json;charset=utf-8;");
			postMethod.setRequestHeader("Accept", "application/json;");
			postMethod.setRequestHeader("Content-Length", String.valueOf(data.length()));
			postMethod.setRequestHeader("Authorization", encodedText);
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				// 读取内容
				resMess = postMethod.getResponseBodyAsString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			log.info("http连接 耗费时间："+(System.currentTimeMillis()-startTime)+"ms");
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return resMess;
	}
	
	public HttpClient getHttpClient(){
		HttpClient httpClient = new HttpClient(connectionManager);
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(defaultConnectionTimeout);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(defaultSoTimeout);
		return httpClient;
	}
	
	public PostMethod getPostMethod(Map<String,String> map,String url){
		PostMethod	postMethod = new PostMethod(url);
		if(Util.isNotNull(map) && map.size()>0){
			Set<Entry<String, String>> entrySet = map.entrySet();
			Iterator<Entry<String, String>> iterator = entrySet.iterator();
			while(iterator.hasNext()){
				Entry<String, String> next = iterator.next();
				String key = next.getKey();
				String value = next.getValue();
				postMethod.setRequestHeader(key,value);
			}
		}
		return postMethod;
	}
	
	private static <T extends HttpRequestBase> T createHttpMethod(T method, Map<String,String> headers){
		
		int connectTimeout = getParameter(headers, "Connection-Timeout", defaultConnectionTimeout);
		method.setConfig(RequestConfig.custom().setConnectionRequestTimeout(connectTimeout).setConnectTimeout(connectTimeout).setSocketTimeout(connectTimeout).build());
		
		if(headers == null || headers.size() == 0){
			return method;
		}
		
		Set<Entry<String, String>> entrySet = headers.entrySet();
		Iterator<Entry<String, String>> iterator = entrySet.iterator();
		
		while(iterator.hasNext()){
			Entry<String, String> next = iterator.next();
			String key = next.getKey();
			if("Http-Status".equals(key) || "Read-Error".equals(key) || "Connection-Timeout".equals(key)){
				continue;
			}
			String value = next.getValue();
			method.addHeader(key,value);
		}
		
		return (T) method;
	}
	
	private static class HttpsX509TrustManager implements X509TrustManager{
		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}
		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}
	
}
