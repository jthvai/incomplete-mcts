# Monte-Carlo Tree Search Skeleton

This repository contains an incomplete scaffold for a pure Monte-Carlo tree search AI for [Tsuro of the
Seas](https://www.calliopegames.com/buy/clp119/tsuro-of-the-seas), built on Java 13. This code was written as part of a
larger project for ANU's [COMP1140](https://cs.anu.edu.au/courses/comp1110/), 2020 S2.

It definitely does not work, and I make no promises as to whether it even compiles. The main entry point is
[`src/ai/MCTS.java`](./src/ai/MCTS.java).

## Notes on the board state representation

The parameter `String[] state` as given in `ai.MCTS.generateAction` is an array of 3 strings.

`state[0]` represents the wake tiles on the board -- a sequence of 5 character representations of wake tiles. The first
2 characters of such a representation denotes the tile's ID; the third and fourth, its X and Y coordinates,
respectively; the fifth, its rotation.

`state[1]` represents daikaiju tiles on the board -- a sequence of 5 character representations. The first character, its
colour; the second, its number; the third and fourth, its X and Y coordinates respectively; the fifth, its rotation.

`state[2]` represents ships on the board -- a sequence of 4 character representations. The first character, the ship's
colour; the second and third, its X and Y coordinates respectively; the fourth, the exit at which the ship rests.

## License

Source code in this repository has been made available under Creative Commons Zero v1.0 Universal ([full
text](./LICENSE), [SPDX](https://spdx.org/licenses/CC0-1.0.html)) where practicable, subject to some considerations:

Tsuro of the Seas is Copyright 2013 Compound Fun, LLC. All Rights Reserved. Tsuro, Tsuro of the Seas, and Calliope Games
are trademarks of Compound Fun, LLC.
