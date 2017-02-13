/* custom plug
 * @author: xuhaowen
 * @date 2017/01/07
 */
(function($) {
	
	 /**解决 jquery-1.4.2之后 没有 handleError函数 */
	 jQuery.extend({
		handleError: function (s, xhr, status, e) {
			if (s.error) {
				s.error.call(s.context || s, xhr, status, e);
			}
			if (s.global) {
				(s.context ? jQuery(s.context) : jQuery.event).trigger("ajaxError", [xhr, s, e]);
			}
		}
	 });
				

	/** 公告提示框 */
	$.notice = {
		show: function(message, options) {
			this.options = {
				top: 5,
				left: 25,
				fadeout: 1000,      //ms   
				animateout: 2000,   //ms
				removeout: 3000,	//ms
			}
			
			// Merge options
			this.options = jQuery.extend(true, this.options, message, options);
			
			$('html, body').animate({scrollTop:0});
			
			$('<div></div>').attr('id', 'notice')
			.css('left', (this.options.left)+'%')
			.css('top', (0+this.options.top)+'px')
			.appendTo('body').text(message);
			
			/** Switch off the notification */
			setTimeout(function() {$('#notice').animate({ opacity: 0, top: '-20px' }, this.options.fadeout);}, this.options.animateout);
			setTimeout(function() {$('#notice').remove();}, this.options.removeout);
		}
	}
	jQnotice = function(message, options) {$.notice.show(message, jQuery.extend(options));};

})(jQuery);

/**
 * endWith
 */
String.prototype.endWith=function(s){
  if(s==null||s==""||this.length==0||s.length>this.length)
     return false;
  if(this.substring(this.length-s.length)==s)
     return true;
  else
     return false;
  return true;
}

/**
 * startWith
 */
String.prototype.startWith=function(s){
  if(s==null||s==""||this.length==0||s.length>this.length)
   return false;
  if(this.substr(0,s.length)==s)
     return true;
  else
     return false;
  return true;
 }