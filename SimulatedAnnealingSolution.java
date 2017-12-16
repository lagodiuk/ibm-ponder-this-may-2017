import java.util.Arrays;
import java.util.Random;

public class SimulatedAnnealingSolution {

	private static final boolean DEBUG_OUTPUT = false;

	public static void main(String[] args) {
		for (int size = 1; size <= 100; size++) {
			int[] solution = findOptimalSolution(size);
			int error = calcError(solution);
			if (error > 0.1) {
				break;
			}
			System.out.println(size + "\t" + error + "\t" + solutionToString(solution));
		}
	}

	private static int[] findOptimalSolution(int size) {
		return findOptimalSolution(size, 0.1, 1000, 0.01, 0.999, 100, new Random(1));
	}

	private static int[] findOptimalSolution(
			int size,
			double minEnergy, // minimal value of energy (termination criteria)
			double initialTemperature, // initial temperature
			double minTemperature, // minimal value of temperature (termination criteria)
			double temperatureDecreaseRatio, // (decreasing geometric progression)
			int numberOfTrials, // per iteration
			Random random) {

		// Initialize current solution
		int[] currentSolution = generateInitialSolution(size);
		int sequenceLength = currentSolution.length;

		// Initialize energy of a current solution
		int[] counter = new int[size + 1];
		int currentEnergy = calcError(currentSolution, counter);

		if (DEBUG_OUTPUT) {
			System.out.println("Current energy is: " + currentEnergy);
		}

		// Memorize the solution with smallest value of energy
		int[] bestSolution = currentSolution.clone();
		int bestEnergy = currentEnergy;

		double temperature = initialTemperature;

		while (temperature > minTemperature
				&& currentEnergy > minEnergy) {

			for (int i = 0; i < numberOfTrials; i++) {

				// Generate new solution:
				// swap two nearby items
				int pos1 = random.nextInt(sequenceLength);
				int pos2 = (pos1 + 1) % sequenceLength;
				swap(currentSolution, pos1, pos2);

				int newEnergy = calcError(currentSolution, counter);

				// According to the Boltzmann distribution
				double acceptanceProbability =
						Math.exp(-(newEnergy - currentEnergy) / temperature);

				// Solutions with smaller energy - will be accepted always
				if (newEnergy < currentEnergy
						|| random.nextDouble() < acceptanceProbability) {

					currentEnergy = newEnergy;
					if (DEBUG_OUTPUT) {
						System.out.println("Current energy is: " + currentEnergy);
					}

					if (newEnergy < bestEnergy) {
						// Current solution is better than the best solution found so far
						System.arraycopy(currentSolution, 0, bestSolution, 0, currentSolution.length);
						bestEnergy = newEnergy;
					}
				} else {
					// If solution can't be accepted - rollback:
					// un-swap the items, which were swapped
					swap(currentSolution, pos1, pos2);
				}
			}
			// Decreasing temperature
			temperature *= temperatureDecreaseRatio;
		}
		// Return the best solution
		return bestSolution;
	}

	private static final int GAP_ITEM = -1;

	// Initial solution is: "1 1 2 2 3 3...."
	private static int[] generateInitialSolution(int size) {
		boolean withGap = (size - 3) % 4 != 0 && size % 4 != 0;
		int sequenceLength = 2 * size;
		if (withGap) {
			sequenceLength += 1;
		}
		int[] sequence = new int[sequenceLength];
		int pos = 0;
		for (int i = 0; i < size; i++) {
			int currChar = i + 1;
			sequence[pos] = currChar;
			sequence[pos + 1] = currChar;
			pos += 2;
		}
		if (withGap) {
			sequence[sequence.length - 1] = GAP_ITEM;
		}
		return sequence;
	}

	private static final int NOT_INITIALIZED = -1;

	private static int calcError(int[] sequence, int[] counter) {
		Arrays.fill(counter, NOT_INITIALIZED);
		int error = 0;
		for (int i = 0; i < sequence.length; i++) {
			int item = sequence[i];
			if (item == GAP_ITEM) {
				continue;
			}
			if (counter[item] == NOT_INITIALIZED) {
				int expectedPosition = i + item + 1;
				counter[item] = expectedPosition;
			} else {
				int expectedPosition = counter[item];
				error += Math.abs(expectedPosition - i);
			}
		}
		return error;
	}

	private static int calcError(int[] sequence) {
		return calcError(sequence, new int[sequence.length / 2 + 1]);
	}

	private static void swap(int[] sequence, int i, int j) {
		int tmp = sequence[i];
		sequence[i] = sequence[j];
		sequence[j] = tmp;
	}

	private static String solutionToString(int[] solution) {
		char[] chars = new char[solution.length];
		for (int i = 0; i < solution.length; i++) {
			if (solution[i] == GAP_ITEM) {
				chars[i] = '-';
			} else {
				chars[i] = (char) ('A' + solution[i] - 1);
			}
		}
		return new String(chars);
	}
}