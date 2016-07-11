$(document).ready(function() {
	jQuery.get(' https://crossorigin.me/https://raw.githubusercontent.com/xabaras/ACheckableWordList/master/README.md', function(data) {
	   //process text file line by line
	   var converter = new showdown.Converter(),
	   	   html = converter.makeHtml(data);
	   	   
   	   	   $('#page').html(html);
	});
});
