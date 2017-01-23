var mysql = require('mysql');
var express = require('express');
var bodyparser = require('body-parser');
var PropertiesReader = require('properties-reader');
//var properties = PropertiesReader('credentials.properties');
var app = express();

/*var Oconnection = mysql.createConnection({
host	: properties.get('host'),
user	: properties.get('user'),
password : properties.get('password'),
database : 'ObamaGov',
});

var Tconnection = mysql.createConnection({
host	: properties.get('host'),
user	: properties.get('user'),
password : properties.get('password'),
database : 'TrumpGov',
});

Oconnection.connect();
Tconnection.connect();  */

var trumpURLS = ["www.google.com", "www.whitehouse.gov", "www.reddit.com", "www.facebook.com"];
var obamaURLS = ["www.yahoo.com", "www.twitter.com", "www.4chan.org", "www.amazon.com"];
var obamadesc = ["hello world", "ya ya ya", "some text here", "blah blah blah"];
var trumpdesc = ["This is a description", "result2", "result3", "result4"];
app.set('view engine', 'ejs');

app.use(bodyparser.urlencoded({ extended: true }));
app.use(express.static('site'));

app.post('/search', function(req, res){
	/*	var query = req.body.searchbox.split(" ");
		var trumpquery = "";
		var obamaquery = "";
		var trumpids =  [];
		var obamaids = [];
		var trumpURLS = [];
		var obamaURLS = [];
		var trumpdesc = [];
		var obamadesc = [];
		for(var x = 0; x < query.length; x++){
		trumpquery = trumpquery + 'SELECT id from trumpWords WHERE word=\'' + query[x] + '\'';
		obamaquery = obamaquery + 'SELECT id from obamaWords WHERE word=\'' + query[x] + '\'';	
		if (x != query.length -1){
		trumpquery = trumpquery + ' INTERSECT ';
		obamaquery = obamaquery + ' INTERSECT ';
		}

		}



		if (obamaquery !== ""){
			obamaquery = obamaquery + ';';
			console.log('Obamaquery: ' +  obamaquery);
			Oconnection.query(obamaquery, function(error, results, fields){
					if(error) throw error;
					for(var x = 0; x < results.length; x++){
					obamaids.push(results[x].id);			
					console.log(results[x].id);
					console.log('Pushing id to obamaids: ' +  results[x].id);

					}
					var oURLq = 'SELECT * from obama WHERE ';
					for (var x = 0; x < obamaids.length; x++) {
					oURLq = oURLq + 'id=\'' + obamaids[x] + '\'';
					if (x != obamaids.length -1 ){
					oURLq = oURLq + ' OR ';	
					}
					}

					if(oURLq !== 'SELECT * from obama WHERE ') {
					oURLq = oURLq + ';';
					console.log(oURLq);
					Oconnection.query(oURLq, function(error, results, fields){
							if(error) throw error;
							for (var x = 0 ; x < results.length; x++){
							obamaURLS.push(results[x].url);
							console.log('Pushing url to obamaURL: ' + results[x].url);
							obamadesc.push(results[x].description);	
							console.log('Pushing description to ObamaDesc: ' + results[x].description);
							}
							if (trumpquery !== ""){
							trumpquery = trumpquery + ';';
							console.log('trumpquery: ' +  trumpquery);
							Tconnection.query(trumpquery, function(error, results, fields){
									if(error) throw error;
									for(var x = 0; x < results.length; x++){
									trumpids.push(results[x].id);			
									console.log(results[x].id);
									console.log('Pushing id to trumpids: ' +  results[x].id);

									}
									var tURLq = 'SELECT * from trump WHERE ';
									for (var x = 0; x < trumpids.length; x++) {
									tURLq = tURLq + 'id=\'' + trumpids[x] + '\'';
									if (x != trumpids.length -1 ){
									tURLq = tURLq + ' OR ';	
									}
									}
									if(tURLq !== 'SELECT * from trump WHERE '){
									tURLq = tURLq + ';';
									console.log(tURLq);

									Tconnection.query(tURLq, function(error, results, fields){
											if(error) throw error;
											for (var x = 0 ; x < results.length; x++){
											trumpURLS.push(results[x].url);
											console.log('Pushing url to trumpURL: ' + results[x].url);
											trumpdesc.push(results[x].description);	
											console.log('Pushing description to trumpDesc: ' + results[x].description);
											}	
											});
									}


							});
							}	
					});
					}


					res.render('results' , {searchquery: req.body.searchbox, trumpurls: trumpURLS, obamaurls: obamaURLS, trumpdesc: trumpdesc, obamadesc: obamadesc});
			});
		} */

res.render('results' , {searchquery: req.body.searchbox, trumpurls: trumpURLS, obamaurls: obamaURLS, trumpdesc: trumpdesc, obamadesc: obamadesc});


});

var server = app.listen(3000, function() {
		console.log('Running server on port 3000');
		});
