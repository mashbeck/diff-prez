var express = require('express');
var app = express();

app.use(express.static('site'));

var server = app.listen(3000, function() {
	console.log('Running server on port 3000');
});
