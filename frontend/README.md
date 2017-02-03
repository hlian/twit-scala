# React / Redux Todo App

### Setup
* `yarn install`
* Optional: install [Chrome React Developer Tools](https://chrome.google.com/webstore/detail/react-developer-tools/fmkadmapgofadopljbjfkapdkoienihi)

### Local Development
* `yarn dev`
* open your browser to `localhost:3000`

### Project Structure
```
└── src
    │
    ├── components # react components
    │
    ├── global_styles
    │
    ├── index.coffee # entry point for application
    │
    ├── index.pug
    │
    ├── services
    │
    └── store
        │
        ├-- actions
        │
        ├-- reducer
        │
        ├-- builder.coffee # dependency injection for testing
        │
        └-- index.coffee
```

* Container components should be inline with the component they wrap

  ```
  └── app
      ├── index.coffee     # presentational
      └── container.coffee # container
  ```

### Concepts
* Separation of [presentational and container components](http://redux.js.org/docs/basics/UsageWithReact.html#presentational-and-container-components)
* [CSS modules](https://github.com/css-modules/css-modules)

### Testing Guidelines
* Unit tests for redux on the store level
* Unit tests for react components with state
