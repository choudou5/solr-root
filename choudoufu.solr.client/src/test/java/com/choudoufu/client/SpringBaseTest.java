package com.choudoufu.client;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.choudoufu.client.service.SolrService;

@RunWith(SpringJUnit4ClassRunner.class )
@ContextConfiguration( {"classpath:conf/solr-client-context.xml"} )
public class SpringBaseTest {
	
	@Autowired
	private SolrService solrAPI;
	
	String moduleName = "collection1";
	
	@Before
	public void init() {
		System.out.println("init....");
	}
	
	@Test
	public void testSearch() {
		long start = System.currentTimeMillis();
		for (int i = 0; i < 16; i++) {
			search();
		}
		long end = System.currentTimeMillis();
		System.out.println("time:"+(end - start));
	}
	
	private void search() {
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
