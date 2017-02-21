package com.choudoufu.solr.common.page;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.apache.solr.client.solrj.response.FacetField;

/**
 * 描述： 分页工具
 * 作者：徐浩文
 * 邮箱: xuhaowende@sina.cn
 * 日期:Jun 19, 2015-4:43:13 PM
 * 版权：徐浩文版权所有
 * </pre>
 */
public class Page<T extends Serializable>{

	public static final Integer MAX_ROWS = 30;	// 每页最大记录数限制
	
	protected int pageNo = 1;
	protected int pageSize = 10;
	protected List<T> result = Collections.emptyList();
	protected List<FacetField> facets;//分片数据
	protected long totalCount = 0;
	protected int qTime;

	public Page() {
	}

	public Page(int pageSize) {
		setPageSize(pageSize);
	}
	
	public Page(int pageSize,int pageNo) {
		setPageSize(pageSize);
		setPageNo(pageNo);
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(final int pageNo) {
		this.pageNo = pageNo;
		if (pageNo < 1) {
			this.pageNo = 1;
		}
	}

	public Page<T> pageNo(final int thePageNo) {
		setPageNo(thePageNo);
		return this;
	}

	
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(final int pageSize) {
		this.pageSize = pageSize;

		if (pageSize < 1) {
			this.pageSize = 1;
		}else if(pageSize > MAX_ROWS){
			this.pageSize = MAX_ROWS;
		}
	}

	public Page<T> pageSize(final int thePageSize) {
		setPageSize(thePageSize);
		return this;
	}

	public int getFirstResult() {
		return (pageNo - 1) * pageSize;
	}

	public List<T> getResult() {
		return result;
	}

	
	public void setResult(final List<T> result) {
		this.result = result;
	}
	
	public List<FacetField> getFacets() {
		return facets;
	}

	public void setFacets(List<FacetField> facets) {
		this.facets = facets;
	}

	public long getTotalCount() {
		return totalCount;
	}
	
	public int getIntTotalCount() {
		return Long.valueOf(totalCount).intValue();
	}
	
	public void setTotalCount(final long totalCount) {
		this.totalCount = totalCount;
	}
	
	public int getqTime() {
		return qTime;
	}

	public void setqTime(int qTime) {
		this.qTime = qTime;
	}

	public long getTotalPages() {
		if (totalCount <= 0)
			return 0;

		long count = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			count++;
		}
		return count;
	}
	
	public int getIntTotalPages() {
		if (totalCount <= 0)
			return 0;

		int count = Long.valueOf(totalCount / pageSize).intValue();
		if (totalCount % pageSize > 0) {
			count++;
		}
		return count;
	}

	public boolean isHasNext() {
		return (pageNo + 1 <= getTotalPages());
	}

	public int getNextPage() {
		if (isHasNext())
			return pageNo + 1;
		else
			return pageNo;
	}

	public boolean isHasPre() {
		return (pageNo - 1 >= 1);
	}

	public int getPrePage() {
		if (isHasPre())
			return pageNo - 1;
		else
			return pageNo;
	}
	
}
