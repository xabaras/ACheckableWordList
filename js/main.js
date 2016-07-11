$(document).ready(function() {
	jQuery.get('https://github.com/xabaras/ACheckableWordList/blob/master/README.md', function(data) {
	   //process text file line by line
	   var converter = new showdown.Converter(),
	   	   html = converter.makeHtml(data);
	   	   
   	   	   alert(html);
	});
	
});
