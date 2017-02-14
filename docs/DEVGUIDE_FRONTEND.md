(see [frontend/README.md](./frontend/README.md))





### Internationalization

Our app leverages [i18n-js](https://github.com/fnando/i18n-js) for internationalizing strings.  No copy or UI text
should be included directly within view templates - instead, all copy should be referenced via our i18n operator, `t`.

#### Bad

```coffee-script
class SomeComponent extends Component
  render: ->
    e '.some-visual-element', 'Hello world!'
```

Don't hard-code copy into the react view!

#### Good

Reference the copy via a key, scoped by the component name:

```coffee
class SomeComponent extends Component
  render: ->
    e '.some-visual-element', t 'someComponent.visualElement'
```

And then, in `locales/en_us.coffee`, add the corresponding key to supply the text:

```coffee
module.exports =
  someComponent:
    visualElement: 'Hello world!'
```

#### Get Fancy

i18n-js has rich functionality.  When we need to interpolate values in strings, make sure to leverage this
functionality within i18n itself, and don't do it manually.  Languages are complex, and word order in one language can
be completely different than another.

You can interpolate values in translated strings like this:
```coffee
# index.coffee
class SomeComponent extends Component
  render: ->
    e '.some-visual-element',
      t 'someComponent.greeting', name: @props.fullName

# locales/en.coffee
module.exports =
  someComponent:
    greeting: 'Hi {{name}}, how are you?'
```

Or handle sophisticated pluralization.  For english, you can include the special key of `count` with your translation
call, and leverage special sub-keys of `zero`, `one`, or `other` in the locale file:
```coffee
# index.coffee
class SomeComponent extends Component
  render: ->
    e '.some-visual-element',
      t 'someComponent.greeting',
        count: @props.parallelDimensions

# locales/en_us.coffee
module.exports =
  someComponent:
    greeting:
      one: 'Hello world!'
      other: 'Hello, {{count}} worlds!'
      zero: 'Hello nihilist!'
```
