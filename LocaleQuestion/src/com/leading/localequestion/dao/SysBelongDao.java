package com.leading.localequestion.dao;


import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.widget.Toast;

import com.leading.baselibrary.database.LzDAO;
import com.leading.localequestion.R;
import com.leading.localequestion.entity.SysBelong;
import com.leading.localequestion.net.FeedbackNet;

public class SysBelongDao extends LzDAO<SysBelong>{

	private Context _context;
	public SysBelongDao(Context context) {
		super(context,R.xml.db);
		_context=context;
		// TODO Auto-generated constructor stub
	}
    public void UpdateProjectEnum(){
    	FeedbackNet feedBackNet=new FeedbackNet(_context);
    	List<SysBelong> proList=feedBackNet.getServiceProjects();
    	if(proList==null){
    		Toast.makeText(_context,"所属系统获取失败请重试！",Toast.LENGTH_SHORT).show();
    		return;
    	}
    	if(proList.size()>0){
    		try {
				List<SysBelong> pro_Old=super.getDao().queryForAll();
				super.getDao().delete(pro_Old);
				
				for(SysBelong sb : proList){
	    			super.getDao().createOrUpdate(sb);
	    		}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    int getCount=0;
    public SysBelong getByFsiid(String fsiid){
    	if(getCount>1)return null;
    	getCount++;
    	SysBelong sysBelong=this.query("fsiid",fsiid);
    	if(sysBelong==null){
    		UpdateProjectEnum();
    		getByFsiid(fsiid);
    	}else
    		return sysBelong;
    	return null;
    	
    }
}
