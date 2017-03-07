/**
 * 调用
 * var buffer = new StringBuffer();
 *	buffer.append("Hello ").append("javascript");
 *	alert(buffer.toString(); 
 */
/**
 *字符串 拼接
 * */
function StringBuffer() {
    this.__strings__ = new Array();
}
StringBuffer.prototype.append = function (str) {
    this.__strings__.push(str);
    return this;    //方便链式操作
}
StringBuffer.prototype.toString = function () {
    return this.__strings__.join("");
}
//清除
StringBuffer.prototype.clear = function () {
    this.__strings__ = new Array();
    return this;
}

/*扩展　String 原生方法*/
String.prototype.startWith=function(str){     
  var reg=new RegExp("^"+str);     
  return reg.test(this);        
}  
String.prototype.endWith=function(str){     
  var reg=new RegExp(str+"$");     
  return reg.test(this);        
}
String.prototype.replaceAll = function(s1, s2){ 
	return this.replace(new RegExp(s1,"gm"), s2); 
}