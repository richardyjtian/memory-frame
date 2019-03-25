// connect socket
var socket = io.connect('http://' + document.domain + ':' + location.port);

socket.on('connect', function() {
	socket.emit('my event', {data: 'I\'m connected!'});
});

// handle event broadcasted by server whenever the photo should be switched
socket.on('photo_switch', function() {
	// 1) parse event for img name
	// 2) fetch img file from server
	// 3) replace the old img with the new img
});

socket.on('test', function(message) {
	console.log(message);
});
