package com.choudoufu.solr.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
		if(commit)
			params.add(UpdateParams.COMMIT, "true" );
		return params;
	}
	 
	private static SolrQuery getSolrQuery(){
		return getSolrQuery(null, false);
	}
	
	private static SolrQuery getSolrQuery(String keyword){
		return getSolrQuery(keyword, false);
	}
	
	private static SolrQuery getSolrQuery(boolean commit){
		return getSolrQuery(null, true);
	}
	

	/**
	 * 添加 模型
	 * @param bean
	 * @param core
	 * @param req
	 * @param resp
	 */
	public static <T extends Serializable> void addModel(T bean, SolrCore core, SolrQueryRequest req, SolrQueryResponse resp){
		SolrQuery params = getSolrQuery(true);
	    try {
	    	Collection<ContentStream> streams = getContentStreams(bean);
			SolrQueryRequest sreq = execute(QT_UPDATE, core, params, streams, resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	public static <T extends Serializable> T getModel(String keyword, SolrCore core, SolrQueryRequest req, SolrQueryResponse resp, Class<T> clasz){
		SolrQuery params = getSolrQuery(keyword);
	    try {
	    	ArrayList<ContentStream> streams = new ArrayList<>(1);
			SolrQueryRequest sreq = execute(QT_SELECT, core, params, streams, resp);
			
			SolrDocument doc = getDocument(resp, sreq);
			DocumentObjectBinder binder = new DocumentObjectBinder();
			return binder.getBean(clasz, doc);
		} catch (Exception e) {
			e.printStackTrace();
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
		return sreq;
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
	public static <T extends Serializable> List<T> listModel(String keyword, SolrCore core, SolrQueryRequest req, SolrQueryResponse resp, Class<T> clasz){
		SolrQuery params = getSolrQuery(keyword);
	    try {
	    	ArrayList<ContentStream> streams = new ArrayList<>(1);
	    	SolrQueryRequest sreq = execute(QT_SELECT, core, params, streams, resp);
			
			SolrDocumentList docList = getDocuments(resp, sreq);
			DocumentObjectBinder binder = new DocumentObjectBinder();
			return (List<T>) binder.getBeans(clasz, docList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return null;
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
		for (int i=0; i<size; i++) {
			docs.add(getSolrDocument(ids.iterator(), req.getSearcher(), transformer, schema, fieldNames));
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
		
		SolrDocument doc = getSolrDocument(ids.iterator(), req.getSearcher(), transformer, schema, fieldNames);
		if( transformer != null ) {
			transformer.setContext( null );
		}
		return doc;
	}
	
	private static SolrDocument getSolrDocument(DocIterator iterator, SolrIndexSearcher searcher, DocTransformer transformer, IndexSchema schema, Set<String> fieldNameSet) throws IOException{
		int id = iterator.nextDoc();
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
