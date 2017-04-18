package com.choudoufu.solr.util;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;

import com.choudoufu.solr.common.vo.Select2Vo;
import com.choudoufu.solr.schema.entity.Schema;
import com.choudoufu.solr.service.SchemaService;

public class SchemaUtil {

	/**
	 * 获取 Schema 下拉框vos
	 * @param req
	 * @return
	 */
	public static List<Select2Vo> getSchemaSelect2Vos(HttpServletRequest req){
		List<Schema> schemas = SchemaService.listSchemas(req);
		if(CollectionUtils.isNotEmpty(schemas)){
			List<Select2Vo> vos = new ArrayListExt<Select2Vo>(schemas.size());
			vos.add(new Select2Vo("plug_sensitive_word", "敏感词"));
			for (Schema schema : schemas) {
				vos.add(new Select2Vo(schema.getName(), schema.getTitle()));
			}
			return vos;
		}
		return null;
	}
}
