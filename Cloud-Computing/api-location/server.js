//REST API demo in Node.js
var express = require('express'); // require the express framework
var app = express();
var fs = require('fs'); //require file system object

// Endpoint to Get a list of locations
app.get('/', function(req, res){
    fs.readFile(__dirname + "/" + "locations.json", 'utf8', function(err, data){
        console.log(data);
        res.end(data); // you can also use res.send()
    });
})

app.get('/getLocations', function(req, res){
    fs.readFile(__dirname + "/" + "locations.json", 'utf8', function(err, data){
        console.log(data);
        res.end(data); // you can also use res.send()
    });
})

//Endpoint to get a location by id
app.get('/:id', function (req, res) {
    // First retrieve existing locations list
    fs.readFile( __dirname + "/" + "locations.json", 'utf8', function (err, data) {
       var locations = JSON.parse( data );
       var location = locations[req.params.id] 
       console.log( location );
       res.end( JSON.stringify(location));
    });
 })

app.get('/getLocations/:id', function (req, res) {
    // First retrieve existing locations list
    fs.readFile( __dirname + "/" + "locations.json", 'utf8', function (err, data) {
       var locations = JSON.parse( data );
       var location = locations[req.params.id] 
       console.log( location );
       res.end( JSON.stringify(location));
    });
 })

// Create a server to listen at port 8080
var server = app.listen(8080, function(){
    var host = server.address().address
    var port = server.address().port
    console.log("Location API app is running and listening at http://[EXTERNAL-IP]:%s", port)
})