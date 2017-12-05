file_name <- "input-day1.txt"
conn <- file(file_name)
linn <- readLines(conn)
for (i in 1:length(linn)) {
    print(linn[i])
}
