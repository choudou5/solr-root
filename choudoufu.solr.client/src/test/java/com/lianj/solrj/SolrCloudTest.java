package com.lianj.solrj;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Before;
import org.junit.Test;

import com.lianj.solrj.api.SolrAPI;
import com.lianj.solrj.api.impl.SolrAPIImpl;
import com.lianj.solrj.server.SolrServer;
import com.lianj.solrj.util.SolrUtil.ServerType;

public class SolrCloudTest {
	
	String zkHost = "192.168.36.220:2181,192.168.37.220:3181,192.168.36.220:4181";
	String moduleName = "collection1";
	
	private SolrAPI solrAPI;
	@Before
	public void init() {
		SolrServer cloud = new SolrServer(zkHost, ServerType.CLOUD);
		cloud.setInitConnectionModules("collection1");
		solrAPI = new SolrAPIImpl(cloud);
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
	public void search() {
		System.out.println("The Cloud SolrServer Instance has benn created!");
		try {
			SolrQuery query = new SolrQuery("*:*");
			query.addFilterQuery("type:7");
			QueryResponse response = solrAPI.query(moduleName, query);
			SolrDocumentList docs = response.getResults();
			System.out.println("文档个数：" + docs.getNumFound());
			System.out.println("查询时间：" + response.getQTime()+"毫秒");
			for (SolrDocument doc : docs) {
				Object id = doc.getFieldValue("docno");
				Object contenttitle = doc.getFieldValue("contenttitle");
				Object url = doc.getFieldValue("url");
				Object type = doc.getFieldValue("type");
				System.out.println("id=" + id + ", contenttitle=" + contenttitle + ", url=" + url + ", type=" + type);
			}
		}catch (Exception e) {
			System.out.println("Unknowned Exception!!!!");
			e.printStackTrace();
		}
		System.out.println("测试结束");
	}
	
}
