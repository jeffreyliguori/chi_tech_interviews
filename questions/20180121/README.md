# Questions for the week

## Minimum number of airport gates.
You are given a list of arrival times and departure times for planes into/out
of an airport for the day, where the ith element in each array corresponds to
one plane entering and then departing the airport. Compute the minimum number
of gates needed at the airport to handle all the planes landing that day (aka
the max # of planes at the airport at any given time).

Times are stored as ints in military format, where 0 (or 0000) represents
midnight and 2359 represents 11:59 PM. Input not guaranteed sorted. All times
are inclusive, meaning if one airport departs at 3 and another arrives at 3
they require different gates.

example:
Arrivals: [1000, 1200, 1300, 1330]

Departures: [1130, 1430, 1330, 1500]

Answer: 3, at 1:30 PM (1330) flights #2, 3, and 4 will all be at the airport.

## Count Possible Ways Down a Grid of Points
Given the dimension of a grid of points (m by n), compute the total possible
ways to get from the top left point on that grid to the bottom right point if
the only direction you can go is either down or right.

example:
m = 3, n = 2

Answer: 3

The grid looks like this:

X X X

X X X

And the possible ways to get from top left to bottom right are:
Right Right Down
Right Down Right
Down Right Right

## I do not remember the remaining problems.