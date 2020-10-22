package comp1140.ass2.ai;

import comp1140.ass2.util.Pair;
import comp1140.ass2.util.Triple;
import comp1140.ass2.util.UnorderedPair;

import java.util.*;

import static comp1140.ass2.core.WakeTile.getOppositeExit;

/**
 * Board state.
 */
public class State {
    public Tile[][] board;
    public Set<Ship> ships;

    public Set<Wake> hand;
    public LinkedList<Wake> wakeDrawPile;
    public LinkedList<Daikaiju> daikaijuBoard;
    public LinkedList<Daikaiju> daikaijuDrawPile;

    public State(String[] stateStr, String hand, char shipColour) {
        this.board = new Tile[7][7];
        this.ships = new HashSet<>(8);

        this.hand = new HashSet<>(3);
        for (int i = 0; i < hand.length(); i += 2) {
            Wake t = new Wake(Integer.parseInt(hand.substring(i, i + 2)), 0);
            this.hand.add(t);
        }

        this.wakeDrawPile = new LinkedList<>();
        for (int i = 0; i <= 55; i++) {
            this.wakeDrawPile.add(new Wake(i, 0));
        }
        this.wakeDrawPile.removeAll(this.hand);

        this.daikaijuBoard = new LinkedList<>();
        this.daikaijuDrawPile = new LinkedList<>();
        for (Daikaiju.Col c : Daikaiju.Col.values()) {
            for (int i = 0; i <= 5; i++) {
                this.daikaijuDrawPile.add(new Daikaiju(c, i, 0));
            }
        }

        final String wakes = stateStr[0];
        final String daikaijus = stateStr[1];
        final String ships = stateStr[2];

        for (int i = 0; wakes != null && i < wakes.length(); i += 5) {
            String curr = wakes.substring(i, i + 5);
            Wake t = new Wake(Integer.parseInt(curr.substring(i, i + 2)), 0);
            this.wakeDrawPile.remove(t);

            int x = Character.getNumericValue(curr.charAt(2));
            int y = Character.getNumericValue(curr.charAt(3));
            t.rot = Character.getNumericValue(curr.charAt(4));
            this.board[x][y] = t;
        }
        for (int i = 0; i < daikaijus.length(); i += 5) {
            String curr = daikaijus.substring(i, i + 5);
            Daikaiju.Col c = curr.charAt(0) == 'B' ? Daikaiju.Col.B : Daikaiju.Col.G;
            Daikaiju t = new Daikaiju(c, Character.getNumericValue(curr.charAt(1)), 0);
            this.daikaijuDrawPile.remove(t);

            int x = Character.getNumericValue(curr.charAt(2));
            int y = Character.getNumericValue(curr.charAt(3));
            int rot = Character.getNumericValue(curr.charAt(4));
            this.addDaikaijuToBoard(new Triple<>(x, y, rot),t);
        }
        for (int i = 0; i < ships.length(); i += 4) {
            String curr = ships.substring(i, i + 4);
            char xC = curr.charAt(1);
            char yC = curr.charAt(2);
            int x = xC == 'e' ? -1 : Character.getNumericValue(xC);
            int y = yC == 'e' ? -1 : Character.getNumericValue(yC);
            int e = Character.getNumericValue(curr.charAt(3));

            this.ships.add(new Ship(curr.charAt(0), x, y, e, curr.charAt(0) == shipColour));
        }
    }

    /**
     * Deep clone this.
     *
     * @return Deep clone of this
     * @throws CloneNotSupportedException Object cannot be cloned
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        State clone = (State) super.clone();

        clone.board = (Tile[][]) deepClone(this.board);
        clone.ships = new HashSet<>(this.ships);
        clone.hand = new HashSet<>(this.hand);
        clone.wakeDrawPile = new LinkedList<>(this.wakeDrawPile);
        clone.daikaijuDrawPile = new LinkedList<>(this.daikaijuDrawPile);

        return clone;
    }

    /**
     * Deep clones a 2D array of Objects.
     *
     * @param m Matrix to clone
     * @return Deep clone of an Object
     */
    private static Object[][] deepClone(Object[][] m) {
        Object[][] clone = new Object[m.length][m[0].length];
        for (int i = 0; i < m.length; i++) {
            clone[i] = m[i].clone();
        }
        return clone;
    }

    /**
     * Place a wake tile.
     *
     * @param tile Tile to place
     * @param rot Rotation in which to place the tile
     * @return A pair indicating a new drawn wake tile, and the board state after the move
     * @throws InvalidMoveException Move is not valid
     * @throws CloneNotSupportedException State cannot be cloned
     * @throws GameWonException Game won through this move
     */
    public Pair<Wake, State> applyMove(Wake tile, int rot) throws InvalidMoveException,
        CloneNotSupportedException, GameWonException {
        State clone = (State) this.clone();

        Pair<Integer, Integer> tileDest = clone.myAdjacency();
        tile.rot = rot;
        clone.board[tileDest.a][tileDest.a] = tile;

        for (Ship s : clone.ships) {
            boolean move = false;
            switch (s.exitDir()) {
                case UP:
                    move = s.x == tileDest.a && s.y - 1 == tileDest.b;
                    break;
                case LEFT:
                    move = s.x - 1 == tileDest.a && s.y == tileDest.b;
                    break;
                case DOWN:
                    move = s.x == tileDest.a && s.y + 1 == tileDest.b;
                    break;
                case RIGHT:
                    move = s.x + 1 == tileDest.a && s.y == tileDest.b;
                    break;
                default:
                    break;
            }
            if (move) {
                clone.moveShip(s);
            }
        }

        Set<Triple<Integer, Integer, Integer>> landings = new HashSet<>(8);
        for (Ship s : clone.ships) {
            Triple<Integer, Integer, Integer> dest = new Triple<>(s.x, s.y, s.exit);
            if (landings.contains(dest)) {
                throw new InvalidMoveException();
            }
            landings.add(dest);
        }

        if (clone.ships.size() == 1) {
            throw new GameWonException();
        }

        Random rand = new Random();
        int wInt = rand.nextInt(clone.wakeDrawPile.size());
        Wake w = clone.wakeDrawPile.remove(wInt);

        return new Pair<>(w, clone);
    }

    /**
     * Compute the tile that my ship is facing.
     *
     * @return A pair indicating the coordinates of the adjacent tile
     */
    public Pair<Integer, Integer> myAdjacency() {
        Ship myShip = null;
        for (Ship s : this.ships) {
            if (s.me) {
                myShip = s;
                break;
            }
        }
        assert myShip != null;

        int x = myShip.x;
        int y = myShip.y;
        if (x == -1) {
            x = myShip.exit == 2 || myShip.exit == 3 ? 6 : 0;
        }
        else if (y == -1) {
            y = myShip.exit == 0 || myShip.exit == 1 ? 0 : 6;
        }
        else {
            switch (myShip.exit) {
                case 0:
                case 1:
                    y -= 1;
                    break;
                case 2:
                case 3:
                    x += 1;
                    break;
                case 4:
                case 5:
                    y += 1;
                    break;
                case 6:
                case 7:
                    x -= 1;
                    break;
                default:
                    break;
            }
        }

        return new Pair<>(x, y);
    }

    /**
     * Move a single ship until it is unable to do so.
     *
     * @param s Ship to move
     * @throws InvalidMoveException Move is not valid - propagated to applyMove()
     * @throws GameWonException Game won through this move - propagated to applyMove()
     */
    private void moveShip(Ship s) throws InvalidMoveException, GameWonException {
        final Triple<Integer, Integer, Integer> init = new Triple<>(s.x, s.y, s.exit);

        switch (s.exit) {
            case 0:
            case 1:
                s.y--;
                break;
            case 2:
            case 3:
                s.x++;
                break;
            case 4:
            case 5:
                s.y++;
                break;
            case 6:
            case 7:
                s.x--;
                break;
            default:
                break;
        }
        Wake landing = (Wake) this.board[s.x][s.y];

        UnorderedPair<Integer> path = null;
        for (UnorderedPair<Integer> p : landing.exits) {
            if (p.contains(getOppositeExit(s.exit))) {
                path = p;
            }
        }
        assert path != null;

        s.exit = path.getNot(s.exit);

        this.moveShipRecurse(s, init);
    }

    /**
     * Recursive helper for moveShip().
     *
     * @param s Ship to move
     * @param init Initial state to check against for endless recursion
     * @throws InvalidMoveException Move is not valid - propagated to applyMove()
     * @throws GameWonException Game won through this move - propagated to applyMove()
     */
    private void moveShipRecurse(Ship s, Triple<Integer, Integer, Integer> init)
        throws InvalidMoveException, GameWonException {
        if (s.equalsState(init)) {
            if (s.me) {
                throw new InvalidMoveException();
            }
            else {
                this.ships.remove(s);
            }
        }

        switch (s.exit) {
            case 0:
            case 1:
                s.y--;
                break;
            case 2:
            case 3:
                s.x++;
                break;
            case 4:
            case 5:
                s.y++;
                break;
            case 6:
            case 7:
                s.x--;
                break;
            default:
                break;
        }
        Wake landing = (Wake) this.board[s.x][s.y];

        UnorderedPair<Integer> path = null;
        for (UnorderedPair<Integer> p : landing.exits) {
            if (p.contains(getOppositeExit(s.exit))) {
                path = p;
            }
        }
        assert path != null;

        s.exit = path.getNot(s.exit);

        if (s.isAtEdge()) {
            if (s.me) {
                throw new InvalidMoveException();
            }
            else {
                this.ships.remove(s);
            }
        }

        Tile nextTile = null;
        switch (s.exitDir()) {
            case UP:
                nextTile = this.board[s.x][s.y - 1];
                break;
            case LEFT:
                nextTile = this.board[s.x - 1][s.y];
                break;
            case DOWN:
                nextTile = this.board[s.x][s.y + 1];
                break;
            case RIGHT:
                nextTile = this.board[s.x + 1][s.y];
                break;
            default:
                break;
        }

        if (nextTile instanceof Daikaiju) {
            if (s.me) {
                throw new InvalidMoveException();
            }
            else {
                this.ships.remove(s);
            }
        }
        else if (nextTile instanceof Wake) {
            this.moveShipRecurse(s, init);
        }

        if (this.ships.size() == 1) {
            throw new GameWonException();
        }
    }

    /**
     * Move daikaijus.
     *
     * @throws GameLostException Game lost after movement
     * @throws GameWonException Game won after movement
     */
    public void moveDaikaiju() throws GameLostException, GameWonException {
        Random rand = new Random();
        int die = rand.nextInt(6);

        if (die == 6) {
            Pair<Integer, Integer> dest;
            do {
                dest = daikaijuPlacement();
            } while (this.board[dest.a][dest.b] instanceof Daikaiju);

            int dInd = rand.nextInt(this.daikaijuDrawPile.size());
            Daikaiju d = this.daikaijuDrawPile.remove(dInd);
            int rot = rand.nextInt(4);

            this.addDaikaijuToBoard(new Triple<>(dest.a, dest.b, rot), d);

            for (Ship s : this.ships) {
                Pair <Integer, Integer> adj = this.myAdjacency();
                if (s.me && ((s.x == dest.a && s.y == dest.b) ||
                    (adj.a.equals(dest.a) && adj.b.equals(dest.b)))) {
                    throw new GameLostException();
                }
                else if (s.x == dest.a && s.y == dest.b) {
                    this.ships.remove(s);
                }
            }
        }
        else {
            for (Daikaiju d : this.daikaijuBoard) {
                Daikaiju.Dir dir = d.movements[die].applyRot(d.rot);
                if (dir == Daikaiju.Dir.ROT) {
                    d.rot = (d.rot + 1) % 4;
                }
                else {
                    int x = -1,
                        y = -1;
                    for (int i = 0; i < 7; i++) {
                        for (int j = 0; j < 7; j++) {
                            if (this.board[i][j] == d) {
                                x = i;
                                y = j;
                            }
                        }
                    }
                    assert x != -1;
                    assert y != -1;

                    board[x][y] = null;
                    switch (dir) {
                        case UP:
                            if (y == 0) {
                                this.daikaijuBoard.remove(d);
                                this.addDaikaijuToDraw(d);
                            }
                            else {
                                y--;
                            }
                        case LEFT:
                            if (x == 0) {
                                this.daikaijuBoard.remove(d);
                                this.addDaikaijuToDraw(d);
                            }
                            else {
                                x--;
                            }
                        case DOWN:
                            if (y == 6) {
                                this.daikaijuBoard.remove(d);
                                this.addDaikaijuToDraw(d);
                            }
                            else {
                                y++;
                            }
                        case RIGHT:
                            if (x == 6) {
                                this.daikaijuBoard.remove(d);
                                this.addDaikaijuToDraw(d);
                            }
                            else {
                                x++;
                            }
                    }
                    if (board[x][y] instanceof Daikaiju) {
                        Daikaiju otherD = (Daikaiju) board[x][y];
                        this.daikaijuBoard.remove(otherD);
                        this.addDaikaijuToDraw(otherD);
                    }
                    else if (board[x][y] instanceof Wake) {
                        this.addWakeToDraw((Wake) board[x][y]);
                    }
                    board[x][y] = d;

                    for (Ship s : this.ships) {
                        Pair <Integer, Integer> adj = this.myAdjacency();
                        if (s.me && ((s.x == x && s.y == y) || (adj.a == x && adj.b == y))) {
                            throw new GameLostException();
                        }
                        else if (s.x == x && s.y == y) {
                            this.ships.remove(s);
                        }
                    }
                }
            }

            while (this.daikaijuBoard.size() < 3) {
                Pair<Integer, Integer> dest;
                do {
                    dest = daikaijuPlacement();
                } while (this.board[dest.a][dest.b] instanceof Daikaiju);

                int dInd = rand.nextInt(this.daikaijuDrawPile.size());
                Daikaiju d = this.daikaijuDrawPile.remove(dInd);
                int rot = rand.nextInt(4);

                this.addDaikaijuToBoard(new Triple<>(dest.a, dest.b, rot), d);

                for (Ship s : this.ships) {
                    Pair <Integer, Integer> adj = this.myAdjacency();
                    if (s.me && ((s.x == dest.a && s.y == dest.b) ||
                                 (adj.a.equals(dest.a) && adj.b.equals(dest.b)))) {
                        throw new GameLostException();
                    }
                    else if (s.x == dest.a && s.y == dest.b) {
                        this.ships.remove(s);
                    }
                }
            }
        }

        if (this.ships.size() == 1) {
            throw new GameWonException();
        }
    }

    /**
     * Generate location to place next daikaiju.
     *
     * @return A pair indicating the location to place the new daikaiju.
     */
    private static Pair<Integer, Integer> daikaijuPlacement() {
        Random rand = new Random();
        int gold = rand.nextInt(6);
        int blue = rand.nextInt(6);
        int x = gold - (gold < 4 ? 0 : 1);
        int y = blue - (blue < 4 ? 0 : 1);
        return new Pair<>(gold - (gold < 4 ? 0 : 1), blue - (blue < 4 ? 0 : 1));
    }

    /**
     * Place a daiakaiju on the board.
     *
     * @param xyr A triple indicating the coordinates and orientation to place the daikaiju
     * @param d Daikaiju to place
     */
    private void addDaikaijuToBoard(Triple<Integer, Integer, Integer> xyr, Daikaiju d) {
        d.rot = xyr.c;
        if (this.board[xyr.a][xyr.b] instanceof Wake) {
            this.addWakeToDraw((Wake) this.board[xyr.a][xyr.b]);
        }
        this.board[xyr.a][xyr.b] = d;

        int j = 0;
        for (; j < this.daikaijuBoard.size() && this.daikaijuBoard.get(j).type < d.type; j++);
        j = d.colour == Daikaiju.Col.B && this.daikaijuBoard.get(j).type == d.type ? j + 1 : j;
        this.daikaijuBoard.add(j, d);
    }

    /**
     * Add a wake tile to the draw pile.
     *
     * @param w Wake tile to add
     */
    private void addWakeToDraw(Wake w) {
        Random rand = new Random();
        int insInd = rand.nextInt(this.wakeDrawPile.size());
        w.rot = 0;
        this.wakeDrawPile.add(insInd, w);
    }

    /**
     * Add a daikaiju to the draw pile.
     *
     * @param d Daikaiju to add
     */
    private void addDaikaijuToDraw(Daikaiju d) {
        Random rand = new Random();
        int insInd = rand.nextInt(this.daikaijuDrawPile.size());
        d.rot = 0;
        this.daikaijuDrawPile.add(insInd, d);
    }
}
