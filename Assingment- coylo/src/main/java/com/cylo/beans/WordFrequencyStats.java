package com.cylo.beans;

import java.util.List;

/**
 * @author d.kabha
 *
 */
public class WordFrequencyStats {

	private final int minFrequency;
	private final int medianFrequency;
	private final List<WordFrequency> topFrequencies;

	public WordFrequencyStats(List<WordFrequency> topFrequencies, int minFrequency, int medianFrequency) {
		this.topFrequencies = topFrequencies;
		this.minFrequency = minFrequency;
		this.medianFrequency = medianFrequency;

	}



	public int getMinFrequency() {
		return minFrequency;
	}

	public int getMedianFrequency() {
		return medianFrequency;
	}

	public List<WordFrequency> getTopFrequencies() {
		return topFrequencies;
	}
}
