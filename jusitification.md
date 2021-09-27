
### Models
Following the game rules, the models corresponding to the real-world objects are `Board`, `Cell`, `Block/Dome`, `Player` and `Worker`. Their associations are listed as following:
 - 1 `Board`-> (has 5*5) `Cell`
 - 1 `Cell` -> (has 0/1/2/3, 0/1) `Block/Dome`
 - 1 `Player` -> (has 2 ) `Worker`
 - 1 `Worker` -> (occupies 1) `Cell`  

In order to make connections between `Board`, `Player` and  `Worker` and also to keep the information and status of players, a `Game` model would be helpful here. Then, 2 more associations added as:
 - 1 `Game` -> (has 2 ) `Player`
 - 1 `Game` -> (has 1) `Board`

Since the actions that are related to `Block/Dome` are both "Building", this rule can be extracted from other rules and keep its logic inside a `Tower` model. So, six models are sufficient to represent the whole Santorini game. In addition, their attributes are straightforward, the logic is stated as below:
- `Game`: `phase` records the game status, which should be `PREPARING, RUNNING, DONE`
- `Board`: `numOfRows` & `numOfCols` specify the size the board
- `Cell`: `x` & `y` record its geographic posisiton, and `isOccupied` checks if it contains a `Worker` or a `Dome`
- `Tower`: `level` records its current height, `hasDome` indicates if it's completed and `top` specifies the max level it can have
- `Player`: `name` distinguishes 2 players and `isWinner` checks if this player wins the game
- `Worker`: `type` distinguishes 2 workers a player has

### Design Pattern
From the above logic, neither `parent -> child` relations nor overlapped logic exist among these models and associaitions. Besides, each model has their own inner states to keep track of and also has to interact with others.Thus, `Module Pattern` could be a good choice to design this game. Almost all attributes mentioned above should be labeled as `private` and each module has to expose some `public` methods for the interactions.

### Logic and Data Flow
In order to effectively manipulate the interactions between each modules, some conceputal classes are introduced - `Action`, `GameController` and `ActionController`.
 > This `Action` class is highly dependent on the designed structure of mock actions (for testing purpose), whose implementation will change when a real GUI interface is integrated, but the idea behind it keeps the same.

Mock actions generate a list of `Action` instances which are sent to the `GameController` to setup game and mock rounds. Obviously, `GameController` should control the overall game status, players status and board information. And `ActionController` should focus on that 2 specific actions -- "Move" and "Build". So, one possible design of data flow could be:
- `GameController`: has 3 phases to control, which are corresponding to the `phase` in `Game`
  - `initGame`: with a list of setup `Action` objects; initialize 2 players and all other modules; `phase` starts as `PREPARING`
  - `pickStartingPositions`: each player pick starting positions for his 2 workers; `phase` changes to `RUNNING`
  - `runGame`: players take turns to make worker move and build with the control of `ActionController`; once winner generates, `phase` changes to `DONE`
- `ActionController`: `chooseMove` & `chooseBuild` 
  - the validation of each action is checked
  - control is taken over back and forth between itself and `GameController`   
    - after "Move" action to check if a winner appears 
    - also after "Build" action to let players take turns
---
> In this hw3, I also write a `MockGameLoader`, which copies the code from `hw1&2` and do a little change, for the purpose of mimicing an end-to-end Santorini and also making testing easier. (No need to invoke a whole bunch of methods repeatedly)
