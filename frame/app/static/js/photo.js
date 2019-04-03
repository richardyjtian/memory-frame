var photoNameList = [];
var curr_photo_ind;
var photo_frame = document.getElementById('photo-frame');

/**
 * the img resize to fit the screen everytime the photo is changed
 */
photo_frame.addEventListener('load', function resize() {
	let isLandScape = (photo_frame.width / photo_frame.height) >= (window.innerWidth / window.innerHeight);

	if (isLandScape) {
		photo_frame.setAttribute('width', window.innerWidth);
		let margin = (window.innerHeight - photo_frame.height) / 2;
		photo_frame.style.margin = `${margin}px 0px ${margin}px 0px`;
	} else {
		photo_frame.setAttribute('height', window.innerHeight);
		let margin = (window.innerWidth - photo_frame.width) / 2;
		photo_frame.style.margin = `0px ${margin}px 0px ${margin}px`;
	}
});

/**
 * @param {int} max
 * @return: a random value between 0 ~ max (exclusive)
 */
function getRandomInt(max) {
	return Math.floor(Math.random() * Math.floor(max));
}

/**
 * @param {String} imageUrl
 */
function switch_photo(imageUrl) {
	photo_frame.setAttribute('src', imageUrl);
}

/**
 * switch the photo to another one in the pool
 */
export function nextPhoto() {
	switch_photo(photoNameList[curr_photo_ind]);
	curr_photo_ind = (curr_photo_ind + 1) % photoNameList.length;
}

/**
 * 1) update the current pool of photo URLs with the new one
 * 2) switches the current photo to a random one from the new pool
 * @param {String[]} imageUrlList
 */
export function update_photo_pool(imageUrlList) {
	// no new photos to switch to
	// ideally if no photo is found this event shouldn't have been triggered
	// by the back in the first place, but do this check to be safe
	if (imageUrlList.length == 0) return;
	curr_photo_ind = 0;
	photoNameList = imageUrlList;
}
