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

app.use(bodyparser.urlencoded({ extended: true }));
app.use(express.static('site'));

app.post('/search', function(req, res){
	res.send('You searched for "' + req.body.searchbox + '"');
});

var server = app.listen(3000, function() {
	console.log('Running server on port 3000');
});
