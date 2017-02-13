package com.choudoufu.client;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Before;
import org.junit.Test;

import com.choudoufu.client.server.SolrServer;
import com.choudoufu.client.service.SolrService;
import com.choudoufu.client.service.impl.SolrServiceImpl;
import com.choudoufu.client.util.SolrUtil.ServerType;

public class MasterSlaveTest {

	private SolrService solrAPI;
	
	String hosts = "192.168.36.223:18188,192.168.36.224:18188";
	String moduleName = "collection1";
	
	@Before
	public void init() {
		SolrServer master_slave = new SolrServer(hosts, ServerType.MASTER_SLAVE);
		solrAPI = new SolrServiceImpl(master_slave);
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
		System.out.println("The MasterSlave SolrServer Instance has benn created!");
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
