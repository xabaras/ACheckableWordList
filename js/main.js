$(document).ready(function() {
	jQuery.get('http://jsonp.afeld.me/?url=https://raw.githubusercontent.com/xabaras/ACheckableWordList/master/README.md', function(data) {
	   //process text file line by line
	   var converter = new showdown.Converter(),
	   	   html = converter.makeHtml(data);
	   	   
   	   	   $('#page').html(html);
	});
});
