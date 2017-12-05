options(echo=FALSE)

args <- commandArgs(trailingOnly = TRUE)
if (length(args) < 1) {
    message("Specify a file name for input")
    quit(status=1)
}
filename <- args[1]

table <- read.csv(filename, header = FALSE, sep = "")

##########
#solveRow <- function(eachRow) {
#    return( diff(range(eachRow)) )
#}
#solution <- sum(apply(table, 1, solveRow))
#print(solution)
##########

##########
#solution <- 0
#for (rownum in 1:nrow(table)) {
#    eachRow <- table[rownum,]
#    rowSolution <- diff(range(eachRow))
#    solution <- solution + rowSolution
#}
#print(solution)
##########

part1SolveRow <- function(eachRow) {
    return( diff(range(eachRow)) )
}

applyOnRows <- function(someTable, someFunction) {
    result <- c()
    for (rowNum in 1:nrow(someTable)) {
        eachRow <- someTable[rowNum,]
        rowResult <- someFunction(eachRow)
        result <- c(result, rowResult)
    }
    return(result)
}

part2SolveRow <- function(eachRow) {
    answer <- 0
    n <- length(eachRow)

    for (i in 1:(n-1)) {
        for (j in (i+1):n) {
            inum <- eachRow[i]
            jnum <- eachRow[j]
            if (inum %% jnum == 0) {
                #print(sprintf("case1: inum=%d, jnum=%d\n", inum, jnum))
                return(inum / jnum)
            } else if (jnum %% inum == 0) {
                #print(sprintf("case2: inum=%d, jnum=%d\n", inum, jnum))
                return(jnum / inum)
            }
        }
    }
    return(answer)
}
#part1Solution <- sum(applyOnRows(table, part1SolveRow))
part1Solution <- sum(apply(table, 1, part1SolveRow))
cat(sprintf("Part1: %d\n", part1Solution))
part2Solution <- sum(apply(table, 1, part2SolveRow))
cat(sprintf("Part2: %d\n", part2Solution))
##########




##########
#for (i in 1:nrow(table)) {
#    rng <- range(table[i,])
#    min_i <- min(table[i,])
#    max_i <- max(table[i,])
#    #print(sprintf("%d, %d\n", min_i, max_i))
#    diff <- abs(min_i - max_i)
#    total <- total + diff
#    #print(diff)
#}
#message(total)
#print(length(table))
#print(min(table[,1]))
#print(max(table[,1]))
