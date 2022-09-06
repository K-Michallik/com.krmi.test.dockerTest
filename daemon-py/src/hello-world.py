#!/usr/bin/env python

import sys

from xmlrpc.server import SimpleXMLRPCServer
from socketserver import ThreadingMixIn

title = ""

def set_title(new_title):
	global title
	title = new_title
	return title

def get_title():
	tmp = ""
	if str(title):
		tmp = title
	else:
		tmp = "No title set"
	return tmp + " (Python)"

def get_message(name):
	if str(name):
		return "Hello " + str(name) + ", welcome to PolyScope!"
	else:
		return "No name set"

sys.stdout.write("Docker daemon started")
sys.stdout.flush()
sys.stderr.write("Docker daemon started")
sys.stderr.flush()

class MultithreadedSimpleXMLRPCServer(ThreadingMixIn, SimpleXMLRPCServer):
	pass

server = MultithreadedSimpleXMLRPCServer(("0.0.0.0", 40405))
server.RequestHandlerClass.protocol_version = "HTTP/1.1"
server.register_function(set_title, "set_title")
server.register_function(get_title, "get_title")
server.register_function(get_message, "get_message")
server.serve_forever()
