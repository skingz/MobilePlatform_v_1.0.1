package com.leading.localequestion.net;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.CloseableIterator;
import com.leading.baselibrary.netutil.NetUtil;
import com.leading.baselibrary.util.FileBeanMakeUp;
import com.leading.baselibrary.util.StringUtils;
import com.leading.localequestion.data.ConstantStore;
import com.leading.localequestion.data.RequestAbsolutePath;
import com.leading.localequestion.entity.LocaleQuestion;
import com.leading.localequestion.entity.LocaleQuestionBo;
import com.leading.localequestion.entity.QuestionAffix;
import com.leading.localequestion.entity.QuestionAffixBo;
import com.leading.localequestion.entity.SysBelong;
import com.leading.localequestion.entity.SysBelongBo;
import com.leading.localequestion.global.LQApplication;

public class FeedbackNet {
	private RequestAbsolutePath requestPath;
	private Context context;
	public final static String ERROR = "error";
	private String[] results = new String[2];
	private String result;

	public FeedbackNet(Context context) {
		requestPath = new RequestAbsolutePath();
		this.context = context;
	}

	/**
	 * 获取问题数据
	 * 
	 * @param questionId
	 *            问题ID
	 * @return
	 */
	public LocaleQuestionBo qsGetInfo(String questionId) {
		JSONObject obj = new JSONObject();
		result = NetUtil.getResponseForPost(
				requestPath.getQsGetInfoPath(questionId), obj.toString(),
				context);
		if (StringUtils.isNotNull(result)) {
			Gson gson=null;
			try {
				gson = new GsonBuilder().setDateFormat(ConstantStore.TIME_PATTERN)
						.create();
				Log.v("sth error from:",result);
				return gson.fromJson(result, LocaleQuestionBo.class);
			} catch (JsonSyntaxException e) {
				gson = new GsonBuilder().setDateFormat(ConstantStore.TIME_PATTERN_ONE)
						.create();
				Log.v("sth error from:",result);
				return gson.fromJson(result, LocaleQuestionBo.class);
			}
		}
		return null;
	}

	/**
	 * 提交問題.
	 * 
	 * @param lq
	 *            问题对象
	 * @return
	 */
	public String qsSubmit(LocaleQuestion lq) {
		System.out.println(getLocaleQuestionJson(lq));
		result = NetUtil.getResponseForPost(requestPath.getQsSubmitPath(),
				getLocaleQuestionJson(lq), context);
		return getResult(result);
	}

	public String qsSendNewMsg(String qsId, String sourceUserID) {
		String result = null;
		JSONObject obj = new JSONObject();
		/*
		 * obj.put("QsId", qsId); obj.put("sourceUserID",sourceUserID);
		 */
		result = NetUtil.getResponseForPost(
				requestPath.getQsSendNewMsg(qsId, sourceUserID),
				obj.toString(), context);
		return getResult(result);
	}

	/**
	 * 提交問題附件.
	 * 
	 * @param path
	 *            附件路径
	 * @return
	 */
	public String qsAffixUpload(String path) {
		result = NetUtil.getResposeForPostStream(
				requestPath.getQsAffixUploadPath(), path, context);
		return getResult(result, "QsAffixUploadResult");
	}

	/**
	 * 获取项目列表
	 * 
	 * @return
	 */
	public List<SysBelong> getServiceProjects() {
		List<SysBelongBo> sysList = new ArrayList<SysBelongBo>();
		List<SysBelong> reSysList = new ArrayList<SysBelong>();
		result = NetUtil.getResponseForPost(
				requestPath.getProjects(LQApplication.getConfig().getUserId()),
				(new JSONObject()).toString(), context);
		Gson gson = new Gson();
		sysList = gson.fromJson(result, new TypeToken<List<SysBelongBo>>() {
		}.getType());
		try {
			for (SysBelongBo sb : sysList) {
				reSysList.add(sb.ToSysBelong());
			}
		} catch (Exception e) {
			return null;
		}
		return reSysList;
	}

	/**
	 * 提交已解决问题.
	 * 
	 * @param lq
	 *            问题信息
	 * @return
	 */
	public String qsResolved(LocaleQuestion lq) {
		result = NetUtil.getResponseForPost(requestPath.getQsResolvedPath(),
				getLocaleQustionJsonTwo(lq), context);
		return getResult(result);
	}

	/**
	 * 问题置成暂不解决状态.
	 * 
	 * @param lq
	 *            问题信息
	 * @return
	 */
	public String qsTempNoResolved(LocaleQuestion lq) {
		result = NetUtil.getResponseForPost(
				requestPath.getQsTempNoResolvedPath(),
				getLocaleQustionJsonTwo(lq), context);
		return getResult(result);
	}

	/**
	 * 获取附件.
	 * 
	 * @param keyid
	 * @return path路径
	 */
	public String qsAffixDownLoad(String keyId) {
		byte[] bytes = NetUtil.getStreamForGet(
				requestPath.getAffixDownLoad(keyId), context);
		if (bytes == null)
			return null;
		if (bytes.length == 0)
			return null;
		File file = new File(ConstantStore.IMG_LOCATION);
		if (!file.exists())
			file.mkdirs();
		Object[] array = FileBeanMakeUp.saveFileByByte(
				ConstantStore.IMG_LOCATION, bytes);
		if (array != null) {
			if ((Boolean) array[0]){
				results[0]=ConstantStore.SUCCESS;
				return (String) array[1];
			}
		}
		return null;
	}

	/**
	 * 本次请求是否成功
	 * 
	 * @return
	 */
	public boolean isSuccess() {
		if (ConstantStore.SUCCESS.equals(results[0]))
			return true;
		else if (ConstantStore.ERROR.equals(results[0]))
			return false;
		else if (ERROR.equals(results[0]))
			return false;
		else if(result==null ||"".equals(result))
			return false;
		return false;
	}

	/**
	 * 获取返回的结果.
	 * 
	 * @param result
	 *            返回结果
	 * @return
	 */
	public String getResult(String result) {
		results[0] = ERROR;
		if (StringUtils.isNotNull(result)) {
			try {
				JSONObject obj = new JSONObject(result);
				results[0] = obj.getString("ResultTag");
				results[1] = obj.getString("ResultRemark");
			} catch (JSONException e) {
				return results[1];
			}
		}
		return results[1];
	}

	public String getResult(String result, String nodeName) {
		results[0] = ERROR;
		if (StringUtils.isNotNull(result)) {
			try {
				JSONObject obj = new JSONObject(result);
				results[0] = obj.getString(nodeName);
			} catch (JSONException e) {
				return results[0];
			}
		}
		return "OK";
	}

	public String getLocaleQustionJsonTwo(LocaleQuestion lq) {
		LocaleQuestionBo lqb = new LocaleQuestionBo(lq.getFsiid(),
				lq.getTitle(), lq.getSysBelongId(), lq.getHappenTime(),
				lq.getQsClass(), lq.getQsGrade(), lq.getQsDiscrable(),
				lq.getNeedFinishTime(), lq.getQsState(), lq.isIfSolvedRoot(),
				lq.getResolvent(), lq.getManHour(), lq.getRemark(),
				lq.getSolvedTime(), lq.getUserId(), null);
		Gson gson = new Gson();
		String str = gson.toJson(lqb);
		// Log.v("Json", str);
		return str;
	}

	/**
	 * 序列化为JSON数据.
	 * 
	 * @param lq
	 *            问题对象
	 * @return
	 */
	public String getLocaleQuestionJson(LocaleQuestion lq) {
		CloseableIterator<QuestionAffix> list = lq.getQuestionAffixs()
				.closeableIterator();
		List<QuestionAffixBo> qbList = new ArrayList<QuestionAffixBo>();
		while (list.hasNext()) {
			QuestionAffix qa = list.next();
			QuestionAffixBo qb = new QuestionAffixBo();
			qb.setAffixName(qa.getAffixName());
			qb.setKeyId(qa.getFsiid());
			qbList.add(qb);
		}
		LocaleQuestionBo lqb = new LocaleQuestionBo(lq.getFsiid(),
				lq.getTitle(), lq.getSysBelongId(), lq.getHappenTime(),
				lq.getQsClass(), lq.getQsGrade(), lq.getQsDiscrable(),
				lq.getNeedFinishTime(), lq.getQsState(), lq.isIfSolvedRoot(),
				lq.getResolvent(), lq.getManHour(), lq.getRemark(),
				lq.getSolvedTime(), lq.getUserId(), qbList);
		Gson gson = new GsonBuilder().setDateFormat(ConstantStore.TIME_PATTERN)
				.create();
		String str = gson.toJson(lqb);
		return str;
	}

}
