<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<input id="analyzer_tokenizerClass" type="hidden" name="fields[0].type.tokenizerClass" value=""/>
<input id="analyzer_index_useSmart" type="hidden" name="fields[0].type.index.useSmart" value=""/>
<input id="analyzer_index_code" type="hidden" name="fields[0].type.index.code" value=""/>
<input id="analyzer_index_separator" type="hidden" name="fields[0].type.index.separator" value=""/>
<input id="analyzer_index_sqlGroupSymbol" type="hidden" name="fields[0].type.index.sqlGroupSymbol" value=""/>
<input id="analyzer_index_filters" type="hidden" name="fields[0].type.index.filters" value=""/>

<input id="analyzer_query_useSmart" type="hidden" name="fields[0].type.query.useSmart" value=""/>
<input id="analyzer_query_code" type="hidden" name="fields[0].type.query.code" value=""/>
<input id="analyzer_query_separator" type="hidden" name="fields[0].type.query.separator" value=""/>
<input id="analyzer_query_sqlGroupSymbol" type="hidden" name="fields[0].type.query.sqlGroupSymbol" value=""/>
<input id="analyzer_query_filters" type="hidden" name="fields[0].type.query.filters" value=""/>

<a id="chooseAnalyzerBtn" href="javascript:chooseAnalyzerModal(0)" class="mgl-10">&nbsp;<i class="icon icon-plus"></i>&nbsp;</a>
<span id="analyzerTag" class="mgl-10 label label-success"></span>