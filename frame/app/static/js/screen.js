var screen = document.getElementById('screen');

const ON = 'on';
const OFF = 'off';

export function turnScreenOn() {
    screen.setAttribute('class', ON);
}

export function turnScreenOff() {
    screen.setAttribute('class', OFF);
}
