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
	res.render('results' , {searchquery: req.body.searchbox, results: array});
});

var server = app.listen(3000, function() {
	console.log('Running server on port 3000');
});
