import { update_photo_pool, nextPhoto } from './photo.js';

// connect socket
var socket = io.connect('http://' + document.domain + ':' + location.port);
var switch_photo_button = document.getElementById('photo-switcher');
var label_input = document.getElementById('label-input');
var label_submit = document.getElementById('label-submit');

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
	console.log('updating photos:', data);
	update_photo_pool(JSON.parse(data));
	nextPhoto();
});

socket.on('test', function(message) {
	console.log(message);
});


label_submit.addEventListener('click', function () {
	socket.emit('filter_photos', label_input.value);
});

socket.on('power', function(status) {
	if (status == 'on') {
		screen.setAttribute('class', status);
		nextPhoto();
	} else if (status == 'off') {
		screen.setAttribute('class', status);
	}
})

socket.on('next_photo', nextPhoto);
