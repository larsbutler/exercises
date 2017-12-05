from sets import ImmutableSet

with open('input-day4.txt') as fh:
    part1_count = 0
    part2_count = 0
    for line in fh:
        words = line.split()

        if len(words) == len(set(words)):
            part1_count += 1

        uniquified = ImmutableSet([ImmutableSet(word) for word in words])
        if len(words) == len(uniquified):
            part2_count += 1

    print('Part 1 count: %d' % part1_count)
    print('Part 2 count: %d' % part2_count)
