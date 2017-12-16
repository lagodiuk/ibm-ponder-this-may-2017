import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class PoissonDistributionCheck {

	public static void main(String[] args) {
		Random rnd = new Random(1);
		for (int size = 20; size < 41; size += 5) {
			evaluate(size, rnd);
			System.out.println();
		}
	}

	private static void evaluate(int size, Random rnd) {
		int trialsCount = 500000;
		Map<Integer, Integer> correct_placements_count = new TreeMap<>();
		for (int i = 0; i < trialsCount; i++) {
			List<Character> characters = random_placement(size, rnd);
			int correct = calc_correct_placements(characters);
			int count = correct_placements_count.getOrDefault(correct, 0);
			correct_placements_count.put(correct, count + 1);
		}
		for (int correct : correct_placements_count.keySet()) {
			int simulated_count = correct_placements_count.get(correct);
			double simulated_prob = (double) simulated_count / trialsCount;
			System.out.printf("%d %d %1.5f %1.5f %n", size, correct, simulated_prob, poisson_prob(correct));
		}
	}

	private static double poisson_prob(int correct) {
		long fact = factorial(correct);
		return Math.exp(-0.75) * Math.pow(0.75, correct) / fact;
	}

	private static long factorial(int n) {
		long fact = 1;
		for (int i = 1; i <= n; i++) {
			fact *= i;
		}
		return fact;
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
