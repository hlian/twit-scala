camelCase = require 'camel-case'


Services = [
  require './todos_service'
]


module.exports = (dependencies) ->
  services = {}
  for Service in Services
    service = new Service dependencies
    service.services = services
    services[camelCase Service.name] = service
  services
