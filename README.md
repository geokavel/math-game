# math-game
Mathematical expression game

The purpose of this game to compete against other bots by creating mathemetical expressions using the given numbers and `+ - * / () ^ sqrt sin cos tan` to get as close as possible to the target number.

Evaluator
-----
The evaluator used in this game comes from this [Stack Overflow answer](https://stackoverflow.com/a/26227947/5311008). It follows order of operations, does all calculations to double precsion, and allows leading zeroes. It also doesn't require parenthesis for `sqrt sin cos tan`, and does trigonometric calculations in degrees. You can play around with it by compiling the source and running `java -cp classes Eval`

Game 
-----
Every round your bot is sent 6 numbers. The first 5 numbers are digits [0-9], and the last number is the target number [0-99]. You need to intersperce operators between the numbers to reach a number as close as possible to the target. You need to use all 5 digits exactly once and in any order. You can also combine digits into larger numbers. For example, given digits `0 5 8 7 2` and target `34` a valid response is `50-28+7` which yields a result of 29.

Scoring
------

Your score each round is the distance between your result and the target number. If multiple bots tie, the tiebreaker is response speed. Your bot has 5 seconds to return a response, otherwise it is disqualified from the round. Another cause of disqualification is if your response is invalid (either if the evaluator can't parse your expression, or if you don't use each digit exactly once).

Bots
----

Bots need to make a standard socket connection to port 7777 on localhost. Upon connecting, the first thing your bot should do is send a string containing its name followed by a new line to the server. From then on, your bot should listen for 6 space-separated numbers followed by a new line, meaning that a new round has started. Then your bot should send back its answer - a mathemtical expresion string followed by a new line. When your bot receives a message that says "quit" followed by a new line, it should know it's time to disconnect.
