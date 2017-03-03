@fresh-browser

Feature: Ping

Scenario: Getting a pong response
  When I go to the ping endpoint
  Then I get a pong response
