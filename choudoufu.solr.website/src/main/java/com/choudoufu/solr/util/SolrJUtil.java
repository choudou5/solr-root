package com.choudoufu.solr.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.index.IndexableField;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrException.ErrorCode;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.UpdateParams;
import org.apache.solr.common.util.ContentStream;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrConfig;
import org.apache.solr.core.SolrCore;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrRequestHandler;
import org.apache.solr.request.SolrRequestInfo;
import org.apache.solr.response.ResultContext;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.response.transform.DocTransformer;
import org.apache.solr.response.transform.TransformContext;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.search.DocIterator;
import org.apache.solr.search.DocList;
import org.apache.solr.search.ReturnFields;
import org.apache.solr.search.SolrIndexSearcher;
import org.apache.solr.servlet.SolrRequestParsers;

public class SolrJUtil {
	
	public static final String QUERY_ALL ="*:*";
	
	/** Query Type */
	public static final String QT_SELECT ="/select";
	public static final String QT_UPDATE ="/update";
	  
	/** Writer Type */
	public static final String WT_XML ="xml";

	
	private static SolrQuery getSolrQuery(String keyword, boolean commit){
		SolrQuery params = new SolrQuery();
		params.add("wt", WT_XML);
		if(keyword != null)
			params.add("q", keyword);
		if(commit){
			params.add(UpdateParams.COMMIT, "true" );
			params.add(UpdateParams.WAIT_SEARCHER, "false");
			//OPEN_SEARCHER
		}
		return params;
	}
	 
	private static SolrQuery getSolrQuery(){
		return getSolrQuery(null, false);
	}
	
	public static SolrQuery getSolrQuery(String keyword){
		return getSolrQuery(keyword, false);
	}
	
	public static SolrQuery getSolrQuery(boolean commit){
		return getSolrQuery(null, true);
	}
	
	
	/**
	 * 删除 模型数据
	 * @param delQuery 删除语句
	 * @param core
	 */
	public static <T extends Serializable> void delModelData(String delQuery, SolrCore core){
		SolrQueryResponse solrResp = new SolrQueryResponse();
		SolrQuery params = getSolrQuery(true);
		params.add("stream.body", "<delete><query>"+delQuery+"</query></delete>");
		//&stream.contentType=text/xml;charset=utf-8&commit=true
	    try {
	    	ArrayList<ContentStream> streams = new ArrayList<>(1);
	    	SolrQueryRequest sreq = execute(QT_UPDATE, core, params, streams, solrResp);
		} catch (Exception e) {
			throw new SolrException(ErrorCode.BAD_REQUEST, "delModelData fail", e);
		}
	}
	
	/**
	 * 添加 模型数据
	 * @param dataMap
	 * @param core
	 */
	public static <T extends Serializable> void addModelData(Map<String, String> inputMap, SolrCore core){
		SolrQueryResponse solrResp = new SolrQueryResponse();
		SolrQuery params = getSolrQuery(true);
	    try {
		    ArrayList<SolrInputDocument> docs =  new ArrayList<SolrInputDocument>(1);
		    SolrInputDocument doc = new SolrInputDocument();
		    for (String field : inputMap.keySet()) {
		    	doc.addField(field, inputMap.get(field));
			}
		    docs.add(doc);
		    Collection<ContentStream> streams = getContentStreams(docs);
	    	SolrQueryRequest sreq = execute(QT_UPDATE, core, params, streams, solrResp);
		} catch (Exception e) {
			throw new SolrException(ErrorCode.BAD_REQUEST, "addModelData by dataMap fail", e);
		}
	}
	
	/**
	 * 添加 模型数据
	 * @param bean
	 * @param core
	 */
	public static <T extends Serializable> void addModelData(T bean, SolrCore core){
		SolrQueryResponse solrResp = new SolrQueryResponse();
		SolrQuery params = getSolrQuery(true);
	    try {
	    	Collection<ContentStream> streams = getContentStreams(bean);
	    	SolrQueryRequest sreq = execute(QT_UPDATE, core, params, streams, solrResp);
		} catch (Exception e) {
			throw new SolrException(ErrorCode.BAD_REQUEST, "addModelData fail", e);
		}
	}
	
	/**
	 * 添加 模型数据集合
	 * @param beans
	 * @param core
	 */
	public static <T extends Serializable> void addModelDatas(List<T> beans, SolrCore core){
		SolrQueryResponse solrResp = new SolrQueryResponse();
		SolrQuery params = getSolrQuery(true);
	    try {
	    	Collection<ContentStream> streams = getContentStreams(beans);
	    	SolrQueryRequest sreq = execute(QT_UPDATE, core, params, streams, solrResp);
		} catch (Exception e) {
			throw new SolrException(ErrorCode.BAD_REQUEST, "addModelDatas fail", e);
		}
	}
	
	/**
	 * 添加 模型数据集合
	 * @param beans
	 * @param core
	 */
	public static <T extends Serializable> void addModelDatas(T[] beans, SolrCore core){
		SolrQueryResponse solrResp = new SolrQueryResponse();
		SolrQuery params = getSolrQuery(true);
	    try {
	    	Collection<ContentStream> streams = getContentStreams(beans);
	    	SolrQueryRequest sreq = execute(QT_UPDATE, core, params, streams, solrResp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获得 模型数据
	 * @param params
	 * @param core
	 * @param clasz
	 * @return
	 */
	public static <T extends Serializable> T getModelData(SolrQuery params, SolrCore core, Class<T> clasz){
		SolrQueryResponse solrResp = new SolrQueryResponse();
	    try {
	    	ArrayList<ContentStream> streams = new ArrayList<>(1);
	    	SolrQueryRequest sreq = execute(QT_SELECT, core, params, streams, solrResp);
			SolrDocument doc = getDocument(solrResp, sreq);
			if(doc != null){
				DocumentObjectBinder binder = new DocumentObjectBinder();
				return binder.getBean(clasz, doc);
			}
		} catch (Exception e) {
			throw new SolrException(ErrorCode.BAD_REQUEST, "getModelData fail", e);
		}
	    return null;
	}
	
	/**
	 * 查询模型
	 * @param keyword
	 * @param core
	 * @param req
	 * @param resp
	 * @param clasz
	 * @return
	 */
	public static <T extends Serializable> List<T> listModelData(SolrQuery params, SolrCore core, Class<T> clasz){
		SolrQueryResponse solrResp = new SolrQueryResponse();
	    try {
	    	ArrayList<ContentStream> streams = new ArrayList<>(1);
	    	SolrQueryRequest sreq = execute(QT_SELECT, core, params, streams, solrResp);
			
			SolrDocumentList docList = getDocuments(solrResp, sreq);
			if(docList != null){
				DocumentObjectBinder binder = new DocumentObjectBinder();
				return (List<T>) binder.getBeans(clasz, docList);
			}
		} catch (Exception e) {
			throw new SolrException(ErrorCode.BAD_REQUEST, "listModelData fail", e);
		}
	    return null;
	}
	
	
	private static SolrQueryRequest execute(String qt, SolrCore core, SolrQuery params, Collection<ContentStream> streams, SolrQueryResponse resp) throws Exception{
		final SolrConfig config = core.getSolrConfig();
		SolrRequestParsers parser = config.getRequestParsers();
		SolrQueryRequest sreq = parser.buildRequestFrom(core, params, streams);
		sreq.getContext().put( "path", qt);
		SolrRequestInfo.setRequestInfo(new SolrRequestInfo(sreq, resp));
		
		SolrRequestHandler handler = core.getRequestHandler(qt);
		sreq.getCore().execute( handler, sreq, resp);
		SolrRequestInfo.clearRequestInfo();
		return sreq;
	}
	
	
	/**
	 * 获得 文档列表
	 * @param resp
	 * @param req
	 * @link http://www.programcreek.com/java-api-examples/index.php?api=org.apache.solr.response.ResultContext
	 * @return
	 * @throws IOException
	 */
	public static SolrDocumentList getDocuments(SolrQueryResponse resp, SolrQueryRequest req) throws IOException {
		NamedList<?> list = resp.getValues();
		ResultContext res = (ResultContext) list.get("response");
		ReturnFields fields = resp.getReturnFields();
		
		DocList ids = res.docs;
		TransformContext context = new TransformContext();
		context.query = res.query;
		context.wantsScores = fields.wantsScore() && ids.hasScores();
		context.req = req;

		SolrDocumentList docs = new SolrDocumentList();

		docs.setNumFound(ids == null ? 0 : ids.matches());
		docs.setStart(ids == null ? 0 : ids.offset());
		docs.setMaxScore(ids == null ? 0 : ids.maxScore());

		DocTransformer transformer = fields.getTransformer();
		context.searcher = req.getSearcher();
		context.iterator = ids.iterator();
		if( transformer != null ) {
			transformer.setContext(context);
		}
		
		Set<String> fieldNames = fields.getLuceneFieldNames();
		int size = ids.size();
		IndexSchema schema = req.getSchema();
		DocIterator iterator = ids.iterator();
		for (int i=0; i<size; i++) {
			if(!iterator.hasNext())
				break;
			docs.add(getSolrDocument(iterator.nextDoc(), req.getSearcher(), transformer, schema, fieldNames));
		}
		if( transformer != null ) {
			transformer.setContext( null );
		}
		return docs;
	}
	
	/**
	 * 获得 文档
	 * @param resp
	 * @param req
	 * @return
	 * @throws IOException
	 */
	public static SolrDocument getDocument(SolrQueryResponse resp, SolrQueryRequest req) throws IOException {
		NamedList<?> list = resp.getValues();
		ResultContext res = (ResultContext) list.get("response");
		ReturnFields fields = resp.getReturnFields();
		
		DocList ids = res.docs;
		TransformContext context = new TransformContext();
		context.query = res.query;
		context.wantsScores = fields.wantsScore() && ids.hasScores();
		context.req = req;

		DocTransformer transformer = fields.getTransformer();
		context.searcher = req.getSearcher();
		context.iterator = ids.iterator();
		if( transformer != null ) {
			transformer.setContext(context);
		}
		Set<String> fieldNames = fields.getLuceneFieldNames();
		IndexSchema schema = req.getSchema();
		
		if(!context.iterator.hasNext())
			return null;
		SolrDocument doc = getSolrDocument(context.iterator.nextDoc(), req.getSearcher(), transformer, schema, fieldNames);
		if( transformer != null ) {
			transformer.setContext( null );
		}
		return doc;
	}
	
	private static SolrDocument getSolrDocument(int id, SolrIndexSearcher searcher, DocTransformer transformer, IndexSchema schema, Set<String> fieldNameSet) throws IOException{
		Document doc = searcher.doc(id, fieldNameSet);
		Set<String> fieldNames = schema.getFields().keySet();
		SolrDocument d = new SolrDocument();
		for (String fname : fieldNames) {
			Object val =doc.getField(fname);
			if ((val instanceof StoredField) || (val instanceof Field)) {
				Object o = schema.getField(fname).getType().toObject((IndexableField) val);
				d.setField(fname, o);
			}

			if( transformer != null ) {
				transformer.transform(d, id);
			}
		}
		return d;
	}
	
	
	private static <T extends Serializable> Collection<ContentStream> getContentStreams(T bean) throws SolrServerException, IOException {
		DocumentObjectBinder binder = new DocumentObjectBinder();
	    ArrayList<SolrInputDocument> docs =  new ArrayList<SolrInputDocument>(1);
	    docs.add(binder.toSolrInputDocument(bean));
	    return getContentStreams(docs);
	}
	
	private static <T extends Serializable> Collection<ContentStream> getContentStreams(T[] beans) throws SolrServerException, IOException {
		DocumentObjectBinder binder = new DocumentObjectBinder();
	    ArrayList<SolrInputDocument> docs =  new ArrayList<SolrInputDocument>(1);
	    for (T bean : beans) {
	        docs.add(binder.toSolrInputDocument(bean));
	    }
	    return getContentStreams(docs);
	}
	
	private static <T extends Serializable> Collection<ContentStream> getContentStreams(List<T> beans) throws SolrServerException, IOException {
		DocumentObjectBinder binder = new DocumentObjectBinder();
	    ArrayList<SolrInputDocument> docs =  new ArrayList<SolrInputDocument>(1);
	    for (T bean : beans) {
	        docs.add(binder.toSolrInputDocument(bean));
	    }
	    return getContentStreams(docs);
	}
	
	
	private static <T extends Serializable> Collection<ContentStream> getContentStreams(ArrayList<SolrInputDocument> docs) throws SolrServerException, IOException {
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
