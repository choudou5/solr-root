package com.choudoufu.solr.util;

import java.util.ArrayList;

/**
 * ArrayList扩展类
 * @author xuhaowen
 * @date 2017年2月23日
 */
public class ArrayListExt<T> extends ArrayList<T>{

	private static final long serialVersionUID = 2824260859288681800L;

	public ArrayListExt(int initialCapacity) {
		super(initialCapacity);
	}
	
	public String[] toArrayStr(){
		return this.toArray(new String[size()]);
	}
}
