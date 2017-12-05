# Solution for Day3, part 1
###########################
defmodule Day3 do

  def min_of_ring(1), do: 2
  # Ring index starts at 1. The value "1" is the center of the spiral.
  # - Ring 1 ranges from 2-9.
  # - Ring 2 ranges from 10-25.
  # etc.
  def min_of_ring(ring_index) do
    (ring_index-1)*8 + min_of_ring(ring_index-1)
  end

  def max_of_ring(1), do: 9
  def max_of_ring(2), do: 25
  #def max_of_ring(3) do
  #  1 + 1*8 + 2*8 + 3*8 = 49
  #end
  def max_of_ring(ring_index) do
    ring_index*8 + max_of_ring(ring_index-1)
  end

  def width_of_ring(1), do: 3
  def width_of_ring(2), do: 5
  def width_of_ring(3), do: 7
  def width_of_ring(4), do: 9
  def width_of_ring(ring_index) do
    ring_index*2+1
  end

  def south(ring) do
    width = width_of_ring(ring)
    max = max_of_ring(ring)
    half = div(width, 2)  # rounded down
    max - half
  end
  def west(ring) do
    width = width_of_ring(ring)
    south(ring) - (width-1)
  end
  def north(ring) do
    width = width_of_ring(ring)
    south(ring) - 2*(width-1)
  end
  def east(ring) do
    width = width_of_ring(ring)
    south(ring) - 3*(width-1)
  end

  def solve(target) do
    ring = get_ring_of_target(1, target)
    :io.format("Target: ~w~n", [target])
    :io.format("Ring of target: ~w~n", [ring])
    :io.format("Min of ring: ~w~n", [min_of_ring(ring)])
    :io.format("Max of ring: ~w~n", [max_of_ring(ring)])
    :io.format("Width of ring: ~w~n", [width_of_ring(ring)])
    n = north(ring)
    e = east(ring)
    s = south(ring)
    w = west(ring)
    :io.format("N=~w, E=~w, S=~w, W=~w~n", [n,e,s,w])
    distance = fn loc -> abs(target - loc) end
    distances = {
      distance.(n), distance.(e), distance.(s), distance.(w)
    }
    :io.format("distances=~w~n", [distances])
    min_distance = Enum.min(Tuple.to_list(distances))
    :io.format("min distance=~w~n", [min_distance])
    # from N, E, S, or W position in the right, the number of moves to the
    # center is simply the ring number -> 3rd ring takes 3 moves to get to
    # the center, etc.
    moves = min_distance + ring
    :io.format("Moves needed: ~w~n", [moves])
  end

  defp get_ring_of_target(ring, target) do
    if max_of_ring(ring) >= target do
      ring
    else
      get_ring_of_target(ring+1, target)
    end
  end

  def main() do
    puzzle = 312051
    solve(puzzle)
  end


end

Day3.main()
