var photoNameList = [];

// reference: mozilla Math.random() page
// @return: a random value between 0 ~ max (exclusive)
function getRandomInt(max) {
	return Math.floor(Math.random() * Math.floor(max));
}

function switch_photo(imageName) {
	let photo = document.getElementById('photo-frame');
	let image_uri = photo.getAttribute('src');
	image_uri = image_uri.substring(0, image_uri.lastIndexOf('/'));
	photo.setAttribute('src', `${image_uri}/${imageName}`);
}

export function nextPhoto() {
	switch_photo(photoNameList[getRandomInt(photoNameList.length)]);
}

export function update_photo_pool(imageNameList) {
	// no new photos to switch to
	// ideally if no photo is found this event shouldn't have been triggered
	// by the back in the first place, but do this check to be safe
	if (imageNameList.length == 0) return;

	photoNameList = imageNameList;
	switch_photo(photoNameList[getRandomInt(photoNameList.length)]);
}
