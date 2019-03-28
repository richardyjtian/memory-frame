var photoNameList = [];

// connect socket
var socket = io.connect('http://' + document.domain + ':' + location.port);

// reference: mozilla Math.random() page
// @return: a random value between 0 ~ max (exclusive)
function getRandomInt(max) {
	return Math.floor(Math.random() * Math.floor(max));
}

function update_photo_pool(imageNameList) {
	// no new photos to switch to
	// ideally if no photo is found this event shouldn't have been triggered
	// by the back in the first place, but do this check to be safe
	if (imageNameList.length == 0) return;

	photoNameList = imageNameList;

	// 1) parse event for img name
	let nextPhoto = photoNameList[getRandomInt(photoNameList.length)];
	let photo = document.getElementById('photo-frame');
	let image_uri = photo.getAttribute('src');
	image_uri = image_uri.substring(0, image_uri.lastIndexOf('/'));

	// 2) replace the old img with the new img
	photo.setAttribute('src', `${image_uri}/${nextPhoto}`);
}

socket.on('connect', function() {
	console.log('connect established');
});

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
