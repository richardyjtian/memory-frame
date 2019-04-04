var photoNameList = [];
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
 * @param {String} imageUrl
 */
function switch_photo(imageUrl) {
	photo_frame.setAttribute('src', imageUrl);
}

export function photosInPool() {
	return photoNameList.length;
} 

/**
 * switch the photo to another one in the pool
 */
export function nextPhoto() {
	switch_photo(photoNameList.pop());
}

/**
 * 1) push a list of image urls to the front of the photo queue
 * @param {String[]} imageUrlList
 */
export function push_photo_queue_front(imageUrlList) {
	photoNameList.push(...imageUrlList);
}

export function push_photo_queue_back(imageUrlList) {
	photoNameList.unshift(...imageUrlList);
}