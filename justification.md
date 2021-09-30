### Models

Following the game rules, the models corresponding to the real-world objects are `Board`, `Cell`, `Block/Dome`, `Player` and `Worker`. Their associations are listed as following:

- 1 `Board`-> (has 5\*5) `Cell`
- 1 `Cell` -> (has 0/1/2/3, 0/1) `Block/Dome`
- 1 `Player` -> (has 2 ) `Worker`
- 1 `Worker` -> (occupies 1) `Cell`

Basically, almost all models above follow the "has-a" modeling technique. With the composition design principle, it is easy to add/change multiple behaviours with dependency injection (/using getters and setters) and also reduce different models coupling.

In order to make connections between `Board`, `Player` and `Worker` and also to keep the information and status of players, a `Game` model would be helpful here. In fact, these logic could all be placed in `Board` class, but for the "single responsibility principle", the labor of each object should be divided into reasonable pieces. Then, 2 more associations are added:

- 1 `Game` -> (has 2 ) `Player`
- 1 `Game` -> (has 1) `Board`

Since the actions that are related to `Block/Dome` are both "Building", this rule can be extracted from other rules and keep its logic inside a `Tower` model. And there is a "trade-off" where all logic in the `Tower` can be directly put in `Cell` class, so the dependencies call will be shorter (e.g. `board.getCell().getTower().addLevel()` in my code, which looks not so good). But again, `Tower` should have a separate class to maintain all the states either for possible future reuse or high cohesion.

So, six models are sufficient to represent the whole Santorini game. In addition, their responsibilities are straightforward, the logic is stated as below:

- `Game`: `phase` records the game status, which should be `PREPARING, RUNNING, DONE`; stored `currentPlayer`; `hasWinner` checks if a winner is generated
- `Board`: `numOfRows` & `numOfCols` specify the size of the board
- `Cell`: `x` & `y` record its geographic position, and `isOccupied` checks if it contains a `Worker` or a `Dome`
- `Tower`: `level` records its current height, `hasDome` indicates if it's completed and `top` specifies the max level it can have
- `Player`: `name` distinguishes 2 players and `isWinner` checks if this player wins the game
- `Worker`: `type` distinguishes 2 workers a player has; stores its current and previous location in `curPosition & prePosition`
  - `getMovableCells() & getBuildCells()` give the possible move and build actions that are valid

### Design Pattern

From the above logic, neither `is-a` relation nor overlapped logic exists among these models and associaitions. Besides, each model has their own inner states to keep track of and also has to interact with others.Thus, `Module Pattern` could be a good choice to design this game. Almost all attributes mentioned above should be labeled as `private` and each module has to expose some `public` methods for the interactions.

### Logic and Data Flow

For the purpose of maintainning low coupling and high cohesion, some conceputal classes are introduced - `GameController` and `ActionController`. Obviously, `GameController` should control the overall game status, players status and board information. And `ActionController` should focus on that 2 specific actions -- "Move" and "Build". With `Controllers`, future user interface and core logic will be decoupled from each other. Although they are coupled to the controllers now, controllers could be served as mediators for the future reuse. In this way, this kind of coupling is less harmful.

So, one possible design of data flow could be:

- `GameController`: has 3 phases to control, which are corresponding to the `phase` in `Game`
  - `initGame`: initialize 2 players and all other modules; `phase` starts as `PREPARING`
  - `pickStartingPosition`: each player pick starting positions for 2 workers
  - `readyGo`: check if all previous steps are correctly, if yes, then set `phase` to `RUNNING`
  - `hitRound`: players take turns to make worker move and build with the control of `ActionController`; once winner generates, `phase` changes to `DONE`
- `ActionController`: `chooseMove` & `chooseBuild`
  - the validation of each action is checked
  - control is taken over back and forth between itself and `GameController`
    - after "Move" action to check if a winner appears
    - also after "Build" action to let players take turns

---

> In this hw3, I also write a `MockGameLoader`, which copies the code from `hw1&2` and do a little change, for the purpose of testing. (No need to invoke a whole bunch of methods repeatedly). And the `Action` class is highly dependent on the designed structure of mock actions (for testing purpose), whose implementation will change when a real GUI interface is integrated. Also, I follow the writeup of HW2 and do not write tests for `Loader` and a series of getters in `Action`.
