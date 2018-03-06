# Work in Progress
## Tasks currently being worked on for the next release

* [X] Better debugging support
    * [X] On-going stats gathering `RenderStats` used by `Renderer`
    * [X] FPS-style display support with information about averages, counts, timings, etc.
* [ ] Integration of Hypertext and Canvas into hybrid display paradigm
    * [ ] Removal of `Renderer` entirely and each canvas component has its own `html.Canvas` instance
    * [ ] Integration of `z-index` as `depth` value
    * [ ] Proper management of each component to position and size via HTML properties
    * [ ] Migration of `hyptertext` module into `ui` with proper distinction while allowing good integration
    * [ ] Core `Widget`, `Component`, `CanvasComponent`, `HTMLComponent`, `AbstractContainer`, etc.
    * [ ] `TextView` with `HTML` and `Canvas` implementations
    * [ ] `Font` with support for use in `HTML` and `Canvas` seamlessly
    * [ ] `ImageView` with `HTML` and `Canvas` implementations
    * [ ] `VideoView` with `HTML` and `Canvas` implementations?
    * [ ] `ExistingHTML` extends `HTMLComponent` for parsing HTML strings, existing HTML tags, etc.
* [X] Better text rendering performance with `CachedFont`
* [X] Support for "reload on ratio change"
* [ ] Update `UIExamples` to render using canvas and look a lot nicer
* [ ] Better Server error logging (URL, request info, etc.)
* [X] Support for fixed zoom ratio for fixed applications that should not zoom