package com.choudoufu.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.solr.client.solrj.SolrQuery;
import org.junit.Before;
import org.junit.Test;

import com.choudoufu.client.server.SolrServer;
import com.choudoufu.client.service.SolrService;
import com.choudoufu.client.service.impl.SolrServiceImpl;
import com.choudoufu.client.util.SolrUtil.ServerType;
import com.choudoufu.solrj.model.SolrTestModel;

public class StandaloneTest {

	String url = "http://192.168.36.226:9934/";
	String moduleName = "collection1";
	
	private SolrService solrAPI;
	
	@Before
	public void init() {
		if(solrAPI == null){
			SolrServer server = new SolrServer(url, ServerType.SINGLE);
			solrAPI = new SolrServiceImpl(server);
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
		List<SolrTestModel> list = new ArrayList<SolrTestModel>();
		String type = "food", status = "Y";
		for (int i = 0; i < loop; i++) {
			list.add(genObj(i, type, status));
		}
		boolean success = solrAPI.addList(moduleName, list);
		System.out.println("添加："+success);
	}
	
	//生成对象
	@SuppressWarnings("deprecation")
	private SolrTestModel genObj(int id, String type, String status){
		SolrTestModel obj = new SolrTestModel();
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
		List<SolrTestModel> testEntities = solrAPI.query(moduleName, query, SolrTestModel.class);
		System.out.println(testEntities);
	}

}
