import { update_photo_pool, nextPhoto } from './photo.js';

// connect socket
var socket = io.connect('http://' + document.domain + ':' + location.port);
var switch_photo_button = document.getElementById('photo-switcher');

switch_photo_button.addEventListener('click', nextPhoto);

/**
 * initialization event that should be triggered on start up
 * @param {String} data: JSON array of the image names
 */
socket.on('initialize', function(data) {
	console.log('initialize data: ', data);
	update_photo_pool(JSON.parse(data));
	socket.emit('test_print', 'initialize success');
});

/**
 * handle event broadcasted by server whenever the photo should be switched
 * @param {String} data: JSON array of the image names
 */
socket.on('photo_switch', function(data) {
	update_photo_pool(JSON.parse(data));
});

socket.on('test', function(message) {
	console.log(message);
});
