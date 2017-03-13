<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<input id="analyzerIndex" type="hidden" name="fields[0].type.tokenizerClass" value=""/>
<input id="analyzerIndexUseSmart" type="hidden" name="fields[0].type.index.useSmart" value=""/>
<input id="analyzerIndexCode" type="hidden" name="fields[0].type.index.code" value=""/>
<input id="analyzerIndexSeparator" type="hidden" name="fields[0].type.index.separator" value=""/>
<input id="analyzerIndexSqlGroupSymbol" type="hidden" name="fields[0].type.index.sqlGroupSymbol" value=""/>
<input id="analyzerIndexFilters" type="hidden" name="fields[0].type.index.filters" value=""/>

<input id="analyzerQueryUseSmart" type="hidden" name="fields[0].type.query.useSmart" value=""/>
<input id="analyzerQueryCode" type="hidden" name="fields[0].type.query.code" value=""/>
<input id="analyzerQuerySeparator" type="hidden" name="fields[0].type.query.separator" value=""/>
<input id="analyzerQuerySqlGroupSymbol" type="hidden" name="fields[0].type.query.sqlGroupSymbol" value=""/>
<input id="analyzerQueryFilters" type="hidden" name="fields[0].type.query.filters" value=""/>

<a href="javascript:chooseAnalyzerModal(0)">&nbsp;<i class="icon icon-plus"></i>&nbsp;</a>
<span id="analyzerTag" class="badge badge-info"></span>