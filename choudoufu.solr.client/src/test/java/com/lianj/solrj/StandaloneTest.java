package com.lianj.solrj;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.solr.client.solrj.SolrQuery;
import org.junit.Before;
import org.junit.Test;

import com.lianj.solrj.api.SolrAPI;
import com.lianj.solrj.api.impl.SolrAPIImpl;
import com.lianj.solrj.entity.SolrTestEntity;
import com.lianj.solrj.server.SolrServer;
import com.lianj.solrj.util.SolrUtil.ServerType;

public class StandaloneTest {

	String url = "http://192.168.36.226:9934/";
	String moduleName = "collection1";
	
	private SolrAPI solrAPI;
	
	@Before
	public void init() {
		if(solrAPI == null){
			SolrServer server = new SolrServer(url, ServerType.SINGLE);
			solrAPI = new SolrAPIImpl(server);
		}
	}
	
	@Test
	public void ping() {	
		String result = solrAPI.ping(moduleName);
		System.out.println(result);
	}
	
	@Test
	public void optimize() {	
		solrAPI.optimize(moduleName);
	}
	
	@Test
	public void initData(){
		int loop = 1000;
		List<SolrTestEntity> list = new ArrayList<SolrTestEntity>();
		String type = "food", status = "Y";
		for (int i = 0; i < loop; i++) {
			list.add(genObj(i, type, status));
		}
		boolean success = solrAPI.addList(moduleName, list);
		System.out.println("添加："+success);
	}
	
	//生成对象
	private SolrTestEntity genObj(int id, String type, String status){
		SolrTestEntity obj = new SolrTestEntity();
		obj.setId(id+"");
		obj.setTitle("标题"+id);
		obj.setTag(genTags());
		Random rd = new Random();
		obj.setNumber(rd.nextInt(100000000));
		obj.setType(type);
		obj.setStatus(status);
		Date create = new Date();
		create.setDate(create.getDate()-rd.nextInt(100));
		obj.setCreateTime(create);
		obj.setUpdateTime(new Date());
		return obj;
	}
	
	private String[] TAGS = new String[]{"冰箱","美食","绿色","电影","动画","手机","电脑","美女","通讯","小说","卫星"}; 
	
	private String[] genTags(){
		Random rd = new Random();
		int loop = rd.nextInt(5);
		String[] tags = new String[loop];
		int start = rd.nextInt(12);
		for (int i = 0; i < loop; i++,start++) {
			if(start == 11){
				start = 0;
			}
			tags[i] = TAGS[start];
		}
		return tags;
	}

	@Test
	public void queryData(){

		String keyword = "tag:冰箱 OR title:冰箱";

		SolrQuery query = new SolrQuery(keyword);
		query.addFilterQuery("status:Y");
		List<SolrTestEntity> testEntities = solrAPI.query(moduleName, query, SolrTestEntity.class);
		System.out.println(testEntities);
	}

}
