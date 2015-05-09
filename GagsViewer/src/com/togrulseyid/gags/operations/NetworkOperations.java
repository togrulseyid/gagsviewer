package com.togrulseyid.gags.operations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.util.Log;

import com.togrulseyid.gags.constants.BusinessConstants;
import com.togrulseyid.gags.constants.UrlConstants;
import com.togrulseyid.gags.enums.LogsEnum;
import com.togrulseyid.gags.enums.MessagesEnum;
import com.togrulseyid.gags.models.CoreModel;
import com.togrulseyid.gags.models.DataModelArrayList;
import com.togrulseyid.gags.models.PostDataModel;
import com.togrulseyid.gags.models.PostModelArrayList;

public class NetworkOperations {

	private Context context;

	public NetworkOperations(Context context) {

		this.context = context;
	}


	public CoreModel checkAppVersion(CoreModel model) {

		model = (CoreModel) SPProvider.initializeObject(model, context);

		if (!Utility.checkNetwork(context)) {
			model.setMessageId(MessagesEnum.NO_NETWORK_CONNECTION.getCode());
		} else if (!Utility.checkInternetConnection()) {
			model.setMessageId(MessagesEnum.NO_INTERNET_CONNECTION.getCode());
		} else {
			try {

				ObjectConvertor<CoreModel> objectConvertorModel = new ObjectConvertor<CoreModel>();

				String result = postAndResponseString(
						objectConvertorModel.getClassString(model),
						convertHttps(Utility.decrypt(
								UrlConstants.URL_CHECK_APP_VERSION,
								Utility.getAppSignature(context))), 4000, 4000);

				model = objectConvertorModel.getClassObject(result,
						CoreModel.class);

				return model;

			} catch (ClientProtocolException ex) {
				model.setMessageId(MessagesEnum.EXCEPTION_ERROR.getCode());
			} catch (IOException ex) {
				model.setMessageId(MessagesEnum.EXCEPTION_ERROR.getCode());
			}
		}

		return model;
	}

	private String postAndResponseString(String convertedModel, String url,
			int connectionTimeout, int businessDataTimeout)
			throws ClientProtocolException, IOException {

		Log.d(LogsEnum.INPUT.getLogName(), convertedModel);

		HttpPost httpPost = new HttpPost(url);

		StringEntity entity = new StringEntity(convertedModel, HTTP.UTF_8);
		httpPost.setEntity(entity);
		HttpClient httpClient = getClient(connectionTimeout,
				businessDataTimeout);
		HttpResponse response = httpClient.execute(httpPost);

		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent()));
		StringBuilder result = new StringBuilder();

		String line = null;

		while ((line = bufferedReader.readLine()) != null) {
			result.append(line);
		}

		Log.d(LogsEnum.OUTPUT.getLogName(), result.toString());

		return result.toString();
	}

	private HttpClient getClient(int connectionTimeOut, int businessDataTimeout) {

		HttpClient httpClient = null;
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				connectionTimeOut);
		HttpConnectionParams.setSoTimeout(httpParameters, businessDataTimeout);

		httpClient = new DefaultHttpClient(httpParameters);

		return httpClient;
	}

	private String convertHttps(String url) {
		return url;
	}


	/*
	 * get PostModelArrayList from all categories Or Filter Data By Category id
	 * @param PostDataModel
	 */
//	public PostModelArrayList getAllPostModelList(PostDataModel coreModel) {
//
//		coreModel = (PostDataModel) SPProvider.initializeObject(coreModel,
//				context);
//
//		PostModelArrayList models = new PostModelArrayList();
//
//		if (!Utility.checkNetwork(context)) {
//			models.setMessageId(MessagesEnum.NO_NETWORK_CONNECTION.getCode());
//		} else if (!Utility.checkInternetConnection()) {
//			models.setMessageId(MessagesEnum.NO_INTERNET_CONNECTION.getCode());
//		} else {
//			try {
//
//				ObjectConvertor<CoreModel> objectConvertorModel = new ObjectConvertor<CoreModel>();
//
//				String result = postAndResponseString(
//						objectConvertorModel.getClassString(coreModel),
//						Utility.decrypt(
//								UrlConstants.URL_GET_MOBCHANNEL_ALL_POST,
//								Utility.getAppSignature(context)),
//						BusinessConstants.CONNECTION_TIMEOUT,
//						BusinessConstants.BUSINESS_DATA_TIMEOUT);
//
//				ObjectConvertor<PostModelArrayList> objectConvertor = new ObjectConvertor<PostModelArrayList>();
//				models = objectConvertor.getClassObject(result,
//						PostModelArrayList.class);
//
//			} catch (ClientProtocolException ex) {
//				models.setMessageId(MessagesEnum.UN_SUCCESSFUL.getCode());
//			} catch (IOException ex) {
//				models.setMessageId(MessagesEnum.UN_SUCCESSFUL.getCode());
//			}
//		}
//
//		return models;
//	}

	/*
	 * Filter PostModelArrayList Data By Category id
	 * @param PostDataModel
	 */
//	public PostModelArrayList getPostsModelListById(PostDataModel coreModel) {
//
//		coreModel = (PostDataModel) SPProvider.initializeObject(coreModel,
//				context);
//
//		PostModelArrayList models = new PostModelArrayList();
//
//		if (!Utility.checkNetwork(context)) {
//			models.setMessageId(MessagesEnum.NO_NETWORK_CONNECTION.getCode());
//		} else if (!Utility.checkInternetConnection()) {
//			models.setMessageId(MessagesEnum.NO_INTERNET_CONNECTION.getCode());
//		} else {
//			try {
//
//				ObjectConvertor<CoreModel> objectConvertorModel = new ObjectConvertor<CoreModel>();
//
//				String result = postAndResponseString(
//						objectConvertorModel.getClassString(coreModel),
//						Utility.decrypt(
//								UrlConstants.URL_GET_MOBCHANNEL_FILTER_POST,
//								Utility.getAppSignature(context)),
//						BusinessConstants.CONNECTION_TIMEOUT,
//						BusinessConstants.BUSINESS_DATA_TIMEOUT);
//
//				ObjectConvertor<PostModelArrayList> objectConvertor = new ObjectConvertor<PostModelArrayList>();
//				models = objectConvertor.getClassObject(result,
//						PostModelArrayList.class);
//
//			} catch (ClientProtocolException ex) {
//				models.setMessageId(MessagesEnum.UN_SUCCESSFUL.getCode());
//			} catch (IOException ex) {
//				models.setMessageId(MessagesEnum.UN_SUCCESSFUL.getCode());
//			}
//		}
//
//		return models;
//	}


	public DataModelArrayList getImageList(CoreModel coreModel) {
		coreModel = (CoreModel) SPProvider.initializeObject(coreModel, context);
		
		DataModelArrayList models = new DataModelArrayList();

		if (!Utility.checkNetwork(context)) {
			models.setMessageId(MessagesEnum.NO_NETWORK_CONNECTION.getCode());
		} else if (!Utility.checkInternetConnection()) {
			models.setMessageId(MessagesEnum.NO_INTERNET_CONNECTION.getCode());
		} else {
			try {

				ObjectConvertor<CoreModel> objectConvertorModel = new ObjectConvertor<CoreModel>();

				String result = postAndResponseString(
						objectConvertorModel.getClassString(coreModel),
						Utility.decrypt(UrlConstants.URL_IMAGE_LIST,
								Utility.getAppSignature(context)),
						BusinessConstants.CONNECTION_TIMEOUT,
						BusinessConstants.BUSINESS_DATA_TIMEOUT);

				ObjectConvertor<DataModelArrayList> objectConvertor = new ObjectConvertor<DataModelArrayList>();
				models = objectConvertor.getClassObject(result,
						DataModelArrayList.class);


			} catch (ClientProtocolException ex) {
				models.setMessageId(MessagesEnum.UN_SUCCESSFUL.getCode());
			} catch (IOException ex) {
				models.setMessageId(MessagesEnum.UN_SUCCESSFUL.getCode());
			} catch (Exception e) {
				models.setMessageId(MessagesEnum.UN_SUCCESSFUL.getCode());
			}
		}

		return models;
	
	}


	public DataModelArrayList getAnimationList(CoreModel coreModel) {
		coreModel = (CoreModel) SPProvider.initializeObject(coreModel, context);
		
		DataModelArrayList models = new DataModelArrayList();

		if (!Utility.checkNetwork(context)) {
			models.setMessageId(MessagesEnum.NO_NETWORK_CONNECTION.getCode());
		} else if (!Utility.checkInternetConnection()) {
			models.setMessageId(MessagesEnum.NO_INTERNET_CONNECTION.getCode());
		} else {
			try {

				ObjectConvertor<CoreModel> objectConvertorModel = new ObjectConvertor<CoreModel>();

				String result = postAndResponseString(
						objectConvertorModel.getClassString(coreModel),
						Utility.decrypt(UrlConstants.URL_ANIMATION_LIST,
								Utility.getAppSignature(context)),
						BusinessConstants.CONNECTION_TIMEOUT,
						BusinessConstants.BUSINESS_DATA_TIMEOUT);
				
				ObjectConvertor<DataModelArrayList> objectConvertor = new ObjectConvertor<DataModelArrayList>();
				models = objectConvertor.getClassObject(result,
						DataModelArrayList.class);


			} catch (ClientProtocolException ex) {
				models.setMessageId(MessagesEnum.UN_SUCCESSFUL.getCode());
			} catch (IOException ex) {
				models.setMessageId(MessagesEnum.UN_SUCCESSFUL.getCode());
			} catch (Exception e) {
				models.setMessageId(MessagesEnum.UN_SUCCESSFUL.getCode());
			}
		}

		return models;
	
	}

	public DataModelArrayList getVideoList(CoreModel coreModel) {
		
		coreModel = (CoreModel) SPProvider.initializeObject(coreModel, context);
		
		DataModelArrayList models = new DataModelArrayList();

		if (!Utility.checkNetwork(context)) {
			
			models.setMessageId(MessagesEnum.NO_NETWORK_CONNECTION.getCode());
			
		} else if (!Utility.checkInternetConnection()) {
			
			models.setMessageId(MessagesEnum.NO_INTERNET_CONNECTION.getCode());
			
		} else {
			
			try {

				ObjectConvertor<CoreModel> objectConvertorModel = new ObjectConvertor<CoreModel>();

				String result = postAndResponseString(
						objectConvertorModel.getClassString(coreModel),
						Utility.decrypt(UrlConstants.URL_VIDEO_LIST,
								Utility.getAppSignature(context)),
						BusinessConstants.CONNECTION_TIMEOUT,
						BusinessConstants.BUSINESS_DATA_TIMEOUT);
				
				ObjectConvertor<DataModelArrayList> objectConvertor = new ObjectConvertor<DataModelArrayList>();
				models = objectConvertor.getClassObject(result,
						DataModelArrayList.class);


			} catch (ClientProtocolException ex) {
				
				models.setMessageId(MessagesEnum.UN_SUCCESSFUL.getCode());
				
			} catch (IOException ex) {
				
				models.setMessageId(MessagesEnum.UN_SUCCESSFUL.getCode());
				
			} catch (Exception e) {
				
				models.setMessageId(MessagesEnum.UN_SUCCESSFUL.getCode());
				
			}
		}

		return models;
	
	}
	
	public DataModelArrayList getJokeList(CoreModel coreModel) {
		
		coreModel = (CoreModel) SPProvider.initializeObject(coreModel, context);
		
		DataModelArrayList models = new DataModelArrayList();

		if (!Utility.checkNetwork(context)) {
			models.setMessageId(MessagesEnum.NO_NETWORK_CONNECTION.getCode());
		} else if (!Utility.checkInternetConnection()) {
			models.setMessageId(MessagesEnum.NO_INTERNET_CONNECTION.getCode());
		} else {
			try {

				ObjectConvertor<CoreModel> objectConvertorModel = new ObjectConvertor<CoreModel>();

				String result = postAndResponseString(
						objectConvertorModel.getClassString(coreModel),
						Utility.decrypt(UrlConstants.URL_JOKE_LIST,
								Utility.getAppSignature(context)),
						BusinessConstants.CONNECTION_TIMEOUT,
						BusinessConstants.BUSINESS_DATA_TIMEOUT);
				
				Log.d("testResult", result);

				ObjectConvertor<DataModelArrayList> objectConvertor = new ObjectConvertor<DataModelArrayList>();
				models = objectConvertor.getClassObject(result,
						DataModelArrayList.class);


			} catch (ClientProtocolException ex) {
				models.setMessageId(MessagesEnum.UN_SUCCESSFUL.getCode());
			} catch (IOException ex) {
				models.setMessageId(MessagesEnum.UN_SUCCESSFUL.getCode());
			} catch (Exception e) {
				models.setMessageId(MessagesEnum.UN_SUCCESSFUL.getCode());
			}
		}

		return models;
	
	}
	
}