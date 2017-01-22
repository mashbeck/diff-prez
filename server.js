var mysql = require('mysql');
var express = require('express');
var bodyparser = require('body-parser');
var app = express();

/*var Oconnection = mysql.createConnection({
host	: 'localhost',
user	: 'root',
password : '',
database : 'ObamaGov',
});

var Tconnection = mysql.createConnection({
host	: 'localhost',
user	: 'root',
password : '',
database : 'TrumpGov',
});

Oconnection.connect();
Tconnection.connect(); */

var array = ["result1", "result2", "result3", "result4"];
app.set('view engine', 'ejs');

app.use(bodyparser.urlencoded({ extended: true }));
app.use(express.static('site'));

app.post('/search', function(req, res){
	/*	var query = req.body.searchbox.split(" ");
		var trumpquery = "";
		var obamaquery = "";
		var trumpids =  [];
		var obamaids = [];
		var trumpURLq = "";
		var obamaURLq = "";
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

		Tconnection.query(trumpquery, function(error, results, fields){
				for(var x = 0; x < results.length; x++){
				trumpids.push(results[x].id);			
				}
				for (var x = 0; x < trumpids.length; x++) {
				trumpURLq = trumpURLq + 'SELECT url, description FROM trump WHERE id=\'' + trumpids[x] + '\'';


				if (x != trumpids.length -1){
				trumpURLq = trumpURLq + ' INTERSECT ';
				}
				}
				Tconnection.query(trumpURLq, function(err, resu, fie){
						for (var x = 0 ; x < resu.length; x++){
						trumpURLS.push(resu[x]);
						trumpdesc.push(resu[x]);	
						}	
						});

				});

		Oconnection.query(obamaquery, function(error, results, fields){
				for(var x = 0; x < results.length; x++){
				obamaids.push(results[x].id);			
				}
				for (var x = 0; x < obamaids.length; x++) {
				obamaURLq = obamaURLq + 'SELECT url, description FROM obama WHERE id=\'' + obamaids[x] + '\'';

				if (x != obamaids.length -1){
				obamaURLq = obamaURLq + ' INTERSECT ';
				}
				}
				Oconnection.query(obamaURLq, function(err, resu, fie){
						for (var x = 0 ; x < resu.length; x++){
						obamaURLS.push(resu[x]);
						obamadesc.push(resu[x]);	
						}	
						});

				});*/

		res.render('results' , {searchquery: req.body.searchbox, results: array});
});

var server = app.listen(3000, function() {
		console.log('Running server on port 3000');
		});
