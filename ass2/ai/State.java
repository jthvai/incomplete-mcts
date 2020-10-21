package comp1140.ass2.ai;

import comp1140.ass2.util.Pair;

import java.util.*;

import static comp1140.ass2.core.WakeTile.getOppositeExit;

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
            t.rot = Character.getNumericValue(curr.charAt(4));
            this.board[x][y] = t;
            this.addDaikaijuToBoard(t);
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

    private static Object[][] deepClone(Object[][] m) {
        Object[][] clone = new Object[m.length][m[0].length];
        for (int i = 0; i < m.length; i++) {
            clone[i] = m[i].clone();
        }
        return clone;
    }

    public State applyMove(Wake tile, int rot) {
        Pair<Integer, Integer> tileDest = this.myAdjacency();

        Random rand = new Random();
        int dice = rand.nextInt(6) + rand.nextInt(6) + 2;

        if (dice > 5 && dice < 9) {
            this.moveDaikaiju();
        }
    }

    public void moveDaikaiju() {

    }

    public boolean isMoveValid() {
        return false;
    }

    private Pair<Integer, Integer> myAdjacency() {
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

    private void addDaikaijuToBoard(Daikaiju d) {
        int j = 0;
        for (; j < this.daikaijuBoard.size() && this.daikaijuBoard.get(j).type < d.type; j++);
        j = d.colour == Daikaiju.Col.B && this.daikaijuBoard.get(j).type == d.type ? j + 1 : j;
        this.daikaijuBoard.add(j, d);
    }
}
