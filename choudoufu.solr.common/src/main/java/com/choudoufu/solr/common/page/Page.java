package com.choudoufu.solr.common.page;

import java.io.Serializable;
import java.util.ArrayList;
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
	protected int length = 6;// 显示页面长度
	protected int rangeEnd = 0;// 范围页尾
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

	/** 返回 总页数 */
	public long getTotalPages() {
		if (totalCount <= 0)
			return 0;

		long count = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			count++;
		}
		return count;
	}
	
	/** 返回 整数型 总页数 */
	public int getIntTotalPages() {
		if (totalCount <= 0)
			return 0;

		int count = Long.valueOf(totalCount / pageSize).intValue();
		if (totalCount % pageSize > 0) {
			count++;
		}
		return count;
	}

	/** 是否有 下一页 */
	public boolean isHasNext() {
		return (pageNo + 1 <= getTotalPages());
	}

	/** 返回 下一页 页数 */
	public int getNextPage() {
		if (isHasNext())
			return pageNo + 1;
		else
			return pageNo;
	}

	/** 是否有 上一页 */
	public boolean isHasPre() {
		return (pageNo - 1 >= 1);
	}

	/** 返回 上一页 页数 */
	public int getPrePage() {
		if (isHasPre())
			return pageNo - 1;
		else
			return pageNo;
	}

	public int getRangeEnd() {
		return rangeEnd;
	}
	
	/**
	 * 获得 分页范围数
	 */
	public List<Integer> getPageRange() {
		List<Integer> ranges = new ArrayList<Integer>(length);
		int firstPage = 1;
		int totalPage = getIntTotalPages();
		int begin = pageNo - (length / 2);
		if (begin < firstPage)
			begin = firstPage;

		int end = begin + length - 1;
		if (end >= totalPage) {
			end = totalPage;
			begin = end - length + 1;
			if (begin < firstPage) {
				begin = firstPage;
			}
		}

		if (begin > firstPage){
			int i = 0;
			for (i = firstPage; i < firstPage + 1 && i < begin; i++) {
				ranges.add(i);
			}
		}
		for (int i = begin; i <= end; i++) {
			ranges.add(i);
		}
		rangeEnd = end;
		return ranges;
	}
	
}
