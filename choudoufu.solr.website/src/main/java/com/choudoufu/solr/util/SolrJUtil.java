package com.choudoufu.solr.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.UpdateParams;
import org.apache.solr.common.util.ContentStream;
import org.apache.solr.core.SolrConfig;
import org.apache.solr.core.SolrCore;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrRequestHandler;
import org.apache.solr.request.SolrRequestInfo;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.servlet.SolrRequestParsers;

import com.choudoufu.client.model.SolrBaseModel;

public class SolrJUtil {

	/**
	 * 添加 模型
	 * @param bean
	 * @param core
	 * @param req
	 * @param resp
	 */
	public static <T extends SolrBaseModel> void addModel(T bean, SolrCore core, SolrQueryRequest req, SolrQueryResponse resp){
		final SolrConfig config = core.getSolrConfig();
		SolrRequestParsers parser = config.getRequestParsers();
		
		String path = "/update";
		SolrRequestHandler handler = core.getRequestHandler(path);
		SolrQuery params = new SolrQuery();
		params.add("wt", "json");
		params.add(UpdateParams.COMMIT, "true" );
	    try {
	    	Collection<ContentStream> streams = getContentStreams(bean);
			SolrQueryRequest sreq = parser.buildRequestFrom( core, params, streams);
			sreq.getContext().put( "path", path );
			SolrRequestInfo.setRequestInfo(new SolrRequestInfo(req, resp));
			sreq.getCore().execute( handler, sreq, resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static <T extends SolrBaseModel> Collection<ContentStream> getContentStreams(T bean) throws SolrServerException, IOException {
		DocumentObjectBinder binder = new DocumentObjectBinder();
	    ArrayList<SolrInputDocument> docs =  new ArrayList<>(1);
	    docs.add(binder.toSolrInputDocument(bean));
	    UpdateRequest req = new UpdateRequest();
	    req.add(docs);
	    req.setCommitWithin(-1);
	    return req.getContentStreams();
	}
	
	@SuppressWarnings("unused")
	private static Collection<ContentStream> getContentStreams(Collection<?> beans) throws SolrServerException, IOException {
		DocumentObjectBinder binder = new DocumentObjectBinder();
	    ArrayList<SolrInputDocument> docs =  new ArrayList<>(beans.size());
	    for (Object bean : beans) {
	      docs.add(binder.toSolrInputDocument(bean));
	    }
	    UpdateRequest req = new UpdateRequest();
	    req.add(docs);
	    req.setCommitWithin(-1);
	    return req.getContentStreams();
	}
	
}
