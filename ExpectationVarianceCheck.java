import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ExpectationVarianceCheck {

	public static void main(String[] args) {
		Random rnd = new Random(1);
		for (int size = 2; size < 46; size++) {
			evaluate(size, rnd);
		}
	}

	private static void evaluate(int size, Random rnd) {

		int trials_count = 500000;

		int correct_placements_cnt = 0;
		int correct_placements_cnt_sqr = 0;
		for (int i = 0; i < trials_count; i++) {
			List<Character> characters = random_placement(size, rnd);
			int correct = calc_correct_placements(characters);
			correct_placements_cnt += correct;
			correct_placements_cnt_sqr += correct * correct;
		}

		double avg_correct_placements =
				(double) correct_placements_cnt / trials_count;
		double experimental_variance =
				(double) correct_placements_cnt_sqr / trials_count - Math.pow(avg_correct_placements, 2);

		System.out.printf("%d  %3.3f  %3.3f  %3.3f  %3.3f %n",
				size, avg_correct_placements, expectation(size),
				experimental_variance, variance(size));
	}

	private static double expectation(int n) {
		return (3.0 * n - 3) / (4 * n - 2);
	}

	private static double variance(int n) {
		return 3.0 / 4 + 4.0 / (n - 1) + 4.0 / (3 * n)
				- 127.0 / (96 * (2 * n - 3)) - 363.0 / (32 * (2 * n - 1))
				- 9.0 / (16 * Math.pow(2 * n - 1, 2));
	}

	private static int calc_correct_placements(List<Character> characters) {
		Map<Character, Integer> first_occurrence = new HashMap<>();
		int correct = 0;
		for (int i = 0; i < characters.size(); i++) {
			char c = characters.get(i);
			if (first_occurrence.get(c) == null) {
				first_occurrence.put(c, i);
			} else {
				int firstOccurrencePos = first_occurrence.get(c);
				if (i - firstOccurrencePos - 1 == c - 'A' + 1) {
					correct++;
				}
			}
		}
		return correct;
	}

	private static List<Character> random_placement(int size, Random rnd) {
		List<Character> characters = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			char curr_char = (char) ('A' + i);
			characters.add(curr_char);
			characters.add(curr_char);
		}
		Collections.shuffle(characters, rnd);
		return characters;
	}
}
