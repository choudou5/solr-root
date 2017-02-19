package com.choudoufu.solr.core.request;

import javax.servlet.http.HttpServletRequest;

import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrRequestHandler;
import org.apache.solr.response.SolrQueryResponse;

public interface CustomRequestHandler extends SolrRequestHandler{

	public void handleRequest(HttpServletRequest req, SolrQueryRequest sreq, SolrQueryResponse rsp);
	
}
