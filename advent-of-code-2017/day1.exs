defmodule Day1 do

  def index_plus1(i, n), do: rem((i + 1), n)
  def index_halfn(i, n), do: rem(i + div(n, 2), n)

  def solve_part1(nums), do: solve(nums, &Day1.index_plus1/2)
  def solve_part2(nums), do: solve(nums, &Day1.index_halfn/2)

  def solve(nums, get_next_index) do
    n = tuple_size(nums)
    i = 0
    the_sum = 0
    sum_loop(nums, i, n, the_sum, get_next_index)
  end

  # base case: we've reached the end of the tuple
  defp sum_loop(_nums, i, n, the_sum, _get_next_index) when i >= n, do: the_sum
  # recursive case:
  defp sum_loop(nums, i, n, the_sum, get_next_index) do
    current = elem(nums, i)
    next_index = get_next_index.(i, n)
    next = elem(nums, next_index)

    if current == next do
      # Add the number to running total:
      sum_loop(nums, i + 1, n, the_sum + current, get_next_index)
    else
      # Don't alter the running total:
      sum_loop(nums, i + 1, n, the_sum, get_next_index)
    end
  end

  def main() do
    if length(System.argv) < 1 do
      IO.puts("Error: specify a file name for the input")
    else
      filename = hd(System.argv)
      IO.puts(filename)
      {:ok, data} = File.read(filename)
      # chop off the new line at the end of the file
      data = String.trim_trailing(data)
      chars = String.to_charlist(data)
      nums = List.to_tuple(for c <- chars, do: c - 48)
      part1_solution = solve_part1(nums)
      IO.inspect(part1_solution, label: "Solution, part 1:")
      part2_solution = solve_part2(nums)
      IO.inspect(part2_solution, label: "Solution, part 2:")
    end
  end

end

Day1.main()
