var express = require('express');
var bodyparser = require('body-parser');
var app = express();

app.use(bodyparser.urlencoded({ extended: true }));
app.use(express.static('site'));

app.post('/search', function(req, res){
	res.send('You searched for "' + req.body.searchbox + '"');
});

var server = app.listen(3000, function() {
	console.log('Running server on port 3000');
});
