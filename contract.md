### Behavioral Contract

<table>
     <tr>
         <td><strong>Operation:</strong></td>
         <td>roundMove(movePos)</td>
     </tr>
     <tr >
         <td><strong>Preconditions:</strong></td>
         <td>
             <ul>
                <li>Game is running and winner does not determined yet. </li>
                <li>Worker's current space and all neighboring spaces checked cannot beyond the board border.</li>
                <li>At least one of the following conditions is matched:
                    1. At least 1 unoccupied neighboring space whose height is not 2 level higher than the current worker's height exists.
                    <br/>
                    2. At least 1 space is occupied by the opponent's worker and he can be further pushed to an unoccupied and valid space along the pushing direction
                </li>
              </ul>
         </td>
     </tr>
     <tr>
         <td><strong>Postconditions:</strong></td>
         <td>
            <ul>
                <li>Worker's current space is set to occupied and if he wins is checked.</li>
                <li>Worker's neighboring spaces are updated.</li>
                <li>Worker's previous space is set to unoccupied.</li>
                <li>Opponent worker's current space is set to occupied if he gets pushed.</li>
                <li>Opponent worker's neighboring spaces are updated if he gets pushed.</li>
            </ul>
         </td>
     </tr>
</table>
