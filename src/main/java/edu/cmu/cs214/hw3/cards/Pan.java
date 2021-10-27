package edu.cmu.cs214.hw3.cards;

import edu.cmu.cs214.hw3.models.Game;
import edu.cmu.cs214.hw3.models.Cell;
import edu.cmu.cs214.hw3.models.Worker;

public class Pan extends God {


    public void doMove(Worker worker, Cell moveTo, Game game) {
        Cell oldPosition = worker.getCurPosition();
        super.doMove(worker, moveTo, game);
        if (oldPosition.getHeight() - moveTo.getHeight() >= 2 ) {
            worker.getPlayer().setIsWinner();
        }
    }
}
