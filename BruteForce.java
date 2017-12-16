public class BruteForce {

	public static void main(String... args) {
		for (int alphabet_size = 1; alphabet_size < 11; alphabet_size++) {
			check(alphabet_size);
		}
	}

	static void check(int alphabet_size) {
		if (can_solve(new boolean[2 * alphabet_size], 1, alphabet_size)) {
			System.out.printf("%3d - no extra space needed %n", alphabet_size);

		} else if (can_solve(new boolean[2 * alphabet_size + 1], 1, alphabet_size)) {
			System.out.printf("%3d + requires extra space %n", alphabet_size);
		}
	}

	static boolean can_solve(boolean[] occupied, int distance, int alphabet_size) {
		if (distance == alphabet_size + 1) {
			return true;
		}
		for (int pos = 0; pos < occupied.length - distance - 1; pos++) {
			// Iterate over all available positions
			if (!(occupied[pos] || occupied[pos + distance + 1])) {
				occupied[pos] = true;
				occupied[pos + distance + 1] = true;
				if (can_solve(occupied, distance + 1, alphabet_size)) {
					return true;
				}
				// Backtracking
				occupied[pos] = false;
				occupied[pos + distance + 1] = false;
			}
		}
		return false;
	}
}
