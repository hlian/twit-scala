{PropTypes} = React = require 'react'
e = require 'react-e'


class Icon extends React.Component

  @propTypes:
    name: PropTypes.string.isRequired


  componentDidMount: ->
    @mounted = yes
    @_load @props.name


  componentWillReceiveProps: (newProps) ->
    @_load newProps.name


  componentWillUnmount: ->
    @mounted = no


  render: ->
    if @state?.LoadedGlyph?
      React.createElement @state.LoadedGlyph, className: 'icon'
    else
      e 'svg.root', className: 'icon'


  _load: (name) ->
    require.ensure [], =>
      return unless @mounted
      @setState LoadedGlyph: require "!babel?presets[]=react!svg-jsx!../../assets/icons/#{name}.svg"



module.exports = Icon
