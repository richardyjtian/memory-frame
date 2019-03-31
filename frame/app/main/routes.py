from flask import Flask, render_template
from . import main

@main.route('/')
def frame():
	return render_template('/index.html');
