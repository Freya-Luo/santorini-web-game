## Models

Following the game rules, the models corresponding to the real-world objects are `Board`, `Cell`, `Player` and `Worker`. Their associations are listed as follows:

- 1 `Board`-> (has 5\*5) `Cell`
- 1 `Player` -> (has 2 ) `Worker`
- 1 `Worker` -> (occupies 1) `Cell`

Almost all models above follow the "has-a" modeling technique. With the composition design principle, it is easy to add/change multiple behaviors with dependency injection (/using getters and setters) and also reduce different models coupling.


## Design Pattern

Each model has its own inner states to keep track of and also has to interact with others. Thus, `Module Pattern` should be used to design the base game models. Almost all attributes mentioned above should be labeled as `private` and each module has to expose some `public` methods for the interactions.  

To implement God cards, `Template Method Pattern` would be a good choice. God cards, from the real-world perspective, only change some behaviors of the worker's `move` and `build` actions (or change the game-winning rules) based on the game's core functionalities. With much duplicate logic, we can extra the core logic into an `<<abstract class>> God`, and revise each god's specific power in their subclasses.  
- Why use `Template Method Pattern`:
    - Concrete gods only override certain parts of the non-god (`Muggle`) game logic which would be less affected by changes happen to other parts of the logic
    - Design for reuse -- pull the massive duplicate code into a superclass
    - Design for understandability -- turn the monolithic non-god behaviors into a series of individual actions in which only some particular steps are extended/modified
    - Design for extensibility -- introduce new gods without having to change many contexts
- Downsides:
    - Some too specific methods in the subclasses need definition in the superclass, which could make it looks heavy



## Extensibility
#### **Gods**

In this game, all the gods do not alter too many things but just some certain behaviors with respect to either `move` or `build` action. The non-god logic `(Muggle.java)` is essentially followed/inherited by each god, and minor differences are implemented. For the future implementation of more gods, they could simply `extends` the abstract `God` class, inherit base methods and add some extra steps/modify the original methods if necessary.   

However, the downside of suppressing a default method vis subclass is not much obvious as each god follows the move and build core functionalities(e.g. `setCurPostion()`, `addLevel()`, `computeMovableCells()` and `computeBuildableCells()`...). So, using `Template Method Pattern` could be a reasonable choice for implementing god cards. Also, `Strategy Pattern` could be a candidate. However, with an `interface` instead of a `class`, much duplicate code has to be written in each subtype (concrete god class), which is not good for readability and extensibility.

#### **GUI**
The implementation of GUI and base game is separated by introducing a controller (`Game.java`) for low coupling and high cohesion between `View` and `Model`. In this way, the whole program is much easier to understand, which further leads to good evolvability -- both UI and core logic are easier to change. This also achieves the design goal of low representational gap. Although the code and dependencies look heavy in `Game`, it actually does not do much work itself, instead it delegates the work to other objects. In this game, `Game` is a relative small interface that served as a mediator, which is quite stable. With this decoupling, `UI` can be easily changed without knowing anything about the domain logic design. In the same way, changes made to the domain logic only affects the controller instead of `UI`.

Also, the application of controller supports reuse because it serves as an interface to the core logic and supports extensibility.