package com.choudoufu.solr.website;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

/**
 * 分页填补
 * @author xuhaowen
 * @date 2017年2月25日
 */
public class PagePaddingExample {

	public static void main(String[] args) {
		int pageSize = 10;
		for (int pageNo = 1; pageNo < 8; pageNo++) {
			findPage(pageNo, pageSize);
		}
	}
	
	public static Page<String> findPage(Integer pageNo, Integer pageSize) {
		Page<String> page = new Page<String>(pageSize, pageNo);
		List<String> result = new ArrayList<String>();
		
		//各自类型 总数
		int type1Total = 2;
		int type2Total = 26;
		int type3Total = 15;
		
		int queryTotal = type1Total+type2Total+type3Total;
		int currPageEnd = pageNo * pageSize;//当前页尾数
		int queryRange = queryTotal+pageSize;
		if(currPageEnd > queryRange)//超出范围--终止
			return null;
		
		//查询类型1数据
		int currTypeTotal = 0;
		queryDataByType(currTypeTotal, currPageEnd, type1Total, pageSize, result, "type1");
		
		//查询类型2数据
		currTypeTotal = type1Total;
		queryDataByType(currTypeTotal, currPageEnd, type2Total, pageSize, result, "type2");
		
		//查询类型3数据
		currTypeTotal = type1Total+type2Total;
		queryDataByType(currTypeTotal, currPageEnd, type3Total, pageSize, result, "type3");
				
		page.setResult(result);
		page.setTotalCount(queryTotal);
		System.out.println("pageNo:"+pageNo+" query result size:"+result.size()+"\r\n");
		return page;
	}
	
	/**
	 * 查找 数据
	 * @param beforeTypeTotal 之前类型总数
	 * @param currPageEnd 当前页尾值
	 * @param currTypeTotal 当前类型总数
	 * @param pageSize 
	 * @param result 结果集
	 * @param type 类型
	 */
	private static void queryDataByType(int beforeTypeTotal, int currPageEnd, int currTypeTotal, int pageSize, List<String> result, String type){
		//是否 需要查询
		if(isNeedQuery(beforeTypeTotal, currPageEnd, currTypeTotal, pageSize)){
			//计算 分页
			SqlPage sqlPage = calculatePage(currPageEnd, beforeTypeTotal, pageSize);
			List<String> list = findList(sqlPage, type);
			if(CollectionUtils.isNotEmpty(list))
				result.addAll(list);
		}
	}
	
	/**
	 * 是否 需要查询
	 * @param beforeTypeTotal
	 * @param currPageEnd
	 * @param currTypeTotal
	 * @param pageSize
	 * @return
	 */
	private static boolean isNeedQuery(int beforeTypeTotal, int currPageEnd,  int currTypeTotal, int pageSize){
		int beforeRange = beforeTypeTotal+currTypeTotal;
		return (currTypeTotal > 0 && beforeTypeTotal < currPageEnd && (currPageEnd-beforeRange) < pageSize);
	}

	/**
	 * 计算 分页
	 * @param currPageEnd
	 * @param beforeTypeTotal
	 * @param pageSize
	 * @return
	 */
	private static SqlPage calculatePage(int currPageEnd, int beforeTypeTotal, int pageSize){
		int remain = currPageEnd - beforeTypeTotal;//剩余
		int pageNo = remain/pageSize, append = remain%pageSize;
		if(remain < pageSize){
			return new SqlPage(pageNo, append);
		}else{
			return new SqlPage(((pageNo-1)*pageSize)+append, pageSize);
		}
	}
	
	private static List<String> findList(SqlPage sqlPage, String type){
		System.out.println(type+" query limit:"+sqlPage.getLimit()+", "+sqlPage.getOffset());
		List<String> result = new ArrayList<String>();
		int start = sqlPage.getLimit();
		for (int i = 0; i < sqlPage.getOffset(); i++) {
			result.add(type+"-"+start);
			start++;
		}
		return result;
	}
	
	
	static class SqlPage{
		
		private int limit;
		private int offset;
		
		public SqlPage(int limit, int offset){
			this.limit = limit;
			this.offset = offset;
		}
		
		public int getLimit() {
			return limit;
		}
		public void setLimit(int limit) {
			this.limit = limit;
		}
		public int getOffset() {
			return offset;
		}
		public void setOffset(int offset) {
			this.offset = offset;
		}
	}
	
	
	static class Page<T> {

		public static final String ASC = "asc";
		public static final String DESC = "desc";

		protected int pageNo = 1;
		protected int pageSize = 1;
		protected String orderBy = null;
		protected String order = null;
		protected boolean autoCount = true;

		protected List<T> result = Collections.emptyList();
		protected long totalCount = 0;

		public Page() {
		}

		public Page(int pageSize) {
			this.pageSize = pageSize;
		}
		
		public Page(int pageSize,int pageNo) {
			this.pageSize = pageSize;
			this.pageNo = pageNo;
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
			}
		}

		public Page<T> pageSize(final int thePageSize) {
			setPageSize(thePageSize);
			return this;
		}

		public int getFirstResult() {
			return (pageNo - 1) * pageSize;
		}

		public String getOrderBy() {
			return orderBy;
		}

		public void setOrderBy(final String orderBy) {
			this.orderBy = orderBy;
		}

		public Page<T> orderBy(final String theOrderBy) {
			setOrderBy(theOrderBy);
			return this;
		}

		public String getOrder() {
			return order;
		}

		
		public void setOrder(final String order) {
			String[] orders = order.toLowerCase().split(",");
			for (String orderStr : orders) {
				if (!DESC.equals(orderStr) || !ASC.equals(orderStr))
					throw new IllegalArgumentException("order " + orderStr + " illegal");
			}

			this.order = order.toLowerCase();
		}

		public Page<T> order(final String theOrder) {
			setOrder(theOrder);
			return this;
		}

		public boolean isOrderBySetted() {
			return (orderBy!=null && !orderBy.trim().isEmpty()) && (order!=null && !order.trim().isEmpty());
		}

		public boolean isAutoCount() {
			return autoCount;
		}

		public void setAutoCount(final boolean autoCount) {
			this.autoCount = autoCount;
		}

		public Page<T> autoCount(final boolean theAutoCount) {
			setAutoCount(theAutoCount);
			return this;
		}

		public List<T> getResult() {
			return result;
		}

		
		public void setResult(final List<T> result) {
			this.result = result;
		}
		
		public long getTotalCount() {
			return totalCount;
		}

		
		public void setTotalCount(final long totalCount) {
			this.totalCount = totalCount;
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
}
