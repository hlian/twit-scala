Module = require 'module'

originalLoader = Module._load
Module._load = (request, parent, isMain) ->
  if request[0] is '.' and request.match /\.styl/
    {}
  else
    originalLoader.call this, request, parent, isMain
