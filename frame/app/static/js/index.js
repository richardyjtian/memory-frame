import { push_photo_queue_front, push_photo_queue_back, nextPhoto, photosInPool } from './photo.js';
import { turnScreenOn, turnScreenOff } from './screen.js';

// connect socket
var socket = io.connect('http://' + document.domain + ':' + location.port);
var switch_photo_button = document.getElementById('photo-switcher');
var label_input = document.getElementById('label-input');
var label_submit = document.getElementById('label-submit');

switch_photo_button.addEventListener('click', function () {
	nextPhoto();
	if (photosInPool() == 0) {
		socket.emit('fetch_all_photos');
	}
});
/**
 * initialization event that should be triggered on start up
 * @param {String} data: JSON array of the image names
 */
socket.on('initialize', function(data) {
	console.log('initialize data: ', data);
	push_photo_queue_back(JSON.parse(data));
	socket.emit('test_print', 'initialize success');
});

/**
 * handle event broadcasted by server whenever the photo should be switched
 * @param {String} data: JSON array of the image names
 */
socket.on('photo_switch', function(data) {
	console.log('updating photos:', data);
	push_photo_queue_front(JSON.parse(data));
	nextPhoto();
});

socket.on('update_photos', function(data) {
	console.log('updating photos with:', data);
	push_photo_queue_back(JSON.parse(data));
});

socket.on('test', function(message) {
	console.log(message);
});

label_submit.addEventListener('click', function () {
	socket.emit('filter_photos', label_input.value);
});

socket.on('power', function(status) {
	if (status == 'on') {
		turnScreenOn();
		nextPhoto();
	} else if (status == 'off') {
		turnScreenOff();
	}
})

socket.on('next_photo', function () {
	nextPhoto();
	if (photosInPool() == 0) {
		console.log('out of photos fetching more!')
		socket.emit('fetch_all_photos');
	}
});

socket.on('sleep', function(time) {
	turnScreenOff();
	setTimeout(function () {
		turnScreenOn();
	}, time * 1000);
});
